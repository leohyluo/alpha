package com.alpha.self.diagnosis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alpha.self.diagnosis.dao.DiagnosisPastmedicalHistoryDao;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;

@Service
public class DiagnosisPastmedicalHistoryServiceImpl implements DiagnosisPastmedicalHistoryService {
	
	@Resource
	private DiagnosisPastmedicalHistoryDao dao;

	@Override
	public List<DiagnosisPastmedicalHistory> queryPastmedicalHistory(Map<String, Object> param) {
		List<DiagnosisPastmedicalHistory> list = dao.queryPastmedicalHistory(param);
		return Optional.ofNullable(list).orElseGet(ArrayList::new);
	}

	@Override
	public List<DiagnosisSubpastmedicalHistory> querySubPastmedicalHistory(Map<String, Object> param) {
		return dao.querySubPastmedicalHistory(param);
	}
}
