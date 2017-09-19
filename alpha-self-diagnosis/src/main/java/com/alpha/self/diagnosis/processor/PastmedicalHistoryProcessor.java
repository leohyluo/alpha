package com.alpha.self.diagnosis.processor;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
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
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionWithSearchVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.service.BasicAnswerService;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.user.pojo.UserInfo;

@BasicAnswerProcessor
@Component
public class PastmedicalHistoryProcessor extends AbstractBasicAnswerProcessor {
	
	@Resource
	private DiagnosisPastmedicalHistoryService diagnosisPastmedicalHistoryService;
	@Resource
	private BasicAnswerService basicAnswerService;
	
	private static final String SEARCH_URL = "/diagnosis/pastmedicalHistory/search";
	private static final String QUESTION_CODE = BasicQuestionType.PAST_MEDICAL_HISTORY.getValue();

	protected Map<String, List<IAnswerVo>> queryAnswers(BasicQuestion question, UserInfo userInfo) {
		int showCount = 6;
		Map<String, List<IAnswerVo>> map = new HashMap<>();
		List<IAnswerVo> showList = new ArrayList<>();
		List<IAnswerVo> searchList = new ArrayList<>();
		
		Map<String, Object> param = userInfo.toBasicMap();
		List<DiagnosisPastmedicalHistory> list = diagnosisPastmedicalHistoryService.queryPastmedicalHistory(param);
		List<BasicAnswerVo> list1 = new ArrayList<>();
		//List<BasicAnswerVo> list2 = new ArrayList<>();
		if(list.size() > showCount) {
			list1 = list.subList(0, showCount).stream().map(BasicAnswerVo::new).collect(toList());
			//list2 = list.subList(showCount, list.size()).stream().map(BasicAnswerVo::new).collect(toList());
		} else {
			list1 = list.stream().map(BasicAnswerVo::new).collect(toList());
		}
		//查出手术史、否/不清楚这两个选项
		List<BasicAnswer> answerList = basicAnswerService.findByQuestionCode(question.getQuestionCode());
		List<BasicAnswerVo> defaultAnswervoList = answerList.stream().map(BasicAnswerVo::new).collect(toList());
		//拼装展示用的数据
		showList.addAll(list1);
		showList.addAll(defaultAnswervoList);
		//查询小类既往史
		/*List<DiagnosisSubpastmedicalHistory> subList = diagnosisPastmedicalHistoryService.querySubPastmedicalHistory(param);
		List<BasicAnswerVo> subAnswervoList = subList.stream().map(BasicAnswerVo::new).collect(toList());*/
		//拼装查询 用的数据
		/*searchList.addAll(list1);
		searchList.addAll(defaultAnswervoList);
		searchList.addAll(list2);
		searchList.addAll(subAnswervoList);*/

		map.put("show", showList);
		map.put("search", searchList);
		return map;
	}
		
	@Override
	protected Map<String, List<IAnswerVo>> getAnswers(BasicQuestion question, UserInfo userInfo) {
		return this.queryAnswers(question, userInfo);
	}

	@Override
	protected IQuestionVo getQuestionVo(Long diagnosisId, BasicQuestion question, UserInfo userInfo,
			Map<String, List<IAnswerVo>> data) {
		List<IAnswerVo> showList = data.get("show");
		//List<IAnswerVo> searchList = data.get("search");
		String userName = getUserName(diagnosisId, userInfo);
		//return new DiseaseQuestionVo(diagnosisId, question, showList, searchList, userInfo, userName);
		BasicQuestionWithSearchVo questionvo = new BasicQuestionWithSearchVo(diagnosisId, question, showList, userInfo, userName);
		questionvo.setSearchUrl(SEARCH_URL);
		return questionvo;
	}
	
	@Override
	protected String setQuestionCode() {
		return QUESTION_CODE;
	}

}
