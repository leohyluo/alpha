package com.alpha.self.diagnosis.service.impl;

import java.util.ArrayList;
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
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.StringUtils;
import com.alpha.self.diagnosis.mapper.BasicQuestionMapper;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.AnswerRequestVo;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.self.diagnosis.pojo.vo.SearchRequestVo;
import com.alpha.self.diagnosis.processor.AbstractBasicAnswerProcessor;
import com.alpha.self.diagnosis.processor.BasicAnswerProcessorAdaptor;
import com.alpha.self.diagnosis.service.BasicQuestionService;
import com.alpha.self.diagnosis.service.DiagnosisAllergicHistoryService;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.self.diagnosis.service.MedicineQuestionService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.server.rpc.user.pojo.UserMember;
import com.alpha.user.service.UserBasicRecordService;
import com.alpha.user.service.UserInfoService;
import com.alpha.user.service.UserMemberService;

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
    private DiagnosisPastmedicalHistoryService queryPastmedicalHistory;
    @Resource
    private MedicineQuestionService medicineQuestionService;
    @Resource
	private DiagnosisPastmedicalHistoryService diagnosisPastmedicalHistoryService;
    @Resource
	private DiagnosisAllergicHistoryService diagnosisAllergicHistoryService;

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
     * @param userId
     * @param inType
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
        if(currentQuestion == null) {
            logger.warn("invalid question code {}", questionCode);
            return null;
        }
        //查出用户基础信息
        AnswerRequestVo answervo = null;
        if(CollectionUtils.isNotEmpty(answerList)) {
        	answervo = answerList.get(0);
        }
        UserInfo userInfo = getUserInfo(userId, inType, currentQuestion, answervo);
        //将用户回答的答案同步至用户信息
        currentQuestion.merge(userInfo, answerList);
        userInfoService.updateUserInfo(userInfo, inType);
        //找出所有问题
        List<BasicQuestion> questionList = this.findNext(currentQuestion);
        //下一问题用户是否已回答过,是则跳过继续找下一个未回答的问题
        //问题是否满足显示条件(年龄、性别等条件),"否"则跳过问题继续找下一个满足显示条件的问题
        Optional<BasicQuestion> optional = questionList.stream().filter(e->notReply(e, userInfo))
                .filter(e->questionMatch(e, userInfo)).findFirst();
        //需返回客户端的问题
        BasicQuestion question = null;
        if(optional.isPresent()) {
            question = optional.get();
        } else {
            //将userInfo的数据同步至UserBasicRecord
            UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
            if(record == null) {
                logger.warn("can not found UserBasicRecord by diagnosisId {}", diagnosisId);
                record = new UserBasicRecord();
                record.setUserId(userId);
            }
            record.copyFromUserInfo(userInfo);
            userBasicRecordService.updateUserBasicRecord(record);
            //进入智能诊断逻辑
            return medicineQuestionService.saveAnswerGetQuestion(diagnosisId, new QuestionRequestVo(), userInfo);
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
        if("1000".equals(questionCode) && "0".equals(answer.getContent())) {
            String memberName = answer.getAnswerTitle();
            Map<String, Object> userMemberParam = new HashMap<>();
            userMemberParam.put("uesrId", userId);
            userMemberParam.put("memberName", memberName);
            List<UserMember> userMemberList = userMemberService.find(userMemberParam);
            logger.info("found {} members by userId:{} and memberName:{}", userMemberList.size(), userId, memberName);

            UserMember userMember = null;
            if(CollectionUtils.isNotEmpty(userMemberList)) {
                userMember = userMemberList.get(0);
            }

            if(userMember == null) {
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
                if(userInfo == null) {
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
     * @param question	问题
     * @param userInfo	用户信息
     * @return
     */
    private Boolean notReply(BasicQuestion question, UserInfo userInfo) {
        BasicQuestionType questionType = BasicQuestionType.findByValue(question.getQuestionCode());
        if(questionType == null) {
            return true;
        }
        Map<BasicQuestionType, Object> map = userInfo.toMap();
        if(!map.containsKey(questionType)) {
            return true;
        }
        Object obj = map.get(questionType);
        if(obj == null) {
            return true;
        }
        return false;
    }

    /**
     * 问题与用户基础信息匹配
     * @param question
     * @param userInfo
     * @return
     */
    private Boolean questionMatch(BasicQuestion question, UserInfo userInfo) {
        Boolean result = true;
        Integer gender = question.getGender();
        Integer minAge = question.getMinAge();
        Integer maxAge = question.getMaxAge();
        userInfo.getBirth();

        if(gender != null) {
            if(gender == userInfo.getGender()) {
                result = true;
            } else {
                return false;
            }
        }
        if(minAge != null && maxAge != null) {
            if(userInfo.getBirth() != null) {
                float age = DateUtils.getAge(userInfo.getBirth());
                if(age > minAge && age < maxAge) {
                    result = true;
                } else {
                    return false;
                }
            }
        }
        //肝肾功能检测
        if(result == true && question.getQuestionCode().equals(BasicQuestionType.LIVER_RENAL.getValue())) {
            result = LiverRenalCheck(userInfo);
        }
        return result;
    }

    /**
     * 显示提问肝肾功能逻辑
     * @param userInfo
     * @return
     */
    private Boolean LiverRenalCheck(UserInfo userInfo) {
        String pastmedicalHistoryCode = userInfo.getPastmedicalHistoryCode();
        String allergicHistoryCode = userInfo.getAllergicHistoryCode();
        //用户既往史为空或病史和过敏史选择否/不清楚时不提问肝肾功能问题
        if(StringUtils.isEmpty(pastmedicalHistoryCode) || ("0".equals(pastmedicalHistoryCode) && "0".equals(allergicHistoryCode))) {
            return false;
        }

        //用户既往史与肝肾功能相关则提问肝肾功能问题
        Map<String, Object> param = new HashMap<>();
        List<String> userPastmedicalHistoryCode = Stream.of(pastmedicalHistoryCode.split(",")).collect(Collectors.toList());
        param.put("userPastmedicalHistoryCode", userPastmedicalHistoryCode);
        param.put("liverAndRenal", "1");
        List<DiagnosisPastmedicalHistory> list = queryPastmedicalHistory.queryPastmedicalHistory(param);
        if(CollectionUtils.isNotEmpty(list)) {
            return true;
        }
        List<DiagnosisSubpastmedicalHistory> subList = queryPastmedicalHistory.querySubPastmedicalHistory(param);
        if(CollectionUtils.isNotEmpty(subList)) {
            return true;
        }
        return false;
    }

	@Override
	public List<IAnswerVo> diseaseSearch(SearchRequestVo diseasevo, DiseaseType type) {
		Map<String, Object> param = new HashMap<>();
		String userId = diseasevo.getUserId();
		UserInfo userInfo = userInfoService.queryByUserId(Long.valueOf(userId));
		if(userInfo != null) {
			float age = DateUtils.getAge(userInfo.getBirth());
			param.put("gender", userInfo.getGender());
			param.put("age", age);
		}
		param.put("diseaseName", diseasevo.getKeyword());
		
		List<IAnswerVo> answerList = new ArrayList<>();
		List<BasicAnswerVo> list1 = new ArrayList<>();
		List<BasicAnswerVo> list2 = new ArrayList<>();
		if(type == DiseaseType.PASTMEDICALHISTORY) {
			List<DiagnosisPastmedicalHistory> pastmedicalHistory = diagnosisPastmedicalHistoryService.queryPastmedicalHistory(param);
			List<DiagnosisSubpastmedicalHistory> subPastmedicalHistory = diagnosisPastmedicalHistoryService.querySubPastmedicalHistory(param);
			list1 = pastmedicalHistory.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
			list2 = subPastmedicalHistory.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
		} else if (type == DiseaseType.ALLERGICHISTORY) {
			List<DiagnosisAllergicHistory> allergicHistoryList = diagnosisAllergicHistoryService.queryAllergicHistory(param);
			List<DiagnosisSuballergicHistory> subAllergicHistoryList = diagnosisAllergicHistoryService.querySubAllergicHistory(param);
			list1 = allergicHistoryList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
			list2 = subAllergicHistoryList.stream().map(BasicAnswerVo::new).collect(Collectors.toList());
		}
		answerList.addAll(list1);
		answerList.addAll(list2);
		return answerList;
	}
}
