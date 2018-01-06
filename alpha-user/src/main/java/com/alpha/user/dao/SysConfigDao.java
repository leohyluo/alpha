package com.alpha.user.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.sys.SysConfig;

public interface SysConfigDao extends IBaseDao<SysConfig, Long> {

	/**
	 * 获取系统参数配置
	 * @param key
	 * @return
	 */
	SysConfig querySysConfig(String key);
	
}
