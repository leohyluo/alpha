package com.alpha.self.diagnosis.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympConcsymp;

import java.util.Collection;
import java.util.List;

/**
 * Created by xc.xiong on 2017/9/5.
 */
public interface DiagnosisMainsympConcsympDao extends IBaseDao<DiagnosisMainsympConcsymp, Long> {

    /**
     * 查询伴随症状，疾病关系
     *
     * @param mainSympCode
     * @return
     */
    List<DiagnosisMainsympConcsymp> listDiagnosisMainsympConcsymp(String mainSympCode);

    /**
     * 查询伴随症状
     *
     * @param mainSympCode
     * @return
     */
    List<DiagnosisMainsympConcsymp> listConcsymp(String mainSympCode);

    /**
     * 查询伴随症状
     *
     * @param mainSympCode
     * @param concSympCodes
     * @return
     */
    List<DiagnosisMainsympConcsymp> listDiagnosisMainsympConcsymp(String mainSympCode, Collection concSympCodes);

}
