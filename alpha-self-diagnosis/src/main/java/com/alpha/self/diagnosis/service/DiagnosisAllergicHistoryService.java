package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;

import java.util.List;
import java.util.Map;

public interface DiagnosisAllergicHistoryService {

    public List<DiagnosisAllergicHistory> queryAllergicHistory(Map<String, Object> param);

    public List<DiagnosisSuballergicHistory> querySubAllergicHistory(Map<String, Object> param);

    /**
     * 查询已选择的过敏史
     *
     * @param param
     * @return
     */
    List<DiseaseVo> querySelectedAllergicHistory(Map<String, Object> param);

    /**
     * 更新既往史用户频次
     *
     * @param param
     */
    void updateUserSelectCount(Map<String, Object> param);
}
