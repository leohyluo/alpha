package com.alpha.self.diagnosis.pojo.vo.vx;

import java.util.List;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;

/**
 * 微信公众号-信息确认实体类
 * @author Administrator
 *
 */
public class UserDiagnosisInfoVo {

	private Long userId;

	// 第三方用户编号，用来同步第三方用户信息
	private String externalUserId;

	// 姓名
	private String userName;

	//年龄
	private String age;

	// 性别
	private Integer gender;

	// 身份证号码
	private String idcard;

	// 电话号码
	private String phoneNumber;
	
	//诊断信息列表
	private List<DiagnosisResultVo> diagnosisInfoList;
	
	public UserDiagnosisInfoVo(UserInfo userInfo) {
		BeanCopierUtil.copy(userInfo, this);
		if(userInfo.getBirth() != null) {
			this.age = DateUtils.getAgeText(userInfo.getBirth());
		}
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getExternalUserId() {
		return externalUserId;
	}

	public void setExternalUserId(String externalUserId) {
		this.externalUserId = externalUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<DiagnosisResultVo> getDiagnosisInfoList() {
		return diagnosisInfoList;
	}

	public void setDiagnosisInfoList(List<DiagnosisResultVo> diagnosisInfoList) {
		this.diagnosisInfoList = diagnosisInfoList;
	}

}
