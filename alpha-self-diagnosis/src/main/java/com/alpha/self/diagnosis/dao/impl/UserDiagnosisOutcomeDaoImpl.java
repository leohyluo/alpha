package com.alpha.self.diagnosis.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.UserDiagnosisOutcomeDao;
import com.alpha.server.rpc.diagnosis.pojo.UserDiagnosisOutcome;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    



}
