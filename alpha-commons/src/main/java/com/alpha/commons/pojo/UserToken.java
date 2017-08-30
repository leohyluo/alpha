package com.alpha.commons.pojo;

import java.time.LocalDateTime;

public class UserToken {

	private String id;
	
	private String account;

	private String token;

	private String hospitalCode;

	private LocalDateTime lastupdTime;
	
	private Long fetchInterval;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public LocalDateTime getLastupdTime() {
		return lastupdTime;
	}

	public void setLastupdTime(LocalDateTime lastupdTime) {
		this.lastupdTime = lastupdTime;
	}

	public Long getFetchInterval() {
		return fetchInterval;
	}

	public void setFetchInterval(Long fetchInterval) {
		this.fetchInterval = fetchInterval;
	}
	
	
}
