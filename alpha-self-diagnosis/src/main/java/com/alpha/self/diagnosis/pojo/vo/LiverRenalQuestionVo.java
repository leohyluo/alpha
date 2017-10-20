package com.alpha.self.diagnosis.pojo.vo;

import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.List;

public class LiverRenalQuestionVo implements IQuestionVo {

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

    //肝功能
    private List<IAnswerVo> liver;

    //肾功能
    private List<IAnswerVo> renal;

    public LiverRenalQuestionVo(Long diagnosisId, BasicQuestion basicQuestion, List<IAnswerVo> liver,
                                List<IAnswerVo> renal, UserInfo userInfo, String userName) {
        this.diagnosisId = diagnosisId;
        this.displayType = basicQuestion.getDisplayType();
        this.questionCode = basicQuestion.getQuestionCode();
        this.questionTitle = basicQuestion.getTitle();
        if (userInfo != null) {
            this.userId = userInfo.getUserId().toString();
            if (this.questionTitle.contains(USER_CHAR)) {
                this.questionTitle = this.questionTitle.replace(USER_CHAR, userName);
            }
        }
        this.liver = liver;
        this.renal = renal;
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

    public List<IAnswerVo> getLiver() {
        return liver;
    }

    public void setLiver(List<IAnswerVo> liver) {
        this.liver = liver;
    }

    public List<IAnswerVo> getRenal() {
        return renal;
    }

    public void setRenal(List<IAnswerVo> renal) {
        this.renal = renal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
