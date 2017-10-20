package com.alpha.user.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.user.dao.UserBasicRecordDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserBasicRecordDaoImpl extends BaseDao<UserBasicRecord, Long> implements UserBasicRecordDao {

    private static final String NAME_SPACE = "com.alpha.user.mapper.UserBasicRecordMapper";

    @SuppressWarnings("unchecked")
    @Override
    public UserBasicRecord findByDiagnosisId(Long diagnosisId) {
        String statement = NAME_SPACE + ".findByDiagnosisId";
        Map<String, Object> param = new HashMap<>();
        param.put("diagnosisId", diagnosisId);
        List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        UserBasicRecord record = null;
        if (!CollectionUtils.isEmpty(resultList)) {
            record = resultList.get(0);
        }
        return record;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserBasicRecord> findByLastMonth(Map<String, Object> param) {
        String statement = NAME_SPACE + ".findByLastMonth";
        List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        return resultList;
    }

    public UserBasicRecordDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<UserBasicRecord> getClz() {
        return UserBasicRecord.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserBasicRecord findLast(Long userId) {
        String statement = NAME_SPACE + ".findLast";
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        UserBasicRecord record = new UserBasicRecord();
        if (!CollectionUtils.isEmpty(resultList)) {
            record = resultList.get(0);
        }
        return record;
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public UserBasicRecord findLastCompleted(Long userId) {
    	 String statement = NAME_SPACE + ".findLast";
         Map<String, Object> param = new HashMap<>();
         param.put("userId", userId);
         param.put("presentIllnessHistory", "1");
         List<DataRecord> list = super.selectForList(statement, param);
         List<UserBasicRecord> resultList = new ArrayList<>();
         if (resultList != null) {
             resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
         }
         UserBasicRecord record = new UserBasicRecord();
         if (!CollectionUtils.isEmpty(resultList)) {
             record = resultList.get(0);
         }
         return record;
	}

    @Override
    public String findTemplateId(Long diagnosisId) {
        Map<String, Object> param = new HashMap<>();
        param.put("diagnosisId", diagnosisId);
        String statement = NAME_SPACE + ".findTemplateId";
        String templateId = super.selectForObject(statement, param);
        return templateId;
    }
}
