package com.alpha.self.diagnosis.pojo.vo;

public class DiseaseHistoryRequestVo extends BasicRequestVo {

    //1既往史 2过敏史
    private Integer historyType;

    public Integer getHistoryType() {
        return historyType;
    }

    public void setHistoryType(Integer historyType) {
        this.historyType = historyType;
    }


}
