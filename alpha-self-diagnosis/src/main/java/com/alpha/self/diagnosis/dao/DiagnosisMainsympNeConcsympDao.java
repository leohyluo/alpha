package com.alpha.self.diagnosis.dao;

import java.util.List;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympNeConcsymp;


public interface DiagnosisMainsympNeConcsympDao extends IBaseDao<DiagnosisMainsympNeConcsymp, Long> {

    /**
     * 查询主症状、阴性伴随症状关系
     *
     * @param mainSympCode
     * @return
     */
    List<DiagnosisMainsympNeConcsymp> listDiagnosisMainsympNeConcsymp(String mainSympCode);


}
