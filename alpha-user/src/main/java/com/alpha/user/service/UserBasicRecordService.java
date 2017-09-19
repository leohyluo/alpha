package com.alpha.user.service;

import com.alpha.server.rpc.diagnosis.pojo.UserBasicRecord;

public interface UserBasicRecordService {

	UserBasicRecord findByDiagnosisId(Long diagnosisId);
	
	void addUserBasicRecord(UserBasicRecord record);
	
	void updateUserBasicRecord(UserBasicRecord record);
}
