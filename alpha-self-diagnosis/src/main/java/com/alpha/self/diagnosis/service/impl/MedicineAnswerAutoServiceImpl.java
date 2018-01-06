package com.alpha.self.diagnosis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.util.DateUtils;
import com.alpha.self.diagnosis.dao.DiagnosisMainsympQuestionDao;
import com.alpha.self.diagnosis.dao.UserDiagnosisDetailDao;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.service.MedicineAnswerAutoService;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.utils.ServiceUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.user.pojo.UserDiagnosisDetail;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by xc.xiong on 2017/10/10.
 * 答案自动计算模块
 */
@Service
@Transactional
public class MedicineAnswerAutoServiceImpl implements MedicineAnswerAutoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineAnswerAutoServiceImpl.class);

    @Autowired
    private DiagnosisMainsympQuestionDao diagnosisMainsympQuestionDao;
    @Autowired
    private MedicineAnswerService medicineAnswerService;
    @Autowired
    private UserDiagnosisDetailDao userDiagnosisDetailDao;

    /**
     * 自动计算医学问题
     *
     * @param diagnosisId
     * @param mainSympCode
     * @param userInfo
     */
    public void autoCalculateAnswer(Long diagnosisId, String mainSympCode, UserInfo userInfo) {
        try {
            //查询所有需要自动计算的问题
            List<DiagnosisMainsympQuestion> autoQuestions = this.listAutoQuestion(mainSympCode, userInfo);
            if (autoQuestions == null || autoQuestions.size() == 0)
                return;
            Set<String> questionCodes = new HashSet<>();
            Map<String, Set<DiagnosisQuestionAnswer>> answerMap = new HashMap<>();
            for (DiagnosisMainsympQuestion dmq : autoQuestions) {
                questionCodes.add(dmq.getQuestionCode());
            }
            //查询所有问题的答案
            List<DiagnosisQuestionAnswer> autoAnswers = medicineAnswerService.listDiagnosisQuestionAnswer(questionCodes, userInfo);
            for (DiagnosisQuestionAnswer dqa : autoAnswers) {
                Set<DiagnosisQuestionAnswer> set = answerMap.get(dqa.getQuestionCode()) == null ? new HashSet<>() : answerMap.get(dqa.getQuestionCode());
                set.add(dqa);
                answerMap.put(dqa.getQuestionCode(), set);
            }
            //根据问题类型计算答案
            for (DiagnosisMainsympQuestion question : autoQuestions) {
                BasicQuestionVo basicQuestionVo = new BasicQuestionVo(question, diagnosisId, mainSympCode, userInfo);
                Set<DiagnosisQuestionAnswer> answerSet = answerMap.get(question.getQuestionCode());
                if (answerSet == null || answerSet.size() == 0) {
                    continue;
                }
                QuestionEnum type = QuestionEnum.getQuestion(basicQuestionVo.getType());
                switch (type) {
                    case 年龄问题:
                        filterAnswerByAge(answerSet, userInfo);//过滤不符合年龄段的答案
                        break;
                    case 季节问题:
                        filterAnswerBySeason(answerSet, userInfo);//过滤不符合季节段的答案
                        break;
                    default:
                        break;
                }
                //保存答案内容
                if (answerSet != null && answerSet.size() > 0) {
                    saveDiagnosisAnswer(diagnosisId, userInfo, question, answerSet);
                }
            }
        } catch (Exception e) {
            LOGGER.error("计算自动回答问题出错》》{}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 自动过滤季节不符合要求的答案
     *
     * @param dqas
     * @param userInfo
     * @return
     */
    public void filterAnswerByAge(Collection<DiagnosisQuestionAnswer> dqas, UserInfo userInfo) {
        for (Iterator iterator = dqas.iterator(); iterator.hasNext(); ) {
            DiagnosisQuestionAnswer dqa = (DiagnosisQuestionAnswer) iterator.next();
            float age = DateUtils.getAge(userInfo.getBirth());
            if ((dqa.getMinValue() != null && dqa.getMinValue() > age) || (dqa.getMaxValue() != null && dqa.getMaxValue() < age)) {
                iterator.remove();
            }
        }
    }

    /**
     * 自动过滤季节不符合要求的答案
     *
     * @param dqas
     * @param userInfo
     * @return
     */
    public void filterAnswerBySeason(Collection<DiagnosisQuestionAnswer> dqas, UserInfo userInfo) {
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(new Date());
        int month = todayCal.get(Calendar.MONTH);
        for (Iterator iterator = dqas.iterator(); iterator.hasNext(); ) {
            DiagnosisQuestionAnswer dqa = (DiagnosisQuestionAnswer) iterator.next();
            Double minValue = dqa.getMinValue();
            Double maxValue = dqa.getMaxValue();
            if (minValue == null || minValue == 0 || maxValue == null || maxValue == 0) {
                continue;
            }
            List<Integer> listMonth = DateUtils.listMonth(minValue.intValue(), maxValue.intValue());
            if (!listMonth.contains(month)) {
                iterator.remove();
            }
        }
    }

    /**
     * 计算特异性
     * 保存符合要求的答案
     *
     * @param
     */
    public void saveDiagnosisAnswer(Long diagnosisId, UserInfo userInfo, DiagnosisMainsympQuestion question, Collection<DiagnosisQuestionAnswer> dqAnswers) {
    	String questionCode = question.getQuestionCode();
    	UserDiagnosisDetail udd = userDiagnosisDetailDao.getUserDiagnosisDetail(diagnosisId, questionCode);
    	if(udd != null) {
    		return;
    	}
        Set<String> answerContents = new HashSet<>();
        Map<Integer, Set<String>> answerSpecMap = new HashMap<>();
        Set<String> answerCodes = new HashSet<>();
        for (DiagnosisQuestionAnswer dqa : dqAnswers) {
            Set<String> specSet = answerSpecMap.get(dqa.getAnswerSpec()) == null ? new HashSet<String>() : answerSpecMap.get(dqa.getAnswerSpec());
            specSet.add(dqa.getDiseaseCode());
            answerSpecMap.put(dqa.getAnswerSpec(), specSet);
            answerContents.add(dqa.getContent());
            answerCodes.add(dqa.getAnswerCode());
        }
        Set<String> forwardDiseaseCode = answerSpecMap.get(1) == null ? new HashSet<String>() : answerSpecMap.get(1);   //正向特异性
        Set<String> reverseDiseaseCode = answerSpecMap.get(-1) == null ? new HashSet<String>() : answerSpecMap.get(-1);   //反向特异性
        Set<String> nothingDiseaseCode = answerSpecMap.get(0) == null ? new HashSet<String>() : answerSpecMap.get(0); //无特异性
        udd = new UserDiagnosisDetail();
        udd.setDiagnosisId(diagnosisId);
        udd.setUserId(0L);
        udd.setMemberId(userInfo.getUserId());
        udd.setQuestionCode(question.getQuestionCode());
        udd.setSympCode(question.getMainSympCode());
        udd.setQuestionContent(question.getTitle());
        udd.setQuestionType(question.getQuestionType());
        udd.setAnswerCode(JSON.toJSONString(answerCodes));
        udd.setAnswerContent(ServiceUtil.arrayConvertToString(answerContents));
        udd.setAnswerJson(JSON.toJSONString(answerCodes));
        udd.setForwardDiseaseCode(JSON.toJSONString(forwardDiseaseCode));
        udd.setReverseDiseaseCode(JSON.toJSONString(reverseDiseaseCode));
        udd.setNothingDiseaseCode(JSON.toJSONString(nothingDiseaseCode));
        udd.setAnswerTime(new Date());
        userDiagnosisDetailDao.insert(udd);
    }


    /**
     * 获取所有的自动计算问题
     *
     * @param mainSympCode
     * @param userInfo
     * @return
     */
    public List<DiagnosisMainsympQuestion> listAutoQuestion(String mainSympCode, UserInfo userInfo) {
        List<DiagnosisMainsympQuestion> dmQuestions = diagnosisMainsympQuestionDao.listAutoQuestion(mainSympCode);
        if (dmQuestions.size() == 0) {
            return null;
        }
        for (DiagnosisMainsympQuestion question : dmQuestions) {
            if (question.getGender() != null && question.getGender() > 0 && question.getGender() != userInfo.getGender()) {
                continue;//过滤性别
            }
            float age = DateUtils.getAge(userInfo.getBirth());
            if ((question.getMinAge() != null && question.getMinAge() > age) || (question.getMaxAge() != null && question.getMaxAge() < age)) {
                continue;//过滤年龄
            }
        }
        return dmQuestions;
    }

}
