package com.alpha.user.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.server.rpc.diagnosis.pojo.UserBasicRecord;
import com.alpha.user.dao.UserBasicRecordDao;

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
		if(resultList != null){
			resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
		UserBasicRecord record = new UserBasicRecord();
		if(!CollectionUtils.isEmpty(resultList)) {
			record = resultList.get(0);
		}
		return record;
	}

	public UserBasicRecordDaoImpl(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	@Override
    public Class<UserBasicRecord> getClz() {
        return UserBasicRecord.class;
    }

}
