package com.alpha.user.service;

import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserBasicRecordService {

    UserBasicRecord findByDiagnosisId(Long diagnosisId);

    void addUserBasicRecord(UserBasicRecord record);

    void updateUserBasicRecord(UserBasicRecord record);

    void updateUserBasicRecordWithTx(UserBasicRecord record);
    
    /**
     * 获取昵称，如果是本人，昵称：您
     *
     * @param diagnosisId
     * @param userInfo
     * @return
     */
    String getUserName(Long diagnosisId, UserInfo userInfo);

    /**
     * 查询最近一个月回答过的既往史、过敏史
     *
     * @param param 用户id
     * @return 一个月回答过的既往史、过敏史
     */
    List<UserBasicRecord> findByLastMonth(Map<String, Object> param);

    /**
     * 查询用户最近一次问诊记录
     *
     * @param userId
     * @return
     */
    UserBasicRecord findLast(Long userId);
    
    /**
     * 查询用户最近一次完整的问诊记录
     *
     * @param userId
     * @return
     */
    UserBasicRecord findLastCompleted(Long userId);
    
    /**
     * 根据挂号号码查看是否已完成预问诊
     * @param userId
     * @return
     */
    UserBasicRecord findFinishByUserId(Long userId, String hisRegisterNo);
    
    /**
     * 根据用户ID查看已完成预问诊的记录
     * @param userId
     * @return
     */
    List<UserBasicRecord> listFinishByUserId(Long userId);

    /**
     * 查找主症状模板id
     *
     * @param param
     * @return
     */
    String findTemplateId(Long diagnosisId);
    
    /**
     * 查询当天未确诊的数据
     * @return
     */
    List<UserBasicRecord> listTodayUnConfirm();
}
