package com.alpha.self.diagnosis.dao;

import java.util.List;
import java.util.Map;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;

/**
 * Created by xc.xiong on 2017/9/5.
 */
public interface DiagnosisMainSymptomsDao extends IBaseDao<DiagnosisMainSymptoms, Long> {

	List<DiagnosisMainSymptoms> query(Map<String, Object> param);

}
