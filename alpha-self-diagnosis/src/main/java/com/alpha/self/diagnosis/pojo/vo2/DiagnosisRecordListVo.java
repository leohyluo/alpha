package com.alpha.self.diagnosis.pojo.vo2;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.alpha.commons.constants.GlobalConstants;
import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;

/**
 * 就医记录列表
 * 
 * @author Administrator
 *
 */
public class DiagnosisRecordListVo {
	
	private Long diagnosisId;
	
	//就诊时间
    private String createTime;

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
    
    //就诊日期
    private String cureDate;
    
    //就诊时间
    private String cureTime;

    public DiagnosisRecordListVo() {}
    
    public DiagnosisRecordListVo(UserBasicRecord record) {
    	BeanCopierUtil.copy(record, this);
    	if(record.getCreateTime() != null) {
    		this.createTime = DateUtils.date2String(record.getCreateTime(), DateUtils.DATE_TIME_FORMAT);
    	}
    	Date cureTime = record.getCureTime();
    	if(cureTime != null) {
    		String cureTimeStr = DateUtils.date2String(cureTime, DateUtils.DATE_TIME_FORMAT);
    		String[] dateArr = cureTimeStr.split(" ");
    		if(dateArr.length == 2) {
    			this.cureDate = dateArr[0];
    			this.cureTime = dateArr[1];
    		}
    	}
    	if(StringUtils.isNotEmpty(this.hospitalLogo)) {
    		this.hospitalLogo = GlobalConstants.IMAGE_BASE_URL + "/" + this.hospitalLogo;
    	} else {
    		this.hospitalLogo = GlobalConstants.IMAGE_BASE_URL + "/" + "hospital_default.png";
    	}
    }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getCureDate() {
		return cureDate;
	}

	public void setCureDate(String cureDate) {
		this.cureDate = cureDate;
	}

	public String getCureTime() {
		return cureTime;
	}

	public void setCureTime(String cureTime) {
		this.cureTime = cureTime;
	}

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
    
    
}
