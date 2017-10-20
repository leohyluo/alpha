package com.alpha.commons.core.dao.impl;

import com.alpha.commons.core.dao.DiagnosisDiseaseDao;
import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 */
@Repository
public class DiagnosisDiseaseDaoImpl extends BaseDao<DiagnosisDisease, Long> implements DiagnosisDiseaseDao {


    @Autowired
    public DiagnosisDiseaseDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<DiagnosisDisease> getClz() {
        return DiagnosisDisease.class;
    }

    /**
     * 查询所有的疾病
     *
     * @param
     * @return
     */
    public List<DiagnosisDisease> listDiagnosisDisease(Map<String, Object> params) {

        List<DataRecord> datas = super.selectForList("com.alpha.commons.core.pojo.DiagnosisDisease.queryDiagnosisDisease", params);
        List<DiagnosisDisease> dds = JavaBeanMap.convertListToJavaBean(datas, DiagnosisDisease.class);
        return dds;
    }

    /**
     * 查询疾病
     *
     * @param diseaseCode
     * @return
     */
    public DiagnosisDisease getDiagnosisDisease(String diseaseCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("diseaseCode", diseaseCode);
        DataRecord data = super.selectForDataRecord("com.alpha.commons.core.pojo.DiagnosisDisease.getDiagnosisDisease", params);
        DiagnosisDisease disease = (DiagnosisDisease) JavaBeanMap.convertMap2JavaBean(data, DiagnosisDisease.class);
        return disease;
    }


}
