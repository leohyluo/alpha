package com.alpha.user.service;

import com.alpha.server.rpc.user.pojo.UserInfo;

public interface UserInfoService {

    /**
     * 根据第三方用户编号，渠道编号获取用户信息
     * 如果没有用户信息，则创建一个新的用户
     *
     * @param userInfo
     * @param inType
     * @return
     */
    UserInfo updateUserInfo(UserInfo userInfo,int inType);
    
    /**
     * 保存用户基础信息
     * @param userInfo
     * @return
     */
    UserInfo save(UserInfo userInfo);
    
    /**
     * 根据用户id查询用户基础信息
     * @param userId
     * @return
     */
    UserInfo queryByUserId(Long userId);
    
    /**
     * 创建用户
     * @param inType
     * @return
     */
    UserInfo create(UserInfo userInfo);

    UserInfo queryByUserIdAndUserName(Long userId, String userName);
}
