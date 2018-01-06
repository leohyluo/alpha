package com.alpha.self.diagnosis.service.impl;

import com.alpha.commons.core.sql.DataSet;
import com.alpha.commons.enums.AppType;
import com.alpha.self.diagnosis.dao.DiagnosisMainSymptomsDao;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.*;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.service.SymptomMainService;
import com.alpha.self.diagnosis.utils.AppUtils;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.service.UserBasicRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by xc.xiong on 2017/9/11.
 * 主症状
 */
@Service
public class SymptomMainServiceImpl implements SymptomMainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SymptomMainServiceImpl.class);

    @Resource
    private DiagnosisMainSymptomsDao diagnosisMainSymptomsDao;
    @Resource
    private MedicineAnswerService medicineAnswerService;

    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    public BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId, UserInfo userInfo,int inType, AppType appType) {
        LOGGER.info("getMainSymptomsQuestion start with {}", diagnosisId);
        DataSet<DiagnosisMainSymptoms> dmsDateSet = diagnosisMainSymptomsDao.selectLimit(1, 1000);
        List<DiagnosisMainSymptoms> dmses = dmsDateSet.getRows();
        dmses = dmses.stream().filter(e -> e.mainSymptomPredicate(userInfo,inType)).collect(Collectors.toList());
        BasicQuestionVo questionVo = new BasicQuestionVo();
        List<IAnswerVo> basicAnswers = new ArrayList<>();
        for (DiagnosisMainSymptoms dms : dmses) {
            BasicAnswerVo basicAnswer = new BasicAnswerVo();
            basicAnswer.setAnswerValue(dms.getSympCode());
            basicAnswer.setAnswerTitle(dms.getSympName());
            basicAnswer.setDefaultOrder(dms.getDefaultOrder());
            basicAnswer.setDescription(dms.getPopuName());
            basicAnswers.add(basicAnswer);
        }
        questionVo.setAnswers(basicAnswers);
        if(appType == AppType.PRE) {
        	String title = "请问{userName}哪里最不舒服?";
        	title = AppUtils.setUserNameAtQuestionTitle(title, userInfo);
        	questionVo.setQuestionTitle(title);
        	questionVo.setTitle(title);
        	questionVo.setSearchUrl("/data/search/mainSymptom");
        } else {
        	questionVo.setQuestionTitle("很抱歉，没有找到相关症状，您可从以下症状中进行选择");
        	questionVo.setTitle("很抱歉，没有找到相关症状，您可从以下症状中进行选择");
        }
        questionVo.setQuestionCode("9992");
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setType(QuestionEnum.主症状.getValue());
        questionVo.setDisplayType("radio_mainsymp");
        questionVo.setSympCode("");
        // 保存问题答案
        medicineAnswerService.saveDiagnosisAnswer(questionVo, userInfo);
        return questionVo;
    }

    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    @Deprecated
    public BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId, List<DiagnosisMainSymptoms> list) {
        LOGGER.info("getMainSymptomsQuestion start with {}", diagnosisId);

        BasicQuestionVo questionVo = new BasicQuestionVo();
        List<IAnswerVo> basicAnswers = new ArrayList<>();
        for (DiagnosisMainSymptoms dms : list) {
            BasicAnswerVo basicAnswer = new BasicAnswerVo();
            basicAnswer.setAnswerValue(dms.getSympCode());
            basicAnswer.setAnswerTitle(dms.getSympName());
            basicAnswer.setDefaultOrder(dms.getDefaultOrder());
            basicAnswer.setDescription(dms.getPopuName());
            basicAnswers.add(basicAnswer);
        }
        questionVo.setAnswers(basicAnswers);
        questionVo.setQuestionTitle("【xxx】的基本情况我已经清楚了解，现在告诉我最不舒服的是什么");
        questionVo.setTitle("【xxx】的基本情况我已经清楚了解，现在告诉我最不舒服的是什么");
        questionVo.setQuestionCode("9999");
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setType(QuestionEnum.主症状.getValue());
        questionVo.setDisplayType("match_mainSymptoms");
        questionVo.setSympCode("");
        // 保存正向反向特异性疾病
//        medicineAnswerService.saveDiagnosisAnswer(questionVo,userInfo);
        return questionVo;
    }

    @Override
    public List<DiagnosisMainSymptoms> query(Map<String, Object> param) {
        return diagnosisMainSymptomsDao.query(param);
    }

    @Override
    public IQuestionVo getMainSymptomsQuestion(Long diagnosisId, UserInfo userInfo,List<DiagnosisMainSymptoms> mainList,int inType) {
        LOGGER.info("getMainSymptomsQuestion start with {}", diagnosisId);
        String questionCode = "9991";
        BasicQuestionWithMoreVo questionVo = new BasicQuestionWithMoreVo();
        List<IAnswerVo> basicAnswers = new ArrayList<>();
        basicAnswers = mainList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
        questionVo.setMatch(basicAnswers);
        questionVo.setQuestionTitle("请选择最不舒服的症状");
        questionVo.setQuestionCode(questionCode);
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setType(QuestionEnum.主症状.getValue());
        questionVo.setDisplayType("radio_mainsymp");
        questionVo.setSympCode("");
        //更多
        Map<String, DiagnosisMainSymptoms> mainMap = mainList.stream().collect(Collectors.toMap(DiagnosisMainSymptoms::getSympCode, Function.identity()));
        DataSet<DiagnosisMainSymptoms> dmsDateSet = diagnosisMainSymptomsDao.selectLimit(1, 1000);
        List<DiagnosisMainSymptoms> dmses = dmsDateSet.getRows();
        List<DiagnosisMainSymptoms> moreList = dmses.stream().filter(e -> e.mainSymptomPredicate(userInfo,inType)).filter(e -> !mainMap.containsKey(e.getSympCode())).collect(Collectors.toList());
        List<IAnswerVo> moreAnswers = moreList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
        questionVo.setAnswers(moreAnswers);
        //保存问诊记录
        BasicQuestionVo basicQuestionVo = new BasicQuestionVo();
        basicQuestionVo.setAnswers(basicAnswers);
        basicQuestionVo.setDiagnosisId(diagnosisId);
        basicQuestionVo.setQuestionCode(questionCode);
        basicQuestionVo.setType(questionVo.getType());
        basicQuestionVo.setSympCode("");
        medicineAnswerService.saveDiagnosisAnswer(basicQuestionVo, userInfo);
        return questionVo;
    }
}
