package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.dto.DiagnosisInfoDTO;
import com.alpha.self.diagnosis.pojo.dto.QueueDTO;

/**
 * his对接业务接口
 * @author Administrator
 *
 */
public interface HisApiService {

	/**
	 * 获取排队信息
	 * @param externalUserId
	 * @return
	 */
	QueueDTO getQueuingInfo(String idcard);
	
	/**
	 * 获取诊断信息
	 * @param hospitalCode 医院编码
	 * @param hisRegisterNo 挂号号码
	 * @return
	 */
	DiagnosisInfoDTO getDiagnosisInfo(String hospitalCode, String hisRegisterNo);
}
