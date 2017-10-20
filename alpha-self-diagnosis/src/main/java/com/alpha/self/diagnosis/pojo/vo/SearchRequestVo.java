package com.alpha.self.diagnosis.pojo.vo;

import java.io.Serializable;

public class SearchRequestVo implements Serializable {

    private static final long serialVersionUID = 1320248897835659560L;

    private String userId;
    private String keyword;
    private Long diagnosisId;
    private Integer inType;
    private String sympCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Integer getInType() {
        return inType;
    }

    public void setInType(Integer inType) {
        this.inType = inType;
    }

    public String getSympCode() {
        return sympCode;
    }

    public void setSympCode(String sympCode) {
        this.sympCode = sympCode;
    }
}
