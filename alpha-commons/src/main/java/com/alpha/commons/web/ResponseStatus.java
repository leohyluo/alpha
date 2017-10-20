package com.alpha.commons.web;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leohyluo 服务器响应请求状态码。
 */
public enum ResponseStatus {
    /**
     * 成功，服务器处理完成 20000, "success"
     */
//	SUCCESS(200, "操作成功"),
    SUCCESS(1000, "操作成功"),
    /**
     * 缺少必要参数 30001, "Required parameter missing"
     */
    REQUIRED_PARAMETER_MISSING(201, "缺少必要参数或参数为空"),
    /**
     * 用户不存在 30002, "User not found"
     */
    USER_NOT_FOUND(202, "用户不存在"),
    /**
     * 无效的签名
     */
    INVALID_SIGN(203, "签名验证不通过"),
    /**
     * 用户密码不匹配 30003, "Incorrect password"
     */
    USER_PASSWORD_INCORRECT(204, "密码错误"),
    /**
     * 缺少必要参数 30001, "Required parameter missing"
     */
    MOBILE_ALREADY_EXISTS(205, "手机号码已被注册"),

    NICKNAME_ALREADY_EXISTS(206, "昵称已存在"),

    UPLOAD_FAIL(207, "头像上传失败"),
    /**
     * 用户密码不匹配 30003, "Incorrect password"
     */
    OLD_PASSWORD_INCORRECT(208, "原密码不正确,请重新输入"),
    /**
     * token格式错误
     */
    ACCESS_TOKEN_INCORRECT(209, "token有误"),
    /**
     * token不存在
     */
    ACCESS_TOKEN_NOT_EXISTS(210, "您的账号已在其他设备登录"),

    /**
     * 无就诊记录
     */
    BASIC_RECORD_NOTFOUND(211, "无就诊记录"),
    /**
     * 座席信息不存在
     */
    PATIENT_INFO_NOTFOUND(212, "患者基本信息不存在"),
    /**
     * 座席信息不存在
     */
    EVENT_NO_NOTFOUND(213, "事件编号不存在"),
    /**
     * 服务器异常 44444, "Exception"
     */
    EXCEPTION(500, "服务器异常"),
    /**
     * 服务器无记录 20002, "Not logged or not connected"
     */
    NO_SESSION(20002, "用户未登录"),
    /**
     * 已存在 44403, "Existed"
     */
    EXISTED(44403, "Existed"),
    /**
     * 未找到数据 44404, "Not found"
     */
    NOT_FOUND(44404, "Not found"),

    /**
     * 服务器地址未找到 44445, "Address not found"
     */
    SERVER_NOT_FOUND(44445, "Address not found"),

    /**
     * 不支持的操作 44531, "Unsupport operation"
     */
    UNSUPPORT_OPERATION(44531, "Unsupport operation"),


    /**
     * 无权限操作 30004, "No permission"
     */
    NO_PERMISSION(30004, "No permission"),
    /**
     * 用户已登录 30005, "User Logged in"
     */
    USER_LOGINED(30005, "User Logged in"),

    /**
     * 不匹配的参数值 30006, "Invalid value"
     */
    INVALID_VALUE(30006, "Invalid value"),

    /**
     * 用户已存在 30022, "Existing user name"
     */
    USER_EXISTED(30022, "用户已存在"),

    NULL_DIAGNOSIS(20011, "没有找到对应的诊断结果");


    private final int code;
    private final String message;

    private ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static ResponseStatus findByCode(int code) {
        for (ResponseStatus rs : values()) {
            if (rs.code == code) {
                return rs;
            }
        }
        throw new IllegalArgumentException("Cannot create enum from " + code + " code!");
    }

    public static Map<Integer, String> all2Map() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (ResponseStatus t : ResponseStatus.values()) {
            map.put(t.code, t.message);
        }
        return map;
    }
}
