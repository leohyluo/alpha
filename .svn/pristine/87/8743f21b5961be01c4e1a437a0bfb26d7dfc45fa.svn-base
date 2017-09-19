package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympConcsymp;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/11.
 */
public interface SymptomAccompanyService {
    /**
     * 查询伴随症状
     *
     * @param mainSympCode
     * @return
     */
    List<DiagnosisMainsympConcsymp> listDiagnosisMainsympConcsymp(String mainSympCode, UserInfo userInfo);

    /**
     * 生成伴随症状提问
     *
     * @param diagnosisId
     * @param mainSympCode
     * @param userInfo
     * @param sympName
     * @return
     */
    BasicQuestionVo getSymptomAccompany(Long diagnosisId, String mainSympCode, UserInfo userInfo, String sympName);

    /**
     * 查询疾病下的伴随症状
     *
     * @param mainSympCode
     * @param concSympCodes
     * @return
     */
    Map<String, List<DiagnosisMainsympConcsymp>> mapDiagnosisMainsympConcsymp(String mainSympCode, Collection<String> concSympCodes);

    /**
     * 生成伴随症状提问
     *
     * @param diagnosisId
     * @param mainSympCode
     * @param userInfo
     * @return
     */
    LinkedHashSet<IAnswerVo> getSymptomAccompanyAnswer(Long diagnosisId, String mainSympCode, UserInfo userInfo);

}
