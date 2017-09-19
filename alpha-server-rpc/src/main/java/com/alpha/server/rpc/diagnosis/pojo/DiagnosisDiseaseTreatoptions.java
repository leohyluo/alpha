package com.alpha.server.rpc.diagnosis.pojo;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;

public class DiagnosisDiseaseTreatoptions{



	/**
     * 
     */
	private Long id;

	/**
     * 疾病编码
     */
	private String diseaseCode;

	/**
     * 治疗方案编码
     */
	private String optionCode;

	/**
     * 治疗方案大类名称
     */
	private String optionName;

	/**
     * 方案具体内容
     */
	private String optionContent;

	/**
     * 序号
     */
	private Integer tOrder;


	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}
	
	public String getDiseaseCode() {
		return this.diseaseCode;
	}
	

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	
	public String getOptionCode() {
		return this.optionCode;
	}
	

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	
	public String getOptionName() {
		return this.optionName;
	}
	

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}
	
	public String getOptionContent() {
		return this.optionContent;
	}
	

	public void setTOrder(Integer tOrder) {
		this.tOrder = tOrder;
	}
	
	public Integer getTOrder() {
		return this.tOrder;
	}
	
	
	
	

}
