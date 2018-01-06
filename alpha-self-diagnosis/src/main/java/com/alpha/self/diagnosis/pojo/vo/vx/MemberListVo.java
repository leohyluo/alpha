package com.alpha.self.diagnosis.pojo.vo.vx;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;

/**
 * 用户成员列表
 * @author Administrator
 *
 */
public class MemberListVo {

	private Long userId;
	private String userName;
	private String age;
	private Integer gender;
	private String idcard;
	
	public MemberListVo() {}
	
	public MemberListVo(UserInfo userInfo) {
		BeanCopierUtil.copy(userInfo, this);
		if(userInfo.getBirth() != null)
			this.age = DateUtils.getAgeText(userInfo.getBirth());
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	
	
}
