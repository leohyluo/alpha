package com.alpha.self.diagnosis.service;

import java.util.List;
import java.util.Map;

import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.pojo.vo.vx.DiagnosisResultVo;
import com.alpha.self.diagnosis.pojo.vo.vx.DiseaseDetailVo;
import com.alpha.self.diagnosis.pojo.vo.vx.DrugDetailVo;
import com.alpha.self.diagnosis.pojo.vo.vx.DrugListVo;
import com.alpha.self.diagnosis.pojo.vo.vx.MemberListVo;
import com.alpha.self.diagnosis.pojo.vo.vx.UserDiagnosisInfoVo;
import com.alpha.user.pojo.vo.vx.UserBasicInfoVo;

public interface WecharService2 {
	
	/**
	 * 关注微信公众号
	 * @param wecharId
	 * @param userId
	 */
	void follow(String wecharId, Long userId);

	/**
	 * 获取用户预问诊信息
	 * @param userId
	 * @return
	 */
	UserDiagnosisInfoVo getUserDiagnosisInfo(Long userId);
	
	/**
	 * 就诊详情
	 * @param idcard
	 * @param diagnosisId
	 * @return 排队信息、疑似诊断结果、精选资讯
	 */
	Map<String, Object> getDiagnosisDetail(Long userId, String idcard, Long diagnosisId);
	
	/**
	 * 查询热门疾病
	 * @return
	 */
	List<DiseaseVo> listHotDisease();
	
	/**
	 * 热门药品列表
	 * @return
	 */
	List<DrugListVo> listHotDrug();
	
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
	 * 根据用户id查出所有的成员列表
	 * @param userId
	 * @return
	 */
	List<MemberListVo> listMemberListByUserId(Long userId);
	
	/**
	 * 查看用户就医记录
	 * @param userId
	 * @return
	 */
	List<DiagnosisResultVo> listUserDiagnosisRecord(Long userId);
	
	/**
	 * 获取用户成员最近健康档案
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> listMemberHealthFile(Long userId);
	
	/**
	 * 获取用户基本信息
	 * @param userId
	 * @return
	 */
	UserBasicInfoVo getUserBasicInfo(Long userId);
}
