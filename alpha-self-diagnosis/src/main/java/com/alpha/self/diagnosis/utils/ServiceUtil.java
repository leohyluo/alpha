package com.alpha.self.diagnosis.utils;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.diagnosis.pojo.UserDiagnosisDetail;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xc.xiong on 2017/9/7.
 */
public class ServiceUtil {

    private static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");




    /**
     * array 转String 【、】隔开
     *
     * @param arrays
     * @return
     */
    public static String arrayConvertToString(List arrays) {
        if (arrays == null || arrays.size() == 0)
            return "";
        String str = JSON.toJSONString(arrays);
        return str.replace("[", "").replace("]", "")
                .replace(",", "、").replace("\"", "");
    }

    /**
     * 根据生日计算年龄
     *
     * @param brithday
     * @return
     */
    public static String createAge(String brithday) {
        int year = getYearByBirthdate(brithday);
        if (year > 0) {
            return year + "岁";
        }
        int month = getMonthByBirthdate(brithday);
        if (month > 0) {
            return month + "个月";
        }
        int day = getDayByBirthdate(brithday);
        if (day > 0) {
            return day + "天";
        }
        return "";
    }

    /**
     * 根据日期计算年龄
     *
     * @param brithday
     * @return
     * @throws
     * @throws Exception
     */
    public static int getYearByBirthdate(String brithday) {
        try {
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(new Date());
            Calendar brithDayCal = Calendar.getInstance();
            brithDayCal.setTime(formatDate.parse(brithday));
            return todayCal.get(Calendar.YEAR) - brithDayCal.get(Calendar.YEAR);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 根据日期计算月份
     *
     * @param brithday
     * @return
     * @throws
     * @throws Exception
     */
    public static int getMonthByBirthdate(String brithday) {
        try {
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(new Date());
            Calendar brithDayCal = Calendar.getInstance();
            brithDayCal.setTime(formatDate.parse(brithday));
            return todayCal.get(Calendar.MONTH) - brithDayCal.get(Calendar.MONTH);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 根据日期计算天数
     *
     * @param brithday
     * @return
     * @throws
     * @throws Exception
     */
    public static int getDayByBirthdate(String brithday) {
        try {
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(new Date());
            Calendar brithDayCal = Calendar.getInstance();
            brithDayCal.setTime(formatDate.parse(brithday));
            return todayCal.get(Calendar.DATE) - brithDayCal.get(Calendar.DATE);
        } catch (Exception e) {
            return 0;
        }
    }





}