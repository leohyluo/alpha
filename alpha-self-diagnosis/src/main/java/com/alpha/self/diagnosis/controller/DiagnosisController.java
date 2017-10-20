package com.alpha.self.diagnosis.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.exception.ServiceException;
import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.commons.web.WebUtils;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionWithSearchVo;
import com.alpha.self.diagnosis.pojo.vo.BasicRequestVo;
import com.alpha.self.diagnosis.pojo.vo.DiagnosisResultVo;
import com.alpha.self.diagnosis.pojo.vo.DiagnosisStartReqeustVo;
import com.alpha.self.diagnosis.pojo.vo.DiseaseHistoryRequestVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.self.diagnosis.service.DiagnosisService;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.self.diagnosis.service.UserDiagnosisOutcomeService;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.controller.req.vo.PatientInfo;
import com.alpha.user.service.UserInfoService;

/**
 * Created by xc.xiong on 2017/9/1.
 * 问诊流程
 */
@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosisController.class);

    @Resource
    private DiagnosisService diagnosisService;
    @Resource
    private BasicQuestionService basicQuestionService;
    @Resource
    private MedicineQuestionService medicineQuestionService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserDiagnosisOutcomeService userDiagnosisOutcomeService;
    @Resource
    private DiagnosisPastmedicalHistoryService diagnosisPastmedicalHistoryService;

    /**
     * 开始问诊，生成问诊编号
     *
     * @return diagnosisId  唯一诊断编号
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST, consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseMessage diagnosisStart(DiagnosisStartReqeustVo vo) {
        Long userId = vo.getUserId();
        Integer inType = vo.getInType();
        LOGGER.info("生成问诊编号,为导诊做准备: {} {}", userId, inType);
        if(userId == null) {
        	return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        BasicQuestionVo firstQuestion = diagnosisService.start(userId, inType);
        return WebUtils.buildSuccessResponseMessage(firstQuestion);
    }

    /**
     * 循环获取下一个问题
     * 接受答案信息
     * 如果没有问题编号，从第一个开始
     *
     * @return diagnosisId  唯一诊断编号
     */
    @PostMapping("/basic/next")
    public ResponseMessage diagnosisNext(QuestionRequestVo questionVo) {

        LOGGER.info("循环获取下一个问题: {}", JSON.toJSONString(questionVo));
        if (questionVo == null || questionVo.getDiagnosisId() == null || StringUtils.isEmpty(questionVo.getUserId())) {
            return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        UserInfo userInfo = userInfoService.queryByUserId(Long.valueOf(questionVo.getUserId()));
        if (userInfo == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        try {
            IQuestionVo result = null;
            if (questionVo.getType() == null || questionVo.getType() < 99) {
                result = basicQuestionService.next(questionVo);
            } else if (questionVo.getType() == QuestionEnum.主症状语义分析.getValue()) {
                result = medicineQuestionService.nextAnalysisByBaidu(questionVo.getDiagnosisId(), questionVo, userInfo);
            } else {
                result = medicineQuestionService.saveAnswerGetQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
            }
            return WebUtils.buildSuccessResponseMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtils.buildResponseMessage(ResponseStatus.EXCEPTION);
        }
    }

    /**
     * 循环获取下一个问题
     * 接受答案信息
     * 如果没有问题编号，从第一个开始
     *
     * @return diagnosisId  唯一诊断编号
     */
    @PostMapping("/medicine/next")
    public ResponseMessage diagnosisMedicineNext(QuestionRequestVo questionVo) {

        LOGGER.info("循环获取下一个问题: {}", JSON.toJSONString(questionVo));
        if (questionVo == null || questionVo.getDiagnosisId() == null || StringUtils.isEmpty(questionVo.getUserId())) {
            return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        UserInfo userInfo = userInfoService.queryByUserId(Long.valueOf(questionVo.getUserId()));
        if (userInfo == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        try {
            if (StringUtils.isEmpty(questionVo.getQuestionCode())) {
                BasicQuestionVo basicQuestionVo = basicQuestionService.getMainSymptomsQuestion(questionVo.getDiagnosisId(), userInfo);
                return WebUtils.buildSuccessResponseMessage(basicQuestionVo);
            }
            IQuestionVo result;
            if (questionVo.getType() == QuestionEnum.主症状语义分析.getValue()) {
                result = medicineQuestionService.nextAnalysisByBaidu(questionVo.getDiagnosisId(), questionVo, userInfo);
            } else {
                result = medicineQuestionService.saveAnswerGetQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
            }
            return WebUtils.buildSuccessResponseMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            return WebUtils.buildResponseMessage(ResponseStatus.EXCEPTION);
        }
    }

    /**
     * 获取诊断结果
     *
     * @param diagnosisId
     * @return
     */
    @PostMapping("/outcome/get")
    public ResponseMessage getOutcome(Long diagnosisId) {
        try {
            LOGGER.info("获取诊断结果: {}", diagnosisId);
            if (diagnosisId == null) {
                return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
            }
            List<UserDiagnosisOutcome> udos = userDiagnosisOutcomeService.listTop5UserDiagnosisOutcome(diagnosisId);
            if (udos != null && udos.size() > 0) {
                return new ResponseMessage(udos, ResponseStatus.SUCCESS);
            } else {
                return new ResponseMessage(ResponseStatus.NULL_DIAGNOSIS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WebUtils.buildResponseMessage(ResponseStatus.EXCEPTION);
    }

    /**
     * 确认诊断结果
     *
     * @param diagnosisId
     * @return
     */
    @PostMapping("/outcome/confirm")
    public ResponseMessage confirmOutcome(Long diagnosisId, String diseaseCode) {
        try {
            LOGGER.info("确认诊断结果: {}", diagnosisId, diseaseCode);
            if (diagnosisId == null || StringUtils.isEmpty(diseaseCode)) {
                return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
            }
            userDiagnosisOutcomeService.confirmODiagnosisOutcome(diagnosisId, diseaseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WebUtils.buildResponseMessage(ResponseStatus.SUCCESS);
    }

    /**
     * 问诊结束后展示就诊信息
     *
     * @param basicRequestVo
     * @return
     */
    @PostMapping("/showResult")
    public ResponseMessage showDiagnosisResult(@RequestBody BasicRequestVo basicRequestVo) {
        Long userId = basicRequestVo.getUserId();
        Long diagnosisId = basicRequestVo.getDiagnosisId();
        if (userId == null || diagnosisId == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        DiagnosisResultVo diagnosisResult = diagnosisService.showDiagnosisResult(userId, diagnosisId);
        return WebUtils.buildSuccessResponseMessage(diagnosisResult);
    }
    
    /**
     * 查看用户病历-对接页面
     * @return
     */
    @PostMapping("/queryMedicalRecord4His/{idcard}")
    public ResponseMessage queryMedicalRecord4His(@PathVariable String idcard) {
    	ResponseMessage result = null;
    	try {
    		DiagnosisResultVo diagnosisResult = diagnosisService.showDiagnosisResult(idcard);
    		result = WebUtils.buildSuccessResponseMessage(diagnosisResult);
		} catch (ServiceException e) {
			if(e.getResultEnum() == ResponseStatus.USER_NOT_FOUND) {
				result = WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
			} else if (e.getResultEnum() == ResponseStatus.BASIC_RECORD_NOTFOUND) {
				result = WebUtils.buildResponseMessage(ResponseStatus.BASIC_RECORD_NOTFOUND);
			}
		}
    	return result;
    }

    /**
     * 疾病史(既往史、过敏史)
     *
     * @return
     */
    @PostMapping("/diseaseHistory")
    public ResponseMessage diseaseHistory(@RequestBody DiseaseHistoryRequestVo vo) {
        Long userId = vo.getUserId();
        Long diagnosisId = vo.getDiagnosisId();
        Integer historyType = vo.getHistoryType();
        if (userId == null || diagnosisId == null || historyType == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        IQuestionVo questionVo = diagnosisPastmedicalHistoryService.queryDiseaseHistory(userId, diagnosisId, historyType);
        BasicQuestionWithSearchVo resultVo = (BasicQuestionWithSearchVo) questionVo;
        List<IAnswerVo> answerList = resultVo.getAnswers();
        return WebUtils.buildSuccessResponseMessage(answerList);
    }
    
    @PostMapping("/queryBasicQuestion")
    public ResponseMessage queryBasicQuestion(@RequestBody PatientInfo patientInfo) {
    	String birth = patientInfo.getBirth();
    	int gender = patientInfo.getGender();
    	if(StringUtils.isEmpty(birth)) {
    		return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
    	}
    	Map<String, String> result = basicQuestionService.queryByBirthOrGender(birth, gender);
    	return WebUtils.buildSuccessResponseMessage(result);
    }

}
