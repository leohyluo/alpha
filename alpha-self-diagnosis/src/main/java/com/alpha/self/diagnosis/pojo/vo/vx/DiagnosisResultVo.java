package com.alpha.self.diagnosis.pojo.vo.vx;

import org.apache.commons.lang.StringUtils;

import com.alpha.commons.enums.VaccinationHistory;
import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;

/**
 * 微信公众号-信息确认-就诊信息实体类
 * @author Administrator
 *
 */
public class DiagnosisResultVo {
	
	private Long diagnosisId;

	//医院
	private String hospitalName;
    //挂号科室
    private String department;
    //就诊时间
    private String cureTime;
    //主诉
    private String mainSymptomName;
    //现病史
    private String presentIllnessHistory;
    //既往史
    private String pastmedicalHistoryText;
    //icd10编码
    private String icd10;
    //疾病名称
    private String diseaseName;
   
    public DiagnosisResultVo() {
    }

    public DiagnosisResultVo(UserBasicRecord record) {
        BeanCopierUtil.copy(record, this);
        if (record.getCureTime() != null) {
            this.cureTime = DateUtils.date2String(record.getCreateTime(), DateUtils.DATE_FORMAT);
        }
        if(StringUtils.isEmpty(record.getDiseaseName())) {
        	this.diseaseName = "暂无";
        }
		this.presentIllnessHistory = StringUtils.trimToEmpty(record.getPresentIllnessHistory())
				.concat(StringUtils.trimToEmpty(record.getPresentIllnessHistoryHospital()));
        this.toPastIllHistoryString(record);
    }

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCureTime() {
		return cureTime;
	}

	public void setCureTime(String cureTime) {
		this.cureTime = cureTime;
	}

	public String getMainSymptomName() {
		return mainSymptomName;
	}

	public void setMainSymptomName(String mainSymptomName) {
		this.mainSymptomName = mainSymptomName;
	}

	public String getPresentIllnessHistory() {
		return presentIllnessHistory;
	}

	public void setPresentIllnessHistory(String presentIllnessHistory) {
		this.presentIllnessHistory = presentIllnessHistory;
	}

	public String getPastmedicalHistoryText() {
		return pastmedicalHistoryText;
	}

	public void setPastmedicalHistoryText(String pastmedicalHistoryText) {
		this.pastmedicalHistoryText = pastmedicalHistoryText;
	}
    
	//拼装既往史
    private void toPastIllHistoryString(UserBasicRecord record) {
    	StringBuffer sb = new StringBuffer();
    	if(StringUtils.isEmpty(record.getPastmedicalHistoryCode()) || "0".equals(record.getPastmedicalHistoryCode()) || "-1".equals(record.getPastmedicalHistoryCode())) {
    		sb.append("既往体健。");
    	} else {
    		sb.append("患者有").append(record.getPastmedicalHistoryText()).append("。");
    	}
    	if(StringUtils.isEmpty(record.getOperationCode()) || "0".equals(record.getOperationCode())) {
    		sb.append("手术史不详。");
    	} else if(StringUtils.isNotEmpty(record.getOperationCode()) && "-1".equals(record.getOperationCode())) {
    		sb.append("无手术史。");
    	} else {
    		sb.append("有手术史。");
    	}
    	if(StringUtils.isEmpty(record.getAllergicHistoryCode()) || "0".equals(record.getAllergicHistoryCode())) {
    		sb.append("药物食物过敏不详。");
    	} else if(StringUtils.isNotEmpty(record.getAllergicHistoryCode()) && "-1".equals(record.getAllergicHistoryCode())) {
    		sb.append("无药物食物过敏。");
    	} else {
    		sb.append("患者对").append(record.getAllergicHistoryText()).append("过敏。");
    	}
    	String vaccinationHistoryText = record.getVaccinationHistoryText();
    	if(StringUtils.isEmpty(vaccinationHistoryText)) {
    		sb.append("无预防接种史");
    	} else if(VaccinationHistory.INTIME.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史按时完成。");
    	} else if(VaccinationHistory.UNTIMELY.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史未按时完成。");
    	} else if(VaccinationHistory.UNDO.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史未进行。");
    	} else if(VaccinationHistory.UNKNOWN.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史不详。");
    	}
    	if(StringUtils.isNotEmpty(record.getFertilityType()) || StringUtils.isNotEmpty(record.getGestationalAge()) || StringUtils.isNotEmpty(record.getFeedType())) {
    		sb.append("患儿为");
    		if(StringUtils.isNotEmpty(record.getGestationalAge())) {
    			sb.append(record.getGestationalAge());
    		}
    		if(StringUtils.isNotEmpty(record.getFertilityType())) {
    			sb.append(record.getFertilityType());
    		}
    		if(StringUtils.isNotEmpty(record.getFeedType())) {
    			sb.append("出生后").append(record.getFeedType()).append("。");
    		}
    	}
    	if("1".equals(record.getMenstrualPeriod())) {
    		sb.append("目前处于月经期内。");
    	}
    	this.pastmedicalHistoryText = sb.toString();
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
    
    
}
