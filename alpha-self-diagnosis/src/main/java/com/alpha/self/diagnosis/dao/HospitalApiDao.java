package com.alpha.self.diagnosis.dao;

import java.util.List;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.HospitalApi;

/**
 * 文章资讯
 * @author Administrator
 *
 */
public interface HospitalApiDao extends IBaseDao<HospitalApi, Long> {

	
	/**
	 * 根据医院编码查询其下所有api地址
	 * @param hospitalCode
	 * @return
	 */
	List<HospitalApi> listByHospitalCode(String hospitalCode);
}
