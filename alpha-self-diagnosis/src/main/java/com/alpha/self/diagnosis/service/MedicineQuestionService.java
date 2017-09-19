package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import com.alpha.server.rpc.user.pojo.UserInfo;

/**
 * Created by xc.xiong on 2017/9/6.
 */
public interface MedicineQuestionService {


    /**
     * 保存医学答案并返回新的问题
     *
     * @param diagnosisId
     * @param questionVo
     * @param userInfo
     * @return
     */
    BasicQuestionVo saveAnswerGetQuestion(Long diagnosisId, QuestionRequestVo questionVo, UserInfo userInfo);

    /**
     * 获取下一个问题
     *
     * @param mainSympCode
     * @param defaultOrder
     * @param userInfo
     * @return
     */
    DiagnosisMainsympQuestion getNextDiagnosisMainsympQuestion(String mainSympCode, int defaultOrder, UserInfo userInfo);
}
