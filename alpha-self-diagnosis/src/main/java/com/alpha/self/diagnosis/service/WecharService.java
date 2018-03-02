package com.alpha.self.diagnosis.service;

import java.util.List;
import java.util.Map;

import com.alpha.self.diagnosis.pojo.vo2.ArticleDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DiagnosisRecordListVo;
import com.alpha.self.diagnosis.pojo.vo2.DiseaseDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DiseaseListVo;
import com.alpha.self.diagnosis.pojo.vo2.DrugDetailVo;
import com.alpha.self.diagnosis.pojo.vo2.DrugListVo;
import com.alpha.self.diagnosis.pojo.vo2.UserBasicInfoVo;

public interface WecharService {
	

	/**
	 * 获取用户预问诊信息
	 * @param userId
	 * @return 用户基本信息、预问诊信息
	 */
	Map<String, Object> getUserDiagnosisInfo(Long userId);
	
	/**
	 * 就诊详情
	 * @param idcard
	 * @param diagnosisId
	 * @return 排队信息、疑似诊断结果、精选资讯
	 */
	Map<String, Object> getDiagnosisDetail(Long userId, String idcard, Long diagnosisId);
	
	/**
	 * 获取文章详情
	 * @param articleCode
	 * @return
	 */
	ArticleDetailVo getArticleDetail(String articleCode);
	
	/**
	 * 查询热门疾病
	 * @return
	 */
	List<DiseaseListVo> listHotDisease();
	
	/**
	 * 热门药品列表
	 * @return
	 */
	List<com.alpha.self.diagnosis.pojo.vo2.DrugListVo> listHotDrug();
	
	/**
	 * 查看疾病治疗意见
	 * @param diseaseCode
	 * @return
	 */
	DiseaseDetailVo getTreatSchema(String diseaseCode);
	
	/**
	 * 根据关键字搜索疾病
	 * @param keyword
	 * @return
	 */
	List<DrugListVo> listByKeyword(String keyword);
	
	/**
	 * 查看药品说明书
	 * @param drugCode
	 * @return
	 */
	DrugDetailVo getDrugDetail(String drugCode);
	
	/**
	 * 根据用户id查出所有的成员列表及每个成员的就医记录
	 * @param userId
	 * @return 用户成员、就诊记录
	 */
	List<Map<String, Object>> listMemberDiagnosisDetail(Long userId);
		
	/**
	 * 查看用户就医记录
	 * @param userId
	 * @return
	 */
	List<DiagnosisRecordListVo> listUserDiagnosisRecord(Long userId);
	
	/**
	 * 获取用户成员最近健康档案
	 * @param userId
	 * @return 用户成员列表、最近身体状况时间、医生诊断结果
	 */
	List<Map<String, Object>> listMemberHealthFile(Long userId);
	
	/**
	 * 获取用户基本信息
	 * @param userId
	 * @return
	 */
	UserBasicInfoVo getUserBasicInfo(Long userId);
	
	
}
