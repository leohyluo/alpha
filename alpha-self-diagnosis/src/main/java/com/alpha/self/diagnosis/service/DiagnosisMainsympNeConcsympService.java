package com.alpha.self.diagnosis.service;

import java.util.List;

import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympNeConcsymp;

public interface DiagnosisMainsympNeConcsympService {

	/**
     * 查询主症状、阴性伴随症状关系
     *
     * @param mainSympCode
     * @return
     */
    List<DiagnosisMainsympNeConcsymp> listDiagnosisMainsympNeConcsymp(String mainSympCode);
}
