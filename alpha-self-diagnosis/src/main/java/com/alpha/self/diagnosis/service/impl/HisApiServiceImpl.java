package com.alpha.self.diagnosis.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alpha.commons.enums.APICode;
import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.commons.util.DateUtils;
import com.alpha.self.diagnosis.dao.HospitalApiDao;
import com.alpha.self.diagnosis.pojo.dto.DiagnosisInfoDTO;
import com.alpha.self.diagnosis.pojo.dto.QueueDTO;
import com.alpha.self.diagnosis.service.HisApiService;
import com.alpha.server.rpc.diagnosis.pojo.HospitalApi;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.user.service.UserBasicRecordService;

@Service
public class HisApiServiceImpl implements HisApiService {
	
	@Resource
	private HospitalApiDao hospitalApiDao;
	@Resource
	private UserBasicRecordService userBasicRecordService; 
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public QueueDTO getQueuingInfo(Long diagnosisId, String idcard) {
		UserBasicRecord basicRecord = userBasicRecordService.findByDiagnosisId(diagnosisId);
		// 模拟His返回排队信息json字符串
		String department = "未知";
		String doctorName = "未知";
		if(basicRecord == null) {
			logger.error("根据就诊id{}无法找到就诊记录", diagnosisId);
		} else {
			department = basicRecord.getDepartment();
			doctorName = basicRecord.getDoctorName();
		}
		JSONObject json = new JSONObject();
		json.put("tip", "提示:排队中");
		json.put("queuingNumber", "A502");
		json.put("department", department);
		json.put("doctorName", doctorName);
		json.put("prevQueuingNumber", "67人");
		json.put("status", DiagnosisStatus.IN_QUEUE.getText());
		json.put("currentTime", DateUtils.date2String(new Date(), DateUtils.DATE_FORMAT));

		QueueDTO queueDto = JSON.parseObject(json.toJSONString(), QueueDTO.class);
		return queueDto;
	}

	@Override
	public DiagnosisInfoDTO getDiagnosisInfo(String hospitalCode, String hisRegisterNo) {
		List<HospitalApi> apiList = hospitalApiDao.listByHospitalCode(hospitalCode);
		String apiUrl = "";
		for(HospitalApi item : apiList) {
			if(APICode.GET_DIAGNOSISINFO.getValue().equals(item.getApiCode())) {
				apiUrl = item.getApiUrl();
				break;
			}
		}
		if(StringUtils.isEmpty(apiUrl)){
			logger.error("api地址为空导致无法调用医院API获取诊断结果");
			return null;
		}
		
		// 模拟His返回排队信息json字符串
		JSONObject json = new JSONObject();
		json.put("hisRegisterNo", "001");
		json.put("icd10", "x001");
		json.put("diseaseName", "ACEI所致咳嗽");
		json.put("status", "12");
		json.put("memo", "小心着凉");

		//构建检查
		JSONArray checkList = new JSONArray();
		JSONObject check1 = new JSONObject();
		check1.put("code", "001");
		check1.put("name", "心电图");
		checkList.add(check1);
		JSONObject check2 = new JSONObject();
		check2.put("code", "002");
		check2.put("name", "胃镜");
		checkList.add(check2);
		json.put("checkList", checkList.toJSONString());
		//构建药品
		JSONArray drugList = new JSONArray();
		JSONObject drug1 = new JSONObject();
		drug1.put("code", "001");
		drug1.put("name", "安奶近");
		drugList.add(drug1);
		JSONObject drug2 = new JSONObject();
		drug2.put("code", "002");
		drug2.put("name", "阿莫西林");
		drugList.add(drug2);
		json.put("drugList", drugList.toJSONString());

		DiagnosisInfoDTO diagnosisInfoDTO = JSON.parseObject(json.toJSONString(), DiagnosisInfoDTO.class);
		return diagnosisInfoDTO;
	}

}
