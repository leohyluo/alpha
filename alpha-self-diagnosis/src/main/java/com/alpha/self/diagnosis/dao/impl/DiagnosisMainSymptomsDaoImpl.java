package com.alpha.self.diagnosis.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.DiagnosisMainSymptomsDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 */
@Repository
public class DiagnosisMainSymptomsDaoImpl extends BaseDao<DiagnosisMainSymptoms, Long> implements DiagnosisMainSymptomsDao {


    @SuppressWarnings("unchecked")
    @Override
    public List<DiagnosisMainSymptoms> query(Map<String, Object> param) {
        String statement = "com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms.queryByKeyword";
        List<DataRecord> list = super.selectForList(statement, param);
        List<DiagnosisMainSymptoms> result = new ArrayList<>();
        if (list != null) {
            result = JavaBeanMap.convertListToJavaBean(list, DiagnosisMainSymptoms.class);
        }
        return result;
    }

    @Autowired
    public DiagnosisMainSymptomsDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<DiagnosisMainSymptoms> getClz() {
        return DiagnosisMainSymptoms.class;
    }


}
