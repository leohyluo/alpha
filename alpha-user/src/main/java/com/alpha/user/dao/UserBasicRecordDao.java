package com.alpha.user.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;

import java.util.List;
import java.util.Map;

public interface UserBasicRecordDao extends IBaseDao<UserBasicRecord, Long> {

    UserBasicRecord findLast(Long userId);
    
    /**
     * 查询用户最近一次完整的问诊记录
     *
     * @param userId
     * @return
     */
    UserBasicRecord findLastCompleted(Long userId);

    UserBasicRecord findByDiagnosisId(Long diagnosisId);

    /**
     * 查询最近一个月回答过的既往史、过敏史
     *
     * @param param 用户id
     * @return 一个月回答过的既往史、过敏史
     */
    List<UserBasicRecord> findByLastMonth(Map<String, Object> param);

    /**
     * 查找主症状模板id
     *
     * @param param
     * @return
     */
    String findTemplateId(Long diagnosisId);
    
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
     * 根据用户ID查看当天已完成预问诊的记录
     * @param userId
     * @return
     */
    List<UserBasicRecord> listTodayFinishByUserId(Long userId);
    
    /**
     * 查询当天未确诊的数据
     * @return
     */
    List<UserBasicRecord> listTodayUnConfirm();
    
    /**
     * 获取用户所有就诊记录 
     * @param userId
     * @return
     */
    List<UserBasicRecord> listByUserId(Long userId);
    
    /**
     * 查询用户最后一条就诊记录
     * @param userId
     * @return
     */
    UserBasicRecord getLastFinishByUserId(Long userId);
}
