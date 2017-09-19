package com.alpha.self.diagnosis.processor;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.self.diagnosis.annotation.BasicAnswerProcessor;
import com.alpha.self.diagnosis.pojo.BasicAnswer;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.LiverRenalQuestionVo;
import com.alpha.self.diagnosis.service.BasicAnswerService;
import com.alpha.server.rpc.user.pojo.UserInfo;

@BasicAnswerProcessor
@Component
public class LiverRenalProcessor extends AbstractBasicAnswerProcessor {

	private static final String QUESTION_CODE = BasicQuestionType.LIVER_RENAL.getValue();
	
	@Resource
	private BasicAnswerService basicAnswerService;
	
	protected List<IAnswerVo> queryAnswers(BasicQuestion question, UserInfo userInfo) {
		List<BasicAnswer> answerList = basicAnswerService.findByQuestionCode(question.getQuestionCode());
		List<IAnswerVo> answervoList = answerList.stream().map(BasicAnswerVo::new).collect(toList());
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
		return new LiverRenalQuestionVo(diagnosisId, question, answers, answers, userInfo, userName);
	}

}
