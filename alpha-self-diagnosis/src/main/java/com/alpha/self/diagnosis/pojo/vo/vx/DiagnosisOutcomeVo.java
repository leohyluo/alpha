package com.alpha.self.diagnosis.pojo.vo.vx;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;

/**
 * 微信公众号-信息确认-就诊信息-疑似结果实体类
 * @author Administrator
 *
 */
public class DiagnosisOutcomeVo {

    private Long diagnosisId;

    //疾病名称
    private String diseaseName;

    //疾病编码
    private String diseaseCode;
    
    //疾病描述
    private String description;

    //发病概率
    private Double probability;

    //治疗建议
    private String suggest;
    
    public DiagnosisOutcomeVo() {}
    
    public DiagnosisOutcomeVo(UserDiagnosisOutcome outcome) {
    	BeanCopierUtil.copy(outcome, this);
    }

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getDiseaseCode() {
		return diseaseCode;
	}

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
    
    
}
