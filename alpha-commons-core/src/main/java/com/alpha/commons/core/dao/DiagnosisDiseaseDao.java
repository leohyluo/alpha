package com.alpha.commons.core.dao;

import com.alpha.commons.core.pojo.DiagnosisDisease;

import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 */
public interface DiagnosisDiseaseDao extends IBaseDao<DiagnosisDisease, Long> {

    /**
     * 查询所有的疾病
     *
     * @param params
     * @return
     */
    List<DiagnosisDisease> listDiagnosisDisease(Map<String, Object> params);

    /**
     * 查询疾病
     *
     * @param diseaseCode
     * @return
     */
    DiagnosisDisease getDiagnosisDisease(String diseaseCode);

}
