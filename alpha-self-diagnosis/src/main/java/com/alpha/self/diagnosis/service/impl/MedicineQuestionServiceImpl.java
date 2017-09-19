package com.alpha.self.diagnosis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.exception.ServiceException;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.self.diagnosis.dao.DiagnosisMainsympQuestionDao;
import com.alpha.self.diagnosis.dao.DiagnosisQuestionAnswerDao;
import com.alpha.self.diagnosis.dao.UserDiagnosisDetailDao;
import com.alpha.self.diagnosis.dao.UserDiagnosisOutcomeDao;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.*;
import com.alpha.self.diagnosis.service.*;
import com.alpha.self.diagnosis.utils.MedicineSortUtil;
import com.alpha.server.rpc.diagnosis.pojo.*;
import com.alpha.server.rpc.diagnosis.pojo.vo.MedicineQuestionVo;
import com.alpha.server.rpc.user.pojo.UserInfo;
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

    public static final Map<String, DiagnosisMainsympQuestion> QUESTION_MAP = new HashMap<>();
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
    private DiagnosisDiseaseService diagnosisDiseaseService;
    @Resource
    private UserDiagnosisOutcomeDao userDiagnosisOutcomeDao;


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
            return symptomMainService.getMainSymptomsQuestion(diagnosisId,userInfo);
        }
        // 保存问题答案 // 保存正向反向特异性疾病 //TODO 是否计算权重（权重不计算，答案排序根据正向特异性来排序）
        if (questionVo.getType() == QuestionEnum.主症状.getValue()) {
            AnswerRequestVo answerVo = questionVo.getAnswers().get(0);
            questionVo.setSympCode(answerVo.getContent());//如果是主症状提问，那么答案编码就是主症状编码
        }
        medicineAnswerService.updateDiagnosisAnswer(questionVo);
        DiagnosisMainsympQuestion dmQuestion = diagnosisMainsympQuestionDao.getDiagnosisMainsympQuestion(questionVo.getQuestionCode());
        DiagnosisMainsympQuestion question = null;
        //如果是主症状，从0开始获取问题
        if (questionVo.getType() == QuestionEnum.主症状.getValue()) {
            //主症状获取下一个问题
            AnswerRequestVo answerVo = questionVo.getAnswers().get(0);
            question = getNextDiagnosisMainsympQuestion(answerVo.getContent(), 0, userInfo);
        } else if(questionVo.getType() == QuestionEnum.医学问题.getValue()) {
            //如果不是主症状获取下一个问题
            question = getNextQuestion(dmQuestion, questionVo, userInfo);
        }
        if (question == null) {
            // 生成诊断结果
            LOGGER.info("生成诊断结果{}", diagnosisId);
            List<UserDiagnosisOutcome> userDiagnosisOutcomes = diagnosisOutcome(diagnosisId, questionVo.getSympCode());
            MedicineSortUtil.sortUserDiagnosisOutcome(userDiagnosisOutcomes);
            if (userDiagnosisOutcomes.size() > 5) {
                userDiagnosisOutcomes.subList(0, 5);
            }
            List<IAnswerVo> outcomeVos = DiagnosisOutcomeVo.convertDiagnosisOutcomeVo(userDiagnosisOutcomes);
            BasicQuestionVo basicQuestionVo = new BasicQuestionVo();
            basicQuestionVo.setType(QuestionEnum.诊断结果.getValue());
            basicQuestionVo.setDiagnosisId(diagnosisId);
            basicQuestionVo.setSympCode(questionVo.getSympCode());
            basicQuestionVo.setAnswers(outcomeVos);
            return basicQuestionVo;
        }
//        else if (question.getQuestionType() == QuestionEnum.伴随症状.getValue()) {
//            //TODO 伴随症状 去重复，算权重，排序:正向特异性多的排前面
//            LOGGER.info("生成伴随症状{}", diagnosisId);
//            BasicQuestionVo basicQuestionVo = symptomAccompanyService.getSymptomAccompany(diagnosisId, dmQuestion.getMainSympCode(), userInfo, dmQuestion.getSympName());
//            medicineAnswerService.saveDiagnosisAnswer(basicQuestionVo);
//            return basicQuestionVo;
//        }
        BasicQuestionVo basicQuestionVo = null;
        LinkedHashSet<IAnswerVo> specAnswer = null;
        //非伴随症状问题
        if (question.getQuestionType() != QuestionEnum.伴随症状.getValue()) {
            //查询答案,过滤年龄，性别
            List<DiagnosisQuestionAnswer> dqAnswers = medicineAnswerService.listDiagnosisQuestionAnswer(question.getQuestionCode(), userInfo);
            // 答案排序，答案之前的问题设计到的正向特异性疾病权重
            // 查询之前所有的提问 类型101+诊断编码，找到所有的正向特异性疾病编码
            specAnswer = medicineAnswerService.getSpecAnswer(dqAnswers, diagnosisId);
            // 答案排序，答案内部权重
            LinkedHashSet<IAnswerVo> answers = MedicineSortUtil.sortAnswer(dqAnswers);
            specAnswer.addAll(answers);
        } else {
            //TODO 伴随症状 去重复，算权重，排序:正向特异性多的排前面
            specAnswer = symptomAccompanyService.getSymptomAccompanyAnswer(diagnosisId, dmQuestion.getMainSympCode(), userInfo);
        }
        basicQuestionVo = new BasicQuestionVo(question, diagnosisId, questionVo.getSympCode());
        basicQuestionVo.setAnswers(new ArrayList<>(specAnswer));
        // 问题转换
        medicineAnswerService.saveDiagnosisAnswer(basicQuestionVo);
        return basicQuestionVo;
    }


    /**
     * 诊断方法
     *
     * @param diagnosisId
     */
    public List<UserDiagnosisOutcome> diagnosisOutcome(Long diagnosisId, String mainSympCode) {
        List<UserDiagnosisOutcome> userDiagnosisOutcomes = new ArrayList<>();
        List<UserDiagnosisDetail> udds = userDiagnosisDetailDao.listUserDiagnosisDetail(diagnosisId);
        Set<String> questionCodes = new HashSet<>();
        Set<String> answerCodes = new HashSet<>();
        Set<String> forwardDiseaseCodeSet = new HashSet<>();
        Set<String> nothingDiseaseCodeSet = new HashSet<>();
        Set<String> reverseDiseaseCodeSet = new HashSet<>();
        UserDiagnosisDetail mainsympConcsympQuestion = null;
        for (Iterator iterator=udds.iterator();iterator.hasNext();) {
            UserDiagnosisDetail udd = (UserDiagnosisDetail) iterator.next();
            if(udd.getAnswerTime()==null)
                continue;
            questionCodes.add(udd.getQuestionCode());
            if (udd.getQuestionType() == QuestionEnum.伴随症状.getValue()) {
                mainsympConcsympQuestion = udd;
                continue;
            }
            answerCodes.addAll(JSON.parseArray(udd.getAnswerCode(),String.class));

            String forwardDiseaseCode = udd.getForwardDiseaseCode();
            String nothingDiseaseCode = udd.getNothingDiseaseCode();
            String reverseDiseaseCode = udd.getReverseDiseaseCode();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(forwardDiseaseCode)) {
                List<String> codes = (List) JSON.parseArray(forwardDiseaseCode);
                if (codes != null && codes.size() > 0)
                    forwardDiseaseCodeSet.addAll(codes);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(reverseDiseaseCode)) {
                List<String> codes = (List) JSON.parseArray(reverseDiseaseCode);
                if (codes != null && codes.size() > 0)
                    reverseDiseaseCodeSet.addAll(codes);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(nothingDiseaseCode)) {
                List<String> codes = (List) JSON.parseArray(nothingDiseaseCode);
                if (codes != null && codes.size() > 0)
                    nothingDiseaseCodeSet.addAll(codes);
            }
        }

        // 计算所有答案、对应疾病的权重，得出疾病的权重。
        // 每个答案出现同样的疾病，则权重相加
        // 根据特异性分组
        //
        // 保存所有的诊断结果

        //查询所有的问题  -疾病-主症状，获取权重
//        Map<String, List<DiagnosisMainsympQuestion>> questionMap =this.mapQuestion(mainSympCode);
        //查询所有的答案  -疾病-主症状，获得权重
//        Map<String, List<DiagnosisQuestionAnswer>> answersMap = medicineAnswerService.mapAnswers(questionCodes, answerCodes);
        //计算问题权重
        Map<String, List<DiagnosisMainsympConcsymp>> dmcsMap = null;
        List<String> concSympCodes = new ArrayList<>();
        //获取伴随症状数据
        if (mainsympConcsympQuestion != null && StringUtils.isNotEmpty(mainsympConcsympQuestion.getAnswerCode())) {
            concSympCodes = (List) JSON.parseArray(mainsympConcsympQuestion.getAnswerCode());
            if (concSympCodes != null && concSympCodes.size() > 0) {
                dmcsMap = symptomAccompanyService.mapDiagnosisMainsympConcsymp(mainSympCode, concSympCodes);
            }
        }
        //主症状下的问题总数
        List<DiagnosisMainsympQuestion> dmQuestions = diagnosisMainsympQuestionDao.listDiagnosisMainsympQuestion(mainSympCode);
        // 查找症状下疾病答案最多的答案总数总数，条件：所有疾病、主、问题，排序，取最多的一条
        Map<String, Map<String, DiagnosisMainsympQuestion>> deiseaseQuestionMap = listAnswerCount(mainSympCode);
        // 获取所有的答案
        List<MedicineQuestionVo> mqvAnswers = diagnosisQuestionAnswerDao.listMedicineQuestionVo(questionCodes, answerCodes);
        Map<String, List<MedicineQuestionVo>> mqvAnswerMap = new HashMap<>();
        for (Iterator iterator = mqvAnswers.iterator(); iterator.hasNext(); ) {
            MedicineQuestionVo mqv = (MedicineQuestionVo) iterator.next();
            List<MedicineQuestionVo> questions = mqvAnswerMap.get(mqv.getDiseaseCode()) == null ? new ArrayList<>() : mqvAnswerMap.get(mqv.getDiseaseCode());
            questions.add(mqv);
            mqvAnswerMap.put(mqv.getDiseaseCode(), questions);
        }
        //查询所有的疾病名称
        Map<String, DiagnosisDisease> diagnosisDiseaseMap = diagnosisDiseaseService.mapDiagnosisDisease(mqvAnswerMap.keySet());

        for (Map.Entry<String, List<MedicineQuestionVo>> entry : mqvAnswerMap.entrySet()) {
            Double diseaseWeight = 0d;
            StringBuffer calculationFormula = new StringBuffer();
            for (MedicineQuestionVo mqv : entry.getValue()) {
                if (deiseaseQuestionMap.get(mqv.getDiseaseCode()) == null || deiseaseQuestionMap.get(mqv.getDiseaseCode()).get(mqv.getQuestionCode()) == null)
                    continue;
                DiagnosisMainsympQuestion dmQuestion = deiseaseQuestionMap.get(mqv.getDiseaseCode()).get(mqv.getQuestionCode());
                if (mqv.getQuestionType() == QuestionEnum.伴随症状.getValue()) {
                    diseaseWeight = diseaseWeight + diagnosisOutcome(dmQuestions.size(), calculationFormula, mqv, dmcsMap, dmQuestion);
                } else {

                    Double Y = questionWeightFormula(dmQuestions.size(), mqv.getQuestionWeight(), mqv.getQuestionStandardDeviation(), calculationFormula);
                    Double N = answerWeightFormula(mqv.getAnswerWeight(), mqv.getAnswerStandardDeviation(), dmQuestion, calculationFormula);
                    diseaseWeight = diseaseWeight + diseaseWeightFormula(Y, N, calculationFormula);
                }
//
//                //排序在最后的问题的权重 X=（100-N*（N+1）/2 *标准差）/(N+1)
//                Double X = (100 - dmQuestions.size() * (dmQuestions.size() + 1) / 2 * mqv.getQuestionStandardDeviation()) / (dmQuestions.size() + 1);
//                calculationFormula.append("Double " + X + " = (100 - " + dmQuestions.size() + " * (" + dmQuestions.size() + " + 1) / 2 * " + mqv.getQuestionStandardDeviation() + ") / (" + dmQuestions.size() + " + 1)");
//                //每个问题的权重 Y=    X+（问题权重排序-1）*标准差
//                Double Y = X + (mqv.getQuestionWeight() - 1) * mqv.getQuestionStandardDeviation();
//                calculationFormula.append("&&");
//                calculationFormula.append("Double " + Y + " = " + X + " + (" + mqv.getQuestionWeight() + " - 1) * " + mqv.getQuestionStandardDeviation() + "");
//                if (deiseaseQuestionMap.get(mqv.getDiseaseCode()) == null || deiseaseQuestionMap.get(mqv.getDiseaseCode()).get(mqv.getQuestionCode()) == null)
//                    continue;
//                DiagnosisMainsympQuestion dmQuestion = deiseaseQuestionMap.get(mqv.getDiseaseCode()).get(mqv.getQuestionCode());
//                //排序在最后的答案的权重  X=（100-N*（N+1）/2 *标准差）/(N+1)
//                Double M = (100 - dmQuestion.getAnswerTotal() * mqv.getAnswerStandardDeviation() / (dmQuestion.getAnswerTotal() + 1));
//                calculationFormula.append("&&");
//                calculationFormula.append("Double " + M + " = (100 - " + dmQuestion.getAnswerTotal() + " * " + mqv.getAnswerStandardDeviation() + " / (" + dmQuestion.getAnswerTotal() + " + 1));");
//                //每个答案的权重   N=M+（答案权重排序-1）*标准差
//                Double N = M + (mqv.getAnswerWeight() - 1) * mqv.getAnswerStandardDeviation();
//                calculationFormula.append("&&");
//                calculationFormula.append("Double " + N + " = " + M + " + (" + mqv.getAnswerWeight() + " - 1) * " + mqv.getAnswerStandardDeviation() + "");
//                //疾病的权重每个问题和答案的乘积求和。
//                calculationFormula.append("&&");
//                calculationFormula.append("diseaseWeight = " + diseaseWeight + " + " + N + " * " + Y);
//                calculationFormula.append("&&");
//                diseaseWeight = diseaseWeight + N * Y;
//                if (mqv.getAnswerSpec() == 0)
//                    nothingDiseaseCodeSet.add(mqv.getDiseaseCode());
//                if (mqv.getAnswerSpec() == 1)
//                    forwardDiseaseCodeSet.add(mqv.getDiseaseCode());    //正向特异性编码
//                if (mqv.getAnswerSpec() == -1)
//                    reverseDiseaseCodeSet.add(mqv.getDiseaseCode());    //反向特异性编码

                if (nothingDiseaseCodeSet.contains(mqv.getDiseaseCode()))
                    nothingDiseaseCodeSet.add(mqv.getDiseaseCode());
                if (forwardDiseaseCodeSet.contains(mqv.getDiseaseCode()))
                    forwardDiseaseCodeSet.add(mqv.getDiseaseCode());    //正向特异性编码
                if (reverseDiseaseCodeSet.contains(mqv.getDiseaseCode()))
                    reverseDiseaseCodeSet.add(mqv.getDiseaseCode());    //反向特异性编码
            }
            UserDiagnosisOutcome udo = new UserDiagnosisOutcome();
            udo.setDiagnosisId(diagnosisId);
            udo.setDiseaseCode(entry.getValue().get(0).getDiseaseCode());
            udo.setDiseaseName(diagnosisDiseaseMap.get(entry.getValue().get(0).getDiseaseCode()).getDiseaseName());
            udo.setDescription(diagnosisDiseaseMap.get(entry.getValue().get(0).getDiseaseCode()).getDefinition());
            udo.setAnswerSpec(checkSpec(entry.getValue().get(0).getDiseaseCode(), forwardDiseaseCodeSet, nothingDiseaseCodeSet, reverseDiseaseCodeSet));
            udo.setWeight(diseaseWeight);
            udo.setProbability(0.5);
            udo.setCalculationFormula(calculationFormula.toString());
            userDiagnosisOutcomeDao.insert(udo);
            userDiagnosisOutcomes.add(udo);
        }
        return userDiagnosisOutcomes;
    }

    /**
     * 计算伴随症状权重
     *
     * @param mqv
     */
    public Double diagnosisOutcome(int questionSize, StringBuffer calculationFormula, MedicineQuestionVo mqv, Map<String, List<DiagnosisMainsympConcsymp>> dmcsMap, DiagnosisMainsympQuestion dmQuestion) {
        if (dmcsMap == null || dmcsMap.size() == 0) {
            return 0d;
        }
        List<DiagnosisMainsympConcsymp> dmcs = dmcsMap.get(mqv.getDiseaseCode());
        if (dmcs == null || dmcs.size() == 0) {
            return 0d;
        }
        Double weight = 0d;
        Double N = 0d;//答案
        Double Y = questionWeightFormula(questionSize, mqv.getQuestionWeight(), mqv.getQuestionStandardDeviation(), calculationFormula); //问题
        for (DiagnosisMainsympConcsymp mdc : dmcs) {
            N = N + answerWeightFormula(mqv.getAnswerWeight(), mqv.getAnswerStandardDeviation(), dmQuestion, calculationFormula);

        }
        return diseaseWeightFormula(Y, N, calculationFormula);
    }

    public Double questionWeightFormula(int questionSize, Double questionWeight, Double questionStandardDeviation, StringBuffer calculationFormula) {
        //排序在最后的问题的权重 X=（100-N*（N+1）/2 *标准差）/(N+1)
        Double X = (100 - questionSize * (questionSize + 1) / 2 * questionStandardDeviation) / (questionSize + 1);
        calculationFormula.append("&&");
        calculationFormula.append("Double " + X + " = (100 - " + questionSize + " * (" + questionSize + " + 1) / 2 * " + questionStandardDeviation + ") / (" + questionSize + " + 1)");
        //每个问题的权重 Y=    X+（问题权重排序-1）*标准差
        Double Y = X + (questionWeight - 1) * questionStandardDeviation;
        calculationFormula.append("&&");
        calculationFormula.append("Double " + Y + " = " + X + " + (" + questionWeight + " - 1) * " + questionStandardDeviation + "");
        return Y;
    }

    public Double answerWeightFormula(Double answerWeight, Double answerStandardDeviation, DiagnosisMainsympQuestion dmQuestion, StringBuffer calculationFormula) {
        //排序在最后的答案的权重  X=（100-N*（N+1）/2 *标准差）/(N+1)
        Double M = (100 - dmQuestion.getAnswerTotal() * answerStandardDeviation / (dmQuestion.getAnswerTotal() + 1));
        calculationFormula.append("&&");
        calculationFormula.append("Double " + M + " = (100 - " + dmQuestion.getAnswerTotal() + " * " + answerStandardDeviation + " / (" + dmQuestion.getAnswerTotal() + " + 1));");
        //每个答案的权重   N=M+（答案权重排序-1）*标准差
        Double N = M + (answerWeight - 1) * answerStandardDeviation;
        calculationFormula.append("&&");
        calculationFormula.append("Double " + N + " = " + M + " + (" + answerWeight + " - 1) * " + answerStandardDeviation + "");
        //疾病的权重每个问题和答案的乘积求和。
        return N;
    }

    public Double diseaseWeightFormula(Double Y, Double N, StringBuffer calculationFormula) {
        calculationFormula.append("&&");
        calculationFormula.append("diseaseWeight =  " + N + " * " + Y);
        return N * Y;
    }


    /**
     * 计算正向特异性，反向特异性，-1是反向特异性，2、是正向特异性，1是正+反特异性 0是无特异性
     *
     * @param diseaseCode
     * @param forwardDiseaseCodeSet
     * @param nothingDiseaseCodeSet
     * @param reverseDiseaseCodeSet
     * @return
     */
    public int checkSpec(String diseaseCode, Set<String> forwardDiseaseCodeSet, Set<String> nothingDiseaseCodeSet, Set<String> reverseDiseaseCodeSet) {
        int spec = 0;
        if (forwardDiseaseCodeSet.contains(diseaseCode)) {
            spec = spec + 2;
        }
        if (reverseDiseaseCodeSet.contains(diseaseCode)) {
            spec = spec + -1;
        }
        return spec;
    }

    /**
     * 处理主症状下的所有问题
     *
     * @param mainSympCode
     * @return
     */
    public Map<String, Map<String, DiagnosisMainsympQuestion>> listAnswerCount(String mainSympCode) {
        List<DiagnosisMainsympQuestion> dmQuestions = diagnosisMainsympQuestionDao.listAnswerCount(mainSympCode);
        Map<String, Map<String, DiagnosisMainsympQuestion>> deiseaseQuestionMap = new HashMap<>();
        for (DiagnosisMainsympQuestion dmq : dmQuestions) {
            Map<String, DiagnosisMainsympQuestion> qMap = deiseaseQuestionMap.get(dmq.getDiseaseCode()) == null ? new HashMap<>() : deiseaseQuestionMap.get(dmq.getDiseaseCode());
            qMap.put(dmq.getQuestionCode(), dmq);
            deiseaseQuestionMap.put(dmq.getDiseaseCode(), qMap);
        }
        return deiseaseQuestionMap;
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
            if (question.getGender() != null && question.getGender() != userInfo.getGender()) {
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
