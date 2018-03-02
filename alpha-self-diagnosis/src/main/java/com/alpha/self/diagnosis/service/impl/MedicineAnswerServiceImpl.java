package com.alpha.self.diagnosis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.commons.enums.Unit;
import com.alpha.commons.exception.ServiceException;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.self.diagnosis.dao.*;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.enums.SyAnswerType;
import com.alpha.self.diagnosis.pojo.vo.*;
import com.alpha.self.diagnosis.service.DiagnosisMainsympNeConcsympService;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.self.diagnosis.utils.ServiceUtil;
import com.alpha.server.rpc.diagnosis.pojo.*;
import com.alpha.server.rpc.user.pojo.UserDiagnosisDetail;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.treatscheme.dao.DiagnosisDiseaseDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by xc.xiong on 2017/9/11.
 * 答案相关操作类
 */
@Service
public class MedicineAnswerServiceImpl implements MedicineAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicineAnswerServiceImpl.class);
    @Resource
    private UserDiagnosisDetailDao userDiagnosisDetailDao;
    @Resource
    private DiagnosisQuestionAnswerDao diagnosisQuestionAnswerDao;
    @Resource
    private MedicineQuestionService medicineQuestionService;
    @Resource
    private DiagnosisMainsympQuestionDao diagnosisMainsympQuestionDao;
    @Resource
    private SyDiagnosisAnswerDao syDiagnosisAnswerDao;
    @Resource
    private DiagnosisMainsympNeConcsympService diagnosisMainsympNeConcsympService;
    @Resource
    private DiagnosisDiseaseDao diagnosisDiseaseDao;
    @Resource
    private DiagnosisMainsympConcsympDao diagnosisMainsympConcsympDao;

    /**
     * 记录用户的答案
     *
     * @param questionVo
     */
    public void updateDiagnosisAnswer(QuestionRequestVo questionVo, UserInfo userInfo) {
        if (questionVo.getAnswers() == null || questionVo.getAnswers().size() == 0) {
            LOGGER.error("没有找到对应的答案");
            return;
        }
        List<String> answerCodes = new ArrayList<>();
        //Set<String> answerContents = new HashSet<>();
        List<String> answerContents = new ArrayList<>();
        String questionCode = questionVo.getQuestionCode();
        String mainSympCode = questionVo.getSympCode();
        Map<Integer, Set<String>> answerSpecMap = new HashMap<>();
        DiagnosisMainsympQuestion diagnosisQuestion = diagnosisMainsympQuestionDao.getDiagnosisMainsympQuestion(questionCode, mainSympCode);
        //判断当前问题是否有答案转化标志
        if(diagnosisQuestion != null && StringUtils.isNotEmpty(diagnosisQuestion.getParseClass())) {
            final String parseClass = diagnosisQuestion.getParseClass();
            List<DiagnosisQuestionAnswer> diagnosisAnswerList = this.listDiagnosisQuestionAnswer(mainSympCode, questionCode, userInfo);
            List<AnswerRequestVo> answervoList = questionVo.getAnswers();
            //遍历客户端回答的答案
            for(AnswerRequestVo answerVo : answervoList) {
                String inputAnswer = answerVo.getContent();
                //如是温度,则将页面传进来的37.5转为37.5℃
                if(parseClass.equals(Unit.TEMPERATURE.getValue())) {
                    inputAnswer = inputAnswer.concat(Unit.TEMPERATURE.getText());
                }
                String inputAnswerWithUnit = inputAnswer;
                //页面自由输入的答案与数据库的某一个答案匹配
                Optional<DiagnosisQuestionAnswer> answerOptional = diagnosisAnswerList.stream().filter(e->matchWithUnit(inputAnswerWithUnit, parseClass, e)).findFirst();
                if(answerOptional.isPresent()) {
                    answerCodes.add(answerOptional.get().getAnswerCode());
                    answerContents.add(inputAnswerWithUnit);
                    //答案转化成功后模拟页面构造参数
                    answerVo.setContent(answerOptional.get().getAnswerCode());
                    answerVo.setAnswerTitle(inputAnswerWithUnit);
                } else {
                    LOGGER.error("页面的答案没能与知识库的答案匹配");
                    answerCodes.add("-1");
                    answerContents.add(inputAnswerWithUnit);
                }
            }
        } else {
            for (AnswerRequestVo answerVo : questionVo.getAnswers()) {
                answerCodes.add(answerVo.getContent());
                answerContents.add(answerVo.getAnswerTitle());
            }
        }
        //常见伴随症状
        if(questionVo.getType() == QuestionEnum.常见伴随症状.getValue()) {
            Set<String> commonConcSympNames = answerContents.stream().collect(Collectors.toSet());
            List<DiagnosisMainsympConcsymp> diagnosisMainsympConcsympList = diagnosisMainsympConcsympDao.listByConcSympNames(mainSympCode, commonConcSympNames);
            answerSpecMap = new HashMap<>();
            for (DiagnosisMainsympConcsymp dmc : diagnosisMainsympConcsympList) {
                Set<String> specSet = answerSpecMap.get(dmc.getSympSpec()) == null ? new HashSet<String>() : answerSpecMap.get(dmc.getSympSpec());
                if(StringUtils.isNotEmpty(dmc.getDiseaseCode())) {
                    specSet.add(dmc.getDiseaseCode());
                    answerSpecMap.put(dmc.getSympSpec(), specSet);
                }
            }
        }
        //添加阴性伴随症状
        if(questionVo.getType() == QuestionEnum.伴随症状.getValue()) {
            //主症状下的所有阴性伴随症状
            List<DiagnosisMainsympNeConcsymp> neconcsympList = diagnosisMainsympNeConcsympService.listDiagnosisMainsympNeConcsymp(mainSympCode);
            neconcsympList.stream().filter(e->!answerCodes.contains(e.getConcSympCode())).forEach(e->{
                answerCodes.add(e.getConcNesympCode());
                if(StringUtils.isNotEmpty(e.getConcSympName())) {
                    answerContents.add(e.getConcSympName());
                }
            });
        }
        //查询医学问题下的隐藏答案
        if(questionVo.getType() == QuestionEnum.医学问题.getValue()) {
            List<String> hiddenAnswerCodes = new ArrayList<>();
            List<DiagnosisQuestionAnswer> hiddenAnswerList = diagnosisQuestionAnswerDao.listHiddenAnswers(questionCode);
            if(CollectionUtils.isNotEmpty(hiddenAnswerList)) {
                LOGGER.info("开始处理问题"+questionCode+"的隐藏答案");
                //找出未回答的隐藏答案编码
                hiddenAnswerCodes = hiddenAnswerList.stream().filter(e -> !answerCodes.contains(e.getMutuallyAnswerCode()))
                        .map(DiagnosisQuestionAnswer::getAnswerCode).distinct().collect(Collectors.toList());
                //将未回答的隐藏答案组装到用户的已回答答案中
                Set<String> hiddenAnswerCodeSet = hiddenAnswerCodes.stream().collect(Collectors.toSet());
                hiddenAnswerList = hiddenAnswerList.stream().filter(e->hiddenAnswerCodeSet.contains(e.getAnswerCode())).collect(Collectors.toList());
                List<AnswerRequestVo> answerRequestVoList = questionVo.getAnswers();
                Set<String> replayAnsewrCodeSet = new HashSet<>();
                for (DiagnosisQuestionAnswer hiddenAnswer : hiddenAnswerList) {
                    if(!replayAnsewrCodeSet.contains(hiddenAnswer.getAnswerCode())) {
                        replayAnsewrCodeSet.add(hiddenAnswer.getAnswerCode());
                        AnswerRequestVo hiddenAnswerVo = new AnswerRequestVo();
                        hiddenAnswerVo.setAnswerTitle(hiddenAnswer.getAnswerTitle());
                        hiddenAnswerVo.setContent(hiddenAnswer.getAnswerCode());
                        answerRequestVoList.add(hiddenAnswerVo);
                    }
                }
                LOGGER.info("问题"+questionCode+"的隐藏答案为"+hiddenAnswerCodes);
            }
            List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(mainSympCode, questionVo.getQuestionCode(), answerCodes, hiddenAnswerCodes);
            answerSpecMap = new HashMap<>();
            for (DiagnosisQuestionAnswer dqa : dqAnswers) {
                Set<String> specSet = answerSpecMap.get(dqa.getAnswerSpec()) == null ? new HashSet<String>() : answerSpecMap.get(dqa.getAnswerSpec());
                if(StringUtils.isNotEmpty(dqa.getDiseaseCode())) {
                    specSet.add(dqa.getDiseaseCode());
                    answerSpecMap.put(dqa.getAnswerSpec(), specSet);
                }
            }
        }

        UserDiagnosisDetail udd = userDiagnosisDetailDao.getUserDiagnosisDetail(questionVo.getDiagnosisId(), questionVo.getQuestionCode());
        if (udd == null) {
            throw new ServiceException(ResponseStatus.INVALID_VALUE, "没有找到提问记录");
        }

        Set<String> forwardDiseaseCode = answerSpecMap.get(1) == null ? new HashSet<String>() : answerSpecMap.get(1);   //正向特异性
        Set<String> reverseDiseaseCode = answerSpecMap.get(-1) == null ? new HashSet<String>() : answerSpecMap.get(-1);   //反向特异性
        Set<String> nothingDiseaseCode = answerSpecMap.get(0) == null ? new HashSet<String>() : answerSpecMap.get(0); //无特异性
//        udd.setAnswerCode(ServiceUtil.arrayConvertToString(answerCodes));
        udd.setAnswerCode(JSON.toJSONString(answerCodes));
        udd.setAnswerContent(ServiceUtil.arrayConvertToString(answerContents));
        udd.setAnswerJson(JSON.toJSONString(questionVo.getAnswers()));
        udd.setForwardDiseaseCode(JSON.toJSONString(forwardDiseaseCode));
        udd.setReverseDiseaseCode(JSON.toJSONString(reverseDiseaseCode));
        udd.setNothingDiseaseCode(JSON.toJSONString(nothingDiseaseCode));
        udd.setQuestionType(questionVo.getType());
        udd.setAnswerTime(new Date());
        udd.setSympCode(questionVo.getSympCode());
        userDiagnosisDetailDao.update(udd);
    }


    /**
     * 保存问题答案
     *
     * @param questionVo
     */
    public void saveDiagnosisAnswer(BasicQuestionVo questionVo, UserInfo userInfo) {
        Long diagnosisId = questionVo.getDiagnosisId();
        String questionCode = questionVo.getQuestionCode();
        //查询同一就诊过程中主诉是否有变化
        String sympCode = questionVo.getSympCode();
        List<UserDiagnosisDetail> otherMainSympDetailList = userDiagnosisDetailDao.listUserDiagnosisDetail(diagnosisId, sympCode);
        if(CollectionUtils.isNotEmpty(otherMainSympDetailList)) {
            //删除其它主诉的问诊过程
            userDiagnosisDetailDao.deleteUserDiagnosisDetail(diagnosisId, sympCode);
        }

        UserDiagnosisDetail udd = userDiagnosisDetailDao.getUserDiagnosisDetail(diagnosisId, questionCode);
        if(udd != null) {
            return;
        }

        List<String> answerCodes = new ArrayList<>();
        List<String> answerContents = new ArrayList<>();

        for (IAnswerVo answerVo : questionVo.getAnswers()) {
            if (answerVo instanceof BasicAnswerVo) {
                BasicAnswerVo answer = (BasicAnswerVo) answerVo;
                answerCodes.add(answer.getAnswerValue());
                answerContents.add(answer.getAnswerTitle());
            } else if (answerVo instanceof Level1AnswerVo) {
                Level1AnswerVo answer = (Level1AnswerVo) answerVo;
                answerCodes.add(answer.getAnswerValue());
                answerContents.add(answer.getAnswerTitle());
            }
        }
        udd = new UserDiagnosisDetail();
        udd.setDiagnosisId(questionVo.getDiagnosisId());
        udd.setUserId(userInfo.getUserId());
        udd.setMemberId(userInfo.getUserId());
        udd.setQuestionCode(questionVo.getQuestionCode());
        udd.setAnswerCode(JSON.toJSONString(answerCodes));
        udd.setAnswerContent(JSON.toJSONString(answerContents));
        udd.setForwardDiseaseCode(JSON.toJSONString(new HashSet<>()));
        udd.setReverseDiseaseCode(JSON.toJSONString(new HashSet<>()));
        udd.setNothingDiseaseCode(JSON.toJSONString(new HashSet<>()));
        udd.setQuestionType(questionVo.getType());
        udd.setSympCode(questionVo.getSympCode());
        udd.setQuestionContent(questionVo.getQuestionTitle());
        userDiagnosisDetailDao.insert(udd);
    }

    /**
     * 获取所有的答案，并过滤年龄，性别
     *
     * @param questionCode
     * @param userInfo
     * @return
     */
    public List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String mainSympCode, String questionCode, UserInfo userInfo) {
        //查询答案
        ArrayList<String> questionCodes = new ArrayList<>();
        questionCodes.add(questionCode);
        List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(mainSympCode, questionCodes);
        dqAnswers = filterHiddenAnswer(questionCode, dqAnswers);
        filterAnswer(dqAnswers, userInfo);
        //查询小类所属的大类答案编码
        List<String> syanswercodeList = dqAnswers.stream().filter(e->StringUtils.isNotEmpty(e.getSyAnswerCode()))
                .map(DiagnosisQuestionAnswer::getSyAnswerCode).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(syanswercodeList)) {
            return dqAnswers;
        }
        //根据大类编码查询答案大类
        List<SyDiagnosisAnswer> syDiagnosisAnswerList = syDiagnosisAnswerDao.listSyDiagnosisAnswer(syanswercodeList, SyAnswerType.PARENT_ANSWER.getValue());
        Map<String, SyDiagnosisAnswer> syAnswerMap = syDiagnosisAnswerList.stream().collect(Collectors.toMap(SyDiagnosisAnswer::getAnswerCode, Function.identity()));
        //建立小类与大类的关联关系
        dqAnswers = dqAnswers.stream().peek(e->{
            if(StringUtils.isNotEmpty(e.getSyAnswerCode())) {
                SyDiagnosisAnswer syAnswer = syAnswerMap.get(e.getSyAnswerCode());
                e.setSyAnswer(syAnswer);
                if(syAnswer != null)
                    LOGGER.info(e.getContent()+"所属大类为"+syAnswer.getContent());
            }
        }).collect(Collectors.toList());
        return dqAnswers;
    }

    /**
     * 获取所有的答案，并过滤年龄，性别
     *
     * @param questionCodes
     * @param userInfo
     * @return
     */
    public List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String mainSympCode, Collection<String> questionCodes, UserInfo userInfo) {
        //查询答案
        List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(mainSympCode, questionCodes);
        filterAnswer(dqAnswers, userInfo);
        return dqAnswers;
    }


    @Override
    public List<SyDiagnosisAnswer> listSyDiagnosisAnswer(String connCode, UserInfo userInfo) {
        List<SyDiagnosisAnswer> answerList = syDiagnosisAnswerDao.listSyDiagnosisAnswer(connCode, SyAnswerType.SUB_ANSWER.getValue());
        filterSyAnswer(answerList, userInfo);
        return answerList;
    }

    /**
     * 过滤隐藏答案
     * @param dqAnswers
     */
    public List<DiagnosisQuestionAnswer> filterHiddenAnswer(String questionCode, List<DiagnosisQuestionAnswer> dqAnswers) {
        List<DiagnosisQuestionAnswer> hiddenAnswerList = diagnosisQuestionAnswerDao.listHiddenAnswers(questionCode);
        Set<String> hiddenAnswerCodeSet = hiddenAnswerList.stream().map(DiagnosisQuestionAnswer::getAnswerCode).collect(Collectors.toSet());
        List<DiagnosisQuestionAnswer> answerList = dqAnswers.stream().filter(e->!hiddenAnswerCodeSet.contains(e.getAnswerCode())).collect(Collectors.toList());
        System.out.println("answerList.size="+answerList.size());
        return answerList;
    }

    public void filterAnswer(List<DiagnosisQuestionAnswer> dqAnswers, UserInfo userInfo) {
        for (Iterator iterator = dqAnswers.iterator(); iterator.hasNext(); ) {
            DiagnosisQuestionAnswer answer = (DiagnosisQuestionAnswer) iterator.next();
            if (answer.getGender() != null && answer.getGender() > 0 && answer.getGender() != userInfo.getGender()) {
                iterator.remove();
                continue;//过滤性别
            }
            float age = DateUtils.getAge(userInfo.getBirth());
            if ((answer.getMinAge() != null && answer.getMinAge() > age) || (answer.getMaxAge() != null && answer.getMaxAge() < age)) {
                iterator.remove();
                continue;//过滤年龄
            }
        }
    }

    /**
     * 根据答案查询所有的答案，计算特异性
     */
    @Override
    public Map<Integer, Set<String>> mapAnswerSpec(String mainSympCode, String questionCode, Collection<String> answerCodes, Collection<String> hiddenAnswerCodes) {
        //List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(questionCode, answerCodes, hiddenAnswerCodes);
        List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(mainSympCode, questionCode, answerCodes, hiddenAnswerCodes);
        Map<Integer, Set<String>> answerSpecMap = new HashMap<>();
        for (DiagnosisQuestionAnswer dqa : dqAnswers) {
            Set<String> specSet = answerSpecMap.get(dqa.getAnswerSpec()) == null ? new HashSet<String>() : answerSpecMap.get(dqa.getAnswerSpec());
            specSet.add(dqa.getDiseaseCode());
            answerSpecMap.put(dqa.getAnswerSpec(), specSet);
        }
        return answerSpecMap;
    }

    /**
     * 查询正向特异性的疾病编码
     *
     * @param diagnosisId
     * @return
     */
    public Set<String> listSpecCode(Long diagnosisId) {
        Set<String> codeSet = new HashSet<>();
        List<UserDiagnosisDetail> udds = userDiagnosisDetailDao.listUserDiagnosisDetail(diagnosisId);
        for (UserDiagnosisDetail udd : udds) {
            String forwardDiseaseCode = udd.getForwardDiseaseCode();
            if (StringUtils.isNotEmpty(forwardDiseaseCode)) {
                List<String> codes = (List) JSON.parseArray(forwardDiseaseCode);
                if (codes != null && codes.size() > 0)
                    codeSet.addAll(codes);
            }
        }
        return codeSet;
    }

    /**
     * 查询正向特异性的答案
     * 统计正向特异性最多的
     *
     * @param dqAnswers
     * @param diagnosisId
     * @return
     */
    public LinkedHashSet<IAnswerVo> getSpecAnswer(List<DiagnosisQuestionAnswer> dqAnswers, Long diagnosisId) {
        Set<String> specCodeSet = listSpecCode(diagnosisId);
        LinkedHashSet<IAnswerVo> answerVos = new LinkedHashSet<>();
        if (specCodeSet == null || specCodeSet.size() == 0)
            return answerVos;
        for (DiagnosisQuestionAnswer dqa : dqAnswers) {
            if (StringUtils.isNotEmpty(dqa.getDiseaseCode()) && specCodeSet.contains(dqa.getDiseaseCode())) {
                BasicAnswerVo answerVo = new BasicAnswerVo(dqa);
                answerVos.add(answerVo);
            }
        }
        return answerVos;
    }

    /**
     * 所有疾病下的所有答案
     *
     * @return
     */
    public Map<String, List<DiagnosisQuestionAnswer>> mapAnswers(Collection<String> questionCodes, Collection<String> answerCodes) {
        Map<String, List<DiagnosisQuestionAnswer>> questionMap = new HashMap<>();
        List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(questionCodes, answerCodes);
        for (DiagnosisQuestionAnswer dmq : dqAnswers) {
            List<DiagnosisQuestionAnswer> questions = questionMap.get(dmq.getDiseaseCode()) == null ? new ArrayList<>() : questionMap.get(dmq.getDiseaseCode());
            questions.add(dmq);
            questionMap.put(dmq.getDiseaseCode(), questions);
        }
        return questionMap;
    }

    private static boolean matchWithUnit(String inputAnswer, String parseClass, DiagnosisQuestionAnswer answer) {
        Unit inputUnit = Unit.containText(inputAnswer);
        if(inputUnit == null) {
            return false;
        }
        Double minValue = answer.getMinValue();
        Double maxValue = answer.getMaxValue();
        Double inputAnswerWithoutUnit = Double.valueOf(inputAnswer.replace(inputUnit.getText(), ""));
        if(inputUnit == Unit.MINUTE || inputUnit == Unit.HOUR || inputUnit == Unit.DAY || inputUnit == Unit.WEEK
                || inputUnit == Unit.MONTH || inputUnit == Unit.SEASON || inputUnit == Unit.YEAR) {

            Unit unit = Unit.findByValue(parseClass);

            Double answerOfInput = DateUtils.toMillSeond(inputAnswerWithoutUnit, inputUnit);
            Double answerOfMin = DateUtils.toMillSeond(minValue, unit);
            Double answerOfMax = DateUtils.toMillSeond(maxValue, unit);
            if(answerOfInput.doubleValue() >= answerOfMin.doubleValue()
                    && answerOfInput.doubleValue() <= answerOfMax.doubleValue()) {
                return true;
            }
        } else if (inputUnit == Unit.CENTIGRADE) {
            if(inputAnswerWithoutUnit >= minValue && inputAnswerWithoutUnit <= maxValue) {
                return true;
            }

        } else if (inputUnit == Unit.TEMPERATURE) {
            if(inputAnswerWithoutUnit >= minValue && inputAnswerWithoutUnit <= maxValue) {
                return true;
            }

        } else if (inputUnit == Unit.NUM_OF_TIMES) {
            if(inputAnswerWithoutUnit >= minValue && inputAnswerWithoutUnit <= maxValue) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤小类答案
     * @param dqAnswers
     * @param userInfo
     */
    private void filterSyAnswer(List<SyDiagnosisAnswer> dqAnswers, UserInfo userInfo) {
        for (Iterator iterator = dqAnswers.iterator(); iterator.hasNext(); ) {
            SyDiagnosisAnswer answer = (SyDiagnosisAnswer) iterator.next();
            if (answer.getGender() != null && answer.getGender() > 0 && answer.getGender() != userInfo.getGender()) {
                iterator.remove();
                continue;//过滤性别
            }
            float age = DateUtils.getAge(userInfo.getBirth());
            if ((answer.getMinAge() != null && answer.getMinAge() > age) || (answer.getMaxAge() != null && answer.getMaxAge() < age)) {
                iterator.remove();
                continue;//过滤年龄
            }
        }
    }

    @Override
    public LinkedHashSet<IAnswerVo> mapAnswerLevel(List<IAnswerVo> answerList) {
        List<IAnswerVo> level1AnswerList = new ArrayList<>();
        for(IAnswerVo itemAnswer : answerList) {
            BasicAnswerVo bav = (BasicAnswerVo) itemAnswer;
            SyDiagnosisAnswer syAnswer = bav.getSyAnswer();
            if(syAnswer != null) {
                String syAnswerCode = syAnswer.getAnswerCode();
                Optional<Level1AnswerVo> lv1optional = level1AnswerList.stream().map(e->{
                    Level1AnswerVo lv1 = (Level1AnswerVo) e;
                    return lv1;
                }).filter(e->e.getAnswerValue().equals(syAnswerCode)).findFirst();

                if(lv1optional.isPresent()) {
                    Level1AnswerVo level1Answer = lv1optional.get();
                    List<Level2AnswerVo> level2AnswerList = level1Answer.getLevel2Answers();
                    if(level2AnswerList == null) {
                        level2AnswerList = new ArrayList<>();
                        level1Answer.setLevel2Answers(level2AnswerList);
                    }
                    //将BasicAnswerVo转为Level2AnswerVo
                    Level2AnswerVo level2Answer = new Level2AnswerVo(bav);
                    level2AnswerList.add(level2Answer);
                } else {
                    Level1AnswerVo level1Answer = new Level1AnswerVo(syAnswer);
                    Level2AnswerVo level2Answer = new Level2AnswerVo(bav);
                    List<Level2AnswerVo> level2AnswerList = level1Answer.getLevel2Answers();
                    if(level2AnswerList == null) {
                        level2AnswerList = new ArrayList<>();
                        level2AnswerList.add(level2Answer);
                        level1Answer.setLevel2Answers(level2AnswerList);
                    } else {
                        level2AnswerList.add(level2Answer);
                    }

                    level1AnswerList.add(level1Answer);
                }
            } else {
                IAnswerVo level1Answer = new Level1AnswerVo(bav);
                level1AnswerList.add(level1Answer);
            }
        }
        LinkedHashSet<IAnswerVo> lhs = new LinkedHashSet<>(level1AnswerList);
        return lhs;
    }

}
