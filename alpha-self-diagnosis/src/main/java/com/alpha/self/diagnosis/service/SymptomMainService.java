package com.alpha.self.diagnosis.service;

import java.util.List;
import java.util.Map;

import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserInfo;

/**
 * Created by xc.xiong on 2017/9/11.
 */
public interface SymptomMainService {

    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId,UserInfo userInfo);
    
    BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId, List<DiagnosisMainSymptoms> list );
    
    public List<DiagnosisMainSymptoms> query(Map<String, Object> param);
}
