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
import com.alpha.self.diagnosis.service.DiagnosisAllergicHistoryService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.user.pojo.UserInfo;

@BasicAnswerProcessor
@Component
public class AllergicHistoryAnswerProcessor extends AbstractBasicAnswerProcessor {

    private static final String SEARCH_URL = "/data/search/allergicHistory";
    private static final String QUESTION_CODE = BasicQuestionType.ALLERGIC_HISTORY.getValue();

    @Resource
    private DiagnosisAllergicHistoryService diagnosisAllergicHistoryService;
    @Resource
    private BasicAnswerService basicAnswerService;

    protected Map<String, List<IAnswerVo>> queryAnswers(BasicQuestion question, UserInfo userInfo) {
        int showCount = 7;
        Map<String, List<IAnswerVo>> map = new HashMap<>();
        List<IAnswerVo> showList = new ArrayList<>();
        List<IAnswerVo> searchList = new ArrayList<>();
        Date birth = userInfo.getBirth();
        float age = DateUtils.getAge(birth);

        // 用于存储之前已选择的既往史
        List<SelectedBasicAnswerVo> list0 = new ArrayList<>();
        // 用于存储用户频次超过阀值的既往史
        //List<BasicAnswerVo> list1 = new ArrayList<>();
        // 用于存储用户频次不超过阀值的既往史
        List<BasicAnswerVo> list2 = new ArrayList<>();
        // 用于存储手术史、否/不清楚
        List<BasicAnswerVo> defaultAnswervoList = new ArrayList<>();

        // 查询之前已选择的过敏史
        Map<String, Object> selectedParam = new HashMap<>();
        String selectedPastmedicalHistoryCode = userInfo.getAllergicHistoryCode();
        List<DiseaseVo> selectedList = new ArrayList<>();
        if (StringUtils.isNotEmpty(selectedPastmedicalHistoryCode)) {
            List<String> diseaseCodeList = Stream.of(selectedPastmedicalHistoryCode.split(",")).collect(toList());
            selectedParam.put("userAllergicHistoryCode", diseaseCodeList);
            selectedList = diagnosisAllergicHistoryService.querySelectedAllergicHistory(selectedParam);
        }
        if (CollectionUtils.isNotEmpty(selectedList)) {
            list0 = selectedList.stream().map(SelectedBasicAnswerVo::new).collect(toList());
        }
        //查出手术史、否/不清楚这两个选项
        List<BasicAnswer> answerList = basicAnswerService.findByQuestionCode(question.getQuestionCode());
        defaultAnswervoList = answerList.stream().map(BasicAnswerVo::new).collect(toList());
        Map<String, DiseaseVo> selectedMap = selectedList.stream().collect(Collectors.toMap(DiseaseVo::getDiseaseCode, Function.identity()));
        defaultAnswervoList = defaultAnswervoList.stream().filter(e -> !selectedMap.containsKey(e.getAnswerValue())).collect(toList());
        //查询用户频次超过阀值的过敏史大类（暂时加入用户行为逻辑）
        int limit = showCount - selectedList.size() - defaultAnswervoList.size();
        /*if (limit > 0) {
            Map<String, Object> param = userInfo.toBasicMap();
            // 设置阀值
            param.put("gender", userInfo.getGender());
            param.put("age", age);
            param.put("threshold", GlobalConstants.ALLERGIC_HISTORY_THRESHOLD);
            if (StringUtils.isNotEmpty(selectedPastmedicalHistoryCode)) {
                List<String> diseaseCodeList = Stream.of(selectedPastmedicalHistoryCode.split(",")).collect(toList());
                param.put("userAllergicHistoryCodeNotIn", diseaseCodeList);
            }
            param.put("limitSize", limit);
            list1 = queryAllergicHistory(param);
        }*/
        //查询过敏史大类
        //limit = showCount - selectedList.size() - defaultAnswervoList.size() - list1.size();
        if (limit > 0) {
            Map<String, Object> param = userInfo.toBasicMap();
            param.put("gender", userInfo.getGender());
            param.put("age", age);
            if (StringUtils.isNotEmpty(selectedPastmedicalHistoryCode)) {
                List<String> diseaseCodeList = Stream.of(selectedPastmedicalHistoryCode.split(",")).collect(toList());
                param.put("userAllergicHistoryCodeNotIn", diseaseCodeList);
            }
            param.put("limitSize", limit);
            List<DiagnosisAllergicHistory> list = diagnosisAllergicHistoryService.queryAllergicHistory(param);
            list2 = list.stream().map(BasicAnswerVo::new).collect(toList());
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
        String userName = getUserName(diagnosisId, userInfo);
        BasicQuestionWithSearchVo questionvo = new BasicQuestionWithSearchVo(diagnosisId, question, showList, userInfo, userName);
        questionvo.setSearchUrl(SEARCH_URL);
        return questionvo;
    }

    @Override
    protected String setQuestionCode() {
        return QUESTION_CODE;
    }

    private List<BasicAnswerVo> queryAllergicHistory(Map<String, Object> param) {
        List<BasicAnswerVo> result = new ArrayList<>();
        List<DiagnosisAllergicHistory> list = diagnosisAllergicHistoryService.queryAllergicHistory(param);
        result = list.stream().map(BasicAnswerVo::new).collect(toList());
        return result;
    }
}
