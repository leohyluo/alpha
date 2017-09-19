package com.alpha.self.diagnosis.service.impl;

import com.alpha.commons.util.DateUtils;
import com.alpha.self.diagnosis.dao.DiagnosisMainsympConcsympDao;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.service.SymptomAccompanyService;
import com.alpha.self.diagnosis.utils.MedicineSortUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympConcsymp;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xc.xiong on 2017/9/11.
 */
@Service
public class SymptomAccompanyServiceImpl implements SymptomAccompanyService {


    private static final Logger LOGGER = LoggerFactory.getLogger(SymptomAccompanyServiceImpl.class);
    @Autowired
    DiagnosisMainsympConcsympDao diagnosisMainsympConcsympDao;
    @Autowired
    MedicineAnswerService medicineAnswerService;


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
            if (answer.getGender() != null && answer.getGender() != userInfo.getGender()) {
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
        questionVo.setType(QuestionEnum.伴随症状.getValue());
        questionVo.setQuestionCode("1000");
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setSympCode(mainSympCode);
        LinkedHashSet<IAnswerVo> answerVos = new LinkedHashSet<>();//非正向特异性疾病下的伴随症状
        LinkedHashSet<IAnswerVo> answerSpecVos = new LinkedHashSet<>();//正向特异性疾病下的伴随症状
        for (DiagnosisMainsympConcsymp dmc : dmcs) {
            BasicAnswerVo answer = new BasicAnswerVo();
            answer.setAnswerValue(dmc.getConcSympCode());
            answer.setAnswerValue(dmc.getConcSympCode());
            answer.setAnswerTitle(dmc.getSympName());
            answer.setDefaultOrder(dmc.getDefaultOrder());
            if(specCodeSet.contains(dmc.getDiseaseCode())){
                answerSpecVos.add(answer);
            }else {
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
    public  LinkedHashSet<IAnswerVo>  getSymptomAccompanyAnswer(Long diagnosisId, String mainSympCode, UserInfo userInfo) {
        List<DiagnosisMainsympConcsymp> dmcs = this.listDiagnosisMainsympConcsymp(mainSympCode, userInfo);
        Set<String> specCodeSet = medicineAnswerService.listSpecCode(diagnosisId);//正向特异性疾病
        LinkedHashSet<IAnswerVo> answerVos = new LinkedHashSet<>();//非正向特异性疾病下的伴随症状
        LinkedHashSet<IAnswerVo> answerSpecVos = new LinkedHashSet<>();//正向特异性疾病下的伴随症状
        for (DiagnosisMainsympConcsymp dmc : dmcs) {
            BasicAnswerVo answer = new BasicAnswerVo();
            answer.setAnswerValue(dmc.getConcSympCode());
            answer.setAnswerValue(dmc.getConcSympCode());
            answer.setAnswerTitle(dmc.getSympName());
            answer.setDefaultOrder(dmc.getDefaultOrder());
            if(specCodeSet.contains(dmc.getDiseaseCode())){
                answerSpecVos.add(answer);
            }else {
                answerVos.add(answer);
            }
        }
        List<IAnswerVo> answers = MedicineSortUtil.sortAnswerVo(answerSpecVos);
        answers.addAll(MedicineSortUtil.sortAnswerVo(answerVos));
        return new LinkedHashSet<>(answers);
    }

    /**
     * 查询疾病下的伴随症状
     *
     * @param mainSympCode
     * @param concSympCodes
     * @return
     */
    public Map<String, List<DiagnosisMainsympConcsymp>> mapDiagnosisMainsympConcsymp(String mainSympCode, Collection<String> concSympCodes){
        Map<String, List<DiagnosisMainsympConcsymp>> dmcsMap = new HashMap<>();
        if(concSympCodes==null||concSympCodes.size()==0)
            return dmcsMap;
        List<DiagnosisMainsympConcsymp> dmcs = diagnosisMainsympConcsympDao.listDiagnosisMainsympConcsymp(mainSympCode, concSympCodes);
        for(DiagnosisMainsympConcsymp dmc:dmcs){
            List<DiagnosisMainsympConcsymp> dmcList = dmcsMap.get(dmc.getDiseaseCode()) == null ? new ArrayList<>() : dmcsMap.get(dmc.getDiseaseCode());
            dmcsMap.put(dmc.getDiseaseCode(), dmcList);
        }
        return dmcsMap;
    }
}