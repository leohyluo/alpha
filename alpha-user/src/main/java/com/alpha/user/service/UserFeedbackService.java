package com.alpha.user.service;

import com.alpha.server.rpc.user.pojo.UserFeedback;

/**
 * Created by xc.xiong on 2017/10/12.
 */
public interface UserFeedbackService {

    /**
     * 保存用户反馈信息
     *
     * @param userFeedback
     */
    void saveUserFeedback(UserFeedback userFeedback);
}
