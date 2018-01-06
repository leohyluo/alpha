package com.alpha.self.diagnosis.processor;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.StringUtils;
import com.alpha.self.diagnosis.annotation.BasicAnswerProcessor;
import com.alpha.self.diagnosis.pojo.BasicAnswer;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionWithSearchVo;
import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.SelectedBasicAnswerVo;
import com.alpha.self.diagnosis.service.BasicAnswerService;
import com.alpha.self.diagnosis.service.DiagnosisPastmedicalHistoryService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.user.pojo.UserInfo;

@BasicAnswerProcessor
@Component
public class PastmedicalHistoryProcessor extends AbstractBasicAnswerProcessor {

    @Resource
    private DiagnosisPastmedicalHistoryService diagnosisPastmedicalHistoryService;
    @Resource
    private BasicAnswerService basicAnswerService;

    private static final String SEARCH_URL = "/data/search/pastmedicalHistory";
    private static final String QUESTION_CODE = BasicQuestionType.PAST_MEDICAL_HISTORY.getValue();

    protected Map<String, List<IAnswerVo>> queryAnswers(BasicQuestion question, UserInfo userInfo) {
        int showCount = 7;
        Map<String, List<IAnswerVo>> map = new HashMap<>();
        List<IAnswerVo> showList = new ArrayList<>();
        List<IAnswerVo> searchList = new ArrayList<>();
        Date birth = userInfo.getBirth();
        float age = DateUtils.getAge(birth);

        //用于存储之前已选择的既往史
        List<SelectedBasicAnswerVo> list0 = new ArrayList<>();
        // 用于存储用户频次超过阀值的既往史
        //List<BasicAnswerVo> list1 = new ArrayList<>();
        // 用于存储用户频次不超过阀值的既往史
        List<BasicAnswerVo> list2 = new ArrayList<>();
        // 用于存储手术史、否/不清楚
        List<BasicAnswerVo> defaultAnswervoList = new ArrayList<>();

        //查询之前已选择的既往史
        Map<String, Object> selectedParam = new HashMap<>();
        String selectedPastmedicalHistoryCode = userInfo.getPastmedicalHistoryCode();
        List<DiseaseVo> selectedList = new ArrayList<>();
        if (StringUtils.isNotEmpty(selectedPastmedicalHistoryCode)) {
            List<String> diseaseCodeList = Stream.of(selectedPastmedicalHistoryCode.split(",")).collect(toList());
            selectedParam.put("userPastmedicalHistoryCode", diseaseCodeList);
            selectedList = diagnosisPastmedicalHistoryService.querySelectedPastmedicalHistory(selectedParam);
        }

        if (CollectionUtils.isNotEmpty(selectedList)) {
            list0 = selectedList.stream().map(SelectedBasicAnswerVo::new).collect(toList());
        }
        //查出手术史、否/不清楚这两个选项
        List<BasicAnswer> answerList = basicAnswerService.findByQuestionCode(question.getQuestionCode());
        defaultAnswervoList = answerList.stream().map(BasicAnswerVo::new).collect(toList());
        Map<String, DiseaseVo> selectedMap = selectedList.stream().collect(Collectors.toMap(DiseaseVo::getDiseaseCode, Function.identity()));
        defaultAnswervoList = defaultAnswervoList.stream().filter(e -> !selectedMap.containsKey(e.getAnswerValue())).collect(toList());
        
        //查询用户频次超过阀值的既往史大类
        int limit = showCount - selectedList.size() - defaultAnswervoList.size();
        //暂时不需要把用户行为加进来
        /*if (limit > 0) {
            Map<String, Object> param = userInfo.toBasicMap();
            //设置阀值
            param.put("gender", userInfo.getGender());
            param.put("age", age);
            param.put("threshold", GlobalConstants.PAST_MEDICAL_HISTORY_THRESHOLD);
            if (StringUtils.isNotEmpty(selectedPastmedicalHistoryCode)) {
                List<String> diseaseCodeList = Stream.of(selectedPastmedicalHistoryCode.split(",")).collect(toList());
                param.put("userPastmedicalHistoryCodeNotIn", diseaseCodeList);
            }
            param.put("limitSize", limit);
            list1 = queryPastMedicalHistory(param);
        }*/
        //查询按默认排序的既往史大类
        //limit = showCount - selectedList.size() - defaultAnswervoList.size() - list1.size();
        if (limit > 0) {
            Map<String, Object> param = userInfo.toBasicMap();
            param.put("gender", userInfo.getGender());
            param.put("age", age);
            if (StringUtils.isNotEmpty(selectedPastmedicalHistoryCode)) {
                List<String> diseaseCodeList = Stream.of(selectedPastmedicalHistoryCode.split(",")).collect(toList());
                param.put("userPastmedicalHistoryCodeNotIn", diseaseCodeList);
            }
            param.put("limitSize", limit);
            list2 = queryPastMedicalHistory(param);
        }

        //用户之前如没选择过既往史，默认选中"无"
        if(CollectionUtils.isEmpty(list0)) {
        	defaultAnswervoList = defaultAnswervoList.stream().peek(e->{
        		if("-1".equals(e.getAnswerValue())) {
        			e.setChecked("Y");
        		}
        	}).collect(toList());
        }
        //拼装展示用的数据
        showList.addAll(list0);
        //showList.addAll(list1);
        showList.addAll(list2);
        showList.addAll(defaultAnswervoList);
        //查询小类既往史
        /*List<DiagnosisSubpastmedicalHistory> subList = diagnosisPastmedicalHistoryService.querySubPastmedicalHistory(param);
        List<BasicAnswerVo> subAnswervoList = subList.stream().map(BasicAnswerVo::new).collect(toList());*/
        //拼装查询 用的数据
        /*searchList.addAll(list1);
		searchList.addAll(defaultAnswervoList);
		searchList.addAll(list2);
		searchList.addAll(subAnswervoList);*/

        map.put("show", showList);
        map.put("search", searchList);
        return map;
    }


    @Override
    protected Map<String, List<IAnswerVo>> getAnswers(BasicQuestion question, UserInfo userInfo) {
        return this.queryAnswers(question, userInfo);
    }

    @Override
    protected IQuestionVo getQuestionVo(Long diagnosisId, BasicQuestion question, UserInfo userInfo,
                                        Map<String, List<IAnswerVo>> data) {
        List<IAnswerVo> showList = data.get("show");
        //List<IAnswerVo> searchList = data.get("search");
        String userName = getUserName(diagnosisId, userInfo);
        //return new DiseaseQuestionVo(diagnosisId, question, showList, searchList, userInfo, userName);
        BasicQuestionWithSearchVo questionvo = new BasicQuestionWithSearchVo(diagnosisId, question, showList, userInfo, userName);
        questionvo.setSearchUrl(SEARCH_URL);
        return questionvo;
    }

    @Override
    protected String setQuestionCode() {
        return QUESTION_CODE;
    }

    private List<BasicAnswerVo> queryPastMedicalHistory(Map<String, Object> param) {
        List<BasicAnswerVo> result = new ArrayList<>();
        List<DiagnosisPastmedicalHistory> list = diagnosisPastmedicalHistoryService.queryPastmedicalHistory(param);
        result = list.stream().map(BasicAnswerVo::new).collect(toList());
        return result;
    }
}
