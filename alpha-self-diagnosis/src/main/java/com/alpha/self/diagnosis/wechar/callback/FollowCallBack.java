package com.alpha.self.diagnosis.wechar.callback;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alpha.commons.api.tencent.offical.WecharConstant;
import com.alpha.commons.api.tencent.offical.callback.CallBackHandler;
import com.alpha.commons.api.tencent.offical.dto.FollowDTO;
import com.alpha.commons.enums.InType;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.StringUtils;
import com.alpha.self.diagnosis.service.OfficalAccountService;
import com.alpha.self.diagnosis.service.WecharService;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.UserBasicRecordDao;
import com.alpha.user.service.UserInfoService;
import com.alpha.user.service.UserMemberService;

@Service("followCallBack")
public class FollowCallBack implements CallBackHandler<FollowDTO> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private WecharService wecharService;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private UserMemberService userMemberService;
	@Resource
	private UserBasicRecordDao userBasicRecordDao;
	@Resource
	private OfficalAccountService officalAccountService;

	@Override
	public String handle(FollowDTO t) {
		String eventKey = t.getEventKey();
		//场景参数
		if(StringUtils.isNotEmpty(eventKey)) {
			logger.info("用户通过扫码关注预问诊公众号");
			String actionInfo = eventKey.replace(WecharConstant.prefix, "");
			String userId = actionInfo;
			String openId = t.getFromUserName();
			
			UserInfo userInfo = userInfoService.queryByUserId(Long.valueOf(userId));
			if(userInfo != null) {
				if(userInfo.getWecharFollow() != null && userInfo.getWecharFollow() == 1) {
					logger.info("二维码{}已被微信{}绑定,不能再被其它微信关注", userId, openId);
					return null;
				} else {
					userInfo.setExternalUserId(openId);
					userInfo.setWecharFollow(1);
					userInfo.setLastUpdateTime(new Date());
					userInfoService.save(userInfo);
					logger.info("建立用户{}与微信的绑定关系{}成功", userId, openId);
					
					/*List<UserBasicRecord> recordList = userBasicRecordDao.listWecharUserUnFinishOnTodayByUserId(Long.valueOf(userId));
					for(UserBasicRecord record : recordList) {
						UserInfo ui = userInfoService.queryByUserId(record.getUserId());
						officalAccountService.sendQueueInfo(ui, record);
						logger.info("开始推送排队信息{}给用户{}", record.getDiagnosisId(), record.getUserId());
					}*/
				}
			}
		} else { //不是通过扫码形式关注公众号的情况，生成一个新的用户
			logger.info("游客进入预问诊公众号点击关注");
			String openId = t.getFromUserName();
			List<UserInfo> userList = userInfoService.listByExternalUserId(openId);
			if(CollectionUtils.isNotEmpty(userList)) {
				logger.warn("用户已关注公众号");
				return null;
			}
			UserInfo userInfo = new UserInfo();
			//userInfo.setUserName(GlobalConstants.SELF);
			userInfo.setExternalUserId(openId);
			userInfo.setInType(InType.WECHAR.getValue());
			userInfo.setWecharFollow(1);
			userInfoService.create(userInfo);
			logger.info("微信用户{}已成功关注公众号", openId);
		}
		return null;
	}
}
