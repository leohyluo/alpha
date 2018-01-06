package com.alpha.self.diagnosis.pojo.vo2;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;

/**
 * 医生诊断结果
 * 
 * @author Administrator
 *
 */
public class HisDiagnosisResultVo {

	// 挂号码
	private String hisRegisterNo;

	// icd10
	private String icd10;

	//疾病编码
	private String diseaseCode;
	
	// 确诊疾病名称
	private String diseaseName;

	// 检查列表(json数组)
	private String checkList;

	// 药品列表(json数组)
	private String drugList;

	// 备注
	private String memo;
	
	//是否有治疗意见
	private String hasTreatSchema;
	
	public String getHasTreatSchema() {
		return hasTreatSchema;
	}

	public void setHasTreatSchema(String hasTreatSchema) {
		this.hasTreatSchema = hasTreatSchema;
	}

	public HisDiagnosisResultVo() {}
	
	public HisDiagnosisResultVo(UserBasicRecord record) {
		BeanCopierUtil.copy(record, this);
	}

	public String getHisRegisterNo() {
		return hisRegisterNo;
	}

	public void setHisRegisterNo(String hisRegisterNo) {
		this.hisRegisterNo = hisRegisterNo;
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

	public String getCheckList() {
		return checkList;
	}

	public void setCheckList(String checkList) {
		this.checkList = checkList;
	}

	public String getDrugList() {
		return drugList;
	}

	public void setDrugList(String drugList) {
		this.drugList = drugList;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDiseaseCode() {
		return diseaseCode;
	}

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}
	
	
}
