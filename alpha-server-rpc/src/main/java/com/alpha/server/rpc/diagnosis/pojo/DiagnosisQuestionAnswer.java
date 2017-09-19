
package com.alpha.server.rpc.diagnosis.pojo;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.Long;
import java.lang.Double;
import java.lang.String;
import java.lang.Integer;
import java.util.Date;
@Entity
@Table(name = "diagnosis_question_answer")
public class DiagnosisQuestionAnswer implements Serializable{

	private static final long serialVersionUID = 982534919320049029L;

	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	private Long id;

	/**
	 * 问题编码
	 */
	@Column(name = "question_code")
	private String questionCode;
	/**
	 * 问题权重
	 */
	@Column(name = "question_weight")
	private Double questionWeight;

	/**
	 * 答案编码
	 */
	@Column(name = "answer_code")
	private String answerCode;

	/**
	 * 答案内容
	 */
	@Transient
	@Column(name = "content")
	private String content;

	/**
	 * 答案描述
	 */
	@Transient
	@Column(name = "popu_content")
	private String popuContent;

	/**
	 * 性别
	 */
	@Transient
	@Column(name = "gender")
	private Integer gender;

	/**
	 * 最小年龄
	 */
	@Transient
	@Column(name = "min_age")
	private Double minAge;

	/**
	 * 最大年龄
	 */
	@Transient
	@Column(name = "max_age")
	private Double maxAge;

	/**
	 * 疾病编码
	 */
	@Column(name = "disease_code")
	private String diseaseCode;

	/**
	 * 1正向特异性,-1反向特异性，0无
	 */
	@Column(name = "answer_spec")
	private Integer answerSpec;

	/**
	 * 权重
	 */
	@Column(name = "weight")
	private Double weight;

	/**
	 * 顺序
	 */
	@Column(name = "default_order")
	private Integer defaultOrder;

	/**
	 * 下一个问题id
	 */
	@Column(name = "next_question_id")
	private String nextQuestionId;

	/**
	 * 答案权重，计算后的值
	 */
	@Transient
	private Double weightValue;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	public String getQuestionCode() {
		return this.questionCode;
	}


	public void setAnswerCode(String answerCode) {
		this.answerCode = answerCode;
	}

	public String getAnswerCode() {
		return this.answerCode;
	}


	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}

	public String getDiseaseCode() {
		return this.diseaseCode;
	}

	public String getPopuContent() {
		return popuContent;
	}

	public void setPopuContent(String popuContent) {
		this.popuContent = popuContent;
	}

	public void setAnswerSpec(Integer answerSpec) {
		this.answerSpec = answerSpec;
	}

	public Integer getAnswerSpec() {
		return this.answerSpec;
	}


	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getWeight() {
		return this.weight;
	}


	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public Integer getDefaultOrder() {
		return this.defaultOrder;
	}


	public void setNextQuestionId(String nextQuestionId) {
		this.nextQuestionId = nextQuestionId;
	}

	public String getNextQuestionId() {
		return this.nextQuestionId;
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Double getMinAge() {
		return minAge;
	}

	public void setMinAge(Double minAge) {
		this.minAge = minAge;
	}

	public Double getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Double maxAge) {
		this.maxAge = maxAge;
	}

	public Double getQuestionWeight() {
		return questionWeight;
	}

	public void setQuestionWeight(Double questionWeight) {
		this.questionWeight = questionWeight;
	}

	public Double getWeightValue() {
		return weightValue;
	}

	public void setWeightValue(Double weightValue) {
		this.weightValue = weightValue;
	}
}
