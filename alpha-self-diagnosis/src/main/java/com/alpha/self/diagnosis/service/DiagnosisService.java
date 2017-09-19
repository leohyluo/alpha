package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.vo.AnalysisRequestVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;

public interface DiagnosisService {

	BasicQuestionVo start(Long userId, Integer inType);
	
	IQuestionVo lexicalAnalysis(AnalysisRequestVo vo);
}
