package com.alpha.self.diagnosis.service;

import java.util.List;

import com.alpha.self.diagnosis.pojo.BasicAnswer;

public interface BasicAnswerService {

	List<BasicAnswer> findByQuestionCode(String questionCode);
}
