package com.alpha.user.service.impl;

import com.alpha.commons.core.service.SysSequenceService;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.UserInfoDao;
import com.alpha.user.service.UserInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoDao uesrInfoDao;

    @Resource
    private SysSequenceService sysSequenceService;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserInfo create(UserInfo userInfo) {
        Long userId = sysSequenceService.getNextSequence("user_seq").longValue();
        userInfo.setUserId(userId);
        userInfo.setExternalUserId(String.valueOf(userId));
        userInfo.setCreateTime(new Date());
        userInfo.setLastUpdateTime(new Date());
        uesrInfoDao.insert(userInfo);
        return userInfo;
    }

    /**
     * 根据第三方用户编号，渠道编号获取用户信息
     * 如果没有用户信息，则创建一个新的用户
     *
     * @param userInfo
     * @param inType
     * @return
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserInfo updateUserInfo(UserInfo userInfo, int inType) {
//		UserInfo user = userInfoMapper.getUserInfoByExternalUserId(userInfo.getExternalUserId(), inType);
        UserInfo user = uesrInfoDao.getUserInfoByExternalUserId(userInfo.getExternalUserId(), inType);

        if (user == null || user.getUserId() == 0) {
            userInfo.setUserId(sysSequenceService.getNextSequence("user_seq").longValue());
            userInfo.setInType(inType);
            userInfo.setCreateTime(new Date());
            userInfo.setLastUpdateTime(new Date());
            uesrInfoDao.insert(userInfo);
            return userInfo;
        }
        user.setLastUpdateTime(new Date());
        if (StringUtils.isNotEmpty(userInfo.getUserName()))
            user.setUserName(userInfo.getUserName());
        if (userInfo.getBirth() != null)
            user.setBirth(userInfo.getBirth());

        if (userInfo.getGender() != null)
            user.setGender(userInfo.getGender());

        if (StringUtils.isNotEmpty(userInfo.getIdcard()))
            user.setIdcard(userInfo.getIdcard());

        if (StringUtils.isNotEmpty(userInfo.getPhoneNumber()))
            user.setPhoneNumber(userInfo.getPhoneNumber());

        if (StringUtils.isNotEmpty(userInfo.getLiverFuncText()))
            user.setLiverFuncText(userInfo.getLiverFuncText());

        if (userInfo.getLiverFunc() != null)
            user.setLiverFunc(userInfo.getLiverFunc());

        if (StringUtils.isNotEmpty(userInfo.getRenalFuncText()))
            user.setRenalFuncText(userInfo.getRenalFuncText());

        if (userInfo.getRenalFunc() != null)
            user.setRenalFunc(userInfo.getRenalFunc());

        if (StringUtils.isNotEmpty(userInfo.getWeight()))
            user.setWeight(userInfo.getWeight());

        if (StringUtils.isNotEmpty(userInfo.getHeight()))
            user.setHeight(userInfo.getHeight());

        if (StringUtils.isNotEmpty(userInfo.getSpecialPeriod()))
            user.setSpecialPeriod(userInfo.getSpecialPeriod());

        if (StringUtils.isNotEmpty(userInfo.getFertilityType()))
            user.setFertilityType(userInfo.getFertilityType());

        if (StringUtils.isNotEmpty(userInfo.getGestationalAge()))
            user.setGestationalAge(userInfo.getGestationalAge());

        if (StringUtils.isNotEmpty(userInfo.getFeedType()))
            user.setFeedType(userInfo.getFeedType());

        if (StringUtils.isNotEmpty(userInfo.getPastmedicalHistoryCode()))
            user.setPastmedicalHistoryCode(userInfo.getPastmedicalHistoryCode());

        if (StringUtils.isNotEmpty(userInfo.getPastmedicalHistoryText()))
            user.setPastmedicalHistoryText(userInfo.getPastmedicalHistoryText());

        if (StringUtils.isNotEmpty(userInfo.getAllergicHistoryCode()))
            user.setAllergicHistoryCode(userInfo.getAllergicHistoryCode());

        if (StringUtils.isNotEmpty(userInfo.getAllergicHistoryText()))
            user.setAllergicHistoryText(userInfo.getAllergicHistoryText());
        uesrInfoDao.update(user);
        return user;
    }

    @Override
    public UserInfo queryByUserId(Long userId) {
        return uesrInfoDao.queryByUserId(userId);
    }

    @Override
    public UserInfo save(UserInfo userInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserInfo queryByUserIdAndUserName(Long userId, String userName) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("userName", userName);
        List<UserInfo> list = uesrInfoDao.query(param);
        UserInfo userInfo = null;
        if (CollectionUtils.isNotEmpty(list)) {
            userInfo = list.get(0);
        }
        return userInfo;
    }
}
