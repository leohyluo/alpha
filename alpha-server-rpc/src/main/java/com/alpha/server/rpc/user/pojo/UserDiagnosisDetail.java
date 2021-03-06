package com.alpha.server.rpc.user.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_diagnosis_detail")
public class UserDiagnosisDetail implements Serializable {


    private static final long serialVersionUID = 4709483755609783413L;
    /**
     * id
     */
    @Column(name = "id")
    @Id
    private Long id;

    /**
     * 诊断号唯一编码
     */
    @Column(name = "diagnosis_id")
    private Long diagnosisId;

    /**
     * 主症状编码
     */
    @Column(name = "symp_code")
    private String sympCode;

    /**
     * 被问诊人ID
     */
    @Column(name = "member_id")
    private Long memberId;

    /**
     * 问诊人Id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 问题编码
     */
    @Column(name = "question_code")
    private String questionCode;

    /**
     * 问题内容
     */
    @Column(name = "question_content")
    private String questionContent;
    /**
     * 问题内容
     */
    @Column(name = "question_title")
    private String questioTitle;

    /**
     * 答案编码
     */
    @Column(name = "answer_code")
    private String answerCode;

    /**
     * 答案内容
     */
    @Column(name = "answer_content")
    private String answerContent;

    /**
     * 问题类型 1主症状 2普通问题 3伴随症状 4诊断结果
     */
    @Column(name = "question_type")
    private Integer questionType;

    /**
     * 回答问题时间
     */
    @Column(name = "answer_time")
    private Date answerTime;

    /**
     * 答案的JSON串
     */
    @Column(name = "answer_json")
    private String answerJson;

    /**
     * 反向特异性疾病
     */
    @Column(name = "reverse_disease_code")
    private String reverseDiseaseCode;

    /**
     * 正向特异性疾病
     */
    @Column(name = "forward_disease_code")
    private String forwardDiseaseCode;

    /**
     * 无异性疾病
     */
    @Column(name = "nothing_disease_code")
    private String nothingDiseaseCode;

    /**
     *
     */
    @Column(name = "incre_flag")
    private String increFlag;

    /**
     *
     */
    @Column(name = "opera_flag")
    private String operaFlag;

    /**
     *
     */
    @Column(name = "operate_type")
    private String operateType;

    /**
     *
     */
    @Column(name = "data_version")
    private Integer dataVersion;

    /**
     *
     */
    @Column(name = "version_evolution")
    private String versionEvolution;

    /**
     * 来源
     */
    @Column(name = "source_")
    private String source;

    /**
     *
     */
    @Column(name = "version_")
    private String version;

    /**
     *
     */
    @Column(name = "creator")
    private String creator;

    /**
     *
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     *
     */
    @Column(name = "reviewer")
    private String reviewer;

    /**
     *
     */
    @Column(name = "review_time")
    private Date reviewTime;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getReverseDiseaseCode() {
        return reverseDiseaseCode;
    }

    public void setReverseDiseaseCode(String reverseDiseaseCode) {
        this.reverseDiseaseCode = reverseDiseaseCode;
    }

    public String getForwardDiseaseCode() {
        return forwardDiseaseCode;
    }

    public void setForwardDiseaseCode(String forwardDiseaseCode) {
        this.forwardDiseaseCode = forwardDiseaseCode;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Long getDiagnosisId() {
        return this.diagnosisId;
    }


    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return this.memberId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }


    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestionCode() {
        return this.questionCode;
    }


    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getQuestionContent() {
        return this.questionContent;
    }


    public void setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
    }

    public String getAnswerCode() {
        return this.answerCode;
    }


    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerContent() {
        return this.answerContent;
    }


    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getQuestionType() {
        return this.questionType;
    }


    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Date getAnswerTime() {
        return this.answerTime;
    }


    public void setIncreFlag(String increFlag) {
        this.increFlag = increFlag;
    }

    public String getIncreFlag() {
        return this.increFlag;
    }


    public void setOperaFlag(String operaFlag) {
        this.operaFlag = operaFlag;
    }

    public String getOperaFlag() {
        return this.operaFlag;
    }


    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateType() {
        return this.operateType;
    }


    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

    public Integer getDataVersion() {
        return this.dataVersion;
    }


    public void setVersionEvolution(String versionEvolution) {
        this.versionEvolution = versionEvolution;
    }

    public String getVersionEvolution() {
        return this.versionEvolution;
    }


    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }


    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }


    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }


    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }


    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewer() {
        return this.reviewer;
    }


    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Date getReviewTime() {
        return this.reviewTime;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public String getNothingDiseaseCode() {
        return nothingDiseaseCode;
    }

    public void setNothingDiseaseCode(String nothingDiseaseCode) {
        this.nothingDiseaseCode = nothingDiseaseCode;
    }

    public String getSympCode() {
        return sympCode;
    }

    public void setSympCode(String sympCode) {
        this.sympCode = sympCode;
    }

    public String getQuestioTitle() {
        return questioTitle;
    }

    public void setQuestioTitle(String questioTitle) {
        this.questioTitle = questioTitle;
    }
}
