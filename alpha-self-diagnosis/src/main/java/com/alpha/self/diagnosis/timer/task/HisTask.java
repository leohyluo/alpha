package com.alpha.self.diagnosis.timer.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.self.diagnosis.pojo.dto.DiagnosisInfoDTO;
import com.alpha.self.diagnosis.service.HisApiService;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.user.service.UserBasicRecordService;

/**
 * 与His交互定时任务
 * @author Administrator
 *
 */
@Component
public class HisTask {
	
	@Resource
	private UserBasicRecordService userBasicRecordService;
	@Resource
	private HisApiService hisApiService;

	/**
	 * 调用His接口更新问诊状态(每5分钟请求一次)
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	public void updateDiagnosisResult() {
		List<UserBasicRecord> basicRecordList = userBasicRecordService.listTodayUnConfirm();
		for(UserBasicRecord record : basicRecordList) {
			String hospitalCode = record.getHospitalCode();
			String hisRegisterNo = record.getHisRegisterNo();
			if(StringUtils.isEmpty(hospitalCode) || StringUtils.isEmpty(hisRegisterNo)) {
				continue;
			}
			DiagnosisInfoDTO dto = hisApiService.getDiagnosisInfo(hospitalCode, hisRegisterNo);
			if(dto == null) {
				continue;
			}
			String icd10 = dto.getIcd10();
			String diseaseName = dto.getDiseaseName();
			if(StringUtils.isNotEmpty(icd10) || StringUtils.isNotEmpty(diseaseName)) {
				record.setStatus(DiagnosisStatus.HIS_CONFIRMED.getValue());
				record.setIcd10(icd10);
				record.setDiseaseName(diseaseName);
				record.setCheckList(dto.getCheckList());
				record.setDrugList(dto.getDrugList());
				record.setUpdateTime(new Date());
				userBasicRecordService.updateUserBasicRecord(record);
			}
		}
	}
}
