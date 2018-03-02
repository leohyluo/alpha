package com.alpha.self.diagnosis.wechar.callback;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alpha.commons.api.tencent.offical.callback.CallBackHandler;
import com.alpha.commons.api.tencent.offical.dto.FollowDTO;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.self.diagnosis.service.WecharService;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.service.UserInfoService;
import com.alpha.user.service.UserMemberService;

@Service("unFollowCallBack")
public class UnFollowCallBack implements CallBackHandler<FollowDTO> {
	
	@Resource
	private WecharService wecharService;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private UserMemberService userMemberService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
//	@Transactional
	public String handle(FollowDTO t) {
		String openId = t.getFromUserName();
		//UserInfo userInfo = userInfoService.queryByExternalUserId(openId, InType.WECHAR.getValue());
		List<UserInfo> userList = userInfoService.listByExternalUserId(openId);
		UserInfo wxUser = userInfoService.getSelfUserInfoByExternalUserId(openId);
		if(CollectionUtils.isEmpty(userList)) {
			logger.info("微信用户{}不存在", openId);
			return null;
		}
		for(UserInfo userInfo : userList) {
			userInfo.setExternalUserId("");
			userInfo.setLastUpdateTime(new Date());
			userInfo.setWecharFollow(0);
			userInfoService.save(userInfo);
			logger.info("用户{}取消关注", openId);
		}
		//删除用户与成员的绑定关系
		if(wxUser != null) {
			userMemberService.deleteByUserId(wxUser.getUserId());
			logger.info("解决用户{}与成员的绑定关系", wxUser.getUserId());
		}
		return "success";
	}
}
