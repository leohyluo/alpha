package com.alpha.self.diagnosis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.commons.api.tencent.offical.dto.AuthDTO;
import com.alpha.commons.core.annotation.JSON;
import com.alpha.commons.core.annotation.JSONS;
import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.commons.enums.InType;
import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.commons.web.WebUtils;
import com.alpha.self.diagnosis.pojo.dto.DiagnosisInfoDTO;
import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.pojo.vo2.ArticleDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DiagnosisRecordListVo;
import com.alpha.self.diagnosis.pojo.vo2.DiseaseDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DiseaseListVo;
import com.alpha.self.diagnosis.pojo.vo2.DrugDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DrugListVo;
import com.alpha.self.diagnosis.pojo.vo2.PreDiagnosisResultVo;
import com.alpha.self.diagnosis.pojo.vo2.UserBasicInfoVo;
import com.alpha.self.diagnosis.service.DiagnosisDiseaseService;
import com.alpha.self.diagnosis.service.HisApiService;
import com.alpha.self.diagnosis.service.OfficalAccountService;
import com.alpha.self.diagnosis.service.WecharService;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.service.UserBasicRecordService;
import com.alpha.user.service.UserInfoService;

@RestController
@RequestMapping("/wechar")
public class WeCharController {

	@Resource
	private WecharService wecharService;
	@Resource
	private HisApiService hisApiService;
	@Resource
    private DiagnosisDiseaseService diagnosisDiseaseService;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private UserBasicRecordService userBasicRecordService;
	@Resource
	private OfficalAccountService officalAccountService;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 用户信息确认
	 * @param userId
	 * @return
	 */
	@PostMapping("/user/confirm")
	@JSONS({
		@JSON(type = UserBasicInfoVo.class, include="userId,externalUserId,userName,gender,age,idcard,phoneNumber"),
		@JSON(type = PreDiagnosisResultVo.class, include="diagnosisId,hospitalName,department,cureTime,mainSymptomName,presentIllnessHistory,pastmedicalHistoryText")	
	})
	public ResponseMessage userInfoConfirm(Long userId) {
		if(userId == null) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		Map<String,Object> diagnosisMap = wecharService.getUserDiagnosisInfo(userId);
		return WebUtils.buildSuccessResponseMessage(diagnosisMap);
	}
			
	/**
	 * 就诊详情
	 * @param userId
	 * @param diagnosisId
	 * @return
	 */
	@JSONS({
		@JSON(type = PreDiagnosisResultVo.class, exclude="diseaseList,pastmedicalHistory")
	})
	@PostMapping("/diagnosis/detail")
	public ResponseMessage diagnosisDetail(Long diagnosisId) {
		UserBasicRecord basicRecord = userBasicRecordService.findByDiagnosisId(diagnosisId);
		if(basicRecord == null) {
			return WebUtils.buildResponseMessage(ResponseStatus.INVALID_VALUE);
		}
		Long userId = basicRecord.getUserId();
		UserInfo userInfo = userInfoService.queryByUserId(userId);
		String idcard = userInfo.getIdcard();
		Map<String, Object> map = wecharService.getDiagnosisDetail(userId, idcard, diagnosisId);
		return WebUtils.buildSuccessResponseMessage(map);
	}
	
	/**
	 * 查看文章详情
	 * @param articleCode
	 * @return
	 */
	@PostMapping("/article/detail")
	public ResponseMessage articleDetail(String articleCode) {
		if(StringUtils.isEmpty(articleCode)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		ArticleDetailVo articleDetailVo = wecharService.getArticleDetail(articleCode);
		return WebUtils.buildSuccessResponseMessage(articleDetailVo);
	}
	
	/**
	 * 关键字搜索疾病
	 * @param keyword
	 * @return
	 */
	@PostMapping("/disease/list")
	public ResponseMessage diseaseList(String keyword) {
        if(StringUtils.isEmpty(keyword)) {
        	return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        List<DiseaseVo> diseaseList = diagnosisDiseaseService.findByDiseaseName(keyword);
        return WebUtils.buildSuccessResponseMessage(diseaseList);
    }
	
	/**
	 * 热门疾病列表
	 * @return
	 */
	@PostMapping("/disease/hot/list")
	public ResponseMessage hotDiseaseList() {
		List<DiseaseListVo> diseaseVoList = wecharService.listHotDisease();
		return WebUtils.buildSuccessResponseMessage(diseaseVoList);
	}
	
	/**
	 * 查询疾病诊疗意见--不用此接口，因为之前已经有同样返回值的接口
	 * @param diseaseCode
	 * @return
	 */
	@PostMapping("/disease/treatschema/show")
	public ResponseMessage diseaseTreatSchema(String diseaseCode) {
		if(StringUtils.isEmpty(diseaseCode)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		DiseaseDetailVo diseaseDetailVo = wecharService.getTreatSchema(diseaseCode);
		return WebUtils.buildSuccessResponseMessage(diseaseDetailVo);
	}
	
	/**
	 * 热门药品列表
	 * @return
	 */
	@PostMapping("/drug/hot/list")
	public ResponseMessage hotDrugList() {
		List<DrugListVo> drugVoList = wecharService.listHotDrug();
		return WebUtils.buildSuccessResponseMessage(drugVoList);
	}
	
	/**
	 * 药品列表
	 * @param keyword
	 * @return
	 */
	@PostMapping("/drug/list")
	public ResponseMessage drugList(String keyword) {
		List<DrugListVo> drugList = new ArrayList<>();
		if(StringUtils.isNotEmpty(keyword)) {
			drugList = wecharService.listByKeyword(keyword);
		}
		return WebUtils.buildSuccessResponseMessage(drugList);
	}
	
	/**
	 * 药品详细信息
	 * @param drugCode
	 * @return
	 */
	@PostMapping("/drug/detail")
	public ResponseMessage drugDetail(String drugCode) {
		DrugDetailVo drugDetail = wecharService.getDrugDetail(drugCode);
		return WebUtils.buildSuccessResponseMessage(drugDetail);
	}
	
	/**
	 * 用户注册
	 * @param registerText
	 * @return
	 */
	@PostMapping("/user/register")
	public ResponseMessage register(String allParam) {
		UserInfo userInfo = com.alibaba.fastjson.JSON.parseObject(allParam,UserInfo.class);
		String phoneNumber = userInfo.getPhoneNumber();
		String externalUserId = userInfo.getExternalUserId();
		if(StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(externalUserId)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		if(userInfoService.getByPhoneNumber(phoneNumber) != null) {
			return WebUtils.buildResponseMessage(ResponseStatus.MOBILE_ALREADY_EXISTS);
		}
		userInfo.setInType(InType.WECHAR.getValue());
		userInfo = userInfoService.create(userInfo);
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userInfo.getUserId());
		return WebUtils.buildSuccessResponseMessage(map);
	}
	
	/**
	 * 查看自己和他人的就诊记录(同一微信id)
	 * @param externalUserId
	 * @return
	 */
	@PostMapping("/member/diagnosisRecord/list")
	public ResponseMessage memberList(String wecharId) {
		logger.info("invoke /member/diagnosisRecord/list start");
		if(StringUtils.isEmpty(wecharId)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		//UserInfo userInfo = userInfoService.queryByExternalUserId(wecharId, InType.HIS.getValue());
		UserInfo userInfo = userInfoService.getSelfUserInfoByExternalUserId(wecharId);
		if(userInfo == null) {
			return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
		}
		List<Map<String, Object>> memberDiagnosisRecordList = wecharService.listMemberDiagnosisDetail(userInfo.getUserId());
		return WebUtils.buildSuccessResponseMessage(memberDiagnosisRecordList);
	}
	
	/**
	 * 获取用户就诊记录
	 * @param userId 用户id
	 * @return
	 */
	@PostMapping("/diagnosisRecord/list")
	public ResponseMessage getUserDiagnosisRecord(Long userId) {
		if(userId == null) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		List<DiagnosisRecordListVo> diagnosisRecordListVo = wecharService.listUserDiagnosisRecord(userId);
		return WebUtils.buildSuccessResponseMessage(diagnosisRecordListVo);
	}
	
	/**
	 * 获取成员健康档案
	 * @param wecharId
	 * @return
	 */
	@PostMapping("/member/healthFile")
	public ResponseMessage memberHealthFiles(String wecharId) {
		if(StringUtils.isEmpty(wecharId)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		UserInfo userInfo = userInfoService.getSelfUserInfoByExternalUserId(wecharId);
		//UserInfo userInfo = userInfoService.queryByExternalUserId(wecharId);
		if(userInfo == null) {
			return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
		}
		List<Map<String, Object>> list = wecharService.listMemberHealthFile(userInfo.getUserId());
		return WebUtils.buildSuccessResponseMessage(list);
	}
	
	/**
	 * 获取个人资料
	 * @param userId
	 * @return
	 */
	@PostMapping("/user/detail")
	public ResponseMessage personalInfo(Long userId) {
		UserBasicInfoVo basicInfo = wecharService.getUserBasicInfo(userId);
		return WebUtils.buildSuccessResponseMessage(basicInfo);
	}
	
	@PostMapping("/test/hisDiagnosisResult/get")
	public ResponseMessage testHisDiagnosisResult(Long diagnosisId) {
		DiagnosisInfoDTO dto = hisApiService.getDiagnosisInfo("A001", "");
		UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
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
		return WebUtils.buildSuccessResponseMessage();
	}
	
	/**
	 * 通过code换取网页授权access_token
	 * @param code
	 * @return
	 */
	@PostMapping("/webAuthAccessToken/get")
	public ResponseMessage getWebAccessToken(String code) {
		logger.info("get wechat web accessToken start");
		if(StringUtils.isEmpty(code)) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
		AuthDTO authDto = officalAccountService.getAuthAccessToken(code);
		return WebUtils.buildSuccessResponseMessage(authDto);
	}
	
	@GetMapping("/menu/create")
	public ResponseMessage createMenu() {
		officalAccountService.createMenu();
		return WebUtils.buildSuccessResponseMessage();
	}
}
