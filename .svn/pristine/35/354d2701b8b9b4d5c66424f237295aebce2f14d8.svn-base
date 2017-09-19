package com.alpha.user.pojo.vo;

import org.springframework.beans.BeanUtils;

import com.alpha.user.pojo.User;

public class UserVo {

	private Long id;

	private String userName;
	
	public UserVo(User u){
		BeanUtils.copyProperties(u, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
