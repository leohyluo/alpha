package com.alpha.user.service;

import java.util.List;
import java.util.Map;

import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.pojo.vo.HisUserInfoVo;
import com.alpha.user.pojo.vo.OtherHospitalInfo;
import com.alpha.user.pojo.vo.SaveUserInfoVo;

public interface UserInfoService {
	
	/**
	 * 用户通过微信公众号关注预问诊
	 * @param userInfo
	 * @param wecharId
	 * @return
	 */
	UserInfo follow(UserInfo userInfo, String wecharId);
	
    /**
     * 根据第三方用户编号，渠道编号获取用户信息
     * 如果没有用户信息，则创建一个新的用户
     *
     * @param userInfo
     * @param inType
     * @return
     */
    UserInfo createOrUpdateUserInfo(UserInfo userInfo, int inType);
    
    /**
     * 更新用户信息
     * @param userInfo
     * @param inType
     * @return
     */
    UserInfo updateUserInfo(UserInfo userInfo);

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
     * 根据第三方渠道的用户id查询用户基础信息
     * @param externalUserId
     * @param inType
     * @return
     */
    //UserInfo queryByExternalUserId(String externalUserId, int inType);
    
    /**
     * 根据externalUserId获取自己用户信息
     * @param externalUserId
     * @return
     */
    UserInfo getSelfUserInfoByExternalUserId(String externalUserId);
    
    List<UserInfo> listByExternalUserId(String externalUserId);

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
    
    /**
     * 获取用户/用户成员列表
     * @param userId
     * @return
     */
    List<HisUserInfoVo> list(Long userId);
    
    /**
     * 保存用户成员
     * @param userId
     * @param memberName
     */
    HisUserInfoVo saveUserMember(Long userId, String memberName);
    
    /**
     * 通过电话号码获取信息
     * @param phoneNumber
     * @return
     */
    UserInfo getByPhoneNumber(String phoneNumber);
    
    /**
     * 根据用户id查询下属成员的信息
     * @param userId
     * @return
     */
    List<UserInfo> listUserMemberInfo(Long userId);
    
    /**
     * 保存用户信息
     * @param userInfo
     */
    void save(UserInfo userInfo);
    
    /**
     * 查询用户
     * @param userIdList
     * @return
     */
    List<UserInfo> listByUserId(List<Long> userIdList);
    
    
}
