package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;

import java.util.List;
import java.util.Map;

public interface DiagnosisPastmedicalHistoryService {

    List<DiagnosisPastmedicalHistory> queryPastmedicalHistory(Map<String, Object> param);

    List<DiagnosisSubpastmedicalHistory> querySubPastmedicalHistory(Map<String, Object> param);

    /**
     * 查询已选择的既往史
     *
     * @param param
     * @return
     */
    List<DiseaseVo> querySelectedPastmedicalHistory(Map<String, Object> param);

    /**
     * 更新既往史用户频次
     *
     * @param param
     */
    void updateUserSelectCount(Map<String, Object> param);

    /**
     * 展示病史(既往史、过敏史)
     *
     * @param userId
     * @param diagnosisId
     * @param historyType 1:既往史 2:过敏史
     * @return
     */
    IQuestionVo queryDiseaseHistory(Long userId, Long diagnosisId, Integer historyType);
}
