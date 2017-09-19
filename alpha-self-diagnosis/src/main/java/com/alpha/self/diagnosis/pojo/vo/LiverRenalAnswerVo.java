package com.alpha.self.diagnosis.pojo.vo;

import java.util.List;

public class LiverRenalAnswerVo implements IAnswerVo {

	private List<BasicAnswerVo> liver;
	private List<BasicAnswerVo> renal;
	
	public LiverRenalAnswerVo() {}
	
	public LiverRenalAnswerVo(List<BasicAnswerVo> liver, List<BasicAnswerVo> renal) {
		this.liver = liver;
		this.renal = renal;
	}
	
	public List<BasicAnswerVo> getLiver() {
		return liver;
	}
	public void setLiver(List<BasicAnswerVo> liver) {
		this.liver = liver;
	}
	public List<BasicAnswerVo> getRenal() {
		return renal;
	}
	public void setRenal(List<BasicAnswerVo> renal) {
		this.renal = renal;
	}
	
}
