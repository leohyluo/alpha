package com.alpha.self.diagnosis.service.impl;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.self.diagnosis.dao.DiagnosisPastmedicalHistoryDao;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.processor.AbstractBasicAnswerProcessor;
import com.alpha.self.diagnosis.processor.BasicAnswerProcessorAdaptor;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DiagnosisPastmedicalHistoryServiceImpl implements DiagnosisPastmedicalHistoryService {

    @Resource
    private DiagnosisPastmedicalHistoryDao dao;
    @Resource
    private BasicQuestionService basicQuestionService;
    @Resource
    private UserInfoService userInfoService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<DiagnosisPastmedicalHistory> queryPastmedicalHistory(Map<String, Object> param) {
        List<DiagnosisPastmedicalHistory> list = dao.queryPastmedicalHistory(param);
        return Optional.ofNullable(list).orElseGet(ArrayList::new);
    }

    @Override
    public List<DiagnosisSubpastmedicalHistory> querySubPastmedicalHistory(Map<String, Object> param) {
        return dao.querySubPastmedicalHistory(param);
    }

    @Override
    public List<DiseaseVo> querySelectedPastmedicalHistory(Map<String, Object> param) {
        return dao.querySelectedPastmedicalHistory(param);
    }

    @Override
    public void updateUserSelectCount(Map<String, Object> param) {
        dao.updateUserSelectCount(param);
    }

    @Override
    public IQuestionVo queryDiseaseHistory(Long userId, Long diagnosisId, Integer historyType) {
        UserInfo userInfo = userInfoService.queryByUserId(userId);
        if (userInfo == null) {
            logger.warn("user not found by userId {}", userId);
            return null;
        }
        String questionCode = historyType == 1 ? BasicQuestionType.PAST_MEDICAL_HISTORY.getValue() : BasicQuestionType.ALLERGIC_HISTORY.getValue();
        BasicQuestion question = basicQuestionService.findByQuestionCode(questionCode);
        AbstractBasicAnswerProcessor answerProcessor = BasicAnswerProcessorAdaptor.getProcessor(question.getQuestionCode());
        IQuestionVo questionVo = answerProcessor.build(diagnosisId, question, userInfo);
        return questionVo;
    }
}
