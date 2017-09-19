package com.alpha.self.diagnosis.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.api.tencent.ApiConstants;
import com.alpha.commons.api.tencent.WenZhiApi;
import com.alpha.commons.api.tencent.vo.KeywordResultVo;
import com.alpha.commons.api.tencent.vo.Keywords;
import com.alpha.commons.api.tencent.vo.SynonymResultVo;
import com.alpha.commons.core.service.SysSequenceService;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.self.diagnosis.pojo.BasicAnswer;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.AnalysisRequestVo;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.service.BasicAnswerService;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.DiagnosisService;
import com.alpha.self.diagnosis.service.SymptomMainService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.diagnosis.pojo.UserBasicRecord;
import com.alpha.user.service.UserBasicRecordService;
import com.alpha.user.service.UserMemberService;

@Service
@Transactional
public class DiagnosisServiceImpl implements DiagnosisService {
	
	@Resource
	private UserMemberService userMemberService; 
	@Resource
	private SysSequenceService sysSequenceService;
	@Resource
	private BasicQuestionService basicQuestionService;
	@Resource
	private BasicAnswerService basicAnswerService;
	@Resource
	private UserBasicRecordService userBasicRecordService;
	@Resource
	private SymptomMainService symptomMainService;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public BasicQuestionVo start(Long userId, Integer inType) {
		String questionCode = null;
		Long diagnosisId = sysSequenceService.getNextSequence("diagnosis_seq");
		//获取第一个问题
		BasicQuestion param = new BasicQuestion(1, null, null, null, null);
		BasicQuestion question = basicQuestionService.find(param);
		if(question != null) {
			questionCode = question.getQuestionCode();
		}
		//获取第一个问题的答案
		List<BasicAnswer> answerList = basicAnswerService.findByQuestionCode(questionCode);
		//获取用户下的成员
		/*Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		List<UserMember> userMemberList = userMemberService.find(map);*/
		
		List<IAnswerVo> answervoList = new ArrayList<>(); 
		List<BasicAnswerVo> defaultAnswervoList = answerList.stream().map(BasicAnswerVo::new).collect(toList());
		//将自己的answerCode设置为userId,将他人的answerCode设置为0
		defaultAnswervoList.stream().peek(e->updateAnswerVo(e, userId)).collect(toList());
		//List<BasicAnswerVo> answervoList4userMember = userMemberList.stream().map(BasicAnswerVo::new).collect(toList());
		answervoList.addAll(defaultAnswervoList);
		//answervoList.addAll(answervoList4userMember);
		//添加就诊记录
		UserBasicRecord record = new UserBasicRecord();
		record.setUserId(userId);
		record.setDiagnosisId(diagnosisId);
		userBasicRecordService.addUserBasicRecord(record);
		return new BasicQuestionVo(diagnosisId, question, answervoList, null, null);
	}

	private void updateAnswerVo(BasicAnswerVo basicAnswer, Long userId) {
		String answerCode = "0";
		if("自己".equals(basicAnswer.getAnswerTitle())) {	//自己
			answerCode = String.valueOf(userId);
			basicAnswer.setAnswerValue(answerCode);
		} if("他人".equals(basicAnswer.getAnswerTitle())) { //他人
			basicAnswer.setAnswerValue(answerCode);
		}
	}

	@Override
	public IQuestionVo lexicalAnalysis(AnalysisRequestVo vo) {
		IQuestionVo questionVo = null;
		Long diagnosisId = vo.getDiagnosisId();
		//初始化文智
		WenZhiApi wenzhiApi = new WenZhiApi();
		TreeMap<String, Object> keywordParams = new TreeMap<String, Object>();
		keywordParams.put("title", vo.getContent());
		keywordParams.put("content", vo.getContent());
		//调用腾迅API获取关键词
		String result = wenzhiApi.invoke(ApiConstants.ACTION_TEXT_KWYWORDS, keywordParams);
		KeywordResultVo keywordResultVo = JSON.parseObject(result, KeywordResultVo.class);
		List<String> keywordList = keywordResultVo.getKeywords().stream().map(Keywords::getKeyword).distinct().collect(toList());
		Map<String, Object> param = new HashMap<>();
		param.put("sympNameList", keywordList);
		List<DiagnosisMainSymptoms> mainList = symptomMainService.query(param);
		if(CollectionUtils.isNotEmpty(mainList)) {
			questionVo = symptomMainService.getMainSymptomsQuestion(diagnosisId, mainList);
			return questionVo;
		}
		//调用腾迅API获取同义词
		TreeMap<String, Object> synonymParams = new TreeMap<String, Object>();
		synonymParams.put("text", vo.getContent());
		result = wenzhiApi.invoke(ApiConstants.ACTION_TEXT_SYNONYM, synonymParams);
		SynonymResultVo synonymResultVo = JSON.parseObject(result, SynonymResultVo.class);
		//关键词、同义词均未找到匹配的主症状
//		questionVo = symptomMainService.getMainSymptomsQuestion(diagnosisId);
		
		return questionVo;
	}
}
