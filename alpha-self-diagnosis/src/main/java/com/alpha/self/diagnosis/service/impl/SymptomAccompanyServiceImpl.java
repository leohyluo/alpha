package com.alpha.self.diagnosis.service.impl;

import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.StringUtils;
import com.alpha.commons.util.sim.CosineSimilarAlgorithm;
import com.alpha.self.diagnosis.dao.DiagnosisMainsympConcsympDao;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.service.MedicineDiagnosisService;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.self.diagnosis.service.SymptomAccompanyService;
import com.alpha.self.diagnosis.utils.MedicineSortUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympConcsymp;
import com.alpha.server.rpc.diagnosis.pojo.vo.MedicineQuestionVo;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xc.xiong on 2017/9/11.
 * 伴随症状
 */
@Service
public class SymptomAccompanyServiceImpl implements SymptomAccompanyService {


    private static final Logger LOGGER = LoggerFactory.getLogger(SymptomAccompanyServiceImpl.class);
    @Autowired
    DiagnosisMainsympConcsympDao diagnosisMainsympConcsympDao;
    @Autowired
    MedicineAnswerService medicineAnswerService;
    @Autowired
    MedicineQuestionService medicineQuestionService;
    @Autowired
    MedicineDiagnosisService medicineDiagnosisService;


    /**
     * 查询伴随症状
     *
     * @param mainSympCode
     * @return
     */
    public List<DiagnosisMainsympConcsymp> listDiagnosisMainsympConcsymp(String mainSympCode, UserInfo userInfo) {
        List<DiagnosisMainsympConcsymp> dmcs = diagnosisMainsympConcsympDao.listDiagnosisMainsympConcsymp(mainSympCode);
        for (Iterator iterator = dmcs.iterator(); iterator.hasNext(); ) {
            DiagnosisMainsympConcsymp answer = (DiagnosisMainsympConcsymp) iterator.next();
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
        return dmcs;
    }

    /**
     * 查询主症状下所有的伴随症状名称，
     *
     * @param mainSympCode
     * @return
     */
    public List<DiagnosisMainsympConcsymp> listConcsymp(String mainSympCode, UserInfo userInfo) {
        List<DiagnosisMainsympConcsymp> dmcs = diagnosisMainsympConcsympDao.listConcsymp(mainSympCode);
        for (Iterator iterator = dmcs.iterator(); iterator.hasNext(); ) {
            DiagnosisMainsympConcsymp answer = (DiagnosisMainsympConcsymp) iterator.next();
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
        return dmcs;
    }

    /**
     * 生成伴随症状提问
     *
     * @param diagnosisId
     * @param mainSympCode
     * @param userInfo
     * @param sympName
     * @return
     */
    public BasicQuestionVo getSymptomAccompany(Long diagnosisId, String mainSympCode, UserInfo userInfo, String sympName) {
        List<DiagnosisMainsympConcsymp> dmcs = this.listDiagnosisMainsympConcsymp(mainSympCode, userInfo);
        Set<String> specCodeSet = medicineAnswerService.listSpecCode(diagnosisId);//正向特异性疾病
        BasicQuestionVo questionVo = new BasicQuestionVo();
        questionVo.setQuestionTitle("除了" + sympName + "外,还出现哪些症状？");
        questionVo.setTitle("除了" + sympName + "外,还出现哪些症状？");
        questionVo.setType(QuestionEnum.伴随症状.getValue());
        questionVo.setQuestionCode("1000");
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setSympCode(mainSympCode);
        LinkedHashSet<IAnswerVo> answerVos = new LinkedHashSet<>();//非正向特异性疾病下的伴随症状
        LinkedHashSet<IAnswerVo> answerSpecVos = new LinkedHashSet<>();//正向特异性疾病下的伴随症状
        for (DiagnosisMainsympConcsymp dmc : dmcs) {
            BasicAnswerVo answer = new BasicAnswerVo(dmc);
            if (specCodeSet.contains(dmc.getDiseaseCode())) {
                answerSpecVos.add(answer);
            } else {
                answerVos.add(answer);
            }
        }
        List<IAnswerVo> answers = MedicineSortUtil.sortAnswerVo(answerSpecVos);
        answers.addAll(MedicineSortUtil.sortAnswerVo(answerVos));
        questionVo.setAnswers(answers);
        return questionVo;
    }

    /**
     * 生成伴随症状提问
     *
     * @param diagnosisId
     * @param mainSympCode
     * @param userInfo
     * @return
     */
    public LinkedHashSet<IAnswerVo> getSymptomAccompanyAnswer(Long diagnosisId, String mainSympCode, UserInfo userInfo) {
        List<DiagnosisMainsympConcsymp> dmcs = this.listDiagnosisMainsympConcsymp(mainSympCode, userInfo);
        Set<String> specCodeSet = medicineAnswerService.listSpecCode(diagnosisId);//正向特异性疾病
        LinkedHashSet<IAnswerVo> answerVos = new LinkedHashSet<>();//非正向特异性疾病下的伴随症状
        LinkedHashSet<IAnswerVo> answerSpecVos = new LinkedHashSet<>();//正向特异性疾病下的伴随症状
        LinkedHashSet<IAnswerVo> answerDiseaseVos = new LinkedHashSet<>();//疾病下伴随症状
        List<UserDiagnosisOutcome> userDiagnosisOutcomes = medicineDiagnosisService.diagnosisOutcome(diagnosisId, mainSympCode,userInfo);//计算疾病的权重
        userDiagnosisOutcomes = MedicineSortUtil.specUserDiagnosisOutcome(userDiagnosisOutcomes);//根据特异性重新计算权重
        MedicineSortUtil.sortUserDiagnosisOutcome(userDiagnosisOutcomes);//排序
        UserDiagnosisOutcome udo = null;
        if (userDiagnosisOutcomes != null && userDiagnosisOutcomes.size() > 0) {
            udo = userDiagnosisOutcomes.get(0);
            LOGGER.info("首选疾病：{}", udo.getDiseaseName());
        }
        for (DiagnosisMainsympConcsymp dmc : dmcs) {
            BasicAnswerVo answer = new BasicAnswerVo(dmc);
            if (specCodeSet.contains(dmc.getDiseaseCode())) {
                answerSpecVos.add(answer);
            } else if (udo != null && udo.getDiseaseCode().equals(dmc.getDiseaseCode())) {
                answerDiseaseVos.add(answer);
            } else {
                answerVos.add(answer);
            }
        }
        List<IAnswerVo> answers = MedicineSortUtil.sortAnswerVo(answerSpecVos);
        answers.addAll(MedicineSortUtil.sortAnswerVo(answerDiseaseVos));
        answers.addAll(MedicineSortUtil.sortAnswerVo(answerVos));
        return new LinkedHashSet<>(answers);
    }

    /**
     * 生成伴随症状提问
     *
     * @param diagnosisId
     * @param mainSympCode
     * @param userInfo
     * @return
     */
    public LinkedHashSet<IAnswerVo> listSymptomAccompany(Long diagnosisId, String mainSympCode, UserInfo userInfo, String keyword) {
        List<DiagnosisMainsympConcsymp> dmcs = this.listConcsymp(mainSympCode, userInfo);
        LinkedHashSet<IAnswerVo> answerVos = new LinkedHashSet<>();

        Iterator<DiagnosisMainsympConcsymp> it = dmcs.iterator();
        while(it.hasNext()){
            DiagnosisMainsympConcsymp dmc = it.next();
            Double similarity = 0.0;
            //判断关键词是否被包含
            if(StringUtils.isChinese(keyword.toUpperCase())){
//                System.out.println(keyword.toUpperCase() + " --  " +  dmc.getSympName() + " -- " + dmc.getSympName().indexOf(keyword));
                if(dmc.getSympName().indexOf(keyword) < 0){
                    similarity = 0.0;
                }else{
                    similarity = CosineSimilarAlgorithm.cosSimilarityByString(keyword.toUpperCase(), dmc.getSympName());
                }
            }
            else{
                similarity = CosineSimilarAlgorithm.cosSimilarityByString(keyword.toUpperCase(), dmc.getSympName());
            }

            if (similarity < 0.5) {
                it.remove();
            } else {
                dmc.setSimilarity(similarity);
            }
        }

//        for (Iterator i = dmcs.iterator(); i.hasNext(); ) {
//            DiagnosisMainsympConcsymp dmc = (DiagnosisMainsympConcsymp) i.next();
//
//            Double similarity = 0.0;
//            //判断关键词是否被包含
//            if(StringUtils.isChinese(keyword.toUpperCase())){
//                System.out.println(keyword.toUpperCase() + " --  " +  dmc.getSympName() + " -- " + dmc.getSympName().indexOf(keyword));
//                if(dmc.getSympName().indexOf(keyword) < 0){
//                    i.remove();
//                }else{
//                    similarity = CosineSimilarAlgorithm.cosSimilarityByString(keyword.toUpperCase(), dmc.getSympName());
//                }
//            }
//            else{
//                similarity = CosineSimilarAlgorithm.cosSimilarityByString(keyword.toUpperCase(), dmc.getSympName());
//            }
//
////            Double sympNameSimilarity = Similarity.sim(FigureUtil.valueOfString(dmc.getSympName()).toUpperCase(), keyword.toUpperCase());
////            Double popuNameSimilarity = Similarity.sim(FigureUtil.valueOfString(dmc.getPopuName()).toUpperCase(), keyword.toUpperCase());
////            Double symbolSimilarity = Similarity.sim(FigureUtil.valueOfString(dmc.getSymbol()).toUpperCase(), keyword.toUpperCase());
////            Double similarity = MedicineSortUtil.sortDouble(symbolSimilarity, popuNameSimilarity, sympNameSimilarity);
//            if (similarity < 0.5) {
//                i.remove();
//            } else {
//                dmc.setSimilarity(similarity);
//            }
//
//        }
        MedicineSortUtil.sortDiagnosisMainsympConcsymp(dmcs);
        for (DiagnosisMainsympConcsymp dmc : dmcs) {
            BasicAnswerVo answer = new BasicAnswerVo(dmc);
            answerVos.add(answer);
        }
        return answerVos;
    }

    @Override
    public DiagnosisMainsympConcsymp getMaxWeightConcSymp(String mainSympCode) {
        return diagnosisMainsympConcsympDao.getMaxWeightConcSymp(mainSympCode);
    }

    /**
     * 查询疾病下的伴随症状
     *
     * @param mainSympCode
     * @param concSympCodes
     * @return
     */
    @Override
    public Map<String, List<DiagnosisMainsympConcsymp>> mapDiagnosisMainsympConcsymp(String mainSympCode, Collection<String> concSympCodes) {
        Map<String, List<DiagnosisMainsympConcsymp>> dmcsMap = new HashMap<>();
        if (concSympCodes == null || concSympCodes.size() == 0)
            return dmcsMap;
        List<DiagnosisMainsympConcsymp> dmcs = diagnosisMainsympConcsympDao.listDiagnosisMainsympConcsymp(mainSympCode, concSympCodes);
        for (DiagnosisMainsympConcsymp dmc : dmcs) {
            List<DiagnosisMainsympConcsymp> dmcList = dmcsMap.get(dmc.getDiseaseCode()) == null ? new ArrayList<>() : dmcsMap.get(dmc.getDiseaseCode());
            dmcsMap.put(dmc.getDiseaseCode(), dmcList);
        }
        return dmcsMap;
    }
    
    /**
     * 查询疾病下的伴随症状
     *
     * @param mainSympCode
     * @param concSympCodes
     * @return
     */
    @Override
    public Map<String, List<MedicineQuestionVo>> mapDiagnosisMainsympConcsymp2(String mainSympCode, List<String> concSympCodes) {
        Map<String, List<MedicineQuestionVo>> dmcsMap = new HashMap<>();
        if (concSympCodes == null || concSympCodes.size() == 0)
            return dmcsMap;
        List<MedicineQuestionVo> dmcs = diagnosisMainsympConcsympDao.listDiagnosisMainsympConcsymp(mainSympCode, concSympCodes);
        for (MedicineQuestionVo dmc : dmcs) {
        	String diseaseCode = dmc.getDiseaseCode();
        	if(dmcsMap.containsKey(diseaseCode)) {
        		List<MedicineQuestionVo> list = dmcsMap.get(diseaseCode);
        		list.add(dmc);
        	} else {
        		List<MedicineQuestionVo> list = new ArrayList<>();
        		list.add(dmc);
        		dmcsMap.put(diseaseCode, list);
        	}
        }
        return dmcsMap;
    }
}
