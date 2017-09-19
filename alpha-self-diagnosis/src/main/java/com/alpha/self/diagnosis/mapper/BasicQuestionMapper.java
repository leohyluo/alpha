package com.alpha.self.diagnosis.mapper;

import java.util.List;

import com.alpha.self.diagnosis.pojo.BasicQuestion;

public interface BasicQuestionMapper {

	BasicQuestion find(BasicQuestion question);
	
	BasicQuestion findByQuestionCode(String questionCode);
	
	List<BasicQuestion> findNext (BasicQuestion question);
}
