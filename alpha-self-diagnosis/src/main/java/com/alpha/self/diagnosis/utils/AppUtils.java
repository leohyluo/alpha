package com.alpha.self.diagnosis.utils;

import java.util.Date;

import com.alpha.commons.constants.GlobalConstants;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;

public class AppUtils {

	/**
     * 问题带上称谓,如{userName}发热多少天改为宝宝发热多少天
     * @param title
     * @param userInfo
     * @return
     */
    public static String setUserNameAtQuestionTitle(String title, UserInfo userInfo) {
    	if(title.contains(GlobalConstants.USER_NAME) || title.contains(GlobalConstants.USER_NAME1)) {
    		Date birth = userInfo.getBirth();
    		float age = DateUtils.getAge(birth);
    		if(age <= 14) {
    			title = title.replace(GlobalConstants.USER_NAME, GlobalConstants.USER_NAME_BABY);
    			title = title.replace(GlobalConstants.USER_NAME1, GlobalConstants.USER_NAME1_BABY);
    		} else {
    			title = title.replace(GlobalConstants.USER_NAME, GlobalConstants.USER_NAME_YOU);
    			title = title.replace(GlobalConstants.USER_NAME1, GlobalConstants.USER_NAME1_YOU);
    		}
    	}
    	return title;
    }
}
