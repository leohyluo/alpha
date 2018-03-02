package com.alpha.user.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alpha.commons.core.service.SysSequenceService;
import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.commons.enums.InType;
import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.RandomUtils;
import com.alpha.server.rpc.user.pojo.HisRegisterInfo;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.server.rpc.user.pojo.UserMember;
import com.alpha.user.dao.DiagnosisMedicalTemplateDao;
import com.alpha.user.dao.UserInfoDao;
import com.alpha.user.pojo.DiagnosisMedicalTemplate;
import com.alpha.user.pojo.vo.DiseaseHistoryVo;
import com.alpha.user.pojo.vo.DrugVo;
import com.alpha.user.pojo.vo.HisUserInfoVo;
import com.alpha.user.pojo.vo.OtherHospitalInfo;
import com.alpha.user.pojo.vo.SaveUserInfoVo;
import com.alpha.user.service.MedicalRecordService;
import com.alpha.user.service.UserBasicRecordService;
import com.alpha.user.service.UserInfoService;
import com.alpha.user.service.UserMemberService;

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
    @Resource
    private UserMemberService userMemberService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public UserInfo create(UserInfo userInfo) {
        Long userId = sysSequenceService.getNextSequence("user_seq").longValue();
        userInfo.setUserId(userId);
        //userInfo.setExternalUserId(String.valueOf(userId));
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
    public UserInfo createOrUpdateUserInfo(UserInfo userInfo, int inType) {
        //UserInfo user = userInfoDao.getUserInfoByExternalUserId(userInfo.getExternalUserId(), inType);
    	//UserInfo user = userInfoDao.getUserInfoByExternalUserId(userInfo.getExternalUserId());
    	List<UserInfo> userList = this.listByExternalUserId(userInfo.getExternalUserId());

        //if (user == null || user.getUserId() == 0) {
    	if (CollectionUtils.isEmpty(userList)) {
            userInfo.setUserId(sysSequenceService.getNextSequence("user_seq").longValue());
            userInfo.setInType(inType);
            userInfo.setCreateTime(new Date());
            userInfo.setLastUpdateTime(new Date());
            userInfoDao.insert(userInfo);
            return userInfo;
        }
        return updateUserInfo(userInfo);
    }
    
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public UserInfo updateUserInfo(UserInfo userInfo) {
		UserInfo user = userInfoDao.queryByUserId(userInfo.getUserId());
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
        
        if (StringUtils.isNotEmpty(userInfo.getVaccinationHistoryCode())) 
        	user.setVaccinationHistoryCode(userInfo.getVaccinationHistoryCode());
        
        userInfoDao.update(user);
        return user;
	}

    @Override
    public UserInfo queryByUserId(Long userId) {
        return userInfoDao.queryByUserId(userId);
    }
    
	/*@Override
	public UserInfo queryByExternalUserId(String externalUserId, int inType) {
		return userInfoDao.getUserInfoByExternalUserId(externalUserId, inType);
	}*/


	@Override
	public List<UserInfo> listByExternalUserId(String externalUserId) {
		return userInfoDao.listByExternalUserId(externalUserId);
	}

	@Override
	public UserInfo getSelfUserInfoByExternalUserId(String externalUserId) {
		List<UserInfo> userList = this.listByExternalUserId(externalUserId);
		if(CollectionUtils.isEmpty(userList)) {
			return null;
		}
		if(userList.size() == 1) {
			return userList.get(0);
		} else {
			for(UserInfo user : userList) {
				List<UserMember> memberList = userMemberService.listByUserId(user.getUserId());
				if(CollectionUtils.isNotEmpty(memberList)) {
					return user;
				}
			}
			logger.warn("根据externalUserId找到多个用户，但无法得知主用户");
			return null;
		}
		/*userList = userList.stream().filter(e->GlobalConstants.SELF.equals(e.getUserName())).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(userList)) {
			return userList.get(0);
		} else {
			return null;
		}*/
	}

    @Override
    public UserInfo save(Long diagnosisId, SaveUserInfoVo userInfo, OtherHospitalInfo hospitalInfo, int inType) {
        //更新用户信息
        if (userInfo != null) {
        	HisRegisterInfo hisRegisterInfo = null;
        	//医院挂号编码
        	String hisRegisterNo = userInfo.getHisRegisterNo();
        	if(StringUtils.isNotEmpty(hisRegisterNo)) {
        		//设置hospitalCode
        		UserInfo ui = queryByUserId(userInfo.getUserId());
                String departmentListStr = ui.getDepartmentList();
                if(StringUtils.isNotEmpty(departmentListStr)) {
                	List<HisRegisterInfo> hisDepartmentList = JSONArray.parseArray(departmentListStr, HisRegisterInfo.class);
                	Optional<HisRegisterInfo> optional = hisDepartmentList.stream().filter(e->e.getHisRegisterNo().equals(hisRegisterNo)).findFirst();
                	if(optional.isPresent()) {
                		hisRegisterInfo = optional.get();
                	}
                }
        	}
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
            //createOrUpdateUserInfo(ui, inType);
            updateUserInfo(ui);
            //重新加载用户信息并将用户信息拷贝至UserBasicRecord
            ui = queryByUserId(userInfo.getUserId());
            updateUserBasicRecord(diagnosisId, ui, hisRegisterInfo);
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
    	List<HisRegisterInfo> hisDepartmentList = new ArrayList<>();
    	if (userId != null) {
    		userInfo = queryByUserId(userId);
    		hisDepartmentList = JSONArray.parseArray(userInfo.getDepartmentList(), HisRegisterInfo.class);
			if(CollectionUtils.isNotEmpty(hisDepartmentList)) {
    			for(HisRegisterInfo dept : hisDepartmentList) {
    				//根据就诊编码查询是否已完成预问诊
    				String hisRegisterNo = dept.getHisRegisterNo();
    				if(StringUtils.isNotEmpty(hisRegisterNo)) {
    					UserBasicRecord record = userBasicRecordService.findFinishByUserId(userId, hisRegisterNo);
    					if(record != null) {
    						dept.setStatus(DiagnosisStatus.PRE_DIAGNOSIS_FINISH.getValue());
    						dept.setDiagnosisId(String.valueOf(record.getDiagnosisId()));
    					}
    				}
    			}
			}
    	} else if(StringUtils.isNotEmpty(cardNo)) {
    		//调用第三方接口获取用户信息
    		String userStr = getThirdUser(cardNo);
    		if(StringUtils.isEmpty(userStr)) {
    			return null;
    		}
    		logger.info("third user is {}", userStr);
    		userInfo = JSONObject.parseObject(userStr, UserInfo.class);
    		//挂号科室由jSONArray字符串转为List
    		hisDepartmentList = JSONArray.parseArray(userInfo.getDepartmentList(), HisRegisterInfo.class);
    		//根据身份证查询用户是否存在
    		Map<String, Object> param = new HashMap<>();
    		param.put("idcard", cardNo);
    		List<UserInfo> list = userInfoDao.query(param);
    		//保存第三方用户信息
    		if(CollectionUtils.isNotEmpty(list)) {
    			userInfo = list.get(0);
    			userId = userInfo.getUserId();
    			hisDepartmentList = JSONArray.parseArray(userInfo.getDepartmentList(), HisRegisterInfo.class);
    			if(CollectionUtils.isNotEmpty(hisDepartmentList)) {
	    			for(HisRegisterInfo dept : hisDepartmentList) {
	    				//根据就诊编码查询是否已完成预问诊
	    				String hisRegisterNo = dept.getHisRegisterNo();
	    				if(StringUtils.isNotEmpty(hisRegisterNo)) {
	    					UserBasicRecord record = userBasicRecordService.findFinishByUserId(userId, hisRegisterNo);
	    					if(record != null) {
	    						dept.setDiagnosisId(String.valueOf(record.getDiagnosisId()));
	    						dept.setStatus(DiagnosisStatus.PRE_DIAGNOSIS_FINISH.getValue());
	    					}
	    				}
	    			}
    			}
    			/*UserInfo alphaUserInfo = list.get(0);
    			mergeUserInfo(alphaUserInfo, userInfo);
    			userInfo = updateUserInfo(alphaUserInfo, userInfo.getInType());*/
    		} else {
    			userInfo = create(userInfo);
    		}
    	} 
    	//目前没有第三方接口,所以只查本地UserInfo，以后需将第三方的用户信息保存至userInfo表
        HisUserInfoVo hisUserInfo = new HisUserInfoVo(userInfo, hisDepartmentList);
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
        //将手术史拼到过既往史
        String operationCode = userInfo.getOperationCode();
        if (StringUtils.isNotEmpty(operationCode) && "1".equals(operationCode)) {
        	List<DiseaseHistoryVo> pastmedicalHistory = hisUserInfo.getPastmedicalHistory();
        	if(pastmedicalHistory == null) {
        		pastmedicalHistory = new ArrayList<>();
        		hisUserInfo.setPastmedicalHistory(pastmedicalHistory);
        	}
        	DiseaseHistoryVo operationHistory = new DiseaseHistoryVo(operationCode, "手术史");
        	pastmedicalHistory.add(operationHistory);
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
        //拼装就诊药品
        if(userId != null) {
        	UserBasicRecord basicRecord = userBasicRecordService.findLastCompleted(userId);
        	String userDrugListStr = basicRecord.getOtherHospitalDrugList();
        	if(StringUtils.isNotEmpty(userDrugListStr)) {
        		List<DrugVo> drugList = JSONArray.parseArray(userDrugListStr, DrugVo.class);
        		hisUserInfo.setOtherHospitalDrugList(drugList);
        	}
        }
        List<HisRegisterInfo> deptList = hisUserInfo.getHisDepartmentList();
        if(CollectionUtils.isNotEmpty(deptList)) {
        	deptList.stream().filter(e->StringUtils.isNotEmpty(e.getCureTime())).peek(e->{
        		String cureTimeStr = e.getCureTime();
				if(StringUtils.isNotEmpty(cureTimeStr)) {
					try {
						Date cureTime = DateUtils.string2Date(cureTimeStr);
						String cureDate = DateUtils.date2String(cureTime, DateUtils.DATE_FORMAT);
						e.setCureTime(cureDate);
					} catch (ParseException ex) {
						ex.printStackTrace();
					}
				}
        	}).collect(Collectors.toList());
        }
        return hisUserInfo;
    }
    
    @Override
	public List<UserInfo> query(Map<String, Object> map) {
		return userInfoDao.query(map);
	}

	@Override
	public List<HisUserInfoVo> list(Long userId) {
		List<HisUserInfoVo> userList = new ArrayList<>();
		
		HisUserInfoVo self = this.queryUserInfoFromHis(null, userId);
		self.setSelf("Y");
		userList.add(self);
		List<UserMember> userMemberList = userMemberService.listByUserId(userId);
		
		for(UserMember userMember : userMemberList) {
			HisUserInfoVo memberInfo = this.queryUserInfoFromHis(null, userMember.getMemberId());
			userList.add(memberInfo);
		}
		return userList;
	}

	@Override
	@Transactional
	public HisUserInfoVo saveUserMember(Long userId, String memberName) {
		UserInfo self = userInfoDao.queryByUserId(userId);
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(memberName);
		userInfo.setExternalUserId(self.getExternalUserId());
		userInfo.setInType(InType.ALPHA.getValue());
		userInfo = this.create(userInfo);
		Long memberId = userInfo.getUserId();
		
		UserMember userMember = new UserMember();
		userMember.setUserId(userId);
		userMember.setMemberId(memberId);
		userMember.setMemberName(memberName);
		userMemberService.create(userMember);
		
		HisUserInfoVo hisUserInfo = this.queryUserInfoFromHis(null, memberId);
		return hisUserInfo;
	}
	

	@Override
	public UserInfo getByPhoneNumber(String phoneNumber) {
		return userInfoDao.getByPhoneNumber(phoneNumber);
	}
	

	@Override
	public UserInfo follow(UserInfo userInfo, String wecharId) {
		userInfo.setExternalUserId(wecharId);
		userInfo.setInType(InType.WECHAR.getValue());
		userInfo.setLastUpdateTime(new Date());
		userInfoDao.update(userInfo);
		return userInfo;
	}

	@Override
	public List<UserInfo> listUserMemberInfo(Long userId) {
		return userInfoDao.listUserMemberInfo(userId);
	}
    
    /**
     * 测试代码，模似HIS返回用户信息
     * @param idcard
     * @return
     */
    private String getThirdUser(String idcard) {
    	if("123456".equals(idcard))
    		return null;
    	String userName = RandomUtils.randomUserName();
    	String birth = DateUtils.date2String(RandomUtils.randomDate("2000-01-01", "2017-09-10"), DateUtils.DATE_FORMAT);
    	Integer gender = RandomUtils.getRandomNum(1, 2);
    	String cureTime = DateUtils.date2String(new Date(), DateUtils.DATE_TIME_FORMAT);
    	JSONObject json = new JSONObject();
    	json.put("userName", userName);
//    	json.put("birth", birth);
//    	json.put("gender", gender);
    	json.put("idcard", idcard);
    	json.put("inType", "1");
    	
    	JSONArray jarr = new JSONArray();
    	JSONObject dept1 = new JSONObject();
    	dept1.put("hospitalCode", "A002");
    	dept1.put("hisRegisterNo", System.currentTimeMillis()+"");
    	dept1.put("department", "小儿消化内科");
    	dept1.put("doctorName", "李珊珊");
    	dept1.put("cureTime", cureTime);
    	
    	try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	JSONObject dept2 = new JSONObject();
    	dept2.put("hospitalCode", "A002");
    	dept2.put("department", "小儿皮肤科");
    	dept2.put("doctorName", "袁明珠");
    	dept2.put("hisRegisterNo", System.currentTimeMillis()+"");
    	dept2.put("cureTime", cureTime);
    	
    	jarr.add(dept1);
    	jarr.add(dept2);
    	
    	json.put("departmentList", jarr.toJSONString());
    	
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
    	/*if (hisUser.getDepartment() != null) {
    		alphaUser.setDepartment(hisUser.getDepartment());
    	}
    	if (hisUser.getCureTime() != null) {
    		alphaUser.setCureTime(hisUser.getCureTime());
    	}*/
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void updateUserBasicRecord(Long diagnosisId, UserInfo userInfo, HisRegisterInfo hisRegisterInfo) {
        //将userInfo的数据同步至UserBasicRecord
        UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
        if (record == null) {
            logger.warn("can not found UserBasicRecord by diagnosisId {}", diagnosisId);
            return;
        }
        record.copyFromUserInfo(userInfo);
        if(hisRegisterInfo != null) {
        	record.copyFromHisRegisterInfo(hisRegisterInfo);
        }
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
        List<DrugVo> useDrugList = hospitalInfo.getOtherHospitalUseDrugList();
        String otherHospitalDrugList = "[]";
        if(CollectionUtils.isNotEmpty(useDrugList)) {
        	otherHospitalDrugList = JSON.toJSONString(useDrugList);
        	record.setOtherHospitalDrugList(otherHospitalDrugList);
        }
        //填充模板
        String templateId = userBasicRecordService.findTemplateId(diagnosisId);
        if (StringUtils.isNotEmpty(templateId)) {
            DiagnosisMedicalTemplate diagnosisMedicalTemplate = diagnosisMedicalTemplateDao.getDiagnosisMedicalTemplate(templateId);
            String presentIllnessHistoryHospital = medicalRecordService.getPresentIllnessHistoryHospital(diagnosisMedicalTemplate, record);
            record.setPresentIllnessHistoryHospital(presentIllnessHistoryHospital);
        }
        userBasicRecordService.updateUserBasicRecord(record);
    }

	@Override
	public void save(UserInfo userInfo) {
		userInfoDao.update(userInfo);
	}

	@Override
	public List<UserInfo> listByUserId(List<Long> userIdList) {
		return userInfoDao.listByUserId(userIdList);
	}


}
