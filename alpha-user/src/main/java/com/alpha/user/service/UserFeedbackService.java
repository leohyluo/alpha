package com.alpha.user.service;

import java.util.List;
import java.util.Map;

import com.alpha.commons.enums.System;
import com.alpha.server.rpc.sys.SysConfig;
import com.alpha.server.rpc.user.pojo.UserFeedback;
import com.alpha.user.pojo.UserFeedBackItem;

/**
 * Created by xc.xiong on 2017/10/12.
 */
public interface UserFeedbackService {

    /**
     * 保存用户反馈信息
     *
     * @param userFeedback
     */
    Map<String, String> saveUserFeedback(UserFeedback userFeedback);
    
    /**
	 * 获取用户点赞信息
	 * @return
	 */
	SysConfig queryUserClickup();
    
    /**
	 * 更新用户点赞次数
	 * @return
	 */
	String updateUserClickup();
	
	/**
	 * 查询反馈选项
	 * @return
	 */
	List<UserFeedBackItem> listUserFeedBackItem(System system);
	
	/**
	 * 查询用户反馈
	 * @param diagnosisId
	 * @return
	 */
	List<UserFeedback> listUserFeedback(Long diagnosisId);
}
