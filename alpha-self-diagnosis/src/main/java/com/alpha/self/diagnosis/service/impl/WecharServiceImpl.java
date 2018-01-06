package com.alpha.self.diagnosis.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alpha.commons.constants.GlobalConstants;
import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.commons.enums.InType;
import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.self.diagnosis.dao.DiagnosisArticleDao;
import com.alpha.self.diagnosis.dao.DrugDao;
import com.alpha.self.diagnosis.pojo.dto.QueueDTO;
import com.alpha.self.diagnosis.pojo.vo2.ArticleListVo;
import com.alpha.self.diagnosis.pojo.vo2.DiagnosisRecordListVo;
import com.alpha.self.diagnosis.pojo.vo2.DiseaseDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DiseaseListVo;
import com.alpha.self.diagnosis.pojo.vo2.DrugDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DrugListVo;
import com.alpha.self.diagnosis.pojo.vo2.HisDiagnosisResultVo;
import com.alpha.self.diagnosis.pojo.vo2.MemberListVo;
import com.alpha.self.diagnosis.pojo.vo2.PreDiagnosisResultVo;
import com.alpha.self.diagnosis.pojo.vo2.QueueInfoVo;
import com.alpha.self.diagnosis.pojo.vo2.UserBasicInfoVo;
import com.alpha.self.diagnosis.service.DiagnosisService;
import com.alpha.self.diagnosis.service.HisApiService;
import com.alpha.self.diagnosis.service.UserDiagnosisOutcomeService;
import com.alpha.self.diagnosis.service.WecharService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisArticle;
import com.alpha.server.rpc.diagnosis.pojo.DrugOnSellDetail;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.treatscheme.dao.DiagnosisDiseaseDao;
import com.alpha.user.dao.UserBasicRecordDao;
import com.alpha.user.dao.UserMemberDao;
import com.alpha.user.service.UserInfoService;

@Service
public class WecharServiceImpl implements WecharService {
	
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private HisApiService hisApiService;
	@Resource
    private UserDiagnosisOutcomeService userDiagnosisOutcomeService;
	@Resource
    private DiagnosisService diagnosisService;
	@Resource
	private UserBasicRecordDao userBasicRecordDao;
	@Resource
	private DiagnosisArticleDao diagnosisArticleDao;
	@Resource
	private DiagnosisDiseaseDao diagnosisDiseaseDao;
	@Resource
	private DrugDao drugDao; 
	@Resource
	private UserMemberDao userMemberDao;


	@Override
	public void follow(String wecharId, Long userId) {
		UserInfo userInfo = userInfoService.queryByUserId(userId);
		if(userInfo != null) {
			userInfo.setExternalUserId(wecharId);
			userInfoService.updateUserInfo(userInfo, InType.WECHAR.getValue());
		}
	}
	
	@Override
	public Map<String, Object> getUserDiagnosisInfo(Long userId) {
		UserInfo userInfo = userInfoService.queryByUserId(userId);
		List<UserBasicRecord> recordList = userBasicRecordDao.listTodayFinishByUserId(userId);
		
		//基本信息
		UserBasicInfoVo userBasicInfoVo = new UserBasicInfoVo(userInfo);
		//就诊信息
		List<PreDiagnosisResultVo> preDiagnosisResultVoList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(recordList)) {
			for(UserBasicRecord itemRecord : recordList) {
				PreDiagnosisResultVo preDiagnosisResultVo = new PreDiagnosisResultVo(itemRecord);
				preDiagnosisResultVoList.add(preDiagnosisResultVo);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userBasicInfo", userBasicInfoVo);
		map.put("preDiagnosisResult", preDiagnosisResultVoList);
		return map;
	}

	@Override
	public Map<String, Object> getDiagnosisDetail(Long userId, String idcard, Long diagnosisId) {
		Map<String, Object> map = new HashMap<>();
		UserBasicRecord userBasicRecord = userBasicRecordDao.findByDiagnosisId(diagnosisId);
		//预问诊结果
		PreDiagnosisResultVo preDiagnosisResultVo = new PreDiagnosisResultVo(userBasicRecord);
		map.put("preDiagnosisResult", preDiagnosisResultVo);
		
		//精彩资讯
		String mainSympCode = userBasicRecord.getMainSymptomCode();
		List<DiagnosisArticle> articleList = diagnosisArticleDao.listTop5ByMainSympCode(mainSympCode);
		List<ArticleListVo> articleVoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(articleList)) {
			for (DiagnosisArticle article : articleList) {
				ArticleListVo articleVo = new ArticleListVo(article);
				articleVoList.add(articleVo);
			}
		}
		map.put("articleList", articleVoList);
		//如果医院已确诊返回预问诊信息、医院确诊信息
		if(DiagnosisStatus.HIS_CONFIRMED.getValue().equals(userBasicRecord.getStatus())) {
			HisDiagnosisResultVo hisDiagnosisResultVo = new HisDiagnosisResultVo(userBasicRecord);
			String diseaseName = userBasicRecord.getDiseaseName();
			List<DiagnosisDisease> diseaseList = diagnosisDiseaseDao.listByDiseaseName(diseaseName);
			if(CollectionUtils.isNotEmpty(diseaseList)) {
				DiagnosisDisease disease = diseaseList.get(0);
				hisDiagnosisResultVo.setHasTreatSchema("Y");
				hisDiagnosisResultVo.setDiseaseCode(disease.getDiseaseCode());
			}
			map.put("hisDiagnosisResult", hisDiagnosisResultVo);
			return map;
		}
		//排队提醒
		QueueDTO queueDto = hisApiService.getQueuingInfo(idcard);
		QueueInfoVo queueVo = new QueueInfoVo(queueDto);
		map.put("queueInfo", queueVo);
		//疑似结果
		List<DiseaseListVo> diseaseListVoList = new ArrayList<>();
		List<UserDiagnosisOutcome> udoList = userDiagnosisOutcomeService.listTop5UserDiagnosisOutcome(diagnosisId);
		if(CollectionUtils.isNotEmpty(udoList)) {
			for(UserDiagnosisOutcome udo : udoList) {
				DiseaseListVo itemResultVo = new DiseaseListVo(udo);
				diseaseListVoList.add(itemResultVo);
			}
		}
		map.put("diseaseList", diseaseListVoList);
		return map;
	}

	@Override
	public List<DiseaseListVo> listHotDisease() {
		List<DiseaseListVo> diseasevoList = new ArrayList<>();
		List<DiagnosisDisease> hotDiseaseList = diagnosisDiseaseDao.listUserHotDisease();
		diseasevoList.addAll(hotDiseaseList.stream().map(DiseaseListVo::new).collect(toList()));
		int remainSize = GlobalConstants.HOT_DISEASE_COUNT - hotDiseaseList.size();
		if(remainSize > 0) {
			List<DiagnosisDisease> defaultDiseaseList = diagnosisDiseaseDao.listAnyDiseaseOrderByDefaultOrder(remainSize);
			diseasevoList.addAll(defaultDiseaseList.stream().map(DiseaseListVo::new).collect(toList()));
		}
		return diseasevoList;
	}

	@Override
	public List<DrugListVo> listHotDrug() {
		List<DrugListVo> drugvoList = new ArrayList<>();
		List<DrugOnSellDetail> hotDiseaseList = drugDao.listUserHotDrug();
		drugvoList.addAll(hotDiseaseList.stream().map(e->BeanCopierUtil.uniformCopy(e, DrugListVo::new)).collect(toList()));
		int remainSize = GlobalConstants.HOT_DRUG_COUNT - hotDiseaseList.size();
		if(remainSize > 0) {
			List<DrugOnSellDetail> defaultDiseaseList = drugDao.listAnyDrugOrderByDefaultOrder(remainSize);
			drugvoList.addAll(defaultDiseaseList.stream().map(e->BeanCopierUtil.uniformCopy(e, DrugListVo::new)).collect(toList()));
		}
		return drugvoList;
	}

	@Override
	public DiseaseDetailVo getTreatSchema(String diseaseCode) {
		DiagnosisDisease disease = diagnosisDiseaseDao.getDiagnosisDisease(diseaseCode);
		Long userSelectCount = disease.getUserSelectCount() == null ? 0L : disease.getUserSelectCount();
		userSelectCount++;
		disease.setUserSelectCount(userSelectCount);
		diagnosisDiseaseDao.update(disease);
		DiseaseDetailVo detailVo = new DiseaseDetailVo(disease);
		return detailVo;
	}

	@Override
	public List<DrugListVo> listByKeyword(String keyword) {
		List<DrugOnSellDetail> drugList = drugDao.listByKeyword(keyword);
		List<DrugListVo> resultList = drugList.stream().map(e->BeanCopierUtil.uniformCopy(e, DrugListVo::new)).collect(toList());
		return resultList;
	}

	@Override
	public DrugDetailVo getDrugDetail(String drugCode) {
		DrugOnSellDetail drug = drugDao.getByDrugCode(drugCode);
		if(drug != null) {
			return BeanCopierUtil.uniformCopy(drug, DrugDetailVo::new);
		}
		return null;
	}

	@Override
	public List<MemberListVo> listMemberListByUserId(Long userId) {
		List<UserInfo> userInfoList = userInfoService.listUserMemberInfo(userId);
		return userInfoList.stream().map(MemberListVo::new).collect(toList());
	}

	@Override
	public List<DiagnosisRecordListVo> listUserDiagnosisRecord(Long userId) {
		List<UserBasicRecord> recordList = userBasicRecordDao.listByUserId(userId);
		List<DiagnosisRecordListVo> voList = recordList.stream().map(DiagnosisRecordListVo::new).collect(toList());
		return voList;
	}

	@Override
	public List<Map<String, Object>> listMemberHealthFile(Long userId) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<MemberListVo> membervoList = this.listMemberListByUserId(userId);
		for(MemberListVo member : membervoList) {
			Map<String, Object> memberMap = new HashMap<>();
			memberMap.put("memberInfo", member);
			UserBasicRecord record = userBasicRecordDao.getLastFinishByUserId(userId);
			HisDiagnosisResultVo hisDiagnosisResultVo = null;
			if(record != null) {
				hisDiagnosisResultVo = new HisDiagnosisResultVo(record);
			}
			memberMap.put("hisDiagnosisResult", hisDiagnosisResultVo);
			resultList.add(memberMap);
		}
		return resultList;
	}

	@Override
	public UserBasicInfoVo getUserBasicInfo(Long userId) {
		UserInfo userInfo = userInfoService.queryByUserId(userId);
		UserBasicInfoVo basicInfo = new UserBasicInfoVo(userInfo);
		return basicInfo;
	}
}
