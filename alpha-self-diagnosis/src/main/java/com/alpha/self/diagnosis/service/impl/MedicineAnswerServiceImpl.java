package com.alpha.self.diagnosis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.exception.ServiceException;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.self.diagnosis.dao.DiagnosisQuestionAnswerDao;
import com.alpha.self.diagnosis.dao.UserDiagnosisDetailDao;
import com.alpha.self.diagnosis.pojo.vo.*;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.utils.ServiceUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.diagnosis.pojo.UserDiagnosisDetail;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    /**
     * 记录用户的答案
     *
     * @param questionVo
     */
    public void updateDiagnosisAnswer(QuestionRequestVo questionVo) {
        if (questionVo.getAnswers() == null || questionVo.getAnswers().size() == 0) {
            throw new ServiceException(ResponseStatus.INVALID_VALUE, "没有找到对应的答案");
        }
        List<String> answerCodes = new ArrayList<>();
        List<String> answerContents = new ArrayList<>();
        for (AnswerRequestVo answerVo : questionVo.getAnswers()) {
            answerCodes.add(answerVo.getContent());
//            answerContents.add(answerVo.getAnswerTitle());
        }
        UserDiagnosisDetail udd = userDiagnosisDetailDao.getUserDiagnosisDetail(questionVo.getDiagnosisId(), questionVo.getQuestionCode());
        if (udd == null) {
            throw new ServiceException(ResponseStatus.INVALID_VALUE, "没有找到提问记录");
        }
        Map<Integer, Set<String>> answerSpecMap = this.mapAnswerSpec(questionVo.getQuestionCode(), answerCodes);
        Set<String> forwardDiseaseCode = answerSpecMap.get(1) == null ? new HashSet<String>() : answerSpecMap.get(1);   //正向特异性
        Set<String> reverseDiseaseCode = answerSpecMap.get(-1) == null ? new HashSet<String>() : answerSpecMap.get(-1);   //反向特异性
        Set<String> nothingDiseaseCode = answerSpecMap.get(0) == null ? new HashSet<String>() : answerSpecMap.get(0); //无特异性
//        udd.setAnswerCode(ServiceUtil.arrayConvertToString(answerCodes));
        udd.setAnswerCode(JSON.toJSONString(answerCodes));
//        udd.setAnswerContent(ServiceUtil.arrayConvertToString(answerContents));
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
    public void saveDiagnosisAnswer(BasicQuestionVo questionVo) {
        List<String> answerCodes = new ArrayList<>();
        List<String> answerContents = new ArrayList<>();
        for (IAnswerVo answerVo : questionVo.getAnswers()) {
            BasicAnswerVo answer = (BasicAnswerVo) answerVo;
            answerCodes.add(answer.getAnswerValue());
            answerContents.add(answer.getAnswerTitle());
        }
        UserDiagnosisDetail udd = new UserDiagnosisDetail();
        udd.setDiagnosisId(questionVo.getDiagnosisId());
        udd.setMemberId(0L);
        udd.setUserId(0L);
        udd.setQuestionCode(questionVo.getQuestionCode());
        udd.setAnswerCode(ServiceUtil.arrayConvertToString(answerCodes));
        udd.setForwardDiseaseCode(JSON.toJSONString(new HashSet<>()));
        udd.setReverseDiseaseCode(JSON.toJSONString(new HashSet<>()));
        udd.setNothingDiseaseCode(JSON.toJSONString(new HashSet<>()));
        udd.setQuestionType(questionVo.getType());
        udd.setSympCode(questionVo.getSympCode());
        udd.setQuestionContent(questionVo.getQuestionTitle());
        userDiagnosisDetailDao.insert(udd);
    }

    /**
     * 获取所有的答案，并过来年龄，性别
     *
     * @param questionCode
     * @param userInfo
     * @return
     */
    public List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String questionCode, UserInfo userInfo) {
        //查询答案
        List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(questionCode);
        for (Iterator iterator = dqAnswers.iterator(); iterator.hasNext(); ) {
            DiagnosisQuestionAnswer answer = (DiagnosisQuestionAnswer) iterator.next();
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
        return dqAnswers;
    }

    /**
     * 根据答案查询所有的答案，计算特异性
     */
    public Map<Integer, Set<String>> mapAnswerSpec(String questionCode, Collection<String> answerCodes) {
        List<DiagnosisQuestionAnswer> dqAnswers = diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(questionCode, answerCodes);
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
                List<String> codes =(List) JSON.parseArray(forwardDiseaseCode);
                if (codes != null && codes.size() > 0)
                    codeSet.addAll(codes);
            }
        }
        return codeSet;
    }

    /**
     * 查询正向特异性的答案
     * 统计正向特异性最多的
     * @param dqAnswers
     * @param diagnosisId
     * @return
     */
    public LinkedHashSet<IAnswerVo> getSpecAnswer(List<DiagnosisQuestionAnswer> dqAnswers,Long diagnosisId){
        Set<String> specCodeSet = listSpecCode(diagnosisId);
        LinkedHashSet<IAnswerVo> answerVos = new LinkedHashSet<>();
        if(specCodeSet==null||specCodeSet.size()==0)
            return answerVos;
        for(DiagnosisQuestionAnswer dqa:dqAnswers){
            if(specCodeSet.contains(dqa.getDiseaseCode())) {
                BasicAnswerVo answerVo = new BasicAnswerVo(dqa);
                answerVos.add(answerVo);
            }
        }
        return answerVos;
    }

    /**
     * 所有疾病下的所有答案
     * @return
     */
    public Map<String,List<DiagnosisQuestionAnswer>> mapAnswers(Collection<String> questionCodes, Collection<String> answerCodes){
        Map<String, List<DiagnosisQuestionAnswer>> questionMap = new HashMap<>();
        List<DiagnosisQuestionAnswer> dqAnswers=diagnosisQuestionAnswerDao.listDiagnosisQuestionAnswer(questionCodes, answerCodes);
        for (DiagnosisQuestionAnswer dmq : dqAnswers) {
            List<DiagnosisQuestionAnswer> questions = questionMap.get(dmq.getDiseaseCode()) == null ? new ArrayList<>() : questionMap.get(dmq.getDiseaseCode());
            questions.add(dmq);
            questionMap.put(dmq.getDiseaseCode(), questions);
        }
        return questionMap;
    }
}
