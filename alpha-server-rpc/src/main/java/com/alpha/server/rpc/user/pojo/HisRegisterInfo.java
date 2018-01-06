package com.alpha.server.rpc.user.pojo;

import java.util.Date;

/**
 * 医院挂号信息
 * @author Administrator
 *
 */
public class HisRegisterInfo {

	//医院编码
	private String hospitalCode;
	//医生姓名
	private String doctorName;
	//就诊日期
	private Date cureTime;
	//挂号科室
	private String department;
	//医院挂号号码
	private String hisRegisterNo;
	//状态(1:排除中 10:预问诊结束)
	private String status;
	
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Date getCureTime() {
		return cureTime;
	}
	public void setCureTime(Date cureTime) {
		this.cureTime = cureTime;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getHisRegisterNo() {
		return hisRegisterNo;
	}
	public void setHisRegisterNo(String hisRegisterNo) {
		this.hisRegisterNo = hisRegisterNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	
}
