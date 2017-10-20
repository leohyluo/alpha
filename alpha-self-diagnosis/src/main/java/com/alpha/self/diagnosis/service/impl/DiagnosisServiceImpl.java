package com.alpha.self.diagnosis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alpha.commons.api.baidu.naturelanguage.NatureLanguageApi;
import com.alpha.commons.api.tencent.ApiConstants;
import com.alpha.commons.api.tencent.WenZhiApi;
import com.alpha.commons.api.tencent.vo.KeywordResultVo;
import com.alpha.commons.api.tencent.vo.Keywords;
import com.alpha.commons.constants.GlobalConstants;
import com.alpha.commons.core.service.SysSequenceService;
import com.alpha.commons.exception.ServiceException;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.web.ResponseStatus;
import com.alpha.self.diagnosis.dao.UserDiagnosisDetailDao;
import com.alpha.self.diagnosis.pojo.BasicAnswer;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.Synonym;
import com.alpha.self.diagnosis.pojo.vo.*;
import com.alpha.self.diagnosis.service.*;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserDiagnosisOutcome;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.service.UserBasicRecordService;
import com.alpha.user.service.UserInfoService;
import com.alpha.user.service.UserMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class DiagnosisServiceImpl implements DiagnosisService {

    @Resource
    private UserMemberService userMemberService;
    @Resource
    private SysSequenceService sysSequenceService;
    @Resource
    private BasicQuestionService basicQuestionService;
    @Resource
    private BasicAnswerService basicAnswerService;
    @Resource
    private UserBasicRecordService userBasicRecordService;
    @Resource
    private SymptomMainService symptomMainService;
    @Resource
    private SynonymService synonymService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserDiagnosisOutcomeService userDiagnosisOutcomeService;
    @Resource
    private UserDiagnosisDetailDao userDiagnosisDetailDao;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public BasicQuestionVo start(Long userId, Integer inType) {
        String questionCode = null;
        Long diagnosisId = sysSequenceService.getNextSequence("diagnosis_seq");
        //获取第一个问题
        BasicQuestion param = new BasicQuestion(1, null, null, null, null);
        BasicQuestion question = basicQuestionService.find(param);
        if (question != null) {
            questionCode = question.getQuestionCode();
        }
        //获取第一个问题的答案
        List<BasicAnswer> answerList = basicAnswerService.findByQuestionCode(questionCode);
        //获取用户下的成员
        /*Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
		List<UserMember> userMemberList = userMemberService.find(map);*/

        List<IAnswerVo> answervoList = new ArrayList<>();
        List<BasicAnswerVo> defaultAnswervoList = answerList.stream().map(BasicAnswerVo::new).collect(toList());
        //将自己的answerCode设置为userId,将他人的answerCode设置为0
        defaultAnswervoList.stream().peek(e -> updateAnswerVo(e, userId)).collect(toList());
        //List<BasicAnswerVo> answervoList4userMember = userMemberList.stream().map(BasicAnswerVo::new).collect(toList());
        answervoList.addAll(defaultAnswervoList);
        //answervoList.addAll(answervoList4userMember);
        //添加就诊记录
        UserBasicRecord record = new UserBasicRecord();
        record.setUserId(userId);
        record.setDiagnosisId(diagnosisId);
        record.setCreateTime(new Date());
        userBasicRecordService.addUserBasicRecord(record);
        return new BasicQuestionVo(diagnosisId, question, answervoList, null, null);
    }

    private void updateAnswerVo(BasicAnswerVo basicAnswer, Long userId) {
        String answerCode = "0";
        if ("自己".equals(basicAnswer.getAnswerTitle())) {    //自己
            answerCode = String.valueOf(userId);
            basicAnswer.setAnswerValue(answerCode);
        }
        if ("他人".equals(basicAnswer.getAnswerTitle())) { //他人
            basicAnswer.setAnswerValue(answerCode);
        }
    }

    @Deprecated
    @Override
    public IQuestionVo lexicalAnalysisByTencent(AnalysisRequestVo vo) {
        IQuestionVo questionVo = null;
        Long diagnosisId = vo.getDiagnosisId();
        //初始化文智
        WenZhiApi wenzhiApi = new WenZhiApi();
        TreeMap<String, Object> keywordParams = new TreeMap<String, Object>();
        keywordParams.put("title", vo.getContent());
        keywordParams.put("content", vo.getContent());
        //调用腾迅API获取关键词
        String result = wenzhiApi.invoke(ApiConstants.ACTION_TEXT_KWYWORDS, keywordParams);
        KeywordResultVo keywordResultVo = JSON.parseObject(result, KeywordResultVo.class);
        List<String> keywordList = keywordResultVo.getKeywords().stream().map(Keywords::getKeyword).distinct().collect(toList());
        Map<String, Object> param = new HashMap<>();
        param.put("sympNameList", keywordList);
        List<DiagnosisMainSymptoms> mainList = symptomMainService.query(param);
        if (CollectionUtils.isNotEmpty(mainList)) {
            questionVo = symptomMainService.getMainSymptomsQuestion(diagnosisId, mainList);
            return questionVo;
        }
        //调用腾迅API获取同义词
//        TreeMap<String, Object> synonymParams = new TreeMap<String, Object>();
//        synonymParams.put("text", vo.getContent());
//        result = wenzhiApi.invoke(ApiConstants.ACTION_TEXT_SYNONYM, synonymParams);
//        SynonymResultVo synonymResultVo = JSON.parseObject(result, SynonymResultVo.class);
        //关键词、同义词均未找到匹配的主症状
//		questionVo = symptomMainService.getMainSymptomsQuestion(diagnosisId);

        return questionVo;
    }

    @Override
    public List<DiagnosisMainSymptoms> lexicalAnalysisByBaidu(QuestionRequestVo questionVo, UserInfo userInfo) {
        AnswerRequestVo answerVo = questionVo.getAnswers().get(0);
        String text = answerVo.getContent();
        List<DiagnosisMainSymptoms> mainList = new ArrayList<>();
        //初始化百度云自然语言
        NatureLanguageApi languageApi = new NatureLanguageApi();
        //拆词结果
        List<String> wordList = languageApi.lexer(text);
        if (CollectionUtils.isEmpty(wordList)) {
            return mainList;
        }
        Set<String> wordSet = wordList.stream().collect(Collectors.toSet());

        //拆词结果跟主症状名称完全匹配
        Map<String, Object> mainParam = new HashMap<>();
        List<DiagnosisMainSymptoms> allMainList = symptomMainService.query(mainParam);
        allMainList = allMainList.stream().filter(e -> e.mainSymptomPredicate(userInfo,questionVo.getInType())).collect(toList());

        mainList = allMainList.stream().filter(e -> wordSet.contains(e.getSympName())).collect(toList());
        if (CollectionUtils.isNotEmpty(mainList)) {
            return mainList;
        }

        //查询智慧药师同义词表
        Map<String, Object> param = new HashMap<>();
        param.put("wordList", wordList);
        List<Synonym> synonymList = synonymService.query(param);
        //同义词对应的主症状名称集合
        List<String> mainSymptomNameList = synonymList.stream().map(Synonym::getSymptomName).distinct()
                .collect(toList());

        if (CollectionUtils.isNotEmpty(mainSymptomNameList)) {
            //根据智慧药师主症状名称查询主症状集合
            param = new HashMap<>();
            param.put("sympNameList", mainSymptomNameList);
            mainList = symptomMainService.query(param);
            mainList = mainList.stream().filter(e -> e.mainSymptomPredicate(userInfo,questionVo.getInType())).collect(toList());
        }
        if (CollectionUtils.isEmpty(mainList)) {
            mainList = queryByBaidu(text, wordList, allMainList);
        }
        return mainList;
    }

    private List<DiagnosisMainSymptoms> queryByBaidu(String content, List<String> wordList, List<DiagnosisMainSymptoms> allMainList) {
        Set<String> keywordSet = Stream.of(GlobalConstants.MAINSYMPTOM_KEYWORDS.split(",")).collect(Collectors.toSet());
        //找出短语包含的主症状关键字
        final Set<String> filterKeywordSet = keywordSet.stream().filter(e -> wordContainKeyword(wordList, e)).collect(Collectors.toSet());
        //查询包含有关键字的主症状
        final List<DiagnosisMainSymptoms> filterMainList = allMainList.stream().filter(e -> mainSymptomContainKeyword(filterKeywordSet, e)).collect(toList());
        //如果主症状名称与文本相似度超过60%的则把主症状拿出来
        NatureLanguageApi languageApi = new NatureLanguageApi();
        Predicate<DiagnosisMainSymptoms> simnetPredicate = (e) -> {
            Double simnetRate = languageApi.simnet(content, e.getSympName());
            if (simnetRate.doubleValue() >= 0.5) {
                return true;
            }
            return false;
        };
        List<DiagnosisMainSymptoms> resultList = filterMainList.stream().filter(simnetPredicate).collect(toList());
        return resultList;
    }

    /**
     * 短语是否包含有主症状部位关键字
     *
     * @param wordList
     * @param keyword
     * @return
     */
    private Boolean wordContainKeyword(List<String> wordList, String keyword) {
        return wordList.stream().anyMatch(e -> e.contains(keyword));
    }

    /**
     * 主症状是否包含有主症状部位关键字
     *
     * @param keyword
     * @param mainSymptom
     * @return
     */
    private Boolean mainSymptomContainKeyword(Set<String> keywordSet, DiagnosisMainSymptoms mainSymptom) {
        return keywordSet.stream().anyMatch(e -> mainSymptom.getSympName().contains(e));
    }

    public DiagnosisResultVo showDiagnosisResult(Long userId, Long diagnosisId) {
        UserInfo userInfo = userInfoService.queryByUserId(userId);
        if (userInfo == null) {
            return null;
        }
        UserBasicRecord record = userBasicRecordService.findByDiagnosisId(diagnosisId);
        DiagnosisResultVo resultVo = new DiagnosisResultVo(record);
        resultVo.merge(userInfo);
        //拼装主症状
    	/*List<UserDiagnosisDetail> detailList = userDiagnosisDetailDao.listUserDiagnosisDetail(diagnosisId);
    	if(CollectionUtils.isNotEmpty(detailList)) {
    		UserDiagnosisDetail item = detailList.get(0);
    		String mainCode = item.getSympCode();
    		
    		Map<String, Object> param = new HashMap<>();
    		param.put("sympCode", mainCode);
    		List<DiagnosisMainSymptoms> mainList = symptomMainService.query(param);
    		if(CollectionUtils.isNotEmpty(mainList)) {
    			DiagnosisMainSymptoms mainSymptoms = mainList.get(0);
    			String mainSymptomName = mainSymptoms.getSympName();
    			resultVo.setMainSymptom(mainSymptomName);
    		}
    	}*/
        //拼装诊断结果
        List<UserDiagnosisOutcome> udos = userDiagnosisOutcomeService.listTop5UserDiagnosisOutcome(diagnosisId);
        List<UserDiagnosisOutcomeVo> diseasevoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(udos)) {
            diseasevoList = udos.stream().map(UserDiagnosisOutcomeVo::new).collect(Collectors.toList());
        }
        resultVo.setDiseaseList(diseasevoList);
        return resultVo;
    }

	@Override
	public DiagnosisResultVo showDiagnosisResult(String idcard) throws ServiceException {
		//根据身份证查询用户是否存在
    	Map<String, Object> param = new HashMap<>();
        param.put("idcard", idcard);
        List<UserInfo> list = userInfoService.query(param);
        if(CollectionUtils.isEmpty(list)) {
        	throw new ServiceException(ResponseStatus.USER_NOT_FOUND, "用户不存在");
        } else {
        	UserInfo userInfo = list.get(0);
        	UserBasicRecord record = userBasicRecordService.findLastCompleted(userInfo.getUserId());
        	if(record == null) {
        		throw new ServiceException(ResponseStatus.NOT_FOUND, "用户无相关诊断记录");
        	}
        	DiagnosisResultVo resultVo = new DiagnosisResultVo(record);
            resultVo.merge(userInfo);
            //拼装诊断结果
            List<UserDiagnosisOutcome> udos = userDiagnosisOutcomeService.listTop5UserDiagnosisOutcome(record.getDiagnosisId());
            List<UserDiagnosisOutcomeVo> diseasevoList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(udos)) {
                diseasevoList = udos.stream().map(UserDiagnosisOutcomeVo::new).collect(Collectors.toList());
            }
            resultVo.setDiseaseList(diseasevoList);
            return resultVo;
        }
	}
}
