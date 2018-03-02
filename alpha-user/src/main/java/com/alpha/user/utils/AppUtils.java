package com.alpha.user.utils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alpha.commons.constants.GlobalConstants;
import com.alpha.commons.enums.Gender;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.pojo.vo.DrugVo;

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
    
    /**
     * 获取用户头像
     * @param userInfo
     * @return
     */
    public static String getUserIcon(UserInfo userInfo) {
    	String userIcon = "";
    	float userAge = DateUtils.getAge(userInfo.getBirth());
    	Integer gender = userInfo.getGender();
    	if(gender == null) {
    		return null;
    	}
		if(userAge <= 18 && gender == Gender.MALE.getValue()) {
			userIcon = "alpha/images/user/boy.png";
		} else if(userAge <= 18 && gender == Gender.FEMALE.getValue()) {
			userIcon = "alpha/images/user/girl.png";
		} else if(userAge > 18 && gender == Gender.MALE.getValue()) {
			userIcon = "alpha/images/user/man.png";
		} else if(userAge > 18 && gender == Gender.FEMALE.getValue()) {
			userIcon = "alpha/images/user/woman.png";
		}
		return userIcon;
    }
    
    public static String getDiseaseFromDiagnosisRecord(String drugArrStr) {
    	System.out.println("drugArrStr==============>"+drugArrStr);
    	if(StringUtils.isEmpty(drugArrStr)) {
    		return "";
    	}
    	List<DrugVo> drugList = JSONArray.parseArray(drugArrStr, DrugVo.class);
    	if(CollectionUtils.isEmpty(drugList)) {
    		return "";
    	}
    	String drugNames = drugList.stream().map(DrugVo::getDrugName).collect(Collectors.joining(","));
    	if(StringUtils.isEmpty(drugNames)) {
    		return "";
    	}
    	return drugNames;
    }
}
