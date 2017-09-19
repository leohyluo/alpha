package com.alpha.self.diagnosis.pojo.vo;

import java.io.Serializable;
import java.util.List;

import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.user.pojo.UserInfo;

public class BasicQuestionVo implements Serializable, IQuestionVo {

	private static final String USER_CHAR = "{userName}";

    private static final long serialVersionUID = 6688903127327118326L;

    private String userId;

    private Long diagnosisId;

    private String displayType;

    private String questionTitle;

    private String sympCode;

    private int type;//问题类型

    /**
     * 问题编码
     */
    private String questionCode;

    private List<IAnswerVo> answers;

    public BasicQuestionVo(Long diagnosisId, BasicQuestion basicQuestion, List<IAnswerVo> answers) {
        this.diagnosisId = diagnosisId;
        this.displayType = basicQuestion.getDisplayType();
        this.questionCode = basicQuestion.getQuestionCode();
        this.questionTitle = basicQuestion.getTitle();
        this.answers = answers;
        this.sympCode = sympCode;
    }

    public BasicQuestionVo(Long diagnosisId, BasicQuestion basicQuestion, List<IAnswerVo> answers, UserInfo userInfo, String userName) {
        this.diagnosisId = diagnosisId;
        this.displayType = basicQuestion.getDisplayType();
        this.questionCode = basicQuestion.getQuestionCode();
        this.questionTitle = basicQuestion.getTitle();
        this.answers = answers;
        if(userInfo != null) {
            this.userId = userInfo.getUserId().toString();
            if(this.questionTitle.contains(USER_CHAR)) {
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
    public BasicQuestionVo(DiagnosisMainsympQuestion question, Long diagnosisId, String sympCode) {
        this.displayType = "radio";
        this.diagnosisId = diagnosisId;
        this.questionCode = question.getQuestionCode();
        this.questionTitle = question.getPopuTitle();
        this.sympCode = sympCode;
        this.type = QuestionEnum.医学问题.getValue();
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
    public  BasicQuestionVo(DiagnosisMainsympQuestion question, Long diagnosisId) {
        BasicQuestionVo questionVo = new BasicQuestionVo();
        questionVo.setType(QuestionEnum.医学问题.ordinal());
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setQuestionCode(question.getQuestionCode());
        questionVo.setQuestionTitle(question.getPopuTitle());
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


}