package com.alpha.server.rpc.diagnosis.pojo;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;

public class DiagnosisDiseasePhysicalexam{



	/**
     * id
     */
	private Long id;

	/**
     * 查体编码
     */
	private String examCode;

	/**
     * 疾病编码
     */
	private String diseaseCode;

	/**
     * 建议查体名称
     */
	private String examName;

	/**
     * 拼音助记符
     */
	private String symbol;

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
	

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	
	public String getExamCode() {
		return this.examCode;
	}
	

	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}
	
	public String getDiseaseCode() {
		return this.diseaseCode;
	}
	

	public void setExamName(String examName) {
		this.examName = examName;
	}
	
	public String getExamName() {
		return this.examName;
	}
	

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return this.symbol;
	}
	

	public void setTOrder(Integer tOrder) {
		this.tOrder = tOrder;
	}
	
	public Integer getTOrder() {
		return this.tOrder;
	}
	
	
	
	

}
