package com.alpha.self.diagnosis.pojo.vo;

import java.io.Serializable;

public class SearchRequestVo implements Serializable {

	private static final long serialVersionUID = 1320248897835659560L;
	
	private String userId;
	private String keyword;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
