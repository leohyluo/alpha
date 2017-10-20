package com.alpha.self.diagnosis.pojo.vo;

import java.io.Serializable;
import java.util.List;

public class BasicQuestionWithMoreVo implements Serializable, IQuestionVo {


    private static final long serialVersionUID = 6688903127327118326L;

    private String userId;

    private Long diagnosisId;

    private String displayType;

    private String questionTitle;

    private String sympCode;

    private int type;//问题类型

    /**
     * 问题编码
     */
    private String questionCode;

    private List<IAnswerVo> answers;

    private List<IAnswerVo> match;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSympCode() {
        return sympCode;
    }

    public void setSympCode(String sympCode) {
        this.sympCode = sympCode;
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

    public List<IAnswerVo> getAnswers() {
        return answers;
    }

    public void setAnswers(List<IAnswerVo> answers) {
        this.answers = answers;
    }

    public List<IAnswerVo> getMatch() {
        return match;
    }

    public void setMatch(List<IAnswerVo> match) {
        this.match = match;
    }


}
