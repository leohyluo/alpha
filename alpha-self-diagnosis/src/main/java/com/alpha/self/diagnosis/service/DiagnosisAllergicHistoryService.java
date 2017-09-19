package com.alpha.self.diagnosis.service;

import java.util.List;
import java.util.Map;

import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;

public interface DiagnosisAllergicHistoryService {

	public List<DiagnosisAllergicHistory> queryAllergicHistory(Map<String, Object> param);
	
	public List<DiagnosisSuballergicHistory> querySubAllergicHistory(Map<String, Object> param);
}
