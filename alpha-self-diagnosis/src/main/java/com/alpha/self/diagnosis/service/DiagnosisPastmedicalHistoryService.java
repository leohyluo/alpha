package com.alpha.self.diagnosis.service;

import java.util.List;
import java.util.Map;

import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;

public interface DiagnosisPastmedicalHistoryService {

	List<DiagnosisPastmedicalHistory> queryPastmedicalHistory(Map<String, Object> param);
	
	List<DiagnosisSubpastmedicalHistory> querySubPastmedicalHistory(Map<String, Object> param);
}
