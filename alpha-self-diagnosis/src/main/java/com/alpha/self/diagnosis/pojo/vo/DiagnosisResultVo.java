package com.alpha.self.diagnosis.pojo.vo;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiagnosisResultVo {

    /**
     * 第三方用户编号，用来同步第三方用户信息
     */
    private String externalUserId;

    //姓名
    private String userName;

    //性别
    private Integer gender;

    //1岁8个月
    private String age;

    //挂号科室
    private String department;

    //就诊时间
    private String cureTime;

    //主诉
    private String mainSymptomName;

    //现病史
    private String presentIllnessHistory;

    //既往史    
    private PastmedicalHistoryResultVo pastmedicalHistory;

    //诊断结果
    private List<UserDiagnosisOutcomeVo> diseaseList;

    public DiagnosisResultVo() {
    }

    public DiagnosisResultVo(UserInfo userInfo) {
        BeanCopierUtil.copy(userInfo, this);
        if (userInfo.getBirth() != null) {
            this.age = DateUtils.getAgeText(userInfo.getBirth());
        }
        if (userInfo.getCureTime() != null) {
            this.cureTime = DateUtils.date2String(userInfo.getCreateTime(), DateUtils.DATE_FORMAT);
        }
    }

    public DiagnosisResultVo(UserBasicRecord record) {
        BeanCopierUtil.copy(record, this);
        if (record.getBirth() != null) {
            this.age = DateUtils.getAgeText(record.getBirth());
        }
        if (record.getCureTime() != null) {
            this.cureTime = DateUtils.date2String(record.getCreateTime(), DateUtils.DATE_FORMAT);
        }
		this.presentIllnessHistory = StringUtils.trimToEmpty(record.getPresentIllnessHistory())
				.concat(StringUtils.trimToEmpty(record.getPresentIllnessHistoryHospital()));
        this.buildPastIllHistory(record);
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCureTime() {
        return cureTime;
    }

    public void setCureTime(String cureTime) {
        this.cureTime = cureTime;
    }

    public String getMainSymptomName() {
        return mainSymptomName;
    }

    public void setMainSymptomName(String mainSymptomName) {
        this.mainSymptomName = mainSymptomName;
    }

    public String getPresentIllnessHistory() {
        return presentIllnessHistory;
    }

    public void setPresentIllnessHistory(String presentIllnessHistory) {
        this.presentIllnessHistory = presentIllnessHistory;
    }

    public List<UserDiagnosisOutcomeVo> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<UserDiagnosisOutcomeVo> diseaseList) {
        this.diseaseList = diseaseList;
    }

    public PastmedicalHistoryResultVo getPastmedicalHistory() {
        return pastmedicalHistory;
    }

    public void setPastmedicalHistory(PastmedicalHistoryResultVo pastmedicalHistory) {
        this.pastmedicalHistory = pastmedicalHistory;
    }

    public void merge(UserInfo userInfo) {
        this.externalUserId = userInfo.getExternalUserId();
        this.userName = userInfo.getUserName();
        this.gender = userInfo.getGender();
    }

    private void buildPastIllHistory(UserBasicRecord record) {
        PastmedicalHistoryResultVo vo = new PastmedicalHistoryResultVo();
        //手术史
        List<String> operation = new ArrayList<>();
        vo.setOperation(operation);
        //疾病史
        List<String> diseaseHistory = new ArrayList<>();
        if (StringUtils.isNotEmpty(record.getPastmedicalHistoryText())) {
            diseaseHistory = Stream.of(record.getPastmedicalHistoryText().split(",")).collect(Collectors.toList());
        }
        vo.setDiseaseHistory(diseaseHistory);
        //过敏史
        List<String> allergicHistory = new ArrayList<>();
        if (StringUtils.isNotEmpty(record.getAllergicHistoryText())) {
            allergicHistory = Stream.of(record.getAllergicHistoryText().split(",")).collect(Collectors.toList());
        }
        vo.setAllergicHistory(allergicHistory);
        //出生史
        List<String> fertilityType = new ArrayList<>();
        if (StringUtils.isNotEmpty(record.getFertilityType())) {
            fertilityType.add(record.getFertilityType());
        }
        vo.setFertilityType(fertilityType);
        //喂养史
        List<String> feedType = new ArrayList<>();
        if (StringUtils.isNotEmpty(record.getFeedType())) {
            feedType = new ArrayList<>();
            feedType.add(record.getFeedType());
        }
        vo.setFeedType(feedType);
        //月经史
        List<String> specialPeriod = new ArrayList<>();
        if (StringUtils.isNotEmpty(record.getSpecialPeriod())) {
            specialPeriod.add(record.getSpecialPeriod());
        }
        vo.setSpecialPeriod(specialPeriod);
        this.pastmedicalHistory = vo;
    }
}
