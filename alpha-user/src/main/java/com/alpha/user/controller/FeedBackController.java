package com.alpha.user.controller;

import com.alpha.commons.web.ResponseMessage;
import com.alpha.server.rpc.user.pojo.UserFeedback;
import com.alpha.user.service.UserFeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by xc.xiong on 2017/10/12.
 */
@RestController
@RequestMapping("/feedback")
public class FeedBackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedBackController.class);

    @Resource
    private UserFeedbackService userFeedbackService;

    /**
     * 接收用户反馈信息
     *
     * @param userFeedback
     * @return
     */
    @PostMapping("/put")
    public ResponseMessage authorization(UserFeedback userFeedback) {
        try {
            LOGGER.info("接收用户反馈信息>>{}", userFeedback);
            if (userFeedback != null && StringUtils.isNotEmpty(userFeedback.getContent()) && userFeedback.getUserId() != null) {
                userFeedbackService.saveUserFeedback(userFeedback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseMessage();
    }

}
