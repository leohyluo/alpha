package com.alpha.server.rpc.diagnosis.pojo;

import java.lang.Long;
import java.lang.String;

public class DiagnosisMedicalTemplate{



	/**
     * id
     */
	private Long id;

	/**
     * 模板编号
     */
	private String templateCode;

	/**
     * 病历模版内容
     */
	private String templateName;


	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	public String getTemplateCode() {
		return this.templateCode;
	}
	

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getTemplateName() {
		return this.templateName;
	}
	
	
	
	

}