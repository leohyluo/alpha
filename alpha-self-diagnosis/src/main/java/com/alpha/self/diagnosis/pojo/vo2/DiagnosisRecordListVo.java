package com.alpha.self.diagnosis.pojo.vo2;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;

/**
 * 就医记录列表
 * 
 * @author Administrator
 *
 */
public class DiagnosisRecordListVo {
	
	//就诊时间
    private String cureTime;

    //确诊疾病名称
 	private String icd10;
    
	// 确诊疾病名称
	private String diseaseName;
	
	//医院名称
	private String hospitalName;
	
	//医院logo
	private String hospitalLogo;

	//挂号科室
    private String department;

    public DiagnosisRecordListVo() {}
    
    public DiagnosisRecordListVo(UserBasicRecord record) {
    	BeanCopierUtil.copy(record, this);
    }
    
	public String getCureTime() {
		return cureTime;
	}

	public void setCureTime(String cureTime) {
		this.cureTime = cureTime;
	}

	public String getIcd10() {
		return icd10;
	}

	public void setIcd10(String icd10) {
		this.icd10 = icd10;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalLogo() {
		return hospitalLogo;
	}

	public void setHospitalLogo(String hospitalLogo) {
		this.hospitalLogo = hospitalLogo;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
    
    
}
