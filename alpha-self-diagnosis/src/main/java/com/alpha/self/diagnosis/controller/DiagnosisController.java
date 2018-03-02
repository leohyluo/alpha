package com.alpha.self.diagnosis.controller;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.api.tencent.offical.dto.QRCodeDTO;
import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.commons.exception.ServiceException;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.commons.web.WebUtils;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionWithSearchVo;
import com.alpha.self.diagnosis.pojo.vo.BasicRequestVo;
import com.alpha.self.diagnosis.pojo.vo.DiagnosisResultVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.self.diagnosis.pojo.vo2.DrugListVo;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.self.diagnosis.service.DiagnosisService;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.self.diagnosis.service.OfficalAccountService;
import com.alpha.self.diagnosis.service.SymptomMainService;
import com.alpha.self.diagnosis.service.UserDiagnosisOutcomeService;
import com.alpha.self.diagnosis.service.WecharService;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.controller.req.vo.PatientInfo;
import com.alpha.user.service.UserBasicRecordService;
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
    @Resource
    private SymptomMainService symptomMainService;
    @Resource
    private WecharService wecharService;
    @Resource
    private UserBasicRecordService userBasicRecordService;
    @Resource
    private OfficalAccountService officalAccountService;

    /**
     * 开始问诊，生成问诊编号
     *
     * @return diagnosisId  唯一诊断编号
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST, consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseMessage diagnosisStart(Long userId, Integer inType) {
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
                result = medicineQuestionService.listMainSymptom(questionVo.getDiagnosisId(), questionVo, userInfo);
            } else {
                //result = medicineQuestionService.saveAnswerGetQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
            	result = medicineQuestionService.replyDiagnosisQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
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
    public ResponseMessage diagnosisMedicineNext(String allParam) {
    	QuestionRequestVo questionVo = JSON.parseObject(allParam,QuestionRequestVo.class);
        LOGGER.info("循环获取下一个问题: {}", JSON.toJSONString(questionVo));
        if (questionVo == null || questionVo.getDiagnosisId() == null || StringUtils.isEmpty(questionVo.getUserId())
        		|| StringUtils.isEmpty(questionVo.getSystemType())) {
            return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        UserInfo userInfo = userInfoService.queryByUserId(Long.valueOf(questionVo.getUserId()));
        if (userInfo == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        try {
            if (StringUtils.isEmpty(questionVo.getQuestionCode())) {
                BasicQuestionVo basicQuestionVo = basicQuestionService.
                		getMainSymptomsQuestion(questionVo.getSystemType(), questionVo.getDiagnosisId(), userInfo);
                return WebUtils.buildSuccessResponseMessage(basicQuestionVo);
            }
            IQuestionVo result;
            if (questionVo.getType() == QuestionEnum.主症状语义分析.getValue()) {
                result = medicineQuestionService.listMainSymptom(questionVo.getDiagnosisId(), questionVo, userInfo);
            } else {
                //result = medicineQuestionService.saveAnswerGetQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
            	result = medicineQuestionService.replyDiagnosisQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
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
     * 就诊经历-添加药品
     * @param
     * @param
     * @return
     */
    @PostMapping("/drug/query")
    public ResponseMessage diagnosisDrugQuery(String keyword) {
    	List<DrugListVo> drugList = wecharService.listByKeyword(keyword);
		return WebUtils.buildSuccessResponseMessage(drugList);
    }

    /**
     * 问诊结束后展示就诊信息
     *
     * @param basicRequestVo
     * @return
     */
    @PostMapping("/showResult")
    public ResponseMessage showDiagnosisResult(BasicRequestVo basicRequestVo) {
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
     * @return
     */
    @PostMapping("/diseaseHistory")
    public ResponseMessage diseaseHistory(Long userId, Long diagnosisId, String gender, String birth, Integer historyType) {
        if (userId == null || diagnosisId == null || historyType == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
        }
        UserInfo userInfo = userInfoService.queryByUserId(userId);
        if (userInfo == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        Integer inputGender = StringUtils.isEmpty(gender) ? 1 : Integer.parseInt(gender);
        Date birthday = DateUtils.dayDiffence(ChronoUnit.YEARS, -1);
        if(StringUtils.isNotEmpty(birth)) {
        	birthday = DateUtils.stringToDate(birth);
        }
        userInfo.setGender(inputGender);
        userInfo.setBirth(birthday);

        IQuestionVo questionVo = diagnosisPastmedicalHistoryService.queryDiseaseHistory(userInfo, diagnosisId, historyType);
        BasicQuestionWithSearchVo resultVo = (BasicQuestionWithSearchVo) questionVo;
        List<IAnswerVo> answerList = resultVo.getAnswers();
        return WebUtils.buildSuccessResponseMessage(answerList);
    }
    
    /**
     * 确认病历
     * @param diagnosisId
     * @return
     */
    @PostMapping("/result/confirm")
    public ResponseMessage confirmDiagnosisResult(String mobile, Long diagnosisId){
    	if(diagnosisId == null) {
    		return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
    	}
    	UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
    	if(record == null) {
    		return WebUtils.buildResponseMessage(ResponseStatus.INVALID_VALUE);
    	}
    	UserInfo userInfo = userInfoService.queryByUserId(record.getUserId());
    	if(userInfo == null) {
    		return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
    	}
    	
    	if(StringUtils.isNotEmpty(mobile)) {
    		record.setPhoneNum(mobile);
    		userInfo.setPhoneNumber(mobile);
    		userInfoService.save(userInfo);
    	}
    	record.setStatus(DiagnosisStatus.PRE_DIAGNOSIS_FINISH.getValue());
    	//生成二维码
    	QRCodeDTO codeDto = officalAccountService.getTempQRCode(userInfo, diagnosisId);
    	if(codeDto != null && codeDto.isSuccess()) {
    		record.setQrCode(codeDto.getMsg());
    	}
    	//标记此次预问诊已结束
    	userBasicRecordService.updateUserBasicRecord(record);
        return WebUtils.buildSuccessResponseMessage();
    }
    
    /**
     * 是否需要提问特殊时期、体重、出生史、接种史等问题
     * 
     * @param patientInfo
     * @return
     */
    @PostMapping("/queryBasicQuestion")
    public ResponseMessage queryBasicQuestion(PatientInfo patientInfo) {
    	String birth = patientInfo.getBirth();
    	int gender = patientInfo.getGender();
    	String systemType = patientInfo.getSystemType();
        LOGGER.info("birth="+birth+",gender="+gender+",systemType="+systemType);
    	if(StringUtils.isEmpty(birth)) {
    		return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
    	}
    	Map<String, String> result = basicQuestionService.askBasicQuestion(systemType, birth, gender);
    	return WebUtils.buildSuccessResponseMessage(result);
    }
}
