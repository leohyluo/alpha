package com.alpha.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.server.rpc.diagnosis.pojo.UserBasicRecord;
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

}
