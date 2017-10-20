package com.alpha.treatscheme.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.treatscheme.dao.DiagnosisDiseaseTreatoptionsDao;
import com.alpha.treatscheme.pojo.DiagnosisDiseaseTreatoptions;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 * 治疗方案
 */
@Repository
public class DiagnosisDiseaseTreatoptionsDaoImpl extends BaseDao<DiagnosisDiseaseTreatoptions, Long> implements DiagnosisDiseaseTreatoptionsDao {

    private static final String STATEMENT_HEAD = "com.alpha.treatscheme.pojo.DiagnosisDiseaseTreatoptions";


    /**
     * 查询治疗方案
     *
     * @param diseaseCode
     * @return
     */
    public DiagnosisDiseaseTreatoptions getDiagnosisDiseaseTreatoptions(String diseaseCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("diseaseCode", diseaseCode);
        DataRecord data = super.selectForDataRecord(STATEMENT_HEAD + ".getByDiseaseCode", params);
        DiagnosisDiseaseTreatoptions diagnosisDiseaseTreatoptions = (DiagnosisDiseaseTreatoptions) JavaBeanMap.convertMap2JavaBean(data, DiagnosisDiseaseTreatoptions.class);
        return diagnosisDiseaseTreatoptions;
    }


    @Autowired
    public DiagnosisDiseaseTreatoptionsDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<DiagnosisDiseaseTreatoptions> getClz() {
        return DiagnosisDiseaseTreatoptions.class;
    }


}
