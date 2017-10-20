package com.alpha.user.service.impl;

import com.alpha.server.rpc.user.pojo.UserFeedback;
import com.alpha.user.dao.UserFeedbackDao;
import com.alpha.user.service.UserFeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Transactional
public class UserFeedBackServiceImpl implements UserFeedbackService {

    @Resource
    private UserFeedbackDao userFeedbackDao;

    /**
     * 保存用户反馈信息
     *
     * @param userFeedback
     */
    public void saveUserFeedback(UserFeedback userFeedback) {
        userFeedback.setCreateTime(new Date());
        userFeedbackDao.insert(userFeedback);
    }

}
