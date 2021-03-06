package com.alpha.self.diagnosis.processor;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.self.diagnosis.annotation.BasicAnswerProcessor;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BasicAnswerProcessor
@Component
public class BirthProcessor extends AbstractBasicAnswerProcessor {

    private static final String QUESTION_CODE = BasicQuestionType.BORN.getValue();

    protected List<IAnswerVo> queryAnswers(BasicQuestion question, UserInfo userInfo) {
        // TODO Auto-generated method stub
        return new ArrayList<IAnswerVo>();
    }

    @Override
    protected String setQuestionCode() {
        // TODO Auto-generated method stub
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
