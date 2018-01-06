package com.alpha.self.diagnosis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alpha.self.diagnosis.dao.DiagnosisMainsympNeConcsympDao;
import com.alpha.self.diagnosis.service.DiagnosisMainsympNeConcsympService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympNeConcsymp;

@Service
public class DiagnosisMainsympNeConcsympServiceImpl implements DiagnosisMainsympNeConcsympService {
	
	@Resource
	private DiagnosisMainsympNeConcsympDao diagnosisMainsympNeConcsympDao;

	@Override
	public List<DiagnosisMainsympNeConcsymp> listDiagnosisMainsympNeConcsymp(String mainSympCode) {
		List<DiagnosisMainsympNeConcsymp> neSymptomList = diagnosisMainsympNeConcsympDao.listDiagnosisMainsympNeConcsymp(mainSympCode);
		return neSymptomList;
	}

}
