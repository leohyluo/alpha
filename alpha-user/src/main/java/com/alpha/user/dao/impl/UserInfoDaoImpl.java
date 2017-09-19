package com.alpha.user.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.UserInfoDao;

/**
 * Created by xc.xiong on 2017/9/5.
 */
@Repository
public class UserInfoDaoImpl extends BaseDao<UserInfo, Long> implements UserInfoDao {

    @Autowired
    public UserInfoDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<UserInfo> getClz() {
        return UserInfo.class;
    }


    /**
     * 查询用户信息
     * @param externalUserId
     * @param inType
     * @return
     */
    public UserInfo getUserInfoByExternalUserId(String externalUserId, int inType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("externalUserId", externalUserId);
        params.put("inType", inType);
            UserInfo userInfo = selectOne("com.alpha.user.mapper.UserInfoMapper.getUserInfoByExternalUserId", params);
        return userInfo;
    }

	@Override
	public UserInfo queryByUserId(Long userId) {
		UserInfo userInfo = selectOne("com.alpha.user.mapper.UserInfoMapper.queryByUserId", userId);
		return userInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> query(Map<String, Object> map) {
		String statement = "com.alpha.user.mapper.UserInfoMapper.find";
		List<DataRecord> list = super.selectForList(statement, map);
		List<UserInfo> resultList = new ArrayList<>();
		if(resultList != null){
			resultList = JavaBeanMap.convertListToJavaBean(list, UserInfo.class);
        }
		return resultList;
	}

}
