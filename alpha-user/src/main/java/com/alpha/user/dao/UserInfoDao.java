package com.alpha.user.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Created by xc.xiong on 2017/9/5.
 */
@Component
public interface UserInfoDao extends IBaseDao<UserInfo, Long> {

    /**
     * 查询用户信息
     *
     * @param externalUserId
     * @param inType
     * @return
     */
    UserInfo getUserInfoByExternalUserId(String externalUserId, int inType);
    
    UserInfo queryByUserId(Long userId);
    
    List<UserInfo> query(Map<String, Object> map);
}