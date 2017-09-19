package com.alpha.self.diagnosis.service;

import java.util.List;

import com.alpha.commons.enums.DiseaseType;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.SearchRequestVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;

public interface BasicQuestionService {

	BasicQuestion find(BasicQuestion question);
	
	BasicQuestion findByQuestionCode(String questionCode);
	
	List<BasicQuestion> findNext (BasicQuestion question);
	
	IQuestionVo next(QuestionRequestVo questionVo) throws Exception;
	
	List<IAnswerVo> diseaseSearch(SearchRequestVo diseasevo, DiseaseType type);
}
