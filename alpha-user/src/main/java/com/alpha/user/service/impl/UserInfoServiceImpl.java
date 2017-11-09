package com.alpha.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.alpha.commons.core.service.SysSequenceService;
import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.RandomUtils;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.dao.DiagnosisMedicalTemplateDao;
import com.alpha.user.dao.UserInfoDao;
import com.alpha.user.pojo.DiagnosisMedicalTemplate;
import com.alpha.user.pojo.vo.DiseaseHistoryVo;
import com.alpha.user.pojo.vo.HisUserInfoVo;
import com.alpha.user.pojo.vo.OtherHospitalInfo;
import com.alpha.user.pojo.vo.SaveUserInfoVo;
import com.alpha.user.service.MedicalRecordService;
import com.alpha.user.service.UserBasicRecordService;
import com.alpha.user.service.UserInfoService;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private SysSequenceService sysSequenceService;

    @Resource
    private UserBasicRecordService userBasicRecordService;

    @Resource
    private MedicalRecordService medicalRecordService;

    @Resource
    private DiagnosisMedicalTemplateDao diagnosisMedicalTemplateDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserInfo create(UserInfo userInfo) {
        Long userId = sysSequenceService.getNextSequence("user_seq").longValue();
        userInfo.setUserId(userId);
        userInfo.setExternalUserId(String.valueOf(userId));
        userInfo.setCreateTime(new Date());
        userInfo.setLastUpdateTime(new Date());
        userInfoDao.insert(userInfo);
        return userInfo;
    }

    /**
     * 根据第三方用户编号，渠道编号获取用户信息
     * 如果没有用户信息，则创建一个新的用户
     *
     * @param userInfo
     * @param inType
     * @return
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserInfo updateUserInfo(UserInfo userInfo, int inType) {
//		UserInfo user = userInfoMapper.getUserInfoByExternalUserId(userInfo.getExternalUserId(), inType);
        UserInfo user = userInfoDao.getUserInfoByExternalUserId(userInfo.getExternalUserId(), inType);

        if (user == null || user.getUserId() == 0) {
            userInfo.setUserId(sysSequenceService.getNextSequence("user_seq").longValue());
            userInfo.setInType(inType);
            userInfo.setCreateTime(new Date());
            userInfo.setLastUpdateTime(new Date());
            userInfoDao.insert(userInfo);
            return userInfo;
        }
        user.setLastUpdateTime(new Date());
        if (StringUtils.isNotEmpty(userInfo.getUserName()))
            user.setUserName(userInfo.getUserName());
        if (userInfo.getBirth() != null)
            user.setBirth(userInfo.getBirth());

        if (userInfo.getGender() != null)
            user.setGender(userInfo.getGender());

        if (StringUtils.isNotEmpty(userInfo.getIdcard()))
            user.setIdcard(userInfo.getIdcard());

        if (StringUtils.isNotEmpty(userInfo.getPhoneNumber()))
            user.setPhoneNumber(userInfo.getPhoneNumber());

        if (StringUtils.isNotEmpty(userInfo.getLiverFuncText()))
            user.setLiverFuncText(userInfo.getLiverFuncText());

        if (userInfo.getLiverFunc() != null)
            user.setLiverFunc(userInfo.getLiverFunc());

        if (StringUtils.isNotEmpty(userInfo.getRenalFuncText()))
            user.setRenalFuncText(userInfo.getRenalFuncText());

        if (userInfo.getRenalFunc() != null)
            user.setRenalFunc(userInfo.getRenalFunc());

        if (StringUtils.isNotEmpty(userInfo.getWeight()))
            user.setWeight(userInfo.getWeight());

        if (StringUtils.isNotEmpty(userInfo.getHeight()))
            user.setHeight(userInfo.getHeight());

        if (StringUtils.isNotEmpty(userInfo.getSpecialPeriod()))
            user.setSpecialPeriod(userInfo.getSpecialPeriod());

        if (StringUtils.isNotEmpty(userInfo.getFertilityType()))
            user.setFertilityType(userInfo.getFertilityType());

        if (StringUtils.isNotEmpty(userInfo.getGestationalAge()))
            user.setGestationalAge(userInfo.getGestationalAge());

        if (StringUtils.isNotEmpty(userInfo.getFeedType()))
            user.setFeedType(userInfo.getFeedType());

        //if (StringUtils.isNotEmpty(userInfo.getMenstrualPeriod()))
        	user.setMenstrualPeriod(userInfo.getMenstrualPeriod());
        
        //if (StringUtils.isNotEmpty(userInfo.getPastmedicalHistoryCode()))
            user.setPastmedicalHistoryCode(userInfo.getPastmedicalHistoryCode());

        //if (StringUtils.isNotEmpty(userInfo.getPastmedicalHistoryText()))
            user.setPastmedicalHistoryText(userInfo.getPastmedicalHistoryText());

        if (StringUtils.isNotEmpty(userInfo.getAllergicHistoryCode()))
            user.setAllergicHistoryCode(userInfo.getAllergicHistoryCode());

        if (StringUtils.isNotEmpty(userInfo.getAllergicHistoryText()))
            user.setAllergicHistoryText(userInfo.getAllergicHistoryText());
        
        if (StringUtils.isNotEmpty(userInfo.getOperationCode()))
            user.setOperationCode(userInfo.getOperationCode());
        
        if (StringUtils.isNotEmpty(userInfo.getOperationText())) 
        	user.setOperationText(userInfo.getOperationText());
        userInfoDao.update(user);
        return user;
    }

    @Override
    public UserInfo queryByUserId(Long userId) {
        return userInfoDao.queryByUserId(userId);
    }

    @Override
    public UserInfo save(Long diagnosisId, SaveUserInfoVo userInfo, OtherHospitalInfo hospitalInfo, int inType) {
        //更新用户信息
        if (userInfo != null) {
            //既往史
            List<DiseaseHistoryVo> pastmedicalHistoryList = userInfo.getPastmedicalHistory();
            if (CollectionUtils.isNotEmpty(pastmedicalHistoryList)) {
                String codes = pastmedicalHistoryList.stream().map(DiseaseHistoryVo::getAnswerValue).collect(Collectors.joining(","));
                String names = pastmedicalHistoryList.stream().map(DiseaseHistoryVo::getAnswerTitle).collect(Collectors.joining(","));
                Boolean containOperation = Stream.of(codes.split(",")).anyMatch("1"::equals);
                if (containOperation == true) {
                	String operationCode = "1";
                	String operationText = "手术史";
                	codes = Stream.of(codes.split(",")).filter(e->!operationCode.equals(e)).collect(Collectors.joining(","));
                	names = Stream.of(names.split(",")).filter(e->!operationText.equals(e)).collect(Collectors.joining(","));
                	userInfo.setOperationCode(operationCode);
                	userInfo.setOperationText("是");
                }
                
                userInfo.setPastmedicalHistoryCode(codes);
                userInfo.setPastmedicalHistoryText(names);
            }
            //过敏史
            List<DiseaseHistoryVo> allergicHistoryList = userInfo.getAllergicHistory();
            if (CollectionUtils.isNotEmpty(allergicHistoryList)) {
                String codes = allergicHistoryList.stream().map(DiseaseHistoryVo::getAnswerValue).collect(Collectors.joining(","));
                String names = allergicHistoryList.stream().map(DiseaseHistoryVo::getAnswerTitle).collect(Collectors.joining(","));
                userInfo.setAllergicHistoryCode(codes);
                userInfo.setAllergicHistoryText(names);
            }
            UserInfo ui = new UserInfo();
            BeanCopierUtil.copy(userInfo, ui);
            updateUserInfo(ui, inType);
            //重新加载用户信息并将用户信息拷贝至UserBasicRecord
            ui = queryByUserId(userInfo.getUserId());
            updateUserBasicRecord(diagnosisId, ui);
        }
        if (hospitalInfo != null) {
            //将医院就诊信息拷贝至UserBasicRecord
            updateUserBasicRecord(diagnosisId, hospitalInfo);
        }
        return null;
    }

    @Override
    public UserInfo queryByUserIdAndUserName(Long userId, String userName) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("userName", userName);
        List<UserInfo> list = userInfoDao.query(param);
        UserInfo userInfo = null;
        if (CollectionUtils.isNotEmpty(list)) {
            userInfo = list.get(0);
        }
        return userInfo;
    }

    @Override
    public HisUserInfoVo queryUserInfoFromHis(String cardNo, Long userId) {
    	UserInfo userInfo = null;
    	if (userId != null) {
    		userInfo = queryByUserId(userId);
    	} else if(StringUtils.isNotEmpty(cardNo)) {
    		//调用第三方接口获取用户信息
    		String userStr = getThirdUser(cardNo);
    		logger.info("third user is {}", userStr);
    		userInfo = JSONObject.parseObject(userStr, UserInfo.class);
    		//根据身份证查询用户是否存在
    		Map<String, Object> param = new HashMap<>();
    		param.put("idcard", cardNo);
    		List<UserInfo> list = userInfoDao.query(param);
    		//保存第三方用户信息
    		if(CollectionUtils.isNotEmpty(list)) {
    			UserInfo alphaUserInfo = list.get(0);
    			mergeUserInfo(alphaUserInfo, userInfo);
    			userInfo = updateUserInfo(alphaUserInfo, userInfo.getInType());
    		} else {
    			userInfo = create(userInfo);
    		}
    	} 
    	//目前没有第三方接口,所以只查本地UserInfo，以后需将第三方的用户信息保存至userInfo表
        HisUserInfoVo hisUserInfo = new HisUserInfoVo(userInfo);
        //拼装既往史
        String pastmedicalHistoryCode = userInfo.getPastmedicalHistoryCode();
        if (StringUtils.isNotEmpty(pastmedicalHistoryCode)) {
            List<DiseaseHistoryVo> pastmedicalHistory = new ArrayList<>();
            String names = userInfo.getPastmedicalHistoryText();
            List<String> codeList = Stream.of(pastmedicalHistoryCode.split(",")).collect(Collectors.toList());
            List<String> nameList = Stream.of(names.split(",")).collect(Collectors.toList());
            if (codeList.size() == nameList.size()) {
                for (int i = 0; i < codeList.size(); i++) {
                    String code = codeList.get(i);
                    String name = nameList.get(i);
                    pastmedicalHistory.add(new DiseaseHistoryVo(code, name));
                }
            }
            hisUserInfo.setPastmedicalHistory(pastmedicalHistory);
        }
        //拼装过敏史
        String allergicHistoryCode = userInfo.getAllergicHistoryCode();
        if (StringUtils.isNotEmpty(allergicHistoryCode)) {
            List<DiseaseHistoryVo> allergicHistory = new ArrayList<>();
            String names = userInfo.getAllergicHistoryText();
            List<String> codeList = Stream.of(allergicHistoryCode.split(",")).collect(Collectors.toList());
            List<String> nameList = Stream.of(names.split(",")).collect(Collectors.toList());
            if (codeList.size() == nameList.size()) {
                for (int i = 0; i < codeList.size(); i++) {
                    String code = codeList.get(i);
                    String name = nameList.get(i);
                    allergicHistory.add(new DiseaseHistoryVo(code, name));
                }
            }
            hisUserInfo.setAllergicHistory(allergicHistory);
        }
        return hisUserInfo;
    }
    
    @Override
	public List<UserInfo> query(Map<String, Object> map) {
		return userInfoDao.query(map);
	}

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void updateUserBasicRecord(Long diagnosisId, UserInfo userInfo) {
        //将userInfo的数据同步至UserBasicRecord
        UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
        if (record == null) {
            logger.warn("can not found UserBasicRecord by diagnosisId {}", diagnosisId);
            return;
        }
        record.copyFromUserInfo(userInfo);
        userBasicRecordService.updateUserBasicRecord(record);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void updateUserBasicRecord(Long diagnosisId, OtherHospitalInfo hospitalInfo) {
        //将其它医院的就诊信息数据同步至UserBasicRecord
        UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
        if (record == null) {
            logger.warn("can not found UserBasicRecord by diagnosisId {}", diagnosisId);
            return;
        }

        BeanCopierUtil.copy(hospitalInfo, record);
        //填充模板
        String templateId = userBasicRecordService.findTemplateId(diagnosisId);
        if (StringUtils.isNotEmpty(templateId)) {
            DiagnosisMedicalTemplate diagnosisMedicalTemplate = diagnosisMedicalTemplateDao.getDiagnosisMedicalTemplate(templateId);
            String presentIllnessHistoryHospital = medicalRecordService.getPresentIllnessHistoryHospital(diagnosisMedicalTemplate, record);
            record.setPresentIllnessHistoryHospital(presentIllnessHistoryHospital);
        }
        userBasicRecordService.updateUserBasicRecord(record);
    }
    
    /**
     * 测试代码，模似HIS返回用户信息
     * @param idcard
     * @return
     */
    private String getThirdUser(String idcard) {
    	String userName = RandomUtils.randomUserName();
    	String birth = DateUtils.date2String(RandomUtils.randomDate("2000-01-01", "2017-09-10"), DateUtils.DATE_FORMAT);
    	Integer gender = RandomUtils.getRandomNum(1, 2);
    	String department = "小儿消化内科";
    	String cureTime = DateUtils.date2String(new Date(), DateUtils.DATE_TIME_FORMAT);
    	JSONObject json = new JSONObject();
    	json.put("userName", userName);
    	json.put("birth", birth);
    	json.put("gender", gender);
    	json.put("department", department);
    	json.put("cureTime", cureTime);
    	json.put("idcard", idcard);
    	json.put("inType", "1");
    	return json.toJSONString();
    }
    
    private void mergeUserInfo(UserInfo alphaUser, UserInfo hisUser) {
    	if(StringUtils.isNotEmpty(hisUser.getUserName())) {
    		alphaUser.setUserName(hisUser.getUserName());
    	} 
    	if (hisUser.getBirth() != null) {
    		alphaUser.setBirth(hisUser.getBirth());
    	}
    	if(hisUser.getGender() != null && hisUser.getGender() != 0) {
    		alphaUser.setGender(hisUser.getGender());
    	}
    	if (hisUser.getDepartment() != null) {
    		alphaUser.setDepartment(hisUser.getDepartment());
    	}
    	if (hisUser.getCureTime() != null) {
    		alphaUser.setCureTime(hisUser.getCureTime());
    	}
    }
}
