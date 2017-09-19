package com.alpha.self.diagnosis.service;

import com.alpha.server.rpc.diagnosis.pojo.DiagnosisDisease;

import java.util.Collection;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/11.
 */
public interface DiagnosisDiseaseService {

    /**
     * 查询所有的疾病
     *
     * @param diseaseCodes
     * @return
     */
    Map<String, DiagnosisDisease> mapDiagnosisDisease(Collection diseaseCodes);



}
