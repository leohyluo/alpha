package com.alpha.self.diagnosis.pojo.vo2;

/**
 * 药品详情
 * @author Administrator
 *
 */
public class DrugDetailVo {

	//药品编码
    private String drugCode;

	//商品名称
    private String tradeName;
    
    //适应症
    private String indication;

	public String getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getIndication() {
		return indication;
	}

	public void setIndication(String indication) {
		this.indication = indication;
	}
    
    
}
