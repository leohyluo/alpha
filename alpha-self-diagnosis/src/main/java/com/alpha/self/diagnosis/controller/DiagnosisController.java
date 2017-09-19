package com.alpha.self.diagnosis.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.enums.DiseaseType;
import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.commons.web.WebUtils;
import com.alpha.self.diagnosis.pojo.vo.AnalysisRequestVo;
import com.alpha.self.diagnosis.pojo.vo.AnswerRequestVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.DiagnosisStartReqeustVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.self.diagnosis.pojo.vo.SearchRequestVo;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.DiagnosisService;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.self.diagnosis.service.SymptomMainService;
import com.alpha.server.rpc.user.pojo.UserInfo;
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
    private SymptomMainService symptomMainService;
    @Resource
    private UserInfoService userInfoService;

    /**
     * 开始问诊，生成问诊编号
     *
     * @return diagnosisId  唯一诊断编号
     */
    @PostMapping("/start")
    public ResponseMessage diagnosisStart(@RequestBody DiagnosisStartReqeustVo vo) {
    	Long userId = vo.getUserId();
    	Integer inType = vo.getInType();
        LOGGER.info("生成问诊编号,为导诊做准备: {} {}", userId, inType);
        BasicQuestionVo firstQuestion = diagnosisService.start(userId, inType);
        return WebUtils.buildSuccessResponseMessage(firstQuestion);
    }

    /**
     * 循环获取下一个问题
     * 接受答案信息
     * 如果没有问题编号，从第一个开始
     * @return diagnosisId  唯一诊断编号
     */
    @PostMapping("/basic/next")
    public ResponseMessage diagnosisNext(@RequestBody QuestionRequestVo questionVo) {
    	
        LOGGER.info("循环获取下一个问题: {}",  JSON.toJSONString(questionVo));
		if (questionVo == null || questionVo.getDiagnosisId() == null || StringUtils.isEmpty(questionVo.getUserId())) {
			return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
		}
        UserInfo userInfo = userInfoService.queryByUserId(Long.valueOf(questionVo.getUserId()));
        if (userInfo==null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        try {
            IQuestionVo result = null;
			if (questionVo.getType() == null || questionVo.getType() < 100) {
				result = basicQuestionService.next(questionVo);
			} else {
				result = medicineQuestionService.saveAnswerGetQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
			}
            LOGGER.info("循环获取下一个问题: 结果{}", JSON.toJSONString(result));
            return WebUtils.buildSuccessResponseMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
			return WebUtils.buildResponseMessage(ResponseStatus.EXCEPTION);
		}
    }

    @PostMapping("/pastmedicalHistory/search")
    public ResponseMessage pastmedicalHistorySearch(@RequestBody SearchRequestVo diseasevo) {
    	List<IAnswerVo> answerList = basicQuestionService.diseaseSearch(diseasevo, DiseaseType.PASTMEDICALHISTORY);
    	return WebUtils.buildSuccessResponseMessage(answerList);
    }
    
    @PostMapping("/allergicHistory/search")
    public ResponseMessage allergicHistorySearch(@RequestBody SearchRequestVo diseasevo) {
    	List<IAnswerVo> answerList = basicQuestionService.diseaseSearch(diseasevo, DiseaseType.ALLERGICHISTORY);
    	return WebUtils.buildSuccessResponseMessage(answerList);
    }

    @PostMapping("/test/question/{index}")
    public ResponseMessage medicineQuestion(Integer index) {
        try {
            QuestionRequestVo questionVo = new QuestionRequestVo();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            Long diagnosisId = 1000L;
            BasicQuestionVo question = new BasicQuestionVo();
            AnswerRequestVo bav = new AnswerRequestVo();
            List<AnswerRequestVo> bavs = new ArrayList<>();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(10003L);
            userInfo.setGender(1);
            userInfo.setBirth(formatDate.parse("1990-01-04"));
            switch (index) {
                case 1:
                    question = symptomMainService.getMainSymptomsQuestion(1000L,userInfo);
                    break;
                case 2:
                    questionVo.setDiagnosisId(diagnosisId);
                    questionVo.setDisplayType("radio");
                    questionVo.setQuestionTitle("【xxx】的基本情况我已经清楚了解，现在告诉我最不舒服的是什么，我将调动全身的每一个细胞进行运算！");
                    questionVo.setQuestionCode("9999");
                    questionVo.setType(6);
                    bav.setAnswerTitle("腹泻");
                    bav.setContent("2");
                    bavs.add(bav);
                    questionVo.setAnswers(bavs);


                    question = medicineQuestionService.saveAnswerGetQuestion(diagnosisId, questionVo, userInfo);
                    break;
                case 3:
                    questionVo.setDiagnosisId(diagnosisId);
                    questionVo.setDisplayType("radio");
                    questionVo.setQuestionTitle("大便性状！");
                    questionVo.setQuestionCode("2841");
                    questionVo.setType(7);

                    bav.setAnswerTitle("洗肉水样便*");
                    bav.setContent("2");
                    bavs.add(bav);
                    questionVo.setAnswers(bavs);

                    userInfo.setGender(1);
                    userInfo.setBirth(formatDate.parse("1990-01-04"));
                    question = medicineQuestionService.saveAnswerGetQuestion(diagnosisId, questionVo, userInfo);
                    break;
                case 4:
                    questionVo.setDiagnosisId(diagnosisId);
                    questionVo.setDisplayType("radio");
                    questionVo.setQuestionTitle("致病相关因素！");
                    questionVo.setQuestionCode("2846");
                    questionVo.setType(7);

                    bav.setAnswerTitle("过劳*");
                    bav.setContent("11975");
                    bavs.add(bav);
                    questionVo.setAnswers(bavs);

                    userInfo.setGender(1);
                    userInfo.setBirth(formatDate.parse("1990-01-04"));
                    question = medicineQuestionService.saveAnswerGetQuestion(diagnosisId, questionVo, userInfo);
                    break;
                default:
                    question = medicineQuestionService.saveAnswerGetQuestion(diagnosisId, questionVo, userInfo);
                    break;

            }
            return new ResponseMessage(question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseMessage(ResponseStatus.EXCEPTION);
    }
    @PostMapping("/test/question2")
    public ResponseMessage medicineQuestion(QuestionRequestVo questionVo, String userId) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            UserInfo userInfo = new UserInfo();
            userInfo.setGender(1);
            userInfo.setBirth(formatDate.parse("1990-01-04"));
            BasicQuestionVo question = medicineQuestionService.saveAnswerGetQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
            return new ResponseMessage(question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseMessage(ResponseStatus.EXCEPTION);
    }

    @PostMapping("/mainsymtoms/input")
    public ResponseMessage lexicalAnalysis(@RequestBody AnalysisRequestVo vo) {
//    	IQuestionVo questionVo = diagnosisService.lexicalAnalysis(vo);
//    	return WebUtils.buildSuccessResponseMessage(questionVo);
        return null;
    }

}
