package com.alpha.self.diagnosis.pojo.vo.vx;

import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.commons.util.BeanCopierUtil;

/**
 * 微信公众号-查询模块-疾病-诊疗意见
 * @author Administrator
 *
 */
public class DiseaseDetailVo {

	/**
     * 疾病编码
     */
    private String diseaseCode;

    /**
     * 疾病名称
     */
    private String diseaseName;

    /**
     * 拼音助记符
     */
    private String symbol;

    /**
     * 疾病概述
     */
    private String diseaseSummary;
    
    /**
     * 指南意见
     */
    private String guideOption;
    
    /**
     * 家庭微治疗
     */
    private String treatFamily;
    
    /**
     * 疾病预防
     */
    private String defendOption;
    
    public DiseaseDetailVo() {}
    
    public DiseaseDetailVo(DiagnosisDisease disease) {
    	BeanCopierUtil.copy(disease, this);
    }

	public String getDiseaseCode() {
		return diseaseCode;
	}

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDiseaseSummary() {
		return diseaseSummary;
	}

	public void setDiseaseSummary(String diseaseSummary) {
		this.diseaseSummary = diseaseSummary;
	}

	public String getGuideOption() {
		return guideOption;
	}

	public void setGuideOption(String guideOption) {
		this.guideOption = guideOption;
	}

	public String getTreatFamily() {
		return treatFamily;
	}

	public void setTreatFamily(String treatFamily) {
		this.treatFamily = treatFamily;
	}

	public String getDefendOption() {
		return defendOption;
	}

	public void setDefendOption(String defendOption) {
		this.defendOption = defendOption;
	}
    
    
}
