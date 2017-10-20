package com.alpha.user.service;

import java.util.List;
import java.util.Map;

import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.pojo.vo.HisUserInfoVo;
import com.alpha.user.pojo.vo.OtherHospitalInfo;
import com.alpha.user.pojo.vo.SaveUserInfoVo;

public interface UserInfoService {

    /**
     * 根据第三方用户编号，渠道编号获取用户信息
     * 如果没有用户信息，则创建一个新的用户
     *
     * @param userInfo
     * @param inType
     * @return
     */
    UserInfo updateUserInfo(UserInfo userInfo, int inType);

    /**
     * 保存用户基础信息
     *
     * @param userInfo
     * @return
     */
    UserInfo save(Long diagnosisId, SaveUserInfoVo userInfo, OtherHospitalInfo hospitalInfo, int inType);

    /**
     * 根据用户id查询用户基础信息
     *
     * @param userId
     * @return
     */
    UserInfo queryByUserId(Long userId);

    /**
     * 创建用户
     *
     * @param inType
     * @return
     */
    UserInfo create(UserInfo userInfo);

    UserInfo queryByUserIdAndUserName(Long userId, String userName);

    /**
     * 通过身份证/就诊号向HIS获取患者信息
     *
     * @param cardNo
     * @return
     */
    HisUserInfoVo queryUserInfoFromHis(String cardNo, Long userId);
    
    /**
     * 用户信息查询
     * @param map
     * @return
     */
    List<UserInfo> query(Map<String, Object> map);
}
