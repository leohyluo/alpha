package com.alpha.self.diagnosis.controller;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.self.diagnosis.dao.DiagnosisMainSymptomsDao;
import com.alpha.self.diagnosis.dao.UserDiagnosisDetailDao;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.self.diagnosis.service.SymptomMainService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserDiagnosisDetail;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.UserBasicRecordDao;
import com.alpha.user.service.MedicalRecordService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private static Gson gson = new Gson();
    @Resource
    private MedicalRecordService medicalRecordService;
    @Resource
    private UserDiagnosisDetailDao userDiagnosisDetailDao;
    @Resource
    private UserBasicRecordDao userBasicRecordDao;
    @Resource
    private DiagnosisMainSymptomsDao diagnosisMainSymptomsDao;
    @Resource
    private SymptomMainService symptomMainService;
    @Resource
    private MedicineQuestionService medicineQuestionService;

    @GetMapping("/info")
    public String test() {
        System.out.println("invoke test");
        return " self-diagnosis 启动成功";
    }

    @PostMapping("/getMedicalRecord")
    public ResponseMessage getMedicalRecord() {
        Map<String, Object> param = new HashMap<>();
        param.put("sympCode", 54);
        DiagnosisMainSymptoms symptom = diagnosisMainSymptomsDao.query(param).get(0);
        UserBasicRecord record = userBasicRecordDao.findByDiagnosisId(3153L);
        List<UserDiagnosisDetail> udds = userDiagnosisDetailDao.listUserDiagnosisDetail(3148L);
        medicalRecordService.getMedicalRecord("1", udds, symptom, record);
        return new ResponseMessage();
    }

    @PostMapping("/test/question")
    public ResponseMessage medicineQuestion(Integer index) {
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            BasicQuestionVo question = new BasicQuestionVo();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(10003L);
            userInfo.setGender(1);
            userInfo.setUserName("老张");
            userInfo.setBirth(formatDate.parse("1990-01-04"));
            switch (index) {
                case 1:
                    question = symptomMainService.getMainSymptomsQuestion(1000L, userInfo,0);
                    break;
            }
            return new ResponseMessage(question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseMessage(ResponseStatus.EXCEPTION);
    }

//    @PostMapping("/test/question2")
    @RequestMapping(value = "/test/question2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseMessage medicineQuestion( String userId,String allParam) {
        try {
            QuestionRequestVo questionVo = gson.fromJson(allParam, QuestionRequestVo.class);
            LOGGER.info("test/question2 >> {} , {}", JSON.toJSONString(questionVo), userId);
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            UserInfo userInfo = new UserInfo();
            userInfo.setGender(1);
            userInfo.setBirth(formatDate.parse("2016-01-04"));
            IQuestionVo question = medicineQuestionService.saveAnswerGetQuestion(questionVo.getDiagnosisId(), questionVo, userInfo);
            return new ResponseMessage(question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseMessage(ResponseStatus.EXCEPTION);
    }
}
