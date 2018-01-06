package com.alpha.self.diagnosis.service;

import com.alpha.commons.enums.AppType;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/11.
 */
public interface SymptomMainService {

    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId, UserInfo userInfo,int inType, AppType appType);

    BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId, List<DiagnosisMainSymptoms> list);

    List<DiagnosisMainSymptoms> query(Map<String, Object> param);

    /**
     * 生成主症状问题
     *
     * @param diagnosisId
     * @param userInfo
     * @param mainList
     * @return
     */
    IQuestionVo getMainSymptomsQuestion(Long diagnosisId, UserInfo userInfo, List<DiagnosisMainSymptoms> mainList,int inType);
}
