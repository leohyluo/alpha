package com.alpha.server.rpc.diagnosis.pojo;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alpha.commons.enums.FeedType;
import com.alpha.commons.enums.FertilityType;
import com.alpha.commons.enums.GestationalAge;
import com.alpha.commons.enums.WomenSpecialPeriod;
import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.StringUtils;
import com.alpha.server.rpc.user.pojo.UserInfo;

@Entity
@Table(name = "user_basic_record")
public class UserBasicRecord {
	/**
	 * id
	 */
	@Id
	private Long id;

	/**
	 * 成员唯一编号，张三为李四导药，这里放李四编号
	 */
	@Column(name = "member_id")
	private Long memberId;

	/**
	 * 用户唯一编号,问诊人
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 诊断号唯一编码
	 */
	@Column(name = "diagnosis_id")
	private Long diagnosisId;

	/**
	 * 出生日期
	 */
	@Column(name = "birth")
	private Date birth;

	/**
	 * 肝功能
	 */
	@Column(name = "liver_func")
	private Integer liverFunc;

	/**
	 * 肝功能
	 */
	@Column(name = "liver_func_text")
	private String liverFuncText;

	/**
	 * 肾功能
	 */
	@Column(name = "renal_func_text")
	private String renalFuncText;

	/**
	 * 肾功能
	 */
	@Column(name = "renal_func")
	private Integer renalFunc;

	/**
	 * 身高
	 */
	@Column(name = "height")
	private String height;

	/**
	 * 体重
	 */
	@Column(name = "weight")
	private String weight;

	/**
	 * 女性特殊时期（月经期、备孕中、妊娠期、哺乳期、无）
	 */
	@Column(name = "special_period")
	private String specialPeriod;

	/**
	 * 顺产/剖宫产/产钳助产
	 */
	@Column(name = "fertility_type")
	private String fertilityType;

	/**
	 * 胎龄（出生的时候怀孕了多少周,范围值）
	 */
	@Column(name = "gestational_age")
	private String gestationalAge;

	/**
	 * 母乳喂养、人工喂养、混合喂养
	 */
	@Column(name = "feed_type")
	private String feedType;

	/**
	 * 既往史编码集合
	 */
	@Column(name = "pastmedical_history_code")
	private String pastmedicalHistoryCode;

	/**
	 * 既往史名称集合
	 */
	@Column(name = "pastmedical_history_text")
	private String pastmedicalHistoryText;

	/**
	 * 过敏史编码集合
	 */
	@Column(name = "allergic_history_code")
	private String allergicHistoryCode;

	/**
	 * 过敏史名称集合
	 */
	@Column(name = "allergic_history_text")
	private String allergicHistoryText;

	/**
	 * 最后更新时间，最后一个回答时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 同步时间（暂时不填）
	 */
	@Column(name = "incre_flag")
	private String increFlag;

	/**
	 * 同步-操作时间
	 */
	@Column(name = "opera_flag")
	private String operaFlag;

	/**
	 * 同步-操作类型（'I'增 ,'U'改,'D'删）
	 */
	@Column(name = "operate_type")
	private String operateType;

	/**
	 * 同步-当前版本
	 */
	@Column(name = "data_version")
	private Integer dataVersion;

	/**
	 * 同步-版本演变
	 */
	@Column(name = "version_evolution")
	private String versionEvolution;

	/**
	 * 来源
	 */
	@Column(name = "source_")
	private String source;

	/**
	 * 版本
	 */
	@Column(name = "version_")
	private String version;

	/**
	 * 创建人
	 */
	@Column(name = "creator")
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 审核人
	 */
	@Column(name = "reviewer")
	private String reviewer;

	/**
	 * 审核时间
	 */
	@Column(name = "review_time")
	private Date reviewTime;
	
	
	public void copyFromUserInfo(UserInfo userInfo) {
		Long tmpId = this.id;
		Long tmpUserId = this.userId;
		BeanCopierUtil.copy(userInfo, this);
		this.id = tmpId;
		this.userId = tmpUserId;
		this.memberId = userInfo.getUserId();
		if(StringUtils.isNotEmpty(this.specialPeriod)) {
			Integer specialPeriod = Integer.parseInt(this.specialPeriod);
			this.specialPeriod = WomenSpecialPeriod.getText(specialPeriod);
		}
		if(StringUtils.isNotEmpty(this.fertilityType)) {
			Integer fertilityType = Integer.parseInt(this.fertilityType);
			this.fertilityType = FertilityType.getText(fertilityType);
		}
		if(StringUtils.isNotEmpty(this.gestationalAge)) {
			Integer gestationalAge = Integer.parseInt(this.gestationalAge);
			this.gestationalAge = GestationalAge.getText(gestationalAge);
		}
		if(StringUtils.isNotEmpty(this.feedType)) {
			Integer feedType = Integer.parseInt(this.feedType);
			this.feedType = FeedType.getText(feedType);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Integer getLiverFunc() {
		return liverFunc;
	}

	public void setLiverFunc(Integer liverFunc) {
		this.liverFunc = liverFunc;
	}

	public String getLiverFuncText() {
		return liverFuncText;
	}

	public void setLiverFuncText(String liverFuncText) {
		this.liverFuncText = liverFuncText == null ? null : liverFuncText.trim();
	}

	public String getRenalFuncText() {
		return renalFuncText;
	}

	public void setRenalFuncText(String renalFuncText) {
		this.renalFuncText = renalFuncText == null ? null : renalFuncText.trim();
	}

	public Integer getRenalFunc() {
		return renalFunc;
	}

	public void setRenalFunc(Integer renalFunc) {
		this.renalFunc = renalFunc;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height == null ? null : height.trim();
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight == null ? null : weight.trim();
	}

	public String getSpecialPeriod() {
		return specialPeriod;
	}

	public void setSpecialPeriod(String specialPeriod) {
		this.specialPeriod = specialPeriod == null ? null : specialPeriod.trim();
	}

	public String getFertilityType() {
		return fertilityType;
	}

	public void setFertilityType(String fertilityType) {
		this.fertilityType = fertilityType == null ? null : fertilityType.trim();
	}

	public String getGestationalAge() {
		return gestationalAge;
	}

	public void setGestationalAge(String gestationalAge) {
		this.gestationalAge = gestationalAge == null ? null : gestationalAge.trim();
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType == null ? null : feedType.trim();
	}

	public String getPastmedicalHistoryCode() {
		return pastmedicalHistoryCode;
	}

	public void setPastmedicalHistoryCode(String pastmedicalHistoryCode) {
		this.pastmedicalHistoryCode = pastmedicalHistoryCode == null ? null : pastmedicalHistoryCode.trim();
	}

	public String getPastmedicalHistoryText() {
		return pastmedicalHistoryText;
	}

	public void setPastmedicalHistoryText(String pastmedicalHistoryText) {
		this.pastmedicalHistoryText = pastmedicalHistoryText == null ? null : pastmedicalHistoryText.trim();
	}

	public String getAllergicHistoryCode() {
		return allergicHistoryCode;
	}

	public void setAllergicHistoryCode(String allergicHistoryCode) {
		this.allergicHistoryCode = allergicHistoryCode == null ? null : allergicHistoryCode.trim();
	}

	public String getAllergicHistoryText() {
		return allergicHistoryText;
	}

	public void setAllergicHistoryText(String allergicHistoryText) {
		this.allergicHistoryText = allergicHistoryText == null ? null : allergicHistoryText.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIncreFlag() {
		return increFlag;
	}

	public void setIncreFlag(String increFlag) {
		this.increFlag = increFlag == null ? null : increFlag.trim();
	}

	public String getOperaFlag() {
		return operaFlag;
	}

	public void setOperaFlag(String operaFlag) {
		this.operaFlag = operaFlag == null ? null : operaFlag.trim();
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType == null ? null : operateType.trim();
	}

	public Integer getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Integer dataVersion) {
		this.dataVersion = dataVersion;
	}

	public String getVersionEvolution() {
		return versionEvolution;
	}

	public void setVersionEvolution(String versionEvolution) {
		this.versionEvolution = versionEvolution == null ? null : versionEvolution.trim();
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source == null ? null : source.trim();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version == null ? null : version.trim();
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer == null ? null : reviewer.trim();
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

}
