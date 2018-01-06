package com.alpha.self.diagnosis.pojo.vo.vx;

/**
 * 公众号-查询模块-查药品-药品说明书
 * @author Administrator
 *
 */
public class DrugDetailVo {

	//药品图片
	private String tradeImage;
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
	public String getTradeImage() {
		return tradeImage;
	}
	public void setTradeImage(String tradeImage) {
		this.tradeImage = tradeImage;
	}
	public String getIndication() {
		return indication;
	}
	public void setIndication(String indication) {
		this.indication = indication;
	}
	
	
}
