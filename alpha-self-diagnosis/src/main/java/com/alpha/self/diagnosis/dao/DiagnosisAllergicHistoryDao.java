package com.alpha.self.diagnosis.dao;

import java.util.List;
import java.util.Map;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;

public interface DiagnosisAllergicHistoryDao extends IBaseDao<DiagnosisAllergicHistory, Long> {

	public List<DiagnosisAllergicHistory> queryAllergicHistory(Map<String, Object> param);
	
	public List<DiagnosisSuballergicHistory> querySubAllergicHistory(Map<String, Object> param);
}
