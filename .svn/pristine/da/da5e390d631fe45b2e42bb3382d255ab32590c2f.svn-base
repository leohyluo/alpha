package com.alpha.self.diagnosis.controller;

import com.alpha.commons.web.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xc.xiong on 2017/9/5.
 * 数据查询
 */
@RestController
@RequestMapping("/date")
public class DateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateController.class);

    /**
     * 过敏史搜素
     * @param userId
     * @param inType
     * @param searchKey
     * @return
     */
    @RequestMapping("/search/allergic")
    public ResponseMessage diagnosisStart(Long userId, Integer inType,String searchKey) {
        LOGGER.info("搜索过敏史: {} {}", userId, searchKey);


        return new ResponseMessage();
    }
}
