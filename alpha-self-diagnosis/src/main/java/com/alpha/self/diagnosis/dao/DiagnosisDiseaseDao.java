package com.alpha.self.diagnosis.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisDisease;

import java.util.Collection;
import java.util.List;

/**
 * Created by xc.xiong on 2017/9/5.
 */
public interface DiagnosisDiseaseDao extends IBaseDao<DiagnosisDisease, Long> {
    /**
     * 查询所有的疾病
     *
     * @param diseaseCodes
     * @return
     */
    List<DiagnosisDisease> listDiagnosisDisease(Collection diseaseCodes);

}
