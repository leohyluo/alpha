package com.alpha.self.diagnosis.controller;

import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.enums.DiseaseType;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.StringUtils;
import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.commons.web.WebUtils;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.SearchRequestVo;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.BasicWeightInfoService;
import com.alpha.self.diagnosis.service.SymptomAccompanyService;
import com.alpha.self.diagnosis.service.SymptomMainService;
import com.alpha.self.diagnosis.utils.ServiceUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.pojo.vo.HisUserInfoVo;
import com.alpha.user.service.UserInfoService;

/**
 * Created by xc.xiong on 2017/9/5.
 * 数据查询
 */
@RestController
@RequestMapping("/data/search")
public class DataSearchController {

    @Resource
    private BasicQuestionService basicQuestionService;
    @Resource
    private SymptomAccompanyService symptomAccompanyService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private BasicWeightInfoService basicWeightInfoService;
    @Resource
    private SymptomMainService symptomMainService; 

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSearchController.class);


    /**
     * 既往史搜索
     *
     * @param diseasevo
     * @return
     */
    @PostMapping("/pastmedicalHistory")
    public ResponseMessage pastmedicalHistorySearch(SearchRequestVo diseasevo) {
        String keyword = ServiceUtil.stringFilter(diseasevo.getKeyword());
        if (StringUtils.isEmpty(keyword))
            return new ResponseMessage();
        diseasevo.setKeyword(keyword);
        List<IAnswerVo> answerList = basicQuestionService.diseaseSearch(diseasevo, DiseaseType.PASTMEDICALHISTORY);
        return WebUtils.buildSuccessResponseMessage(answerList);
    }

    /**
     * 过敏史搜索
     *
     * @param diseasevo
     * @return
     */
    @PostMapping("/allergicHistory")
    public ResponseMessage allergicHistorySearch(SearchRequestVo diseasevo) {
        String keyword = ServiceUtil.stringFilter(diseasevo.getKeyword());
        if (StringUtils.isEmpty(keyword))
            return new ResponseMessage();
        diseasevo.setKeyword(keyword);
        List<IAnswerVo> answerList = basicQuestionService.diseaseSearch(diseasevo, DiseaseType.ALLERGICHISTORY);
        return WebUtils.buildSuccessResponseMessage(answerList);
    }

    /**
     * 伴随症状搜索
     *
     * @param diseasevo
     * @param @ModelAttribute
     * @return
     */
    @PostMapping(value = "/concsymp")
    public ResponseMessage diagnosisStart(SearchRequestVo diseasevo) {
        LOGGER.info("伴随症状搜索: {} ", JSON.toJSON(diseasevo));
        if (diseasevo == null || diseasevo.getUserId() == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        String keyword = ServiceUtil.stringFilter(diseasevo.getKeyword());
        if (StringUtils.isEmpty(keyword)) {
            return new ResponseMessage();
        }
        diseasevo.setKeyword(keyword);
        UserInfo userInfo = userInfoService.queryByUserId(Long.valueOf(diseasevo.getUserId()));
        if (userInfo == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        LinkedHashSet<IAnswerVo> answerVos = symptomAccompanyService.listSymptomAccompany(diseasevo.getDiagnosisId(), diseasevo.getSympCode(), userInfo, diseasevo.getKeyword());
        return WebUtils.buildSuccessResponseMessage(answerVos);
    }

    @PostMapping("/weight")
    public ResponseMessage weightSearch(HisUserInfoVo vo) {
    	String birthStr = vo.getBirth();
    	Integer gender = vo.getGender();
    	if(StringUtils.isEmpty(birthStr) || gender == null) {
    		return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
    	}
    	Date birth = DateUtils.stringToDate(birthStr);
        List<IAnswerVo> answerList = basicWeightInfoService.queryAnswers(birth, gender);
        return WebUtils.buildSuccessResponseMessage(answerList);
    }
    
    /**
     * 搜索主症状
     * @param userId
     * @param inType
     * @param keyword
     * @return
     */
    @PostMapping("/mainSymptom")
    public ResponseMessage mainSymptomSearch(Long userId, Integer inType, String keyword) {
    	UserInfo userInfo = userInfoService.queryByUserId(userId);
        if (userInfo == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", keyword);
        List<DiagnosisMainSymptoms> mainList = symptomMainService.query(param);
        mainList = mainList.stream().filter(e -> e.mainSymptomPredicate(userInfo, inType)).collect(toList());
		List<BasicAnswerVo> mainvoList = mainList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
		return WebUtils.buildSuccessResponseMessage(mainvoList);		
    }
}
