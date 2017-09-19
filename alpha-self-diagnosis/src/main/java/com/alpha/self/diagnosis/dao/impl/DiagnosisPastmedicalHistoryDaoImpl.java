package com.alpha.self.diagnosis.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.DiagnosisPastmedicalHistoryDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;

@Repository
public class DiagnosisPastmedicalHistoryDaoImpl extends BaseDao<DiagnosisPastmedicalHistory, Long>
		implements DiagnosisPastmedicalHistoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DiagnosisPastmedicalHistory> queryPastmedicalHistory(Map<String, Object> param) {
		List<DataRecord> list = super.selectForList("com.alpha.commons.mapper.DiagnosisPastmedicalHistoryMapper.queryPastmedicalHistory", param);
		List<DiagnosisPastmedicalHistory> result = new ArrayList<>();
		if(list != null){
			result = JavaBeanMap.convertListToJavaBean(list, DiagnosisPastmedicalHistory.class);
        }
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DiagnosisSubpastmedicalHistory> querySubPastmedicalHistory(Map<String, Object> param) {
		String statement = "com.alpha.self.diagnosis.mapper.DiagnosisPastmedicalHistoryMapper.querySubPastmedicalHistory";
		List<DataRecord> list = super.selectForList(statement, param);
		List<DiagnosisSubpastmedicalHistory> result = new ArrayList<>();
		if(list != null) {
			result = JavaBeanMap.convertListToJavaBean(list, DiagnosisSubpastmedicalHistory.class);
		}
		return result;
	}
	

	@Autowired
	public DiagnosisPastmedicalHistoryDaoImpl(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	@Override
	public Class<DiagnosisPastmedicalHistory> getClz() {
		return DiagnosisPastmedicalHistory.class;
	}

}
