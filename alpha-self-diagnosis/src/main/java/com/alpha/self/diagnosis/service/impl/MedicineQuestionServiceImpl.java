package com.alpha.self.diagnosis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.exception.ServiceException;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.self.diagnosis.dao.*;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.*;
import com.alpha.self.diagnosis.service.*;
import com.alpha.self.diagnosis.utils.MedicineSortUtil;
import com.alpha.self.diagnosis.utils.ServiceUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserDiagnosisDetail;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.DiagnosisMedicalTemplateDao;
import com.alpha.user.dao.UserBasicRecordDao;
import com.alpha.user.pojo.DiagnosisMedicalTemplate;
import com.alpha.user.service.MedicalRecordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by xc.xiong on 2017/9/6.
 * 医学提问相关操作类
 */
@Service
public class MedicineQuestionServiceImpl implements MedicineQuestionService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineQuestionServiceImpl.class);

    @Resource
    private DiagnosisMainsympQuestionDao diagnosisMainsympQuestionDao;
    @Resource
    private DiagnosisQuestionAnswerDao diagnosisQuestionAnswerDao;
    @Resource
    private UserDiagnosisDetailDao userDiagnosisDetailDao;
    @Resource
    private SymptomMainService symptomMainService;
    @Resource
    private MedicineAnswerService medicineAnswerService;

    @Resource
    private SymptomAccompanyService symptomAccompanyService;
    @Resource
    private MedicineDiagnosisService medicineDiagnosisService;
    @Resource
    private DiagnosisService diagnosisService;
    @Resource
    private UserDiagnosisOutcomeDao userDiagnosisOutcomeDao;
    @Resource
    private MedicineAnswerAutoService medicineAnswerAutoService;
    @Resource
    private DiagnosisMedicalTemplateDao diagnosisMedicalTemplateDao;
    @Resource
    private DiagnosisMainSymptomsDao diagnosisMainSymptomsDao;
    @Resource
    private UserBasicRecordDao userBasicRecordDao;
    @Resource
    private MedicalRecordService medicalRecordService;


    /**
     * 问答接口，老接口
     *
     * @param diagnosisId
     * @param questionVo
     * @param userInfo
     * @return
     */
    @Override
    public IQuestionVo next(Long diagnosisId, QuestionRequestVo questionVo, UserInfo userInfo) {
        //主症状语义分析
        if (questionVo.getType() == QuestionEnum.主症状语义分析.getValue()) {
            AnswerRequestVo answerVo;
            List<DiagnosisMainSymptoms> mainList = diagnosisService.lexicalAnalysisByBaidu(questionVo, userInfo);
            //如语义分析未找到主症状,则返回所有主症状
            if (CollectionUtils.isEmpty(mainList)) {
                questionVo.setQuestionCode("");
                return saveAnswerGetQuestion(diagnosisId, null, userInfo);
            }
            //如语义分析找到1个以上的主症状则返回匹配上的主症状+更多
            if (mainList.size() > 1) {
                return symptomMainService.getMainSymptomsQuestion(diagnosisId, userInfo, mainList,questionVo.getInType());
            }
            //如语义分析只找到1个主症状则直接返回该主症状的第一个问题与答案
            if (mainList.size() == 1) {
                DiagnosisMainSymptoms mainSymptom = mainList.get(0);
                questionVo = new QuestionRequestVo();
                questionVo.setDiagnosisId(diagnosisId);
                questionVo.setQuestionCode(mainSymptom.getSympCode());
                questionVo.setType(QuestionEnum.主症状.getValue());
                List<AnswerRequestVo> answerList = new ArrayList<>();
                answerVo = new AnswerRequestVo();
                answerVo.setAnswerTitle(mainSymptom.getSympName());
                answerVo.setContent(mainSymptom.getSympCode());
                answerList.add(answerVo);
                questionVo.setAnswers(answerList);
                //保存就诊问题
                DiagnosisMainsympQuestion question = new DiagnosisMainsympQuestion();
                question.setQuestionCode(questionVo.getQuestionCode());
                question.setPopuTitle(mainSymptom.getSympName());
                question.setQuestionType(QuestionEnum.主症状.getValue());

                BasicQuestionVo basicQuestionVo = new BasicQuestionVo(question, diagnosisId, questionVo.getSympCode());
                List<BasicAnswerVo> basicAnswerList = new ArrayList<>();
                BasicAnswerVo basicAnswerVo = new BasicAnswerVo();
                basicAnswerVo.setAnswerTitle(mainSymptom.getSympName());
                basicAnswerVo.setAnswerValue(mainSymptom.getSympCode());
                basicAnswerVo.setDescription(mainSymptom.getPopuName());
                basicAnswerList.add(basicAnswerVo);
                basicQuestionVo.setAnswers(new ArrayList<>(basicAnswerList));
                // 问题转换
                medicineAnswerService.saveDiagnosisAnswer(basicQuestionVo, userInfo);
                return saveAnswerGetQuestion(diagnosisId, questionVo, userInfo);
            }
            return null;
        } else {
            return saveAnswerGetQuestion(diagnosisId, questionVo, userInfo);
        }
    }

    /**
     * 百度语义分析接口
     *
     * @param diagnosisId
     * @param questionVo
     * @param userInfo
     * @return
     */
    @Override
    public IQuestionVo nextAnalysisByBaidu(Long diagnosisId, QuestionRequestVo questionVo, UserInfo userInfo) {
        //主症状语义分析
        List<DiagnosisMainSymptoms> mainList = diagnosisService.lexicalAnalysisByBaidu(questionVo, userInfo);
        //如语义分析未找到主症状,则返回所有主症状
        if (CollectionUtils.isEmpty(mainList)) {
        	QuestionRequestVo nextQuestionVo = new QuestionRequestVo();
        	nextQuestionVo.setInType(questionVo.getInType());
            return saveAnswerGetQuestion(diagnosisId, nextQuestionVo, userInfo);
        }
        //如语义分析找到1个以上的主症状则返回匹配上的主症状+更多
        if (mainList.size() > 1) {
            return symptomMainService.getMainSymptomsQuestion(diagnosisId, userInfo, mainList,questionVo.getInType());
        }
        //如语义分析只找到1个主症状则直接返回该主症状的第一个问题与答案
        if (mainList.size() == 1) {
            DiagnosisMainSymptoms mainSymptom = mainList.get(0);
            questionVo = new QuestionRequestVo();
            questionVo.setDiagnosisId(diagnosisId);
            questionVo.setQuestionCode(mainSymptom.getSympCode());
            questionVo.setType(QuestionEnum.主症状.getValue());
            List<AnswerRequestVo> answerList = new ArrayList<>();
            AnswerRequestVo answerVo = new AnswerRequestVo();
            answerVo.setAnswerTitle(mainSymptom.getSympName());
            answerVo.setContent(mainSymptom.getSympCode());
            answerList.add(answerVo);
            questionVo.setAnswers(answerList);
            //保存就诊问题
            DiagnosisMainsympQuestion question = new DiagnosisMainsympQuestion();
            question.setQuestionCode(questionVo.getQuestionCode());
            question.setPopuTitle(mainSymptom.getSympName());
            question.setQuestionType(QuestionEnum.主症状.getValue());

            BasicQuestionVo basicQuestionVo = new BasicQuestionVo(question, diagnosisId, questionVo.getSympCode());
            List<BasicAnswerVo> basicAnswerList = new ArrayList<>();
            BasicAnswerVo basicAnswerVo = new BasicAnswerVo();
            basicAnswerVo.setAnswerTitle(mainSymptom.getSympName());
            basicAnswerVo.setAnswerValue(mainSymptom.getSympCode());
            basicAnswerVo.setDescription(mainSymptom.getPopuName());
            basicAnswerList.add(basicAnswerVo);
            basicQuestionVo.setAnswers(new ArrayList<>(basicAnswerList));
            // 问题转换
            medicineAnswerService.saveDiagnosisAnswer(basicQuestionVo, userInfo);
            return saveAnswerGetQuestion(diagnosisId, questionVo, userInfo);
        }
        return null;
    }

    /**
     * 保存医学答案并返回新的问题
     *
     * @param diagnosisId
     * @param questionVo
     * @param userInfo
     * @return
     */
    public BasicQuestionVo saveAnswerGetQuestion(Long diagnosisId, QuestionRequestVo questionVo, UserInfo userInfo) {
        //生成主症状问题
        if (questionVo == null || StringUtils.isEmpty(questionVo.getQuestionCode())) {
            return symptomMainService.getMainSymptomsQuestion(diagnosisId, userInfo,questionVo.getInType());
        }
        // 保存问题答案 // 保存正向反向特异性疾病
        if (questionVo.getType() == QuestionEnum.主症状.getValue()) {
            AnswerRequestVo answerVo = questionVo.getAnswers().get(0);
            questionVo.setSympCode(answerVo.getContent());//如果是主症状提问，那么答案编码就是主症状编码
        }
        medicineAnswerService.updateDiagnosisAnswer(questionVo);
        //非主症状提问的问题需要根据default_order 获取下一个问题
        DiagnosisMainsympQuestion dmQuestion = diagnosisMainsympQuestionDao.getDiagnosisMainsympQuestion(questionVo.getQuestionCode());
        DiagnosisMainsympQuestion question = null;
        //如果是主症状，从0开始获取问题
        if (questionVo.getType() == QuestionEnum.主症状.getValue()) {
            //主症状获取下一个问题
            AnswerRequestVo answerVo = questionVo.getAnswers().get(0);
            question = getNextDiagnosisMainsympQuestion(answerVo.getContent(), 0, userInfo);
        } else if (questionVo.getType() == QuestionEnum.医学问题.getValue()) {
            //非主症状获取下一个问题
            question = getNextQuestion(dmQuestion, questionVo, userInfo);
        }
        if (question == null) {
            // 如果没有下一个问题，生成诊断结果
            //生成诊断结果前，先处理下不需要回答的问题
            medicineAnswerAutoService.autoCalculateAnswer(diagnosisId, questionVo.getSympCode(), userInfo);
            //生成诊断结果前，生成病历
            createTreatScheme(questionVo.getSympCode(), diagnosisId);
            //生成诊断结果
            return diagnosisOutcomeResult(diagnosisId, questionVo.getSympCode(),userInfo);
        }
        BasicQuestionVo basicQuestionVo = null;
        LinkedHashSet<IAnswerVo> specAnswer = null;
        //非伴随症状问题
        if (question.getQuestionType() != QuestionEnum.伴随症状.getValue()) {
            //查询答案,过滤年龄，性别
            List<DiagnosisQuestionAnswer> dqAnswers = medicineAnswerService.listDiagnosisQuestionAnswer(question.getQuestionCode(), userInfo);
            // 答案排序，答案之前的问题设计到的正向特异性疾病权重
            // 查询之前所有的提问 类型101+诊断编码，找到所有的正向特异性疾病编码
            specAnswer = medicineAnswerService.getSpecAnswer(dqAnswers, diagnosisId);
            // 答案排序，答案内部权重,
            List<UserDiagnosisOutcome> userDiagnosisOutcomes = medicineDiagnosisService.diagnosisOutcome(diagnosisId, questionVo.getSympCode(),userInfo);//计算疾病的权重
            LinkedHashSet<IAnswerVo> answers = MedicineSortUtil.sortAnswer(dqAnswers, userDiagnosisOutcomes);
            specAnswer.addAll(answers);
        } else {
            // 伴随症状答案  去重复，算权重，排序:正向特异性多的排前面
            specAnswer = symptomAccompanyService.getSymptomAccompanyAnswer(diagnosisId, dmQuestion.getMainSympCode(), userInfo);
            if (specAnswer == null || specAnswer.size() == 0) {
                return diagnosisOutcomeResult(diagnosisId, questionVo.getSympCode(),userInfo);
            }
        }
        basicQuestionVo = new BasicQuestionVo(question, diagnosisId, questionVo.getSympCode());
        basicQuestionVo.setAnswers(new ArrayList<>(specAnswer));
        if (specAnswer == null || specAnswer.size() == 0) {
            LOGGER.error("没有答案:{}", JSON.toJSON(basicQuestionVo));
        }
        // 问题转换保存
        medicineAnswerService.saveDiagnosisAnswer(basicQuestionVo, userInfo);
        return basicQuestionVo;
    }

    /**
     * 生成病历内容
     *
     * @param mainSympCode
     * @param diagnosisId
     */
    public void createTreatScheme(String mainSympCode, Long diagnosisId) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("sympCode", mainSympCode);
            DiagnosisMainSymptoms symptom = diagnosisMainSymptomsDao.query(param).get(0);
            DiagnosisMedicalTemplate template = diagnosisMedicalTemplateDao.getDiagnosisMedicalTemplate(symptom.getTemplateId() + "");
            List<UserDiagnosisDetail> udds = userDiagnosisDetailDao.listUserDiagnosisDetail(diagnosisId);
            Map<String, String> question = new HashMap<>();
            for (UserDiagnosisDetail udd : udds) {
                question.put(udd.getQuestionCode(), udd.getAnswerContent());
            }
            String maminSymptomName = medicalRecordService.getMaminSymptomName(template, question, symptom);
            String presentIllnessHistory = medicalRecordService.getPresentIllnessHistory(template, question, symptom);
            UserBasicRecord userBasicRecord = userBasicRecordDao.findByDiagnosisId(diagnosisId);
            userBasicRecord.setMainSymptomCode(mainSympCode);
            userBasicRecord.setMainSymptomName(maminSymptomName);
            userBasicRecord.setPresentIllnessHistory(presentIllnessHistory);
            userBasicRecordDao.update(userBasicRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 诊断方法转换成页面视图
     *
     * @param diagnosisId
     */
    public BasicQuestionVo diagnosisOutcomeResult(Long diagnosisId, String mainSympCode,UserInfo userInfo) {
        LOGGER.info("生成诊断结果{}", diagnosisId);
        List<UserDiagnosisOutcome> userDiagnosisOutcomes = medicineDiagnosisService.diagnosisOutcome(diagnosisId, mainSympCode, userInfo);//计算疾病的权重
        userDiagnosisOutcomes = MedicineSortUtil.specUserDiagnosisOutcome(userDiagnosisOutcomes);//根据特异性重新计算权重
        MedicineSortUtil.sortUserDiagnosisOutcome(userDiagnosisOutcomes);//排序
        userDiagnosisOutcomes = calculateProbability(userDiagnosisOutcomes);//计算发病概率,保存，返回5个结果
        BasicQuestionVo basicQuestionVo = convertOutcome(diagnosisId, userDiagnosisOutcomes);
        if (basicQuestionVo != null) {
            return basicQuestionVo;
        } else {
            LOGGER.error("无法生成诊断结果，诊断号：{}", diagnosisId);
            throw new ServiceException(ResponseStatus.INVALID_VALUE, "抱歉，暂时无法生成诊断结果！");
        }
    }

    /**
     * @param diagnosisId
     * @param userDiagnosisOutcomes
     * @return
     */
    public BasicQuestionVo convertOutcome(Long diagnosisId, List<UserDiagnosisOutcome> userDiagnosisOutcomes) {
        List<IAnswerVo> outcomeVos = DiagnosisOutcomeVo.convertDiagnosisOutcomeVo(userDiagnosisOutcomes);
        if (outcomeVos != null && outcomeVos.size() > 0) {
            BasicQuestionVo basicQuestionVo = new BasicQuestionVo();
            basicQuestionVo.setType(QuestionEnum.诊断结果.getValue());
            basicQuestionVo.setDiagnosisId(diagnosisId);
            basicQuestionVo.setQuestionTitle("在您的协助下，阿尔法医生问诊机器人模拟诊断结果如下：（请选择疾病）");
            basicQuestionVo.setTitle("在您的协助下，阿尔法医生问诊机器人模拟诊断结果如下：（请选择疾病）");
            basicQuestionVo.setAnswers(outcomeVos);
            return basicQuestionVo;
        }
        return null;
    }


    /**
     * 计算发病概率，目前使用随机数,
     * 保存，返回5个结果
     *
     * @param userDiagnosisOutcomes
     * @return
     */
    public List<UserDiagnosisOutcome> calculateProbability(List<UserDiagnosisOutcome> userDiagnosisOutcomes) {
        try {
            Double maxProbability = ServiceUtil.getTempProbability();
            for (UserDiagnosisOutcome udo : userDiagnosisOutcomes) {
                Double probability = ServiceUtil.getTempProbability(maxProbability, userDiagnosisOutcomes.get(0).getWeight(), udo.getWeight());
                udo.setProbability(probability);
                userDiagnosisOutcomeDao.insert(udo);
            }
            if (userDiagnosisOutcomes.size() >= 5) {
                return userDiagnosisOutcomes.subList(0, 5);
            }
            return userDiagnosisOutcomes;
        } catch (Exception e) {
            e.printStackTrace();
            return userDiagnosisOutcomes;
        }
    }


    /**
     * 疾病下，所有问题的权重
     *
     * @return
     */
    public Map<String, List<DiagnosisMainsympQuestion>> mapQuestion(String mainSympCode) {
        Map<String, List<DiagnosisMainsympQuestion>> questionMap = new HashMap<>();
        List<DiagnosisMainsympQuestion> dmQuestions = diagnosisMainsympQuestionDao.listDiseaseQuestion(mainSympCode);
        for (DiagnosisMainsympQuestion dmq : dmQuestions) {
            List<DiagnosisMainsympQuestion> questions = questionMap.get(dmq.getDiseaseCode()) == null ? new ArrayList<>() : questionMap.get(dmq.getDiseaseCode());
            questions.add(dmq);
            questionMap.put(dmq.getDiseaseCode(), questions);
        }
        return questionMap;
    }

    /**
     * 非主症状获取下一个问题
     *
     * @param questionVo
     * @param userInfo
     * @return
     */
    public DiagnosisMainsympQuestion getNextQuestion(DiagnosisMainsympQuestion dmQuestion, QuestionRequestVo questionVo, UserInfo userInfo) {
        String nexQuestionId = null;
        if (dmQuestion == null) {
            throw new ServiceException(ResponseStatus.INVALID_VALUE, "没有找到对应的提问");
        }
        AnswerRequestVo answerVo = questionVo.getAnswers().get(0);
        //非伴随症状要判断答案是否存在
        if (dmQuestion.getQuestionType() != QuestionEnum.伴随症状.getValue()) {
            DiagnosisQuestionAnswer dqAnswer = diagnosisQuestionAnswerDao.getDiagnosisQuestionAnswer(dmQuestion.getQuestionCode(), answerVo.getContent());
            if (dqAnswer == null) {
                throw new ServiceException(ResponseStatus.INVALID_VALUE, "没有找到对应的答案");
            }
            nexQuestionId = dqAnswer.getNextQuestionId();
        }
        String mainSympCode = dmQuestion.getMainSympCode();
        int defaultOrder = dmQuestion.getDefaultOrder();
        // 获取下一个问题 1 答案是否存在下一个问题，2没有就查询下一个问题
        DiagnosisMainsympQuestion question = null;
        if (nexQuestionId == null || nexQuestionId.equals("-1")) {
            question = getNextDiagnosisMainsympQuestion(mainSympCode, defaultOrder, userInfo);
        } else {
            question = diagnosisMainsympQuestionDao.getDiagnosisMainsympQuestion(nexQuestionId);
        }
        return question;
    }


    /**
     * 获取下一个问题
     *
     * @param mainSympCode
     * @param defaultOrder
     * @param userInfo
     * @return
     */
    public DiagnosisMainsympQuestion getNextDiagnosisMainsympQuestion(String mainSympCode, int defaultOrder, UserInfo userInfo) {
        List<DiagnosisMainsympQuestion> dmQuestions = diagnosisMainsympQuestionDao.listNextAllQuestion(mainSympCode, defaultOrder);
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
            return question;
        }
        return null;
    }


}
