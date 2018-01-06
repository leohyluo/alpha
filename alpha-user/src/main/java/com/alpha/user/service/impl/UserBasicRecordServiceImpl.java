package com.alpha.user.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.UserBasicRecordDao;
import com.alpha.user.service.UserBasicRecordService;

@Service
@Transactional
public class UserBasicRecordServiceImpl implements UserBasicRecordService {

    @Resource
    private UserBasicRecordDao dao;

    @Override
    public UserBasicRecord findByDiagnosisId(Long diagnosisId) {
        return dao.findByDiagnosisId(diagnosisId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateUserBasicRecord(UserBasicRecord record) {
        dao.update(record);
    }

    @Override
    public void addUserBasicRecord(UserBasicRecord record) {
        dao.insert(record);
    }

    /**
     * 获取昵称，如果是本人，昵称：您
     *
     * @param diagnosisId
     * @param userInfo
     * @return
     */
    public String getUserName(Long diagnosisId, UserInfo userInfo) {
        UserBasicRecord record = this.findByDiagnosisId(diagnosisId);
        String userName = userInfo.getUserName();
        if (record != null && record.getUserId().longValue() == userInfo.getUserId().longValue()) {
            userName = "您";
        }
        userName = StringUtils.isEmpty(userName) ? "您" : userName;
        return userName;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBasicRecord> findByLastMonth(Map<String, Object> param) {
        return dao.findByLastMonth(param);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateUserBasicRecordWithTx(UserBasicRecord record) {
        dao.update(record);
    }

    @Override
    public UserBasicRecord findLast(Long userId) {
        return dao.findLast(userId);
    }

    @Override
    public String findTemplateId(Long diagnosisId) {
        return dao.findTemplateId(diagnosisId);
    }

	@Override
	public UserBasicRecord findLastCompleted(Long userId) {
		return dao.findLastCompleted(userId);
	}

	@Override
	public UserBasicRecord findFinishByUserId(Long userId, String hisRegisterNo) {
		return dao.findFinishByUserId(userId, hisRegisterNo);
	}

	@Override
	public List<UserBasicRecord> listFinishByUserId(Long userId) {
		return dao.listFinishByUserId(userId);
	}

	@Override
	public List<UserBasicRecord> listTodayUnConfirm() {
		return dao.listTodayUnConfirm();
	}
}
