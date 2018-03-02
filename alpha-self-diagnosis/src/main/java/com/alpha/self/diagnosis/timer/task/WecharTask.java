package com.alpha.self.diagnosis.timer.task;

import com.alpha.self.diagnosis.service.OfficalAccountService;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.UserBasicRecordDao;
import com.alpha.user.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 微信公众号定时任务
 * @author Administrator
 *
 */
@Component
public class WecharTask {
	
	@Resource
	private UserBasicRecordDao userBasicRecordDao;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private OfficalAccountService officalAccountService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 推送排队信息
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	public void pushQueueInfo() {
		logger.info("定时推送排队信息");
		List<UserBasicRecord> recordList = userBasicRecordDao.listWecharUserUnFinishOnToday();
		for(UserBasicRecord record : recordList) {
			UserInfo userInfo = userInfoService.queryByUserId(record.getUserId());
			officalAccountService.sendQueueInfo(userInfo, record);
			
//			record.setStatus(DiagnosisStatus.HIS_CONFIRMED.getValue());
//			userBasicRecordDao.update(record);
		}
	}
}
