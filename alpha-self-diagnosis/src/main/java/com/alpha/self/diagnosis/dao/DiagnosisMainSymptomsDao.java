package com.alpha.self.diagnosis.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;

import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 */
public interface DiagnosisMainSymptomsDao extends IBaseDao<DiagnosisMainSymptoms, Long> {

    List<DiagnosisMainSymptoms> query(Map<String, Object> param);

}
