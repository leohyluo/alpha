package com.alpha.self.diagnosis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alpha.self.diagnosis.mapper.BasicAnswerMapper;
import com.alpha.self.diagnosis.pojo.BasicAnswer;
import com.alpha.self.diagnosis.service.BasicAnswerService;

@Service
public class BasicAnswerServiceImpl implements BasicAnswerService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private BasicAnswerMapper mapper;

	@Override
	public List<BasicAnswer> findByQuestionCode(String questionCode) {
		logger.info("findByQuestionCode.questionCode is {}", questionCode);
		
		List<BasicAnswer> list = mapper.findByQuestionCode(questionCode);
		return list;
	}

}