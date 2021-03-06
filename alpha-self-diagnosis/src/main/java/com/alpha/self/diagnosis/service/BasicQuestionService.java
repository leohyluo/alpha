package com.alpha.self.diagnosis.service;

import com.alpha.commons.enums.DiseaseType;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.*;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.List;
import java.util.Map;

public interface BasicQuestionService {

    BasicQuestion find(BasicQuestion question);

    BasicQuestion findByQuestionCode(String questionCode);

    List<BasicQuestion> findNext(BasicQuestion question);

    IQuestionVo next(QuestionRequestVo questionVo) throws Exception;

    /**
     * 疾病搜索
     * @param type      疾病类型
     * @return
     */
    List<IAnswerVo> diseaseSearch(Long userId, String keyword, DiseaseType type);

    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    BasicQuestionVo getMainSymptomsQuestion(String systemType, Long diagnosisId, UserInfo userInfo);
    
    /**
     * 根据出生日期或年龄查询要回答的问题
     * @param birth
     * @param gender
     * @return
     */
    Map<String, String> askBasicQuestion(String systemType, String birth, int gender);
}
