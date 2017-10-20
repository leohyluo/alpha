package com.alpha.self.diagnosis.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.UserDiagnosisOutcomeDao;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;
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
public class UserDiagnosisOutcomeDaoImpl extends BaseDao<UserDiagnosisOutcome, Long> implements UserDiagnosisOutcomeDao {


    @Autowired
    public UserDiagnosisOutcomeDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<UserDiagnosisOutcome> getClz() {
        return UserDiagnosisOutcome.class;
    }

    /**
     * 查询诊断结果
     *
     * @param diagnosisId
     * @return
     */
    public List<UserDiagnosisOutcome> listTop5UserDiagnosisOutcome(Long diagnosisId) {
        Map<String, Object> params = new HashMap<>();
        params.put("diagnosisId", diagnosisId);
        params.put("offset", 0);
        params.put("limit", 5);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome.queryByDiagnosisId", params);
        List<UserDiagnosisOutcome> udds = JavaBeanMap.convertListToJavaBean(datas, UserDiagnosisOutcome.class);
        return udds;
    }

    /**
     * 查询诊断结果
     *
     * @param diagnosisId
     * @return
     */
    public List<UserDiagnosisOutcome> listUserDiagnosisOutcome(Long diagnosisId, String diseaseCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("diagnosisId", diagnosisId);
        params.put("diseaseCode", diseaseCode);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome.queryByDiseaseCode", params);
        List<UserDiagnosisOutcome> udds = JavaBeanMap.convertListToJavaBean(datas, UserDiagnosisOutcome.class);
        return udds;
    }

}
