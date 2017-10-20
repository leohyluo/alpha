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
}
