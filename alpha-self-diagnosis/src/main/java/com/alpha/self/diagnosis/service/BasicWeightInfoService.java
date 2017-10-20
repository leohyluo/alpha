package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.BasicWeightInfo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BasicWeightInfoService {

    List<BasicWeightInfo> query(Map<String, Object> param);

    /**
     * 查询用户体重
     *
     * @param userId
     * @return
     */
    public List<IAnswerVo> queryAnswers(Date birth, Integer gender);
}
