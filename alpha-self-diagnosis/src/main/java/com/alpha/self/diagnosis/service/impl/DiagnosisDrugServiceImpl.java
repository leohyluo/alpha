package com.alpha.self.diagnosis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alpha.self.diagnosis.service.DiagnosisDrugService;
import com.alpha.self.diagnosis.service.UserDiagnosisOutcomeService;

@Service
public class DiagnosisDrugServiceImpl implements DiagnosisDrugService {

	@Resource
	private UserDiagnosisOutcomeService userDiagnosisOutcomeService;
}
