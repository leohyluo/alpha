package com.alpha.self.diagnosis.pojo.dto;

/**
 * 数据传输类-排队提醒
 * @author Administrator
 *
 */
public class QueueDTO {

	//提示
	private String tip;
	// 姓名
	private String userName;
	// 年龄
	private String age;
	// 性别
	private Integer gender;
	//医院
	private String hospitalName;
	// 挂号科室
	private String department;
	//排队号码
	private String queuingNumber;
	//前面等待数量
	private String prevQueuingNumber;
	//预估等待时间
	private String waitTime;
	//排队状态(未排队、排队中)
	private String status;
	//时间
	private String currentTime;
	
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getQueuingNumber() {
		return queuingNumber;
	}
	public void setQueuingNumber(String queuingNumber) {
		this.queuingNumber = queuingNumber;
	}
	public String getPrevQueuingNumber() {
		return prevQueuingNumber;
	}
	public void setPrevQueuingNumber(String prevQueuingNumber) {
		this.prevQueuingNumber = prevQueuingNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
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
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	
	
}
