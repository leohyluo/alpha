package com.alpha.self.diagnosis.dao;

import java.util.List;
import java.util.Map;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;

public interface DiagnosisPastmedicalHistoryDao extends IBaseDao<DiagnosisPastmedicalHistory, Long> {

	public List<DiagnosisPastmedicalHistory> queryPastmedicalHistory(Map<String, Object> param);
	
	public List<DiagnosisSubpastmedicalHistory> querySubPastmedicalHistory(Map<String, Object> param);
}
