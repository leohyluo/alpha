package com.alpha.self.diagnosis.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alpha.self.diagnosis.dao.DiagnosisAllergicHistoryDao;
import com.alpha.self.diagnosis.service.DiagnosisAllergicHistoryService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;

@Service
public class DiagnosisAllergicHistoryServiceImpl implements DiagnosisAllergicHistoryService {
	
	@Resource
	private DiagnosisAllergicHistoryDao dao;

	@Override
	public List<DiagnosisAllergicHistory> queryAllergicHistory(Map<String, Object> param) {
		return dao.queryAllergicHistory(param);
	}

	@Override
	public List<DiagnosisSuballergicHistory> querySubAllergicHistory(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return dao.querySubAllergicHistory(param);
	}
	
	
}
