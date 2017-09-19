package com.alpha.self.diagnosis.pojo.vo;

import java.util.List;

import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.server.rpc.user.pojo.UserInfo;

public class DiseaseQuestionVo implements IQuestionVo {
	
	private static final String USER_CHAR = "{userName}";
	
	private String userId;
	
	private Long diagnosisId;

    private String displayType;

    private String questionTitle;

    private int type;//问题类型

    /**
     * 问题编码
     */
    private String questionCode;
    
    //展示数据
    private List<IAnswerVo> show;
    
    //搜索数据
    private List<IAnswerVo> search;
    
    public DiseaseQuestionVo(Long diagnosisId, BasicQuestion basicQuestion, List<IAnswerVo> show, 
    		List<IAnswerVo> search, UserInfo userInfo, String userName) {
        this.diagnosisId = diagnosisId;
        this.displayType = basicQuestion.getDisplayType();
        this.questionCode = basicQuestion.getQuestionCode();
        this.questionTitle = basicQuestion.getTitle();
        if(userInfo != null) {
        	this.userId = userInfo.getUserId().toString();
        	if(this.questionTitle.contains(USER_CHAR)) {
        		this.questionTitle = this.questionTitle.replace(USER_CHAR, userName);
        	}
        }
        this.show = show;
        this.search = search;
    }

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public List<IAnswerVo> getShow() {
		return show;
	}

	public void setShow(List<IAnswerVo> show) {
		this.show = show;
	}

	public List<IAnswerVo> getSearch() {
		return search;
	}

	public void setSearch(List<IAnswerVo> search) {
		this.search = search;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
