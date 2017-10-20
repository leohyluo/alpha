package com.alpha.self.diagnosis.service;

import java.util.List;

import com.alpha.self.diagnosis.pojo.vo.AnalysisRequestVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.DiagnosisResultVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserInfo;

public interface DiagnosisService {

    BasicQuestionVo start(Long userId, Integer inType);

    IQuestionVo lexicalAnalysisByTencent(AnalysisRequestVo vo);

    List<DiagnosisMainSymptoms> lexicalAnalysisByBaidu(QuestionRequestVo questionVo, UserInfo userInfo);

    /**
     * 问诊结束后展示就诊信息
     *
     * @param userId
     * @param diagnosisId
     * @return
     */
    public DiagnosisResultVo showDiagnosisResult(Long userId, Long diagnosisId);
    
    /**
     * his通过患者身份证号码查看病历
     * @param idcard
     * @param diagnosisId
     * @return
     */
    DiagnosisResultVo showDiagnosisResult(String idcard);
}
