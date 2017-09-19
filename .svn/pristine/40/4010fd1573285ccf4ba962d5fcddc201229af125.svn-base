package com.alpha.self.diagnosis.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.UserDiagnosisDetail;

import java.util.List;

/**
 * Created by xc.xiong on 2017/9/5.
 */
public interface UserDiagnosisDetailDao extends IBaseDao<UserDiagnosisDetail, Long> {

    /**
     * 查询问题
     *
     * @param diagnosisId
     * @param questionCode
     * @return
     */
    UserDiagnosisDetail getUserDiagnosisDetail(Long diagnosisId, String questionCode);

    /**
     * 查询问题
     *
     * @param diagnosisId
     * @return
     */
    List<UserDiagnosisDetail> listUserDiagnosisDetail(Long diagnosisId);

}
