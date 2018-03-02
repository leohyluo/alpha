package com.alpha.self.diagnosis.wechar.callback;

import com.alpha.commons.api.tencent.offical.WecharConstant;
import com.alpha.commons.api.tencent.offical.callback.CallBackHandler;
import com.alpha.commons.api.tencent.offical.dto.FollowDTO;
import com.alpha.commons.util.StringUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.server.rpc.user.pojo.UserMember;
import com.alpha.user.service.UserInfoService;
import com.alpha.user.service.UserMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户已关注公众号，再次扫码触发
 * @author Administrator
 *
 */
@Service("scanCallBack")
public class ScanCallBack implements CallBackHandler<FollowDTO> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private UserMemberService userMemberService;

	@Override
	public String handle(FollowDTO t) {
		String eventKey = t.getEventKey();
		//场景参数
		if(StringUtils.isNotEmpty(eventKey)) {
			logger.info("用户已关注过公众号,现在再次扫预问诊二维码");
			String actionInfo = eventKey.replace(WecharConstant.prefix, "");
			String userId = actionInfo;
			String openId = t.getFromUserName();
			logger.info("ScanCallBack input param userId = {}, openId = {}", userId, openId);
			
			UserInfo wxUser = userInfoService.getSelfUserInfoByExternalUserId(openId);
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
					//建立用户与成员的关系
					if(wxUser != null) {
						UserMember member = new UserMember();
						member.setUserId(wxUser.getUserId());
						member.setMemberId(userInfo.getUserId());
						userMemberService.create(member);
						logger.info("建立用户{}与成员{}关系成功", wxUser.getUserId(), member.getUserId());
					}
				}
			}
		} else { 
			logger.warn("无效的二维码");
		}
		return null;
	}

}
