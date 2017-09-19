package com.alpha.self.diagnosis.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.DiagnosisAllergicHistoryDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;

@Repository
public class DiagnosisAllergicHistoryDaoImpl extends BaseDao<DiagnosisAllergicHistory, Long>
		implements DiagnosisAllergicHistoryDao {

	private static final String NAME_SPACE = "com.alpha.self.diagnosis.mapper.DiagnosisAllergicHistoryMapper";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DiagnosisAllergicHistory> queryAllergicHistory(Map<String, Object> param) {
		String statement = NAME_SPACE + ".queryAllergicHistory";
		List<DataRecord> list = super.selectForList(statement, param);
		List<DiagnosisAllergicHistory> result = new ArrayList<>();
		if(list != null){
			result = JavaBeanMap.convertListToJavaBean(list, DiagnosisAllergicHistory.class);
        }
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DiagnosisSuballergicHistory> querySubAllergicHistory(Map<String, Object> param) {
		String statement = "com.alpha.self.diagnosis.mapper.DiagnosisSubAllergicHistoryMapper.querySubAllergicHistory";
		List<DataRecord> list = super.selectForList(statement, param);
		List<DiagnosisSuballergicHistory> result = new ArrayList<>();
		if(list != null){
			result = JavaBeanMap.convertListToJavaBean(list, DiagnosisSuballergicHistory.class);
        }
		return result;
	}
	
	public DiagnosisAllergicHistoryDaoImpl(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	@Override
	public Class<DiagnosisAllergicHistory> getClz() {
		return DiagnosisAllergicHistory.class;
	}

}
