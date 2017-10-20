package com.alpha.self.diagnosis.processor;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.self.diagnosis.annotation.BasicAnswerProcessor;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.BasicWeightInfo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.WeightAnswerVo;
import com.alpha.self.diagnosis.service.BasicWeightInfoService;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@BasicAnswerProcessor
@Component
public class WeightProcessor extends AbstractBasicAnswerProcessor {

    private static final String QUESTION_CODE = BasicQuestionType.WEIGHT.getValue();

    @Resource
    private BasicWeightInfoService weightInfoService;

    protected List<IAnswerVo> queryAnswers(BasicQuestion question, UserInfo userInfo) {
        Date birth = userInfo.getBirth();
        float age = DateUtils.getAge(birth);
        float month = DateUtils.getDiffMonth(birth);
        float weight = 0;
        float defaultWeight = 3.2f;    //默认3kg
        if (month < 24) {
            //≤6个月，体重=出生体重+月龄*0.7
            if (month <= 6) {
                weight = (float) (defaultWeight + month * 0.7);
            } else {
                //7-23月，体重=6+月龄*0.25
                weight = (float) (6 + month * 0.25);
            }
        } else {
            if (age >= 2 && age <= 6) {
                //2-6岁，体重=年龄（岁）*2+8；
                weight = age * 2 + 8;
            } else {
                //7-12岁，体重=年龄（岁）*3+2
                weight = age * 3 + 2;
            }
        }

        Map<String, Object> param = new HashMap<>();
        param.put("gender", userInfo.getGender());
        param.put("age", age);
        List<BasicWeightInfo> weightList = weightInfoService.query(param);
        BasicWeightInfo weightInfo = null;
        String normalRange = "";
        if (CollectionUtils.isNotEmpty(weightList)) {
            weightInfo = weightList.get(0);
            normalRange = weightInfo.getNormalStart() + "-" + weightInfo.getNormalEnd();
        }


        List<IAnswerVo> answervoList = new ArrayList<>();
        WeightAnswerVo answervo = new WeightAnswerVo();
        answervo.setAnswerTitle(weight + "kg");
        answervo.setAnswerValue(weight + "");
        answervo.setNormal(normalRange);

        answervo.setDescription("");
        answervoList.add(answervo);
        return answervoList;
    }

    @Override
    protected String setQuestionCode() {
        return QUESTION_CODE;
    }

    @Override
    protected Map<String, List<IAnswerVo>> getAnswers(BasicQuestion question, UserInfo userInfo) {
        Map<String, List<IAnswerVo>> map = new HashMap<>();
        map.put(DEFAULT_ANSWER, this.queryAnswers(question, userInfo));
        return map;
    }

    @Override
    protected IQuestionVo getQuestionVo(Long diagnosisId, BasicQuestion question, UserInfo userInfo, Map<String, List<IAnswerVo>> data) {
        List<IAnswerVo> answers = data.get(DEFAULT_ANSWER);
        String userName = getUserName(diagnosisId, userInfo);
        return new BasicQuestionVo(diagnosisId, question, answers, userInfo, userName);
    }
}
