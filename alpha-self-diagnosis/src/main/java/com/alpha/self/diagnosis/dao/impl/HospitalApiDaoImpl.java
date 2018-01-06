package com.alpha.self.diagnosis.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.HospitalApiDao;
import com.alpha.server.rpc.diagnosis.pojo.HospitalApi;

@Repository
public class HospitalApiDaoImpl extends BaseDao<HospitalApi, Long> implements HospitalApiDao {

	public HospitalApiDaoImpl(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	private final String NAME_SPACE = "com.alpha.server.rpc.diagnosis.pojo.HospitalApi";

	@SuppressWarnings("unchecked")
	@Override
	public List<HospitalApi> listByHospitalCode(String hospitalCode) {
		String statement = NAME_SPACE.concat(".queryByHospitalCode");
		Map<String, Object> params = new HashMap<>();
        params.put("hospitalCode", hospitalCode);
        List<DataRecord> datas = super.selectForList(statement, params);
        List<HospitalApi> apiList = JavaBeanMap.convertListToJavaBean(datas, HospitalApi.class);
        return apiList;
	}

	@Override
	public Class<HospitalApi> getClz() {
		return HospitalApi.class;
	}

	
}
