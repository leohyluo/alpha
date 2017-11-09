package com.alpha.user.controller;

import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.commons.web.WebUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.pojo.vo.HisUserInfoVo;
import com.alpha.user.pojo.vo.OtherHospitalInfo;
import com.alpha.user.pojo.vo.SaveUserInfoVo;
import com.alpha.user.pojo.vo.UserInfoRequestVo;
import com.alpha.user.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserInfoService userInfoService;

    /**
     * 问诊前授权信息，目前没有做限制，只要有userinfo.externalUserId 都能授权成功
     *
     * @param userInfo
     * @param inType
     * @param isself   是否本人问诊
     * @return 返回问诊信息，用户编号，用户对象
     */
    @PostMapping("/auth")
    public ResponseMessage authorization(String userInfo, Integer inType, Integer isself) {

//		Map<String, String[]> map = request.getParameterMap();
//		LOGGER.info("为用户授权，获取用户信息，渠道编号: {} ", JSON.toJSONString(map));
        LOGGER.info("为用户授权，获取用户信息，渠道编号: {} ,{}", inType, userInfo);
        try {
            if (inType == null || inType == 0 || userInfo == null) {
                return new ResponseMessage(ResponseStatus.INVALID_VALUE.code(), "授权信息不完整,服务器已拒绝");
            }
            ObjectMapper mapper = new ObjectMapper();
            Gson gson = new Gson();
//			UserInfo user = mapper.readValue(userInfo, UserInfo.class);
            UserInfo user = gson.fromJson(userInfo, UserInfo.class);
            if (StringUtils.isEmpty(user.getExternalUserId())) {
                return new ResponseMessage(ResponseStatus.INVALID_VALUE.code(), "授权信息不完整,服务器已拒绝");
            }
            //获取用户信息
            //如果存在用户信息，返回用户信息
            //如果不存在，生成一个临时的用户，返回UserId
            user = userInfoService.updateUserInfo(user, inType);
//			LOGGER.info("用户信息,{},{}",inType, JSON.toJSON(user));
            return new ResponseMessage(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(ResponseStatus.EXCEPTION);
        }
    }

    /**
     * 调用第三方接口查询患者信息
     *
     * @param cardNo
     * @return
     */
    @PostMapping("/query")
    public ResponseMessage query(HisUserInfoVo vo) {
    	String idcard = vo.getIdcard();
    	Long userId = vo.getUserId();
    	LOGGER.info("idcard is {}, userId is {}", idcard, userId);
    	if(StringUtils.isEmpty(idcard) && userId == null) {
    		return WebUtils.buildResponseMessage(ResponseStatus.REQUIRED_PARAMETER_MISSING);
    	}
        HisUserInfoVo hisUserInfo = userInfoService.queryUserInfoFromHis(vo.getIdcard(), vo.getUserId());
        if (hisUserInfo == null) {
            return WebUtils.buildResponseMessage(ResponseStatus.USER_NOT_FOUND);
        }
        return WebUtils.buildSuccessResponseMessage(hisUserInfo);
    }

    /**
     * 保存用户信息
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/save")
    public ResponseMessage saveUserInfo(UserInfoRequestVo userVo) {
        SaveUserInfoVo userInfo = userVo.getUserInfo();
        OtherHospitalInfo hospitalInfo = userVo.getOtherHospitalInfo();
        userInfoService.save(userVo.getDiagnosisId(), userInfo, hospitalInfo, userVo.getInType());
        return WebUtils.buildSuccessResponseMessage();
    }
}
