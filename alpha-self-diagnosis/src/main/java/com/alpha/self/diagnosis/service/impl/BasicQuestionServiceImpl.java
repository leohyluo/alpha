package com.alpha.self.diagnosis.service.impl;

import static java.util.stream.Collectors.toList;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.commons.enums.DiseaseType;
import com.alpha.commons.enums.DisplayType;
import com.alpha.commons.enums.System;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.StringUtils;
import com.alpha.self.diagnosis.dao.DiagnosisMainSymptomsDao;
import com.alpha.self.diagnosis.mapper.BasicQuestionMapper;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.AnswerRequestVo;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.self.diagnosis.processor.AbstractBasicAnswerProcessor;
import com.alpha.self.diagnosis.processor.BasicAnswerProcessorAdaptor;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.DiagnosisAllergicHistoryService;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.server.rpc.user.pojo.UserMember;
import com.alpha.user.service.UserBasicRecordService;
import com.alpha.user.service.UserInfoService;
import com.alpha.user.service.UserMemberService;
import com.alpha.user.utils.AppUtils;

@Service
@Transactional
public class BasicQuestionServiceImpl implements BasicQuestionService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private BasicQuestionMapper mapper;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserBasicRecordService userBasicRecordService;
    @Resource
    private UserMemberService userMemberService;
    @Resource
    private MedicineQuestionService medicineQuestionService;
    @Resource
    private DiagnosisPastmedicalHistoryService diagnosisPastmedicalHistoryService;
    @Resource
    private DiagnosisAllergicHistoryService diagnosisAllergicHistoryService;
    @Resource
    private MedicineAnswerService medicineAnswerService;
    @Resource
    private DiagnosisMainSymptomsDao diagnosisMainSymptomsDao;

    @Override
    public BasicQuestion find(BasicQuestion question) {
        return mapper.find(question);
    }

    @Override
    public BasicQuestion findByQuestionCode(String questionCode) {
        return mapper.findByQuestionCode(questionCode);
    }

    @Override
    public List<BasicQuestion> findNext(BasicQuestion question) {
        return mapper.findNext(question);
    }

    /**
     * 查找下一个基础问题,如果没有基础问题，调用医学问题接口获取医学问题
     *
     * @param
     * @param
     * @param questionParam
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public IQuestionVo next(QuestionRequestVo questionParam) throws Exception {
        Long userId = Long.valueOf(questionParam.getUserId());
        Integer inType = questionParam.getInType();
        String questionCode = questionParam.getQuestionCode();
        Long diagnosisId = questionParam.getDiagnosisId();
        //客户端传过来的答案
        List<AnswerRequestVo> answerList = questionParam.getAnswers();

        //查出当前问题
        BasicQuestion currentQuestion = this.findByQuestionCode(questionCode);
        if (currentQuestion == null) {
            logger.warn("invalid question code {}", questionCode);
            return null;
        }
        //查出用户基础信息
        AnswerRequestVo answervo = null;
        if (CollectionUtils.isNotEmpty(answerList)) {
            answervo = answerList.get(0);
        }
        UserInfo userInfo = getUserInfo(userId, inType, currentQuestion, answervo);
        //将用户回答的答案同步至用户信息
        currentQuestion.merge(userInfo, answerList);
        userInfoService.createOrUpdateUserInfo(userInfo, inType);
        BasicQuestionType questionType = BasicQuestionType.findByValue(questionCode);
        //如果当前问题是既往史、过敏史、体重则马上更新userBasicRecord
        if (questionType == BasicQuestionType.PAST_MEDICAL_HISTORY || questionType == BasicQuestionType.ALLERGIC_HISTORY
                || questionType == BasicQuestionType.WEIGHT) {
            updateUserBasicRecord(diagnosisId, questionType, userInfo);
        }
        //更新用户既往史、过敏史频次
        if (questionType == BasicQuestionType.PAST_MEDICAL_HISTORY || questionType == BasicQuestionType.ALLERGIC_HISTORY) {
            String code = questionType == BasicQuestionType.PAST_MEDICAL_HISTORY ? userInfo.getPastmedicalHistoryCode()
                    : userInfo.getAllergicHistoryCode();
            updateUserSelectCount(code, questionType);
        }

        //找出所有问题
        List<BasicQuestion> questionList = this.findNext(currentQuestion);
        //下一问题用户是否已回答过,是则跳过继续找下一个未回答的问题
        //问题是否满足显示条件(年龄、性别等条件),"否"则跳过问题继续找下一个满足显示条件的问题
        Optional<BasicQuestion> optional = questionList.stream().filter(e -> notReply(e, userInfo))
                .filter(e -> questionMatch(e, userInfo)).findFirst();
        //需返回客户端的问题
        BasicQuestion question = null;
        if (optional.isPresent()) {
            question = optional.get();
        } else {
            //将userInfo的数据同步至UserBasicRecord
            updateUserBasicRecord(diagnosisId, userInfo);
            //进入智能诊断逻辑
            //return medicineQuestionService.saveAnswerGetQuestion(diagnosisId, new QuestionRequestVo(), userInfo);
            return getMainSymptomsQuestion(null, diagnosisId, userInfo);
        }
        //根据问题找出对应的答案
        AbstractBasicAnswerProcessor answerProcessor = BasicAnswerProcessorAdaptor.getProcessor(question.getQuestionCode());
        IQuestionVo questionVo = answerProcessor.build(diagnosisId, question, userInfo);
        return questionVo;
    }

    private UserInfo getUserInfo(Long userId, int inType, BasicQuestion currentQuestion, AnswerRequestVo answer) {
        UserInfo userInfo = null;
        String questionCode = currentQuestion.getQuestionCode();
        //当选择他人时新建一个用户
        if ("1000".equals(questionCode) && "0".equals(answer.getContent())) {
            String memberName = answer.getAnswerTitle();
            List<UserMember> userMemberList = userMemberService.listByUserIdAndMemberName(userId, memberName);
            logger.info("found {} members by userId:{} and memberName:{}", userMemberList.size(), userId, memberName);

            UserMember userMember = null;
            if (CollectionUtils.isNotEmpty(userMemberList)) {
                userMember = userMemberList.get(0);
            }

            if (userMember == null) {
                logger.info("could not found userMember");
                userInfo = new UserInfo();
                userInfo.setInType(inType);
                userInfo.setUserName(memberName);
                userInfoService.create(userInfo);
                logger.info("create userInfo {}", memberName);

                userMember = new UserMember();
                userMember.setUserId(userId);
                userMember.setMemberId(userInfo.getUserId());
                userMember.setMemberName(userInfo.getUserName());
                userMemberService.create(userMember);
                logger.info("create userMember {}", userInfo.getUserName());
            } else {
                Long memberId = userMember.getMemberId();
                userInfo = userInfoService.queryByUserId(memberId);
                if (userInfo == null) {
                    logger.warn("not found userInfo by memberId {}", memberId);
                }
            }
        } else {
            userInfo = userInfoService.queryByUserId(userId);
        }
        return userInfo;
    }

    /**
     * 问题是否已回答
     *
     * @param question 问题
     * @param userInfo 用户信息
     * @return
     */
    private Boolean notReply(BasicQuestion question, UserInfo userInfo) {
        BasicQuestionType questionType = BasicQuestionType.findByValue(question.getQuestionCode());
        if (questionType == null) {
            return true;
        }
        //月经期、女性特殊时期、过敏史每次都问
        if (questionType == BasicQuestionType.MENSTRUAL_PERIOD || questionType == BasicQuestionType.SPECIAL_PERIOD
                || questionType == BasicQuestionType.ALLERGIC_HISTORY) {
            return true;
        }
        //如果是既往史、体重三个问题则判断一个月内是否已回答过
        if (questionType == BasicQuestionType.PAST_MEDICAL_HISTORY || questionType == BasicQuestionType.WEIGHT) {
            return monthCheck(question, userInfo);
        }
        //如果是其它问题则判断是否已回答过
        Map<BasicQuestionType, Object> map = userInfo.toMap();
        if (!map.containsKey(questionType)) {
            return true;
        }
        Object obj = map.get(questionType);
        if (obj == null) {
            return true;
        }
        return false;
    }

    /**
     * 问题与用户基础信息匹配
     *
     * @param question
     * @param userInfo
     * @return
     */
    private Boolean questionMatch(BasicQuestion question, UserInfo userInfo) {
        Boolean result = true;
        Integer gender = question.getGender();
        Integer minAge = question.getMinAge();
        Integer maxAge = question.getMaxAge();
        String rangeScope = question.getRangeScope();
        userInfo.getBirth();
        //肝肾功能检测
        if (question.getQuestionCode().equals(BasicQuestionType.LIVER_RENAL.getValue())) {
            result = LiverRenalCheck(userInfo);
            if (result == true)
                return true;
        }

        if (gender != null) {
            if (gender == userInfo.getGender()) {
                result = true;
            } else {
                return false;
            }
        }
        if (minAge != null && maxAge != null) {
            if (userInfo.getBirth() != null) {
                float age = DateUtils.getAge(userInfo.getBirth());
                if (StringUtils.isNotEmpty(rangeScope)) {
                    String[] rangeFlagArr = rangeScope.split(",");
                    String minRange = rangeFlagArr[0];
                    String maxRange = rangeFlagArr[1];
                    if ("(".equals(minRange) && "]".equals(maxRange)) {
                        if (age > minAge && age <= maxAge) {
                            result = true;
                        } else {
                            return false;
                        }
                    } else if ("(".equals(minRange) && ")".equals(maxRange)) {
                        if (age > minAge && age < maxAge) {
                            result = true;
                        } else {
                            return false;
                        }
                    } else if ("[".equals(minRange) && ")".equals(maxRange)) {
                        if (age >= minAge && age < maxAge) {
                            result = true;
                        } else {
                            return false;
                        }
                    } else if ("[".equals(minRange) && "]".equals(maxRange)) {
                        if (age >= minAge && age <= maxAge) {
                            result = true;
                        } else {
                            return false;
                        }
                    }
                } else {
                    if (age > minAge && age < maxAge) {
                        result = true;
                    } else {
                        return false;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 显示提问肝肾功能逻辑
     *
     * @param userInfo
     * @return
     */
    private Boolean LiverRenalCheck(UserInfo userInfo) {
        String pastmedicalHistoryCode = userInfo.getPastmedicalHistoryCode();
        String allergicHistoryCode = userInfo.getAllergicHistoryCode();
        //用户既往史为空或病史或过敏史选择否/不清楚时不提问肝肾功能问题
        if (StringUtils.isEmpty(pastmedicalHistoryCode) && StringUtils.isEmpty(allergicHistoryCode)) {
            return false;
        }
        Map<String, Object> param = new HashMap<>();
        //用户既往史是否与肝肾功能相关
        if (StringUtils.isNotEmpty(pastmedicalHistoryCode)) {
            List<String> userPastmedicalHistoryCode = Stream.of(pastmedicalHistoryCode.split(",")).collect(toList());
            param.put("userPastmedicalHistoryCode", userPastmedicalHistoryCode);
            param.put("liverAndRenal", "1");
            List<DiagnosisPastmedicalHistory> list = diagnosisPastmedicalHistoryService.queryPastmedicalHistory(param);
            if (CollectionUtils.isNotEmpty(list)) {
                return true;
            }
        }
        //用户过敏史是否与肝肾功能相关
        if (StringUtils.isNotEmpty(allergicHistoryCode)) {
            param = new HashMap<>();
            List<String> userAllergicHistoryCodeList = Stream.of(allergicHistoryCode.split(",")).collect(toList());
            param.put("userAllergicHistoryCode", userAllergicHistoryCodeList);
            param.put("liverAndRenal", "1");
            List<DiagnosisAllergicHistory> allergicHistoryList = diagnosisAllergicHistoryService.queryAllergicHistory(param);
            if (CollectionUtils.isNotEmpty(allergicHistoryList)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 一个月后重新问诊既往史、体重
     *
     * @param question
     * @param userInfo
     * @return
     */
    private Boolean monthCheck(BasicQuestion question, UserInfo userInfo) {
        String questionCode = question.getQuestionCode();
        BasicQuestionType questionType = BasicQuestionType.findByValue(questionCode);
        Map<String, Object> param = null;
        List<UserBasicRecord> list = new ArrayList<>();
        if (questionType == BasicQuestionType.PAST_MEDICAL_HISTORY) {
            param = new HashMap<>();
            param.put("memberId", userInfo.getUserId());
            param.put("pastmedicalHistoryCodeNotNull", "1");
            list = userBasicRecordService.findByLastMonth(param);
        } else if (questionType == BasicQuestionType.WEIGHT) {
            param = new HashMap<>();
            param.put("memberId", userInfo.getUserId());
            param.put("weightNotNull", "1");
            list = userBasicRecordService.findByLastMonth(param);
        } else {
            return true;
        }
        if (CollectionUtils.isNotEmpty(list)) {
            return false;
        }
        return true;
    }

    private void updateUserBasicRecord(Long diagnosisId, BasicQuestionType questionType, UserInfo userInfo) {
        UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
        if (questionType == BasicQuestionType.PAST_MEDICAL_HISTORY) {
            record.setPastmedicalHistoryCode(userInfo.getPastmedicalHistoryCode());
        } else if (questionType == BasicQuestionType.ALLERGIC_HISTORY) {
            record.setAllergicHistoryCode(userInfo.getAllergicHistoryCode());
        } else if (questionType == BasicQuestionType.WEIGHT) {
            record.setWeight(userInfo.getWeight());
        }
        if (record.getMemberId() == null) {
            record.setMemberId(userInfo.getUserId());
        }
        userBasicRecordService.updateUserBasicRecordWithTx(record);
    }

    private void updateUserSelectCount(String code, BasicQuestionType type) {
        if (StringUtils.isEmpty(code)) {
            return;
        }
        List<String> selectedCodeList = Stream.of(code.split(",")).collect(toList());
        Map<String, Object> param = new HashMap<>();
        if (type == BasicQuestionType.PAST_MEDICAL_HISTORY) {
            param.put("codeList", selectedCodeList);
            diagnosisPastmedicalHistoryService.updateUserSelectCount(param);
        } else if (type == BasicQuestionType.ALLERGIC_HISTORY) {
            param.put("codeList", selectedCodeList);
            diagnosisAllergicHistoryService.updateUserSelectCount(param);
        }
    }

    private void updateUserBasicRecord(Long diagnosisId, UserInfo userInfo) {
        //将userInfo的数据同步至UserBasicRecord
        UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
        if (record == null) {
            logger.warn("can not found UserBasicRecord by diagnosisId {}", diagnosisId);
        }
        record.copyFromUserInfo(userInfo);
        userBasicRecordService.updateUserBasicRecord(record);
    }

    @Override
    public List<IAnswerVo> diseaseSearch(Long userId, String keyword, DiseaseType type) {
        Map<String, Object> param = new HashMap<>();
        UserInfo userInfo = userInfoService.queryByUserId(userId);
        if (userInfo != null) {
            float age = DateUtils.getAge(userInfo.getBirth());
            param.put("gender", userInfo.getGender());
            param.put("age", age);
        }
        String diseaseName = keyword;
        diseaseName = diseaseName.contains("%") ? diseaseName.replace("%", "\\%") : diseaseName;

        param.put("diseaseName", diseaseName);

        List<IAnswerVo> answerList = new ArrayList<>();
        List<BasicAnswerVo> list1 = new ArrayList<>();
        List<BasicAnswerVo> list2 = new ArrayList<>();
        if (type == DiseaseType.PASTMEDICALHISTORY) {
            List<DiagnosisPastmedicalHistory> pastmedicalHistory = diagnosisPastmedicalHistoryService.queryPastmedicalHistory(param);
            //需求变更,所有既往史、过敏史都不再查小类
            //List<DiagnosisSubpastmedicalHistory> subPastmedicalHistory = diagnosisPastmedicalHistoryService.querySubPastmedicalHistory(param);
            list1 = pastmedicalHistory.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
            //list2 = subPastmedicalHistory.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
        } else if (type == DiseaseType.ALLERGICHISTORY) {
            List<DiagnosisAllergicHistory> allergicHistoryList = diagnosisAllergicHistoryService.queryAllergicHistory(param);
            //需求变更,所有既往史、过敏史都不再查小类
            //List<DiagnosisSuballergicHistory> subAllergicHistoryList = diagnosisAllergicHistoryService.querySubAllergicHistory(param);
            list1 = allergicHistoryList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
            //list2 = subAllergicHistoryList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
        }
        answerList.addAll(list1);
        answerList.addAll(list2);
        return answerList;
    }

    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    public BasicQuestionVo getMainSymptomsQuestion(String systemType, Long diagnosisId, UserInfo userInfo) {
        BasicQuestionVo questionVo = new BasicQuestionVo();
        List<IAnswerVo> basicAnswers = new ArrayList<>();
        
        questionVo.setQuestionCode("9990");
        questionVo.setDiagnosisId(diagnosisId);
        
        //预问诊不需要主诉提问，直接返回主诉列表
        if(systemType.equals(System.PRE.getValue())) {
        	questionVo.setType(QuestionEnum.主症状.getValue());
        	questionVo.setDisplayType(DisplayType.RADIO_MORE_INPUT_CONFIRM.getValue());
        	String questionTitle = "请问{userName}哪里最不舒服?";
        	questionTitle = AppUtils.setUserNameAtQuestionTitle(questionTitle, userInfo);
        	questionVo.setQuestionTitle(questionTitle);
        	questionVo.setTitle(questionTitle);
        	//查询符合条件的主症状
        	Map<String, Object> param = new HashMap<>();
        	List<DiagnosisMainSymptoms> mainList = diagnosisMainSymptomsDao.query(param);
    		mainList = mainList.stream().filter(e -> e.mainSymptomPredicate(userInfo, userInfo.getInType())).limit(6).collect(toList());
            basicAnswers = mainList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
            questionVo.setAnswers(basicAnswers);
        } else {//阿尔法医生需要主诉提问
        	questionVo.setType(QuestionEnum.主症状语义分析.getValue());
        	questionVo.setDisplayType("mainsymptom_input");
        	String questionTitle = "{userName}的基本情况我已经清楚了解，现在告诉我最不舒服的是什么";
        	//questionTitle = userBasicRecordService.getUserName(diagnosisId, userInfo) + "的基本情况我已经清楚了解，现在告诉我最不舒服的是什么";
        	questionTitle = AppUtils.setUserNameAtQuestionTitle(questionTitle, userInfo);
        	questionVo.setQuestionTitle(userBasicRecordService.getUserName(diagnosisId, userInfo) + "的基本情况我已经清楚了解，现在告诉我最不舒服的是什么");
        	questionVo.setTitle(userBasicRecordService.getUserName(diagnosisId, userInfo) + "的基本情况我已经清楚了解，现在告诉我最不舒服的是什么");
        	questionVo.setAnswers(basicAnswers);
        }
        questionVo.setUserId(userInfo.getUserId()+"");
        questionVo.setSympCode("");
        // 保存正向反向特异性疾病
        medicineAnswerService.saveDiagnosisAnswer(questionVo, userInfo);
        return questionVo;
    }

	@Override
	public Map<String, String> askBasicQuestion(String systemType, String birth, int gender) {
		Map<String, String> result = new HashMap<>();
		String specialPeriodCode = "0";		//女性特殊时期
		String weightCode = "0";			//体重
		String bornHistoryCode = "0";		//出生史
		String vaccinationHistory = "0";	//预防接种史
    	try {
            System system = System.findByValue(systemType);
    		Date date = DateUtils.string2Date(birth);
    		float age = DateUtils.getAge(date);
    		if(gender == 1) {
    			if(age >= 11 && age < 18) {
    				specialPeriodCode = BasicQuestionType.MENSTRUAL_PERIOD.getValue();
    			} else if (age >=18 && age <= 52) {
    				specialPeriodCode = BasicQuestionType.SPECIAL_PERIOD.getValue();
    			}
    		}
    		if(age >=0 && age <= 12) {
    			weightCode = BasicQuestionType.WEIGHT.getValue();
    		}
    		if(age >= 0 && age <= 1) {
    			bornHistoryCode = BasicQuestionType.FERTILITY_TYPE.getValue();
    		}
    		if(system != null && system == System.PRE) {
                if(age >= 0 && age <= 18) {
                    vaccinationHistory = BasicQuestionType.VACCINATION_HISTORY.getValue();
                }
                result.put("askVaccination", vaccinationHistory);
            }

            result.put("askSpecialPeriod", specialPeriodCode);
            result.put("askWeight", weightCode);
            result.put("askBorn", bornHistoryCode);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return result;
	}
}
