package com.alpha.self.diagnosis.pojo.vo;

public class DiseaseHistoryRequestVo extends BasicRequestVo {

    //1既往史 2过敏史
    private Integer historyType;
    private String gender;
    private String birth;

    public Integer getHistoryType() {
        return historyType;
    }

    public void setHistoryType(Integer historyType) {
        this.historyType = historyType;
    }

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

    
}
