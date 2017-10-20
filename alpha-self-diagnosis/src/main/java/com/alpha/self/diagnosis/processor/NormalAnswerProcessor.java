package com.alpha.self.diagnosis.processor;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.self.diagnosis.annotation.BasicAnswerProcessor;
import com.alpha.self.diagnosis.pojo.BasicAnswer;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.service.BasicAnswerService;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@BasicAnswerProcessor
@Component
public class NormalAnswerProcessor extends AbstractBasicAnswerProcessor {

    private static final String QUESTION_CODE[] = {
            BasicQuestionType.BOY_OR_GIRL.getValue(),
            BasicQuestionType.MAN_OR_WOMAN.getValue(), BasicQuestionType.MENSTRUAL_PERIOD.getValue(),
            BasicQuestionType.SPECIAL_PERIOD.getValue(), BasicQuestionType.FERTILITY_TYPE.getValue(),
            BasicQuestionType.GESTATIONAL_AGE.getValue(), BasicQuestionType.FEED_TYPE.getValue()
    };


    @Resource
    private BasicAnswerService basicAnswerService;

    protected List<IAnswerVo> queryAnswers(BasicQuestion question, UserInfo userInfo) {
        List<BasicAnswer> answerList = basicAnswerService.findByQuestionCode(question.getQuestionCode());
        return answerList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
    }

    @Override
    protected String setQuestionCode() {
        String questionCode = Stream.of(QUESTION_CODE).collect(Collectors.joining(","));
        return questionCode;
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
