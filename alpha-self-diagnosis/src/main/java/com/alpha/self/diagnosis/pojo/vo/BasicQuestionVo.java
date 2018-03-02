package com.alpha.self.diagnosis.pojo.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.alpha.commons.enums.DisplayType;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.user.utils.AppUtils;

public class BasicQuestionVo implements Serializable, IQuestionVo {

    private static final String USER_CHAR = "{userName}";

    private static final long serialVersionUID = 6688903127327118326L;

    private String userId;

    private Long diagnosisId;

    private String displayType;

    private String searchUrl;

    private String questionTitle;

    @JsonIgnore
    private String title;

    private String sympCode;

    private int type;//问题类型

    /**
     * 问题编码
     */
    private String questionCode;

    private List<IAnswerVo> answers;


    public BasicQuestionVo(Long diagnosisId, BasicQuestion basicQuestion, List<IAnswerVo> answers, UserInfo userInfo, String userName) {
        this.diagnosisId = diagnosisId;
        this.displayType = basicQuestion.getDisplayType();
        this.questionCode = basicQuestion.getQuestionCode();
        this.questionTitle = basicQuestion.getTitle();
        this.title = basicQuestion.getTitle();
        this.answers = answers;
        if (userInfo != null) {
            this.userId = userInfo.getUserId().toString();
            if (this.questionTitle.contains(USER_CHAR)) {
                this.questionTitle = this.questionTitle.replace(USER_CHAR, userName);
            }
        }
    }

    public BasicQuestionVo() {
    }

    /**
     * 把医学问题转换成返回视图
     *
     * @param question
     * @return
     */
    public BasicQuestionVo(DiagnosisMainsympQuestion question, Long diagnosisId, String sympCode, UserInfo userInfo) {
        if (question.getQuestionType() == QuestionEnum.伴随症状.getValue()) {
            //this.displayType = "checkbox_more_input_confirm";
        	this.displayType = DisplayType.CHECKBOX_MORE_INPUT_CONFIRM.getValue();
            this.searchUrl = "/data/search/concsymp";
        } else {
            //this.displayType = "radio";
            String displayType = StringUtils.isNotEmpty(question.getDisplayType()) ? question.getDisplayType() : "radio";
        	this.displayType = displayType;
        }
        this.diagnosisId = diagnosisId;
        this.questionCode = question.getQuestionCode();
        this.questionTitle = AppUtils.setUserNameAtQuestionTitle(question.getPopuTitle(), userInfo);
        this.title = question.getTitle();
        this.sympCode = sympCode;
        this.type = question.getQuestionType();
    }

    /**
     * 把医学问题转换成返回视图
     *
     * @param question
     * @return
     */
    public BasicQuestionVo(DiagnosisMainsympQuestion question, List<DiagnosisQuestionAnswer> dqAnswers, Long diagnosisId, String sympCode) {
        this.displayType = "radio";
        this.diagnosisId = diagnosisId;
        this.questionCode = question.getQuestionCode();
        this.questionTitle = question.getPopuTitle();
        this.title = question.getPopuTitle();
        this.sympCode = sympCode;
        //答案转换
        List<IAnswerVo> answerVos = BasicAnswerVo.convertListMedicineAnswerVo(dqAnswers);
        this.answers = answerVos;
        this.type = QuestionEnum.医学问题.getValue();
    }

    /**
     * 把医学问题转换成返回视图
     *
     * @param question
     * @return
     */
    public BasicQuestionVo(DiagnosisMainsympQuestion question, Long diagnosisId) {
        BasicQuestionVo questionVo = new BasicQuestionVo();
        questionVo.setType(QuestionEnum.医学问题.ordinal());
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setQuestionCode(question.getQuestionCode());
        questionVo.setQuestionTitle(question.getPopuTitle());
        questionVo.setTitle(question.getPopuTitle());
        questionVo.setDisplayType("radio");
    }

    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public List<IAnswerVo> getAnswers() {
        return answers;
    }

    public void setAnswers(List<IAnswerVo> answers) {
        this.answers = answers;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSympCode() {
        return sympCode;
    }

    public void setSympCode(String sympCode) {
        this.sympCode = sympCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
