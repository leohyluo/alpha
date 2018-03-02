package com.alpha.user.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.UserInfoDao;
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
     *
     * @param externalUserId
     * @param inType
     * @return
     */
   /* @Override
    public UserInfo getUserInfoByExternalUserId(String externalUserId, int inType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("externalUserId", externalUserId);
        params.put("inType", inType);
        UserInfo userInfo = selectOne("com.alpha.user.mapper.UserInfoMapper.getUserInfoByExternalUserId", params);
        return userInfo;
    }*/
    
	/*@Override
	public UserInfo getUserInfoByExternalUserId(String externalUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("externalUserId", externalUserId);
        UserInfo userInfo = selectOne("com.alpha.user.mapper.UserInfoMapper.queryByExternalUserId", params);
        return userInfo;
	}*/


	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> listByExternalUserId(String externalUserId) {
		 String statement = "com.alpha.user.mapper.UserInfoMapper.listByExternalUserId";
	        Map<String, Object> map = new HashMap<>();
	        map.put("externalUserId", externalUserId);
			List<DataRecord> list = super.selectForList(statement, map);
	        List<UserInfo> resultList = new ArrayList<>();
	        if (resultList != null) {
	            resultList = JavaBeanMap.convertListToJavaBean(list, UserInfo.class);
	        }
	        return resultList;
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
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserInfo.class);
        }
        return resultList;
    }

	@SuppressWarnings("unchecked")
	@Override
	public UserInfo getByPhoneNumber(String phoneNumber) {
		String statement = "com.alpha.user.mapper.UserInfoMapper.find";
		Map<String, Object> param = new HashMap<>();
		param.put("phoneNumber", phoneNumber);
        List<DataRecord> list = super.selectForList(statement, param);
        List<UserInfo> resultList = new ArrayList<>();
        UserInfo userInfo = null;
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserInfo.class);
            userInfo =  resultList.get(0);
        }
        return userInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> listUserMemberInfo(Long userId) {
		String statement = "com.alpha.user.mapper.UserInfoMapper.queryUserMemberInfoList";
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
		List<DataRecord> list = super.selectForList(statement, param);
        List<UserInfo> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserInfo.class);
        }
        return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> listByUserId(List<Long> userIdList) {
		String statement = "com.alpha.user.mapper.UserInfoMapper.findbyUserIdList";
        Map<String, Object> param = new HashMap<>();
        param.put("userIdList", userIdList);
		List<DataRecord> list = super.selectForList(statement, param);
        List<UserInfo> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserInfo.class);
        }
        return resultList;
	}


}
