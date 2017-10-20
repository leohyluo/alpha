package com.alpha.self.diagnosis.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.self.diagnosis.dao.BasicWeightInfoDao;
import com.alpha.self.diagnosis.pojo.BasicWeightInfo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.WeightAnswerVo;
import com.alpha.self.diagnosis.service.BasicWeightInfoService;
import com.alpha.user.service.UserInfoService;

@Service
public class BasicWeightInfoServiceImpl implements BasicWeightInfoService {

    @Resource
    private BasicWeightInfoDao dao;
    @Resource
    private UserInfoService userInfoService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<BasicWeightInfo> query(Map<String, Object> param) {
        return dao.query(param);
    }

    public List<IAnswerVo> queryAnswers(Date birth, Integer gender) {
        float age = DateUtils.getAge(birth);
        float month = DateUtils.getDiffMonth(birth);
        float weight = 0;
        float defaultWeight = 3.2f;    //默认3kg
        if (month < 24) {
            //≤6个月，体重=出生体重+月龄*0.7
            if (month <= 6) {
                weight = (float) (defaultWeight + month * 0.7);
            } else {
                //7-23月，体重=6+月龄*0.25
                weight = (float) (6 + month * 0.25);
            }
        } else {
            if (age >= 2 && age <= 6) {
                //2-6岁，体重=年龄（岁）*2+8；
                weight = age * 2 + 8;
            } else {
                //7-12岁，体重=年龄（岁）*3+2
                weight = age * 3 + 2;
            }
        }

        Map<String, Object> param = new HashMap<>();
        param.put("gender", gender);
        param.put("age", age);
        List<BasicWeightInfo> weightList = query(param);
        BasicWeightInfo weightInfo = null;
        String normalRange = "";
        if (CollectionUtils.isNotEmpty(weightList)) {
            weightInfo = weightList.get(0);
            normalRange = weightInfo.getNormalStart() + "-" + weightInfo.getNormalEnd();
        }


        List<IAnswerVo> answervoList = new ArrayList<>();
        WeightAnswerVo answervo = new WeightAnswerVo();
        answervo.setAnswerTitle(weight + "kg");
        answervo.setAnswerValue(weight + "");
        answervo.setNormal(normalRange);

        answervo.setDescription("");
        answervoList.add(answervo);
        return answervoList;
    }
}
