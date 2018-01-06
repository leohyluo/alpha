package com.alpha.self.diagnosis.pojo.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import com.alpha.commons.enums.VaccinationHistory;
import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.server.rpc.user.pojo.UserInfo;

public class DiagnosisResultVo {
	
	private Long diagnosisId;

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
    
    //体重40kg
    private String weight;

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

    //经过拼装的既往史
    private String pastmedicalHistoryText;
    
    //诊断结果
    private List<UserDiagnosisOutcomeVo> diseaseList;
    
    //联系电话
    private String phoneNumber;

    public DiagnosisResultVo() {
    }

    /*public DiagnosisResultVo(UserInfo userInfo) {
        BeanCopierUtil.copy(userInfo, this);
        if (userInfo.getBirth() != null) {
            this.age = DateUtils.getAgeText(userInfo.getBirth());
        }
        if (userInfo.getCureTime() != null) {
            this.cureTime = DateUtils.date2String(userInfo.getCreateTime(), DateUtils.DATE_FORMAT);
        }
    }*/

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
        this.toPastIllHistoryString(record);
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
        this.weight = userInfo.getWeight() + "kg";
        this.phoneNumber = userInfo.getPhoneNumber();
    }
        
    public String getPastmedicalHistoryText() {
		return pastmedicalHistoryText;
	}

	public void setPastmedicalHistoryText(String pastmedicalHistoryText) {
		this.pastmedicalHistoryText = pastmedicalHistoryText;
	}

	public Long getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	private void buildPastIllHistory(UserBasicRecord record) {
        PastmedicalHistoryResultVo vo = new PastmedicalHistoryResultVo();
        //手术史
        List<String> operation = new ArrayList<>();
        if(StringUtils.isNotEmpty(record.getOperationText())) {
        	operation = Stream.of(record.getOperationText().split(",")).collect(Collectors.toList());
        }
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
    
    private void toPastIllHistoryString(UserBasicRecord record) {
    	StringBuffer sb = new StringBuffer();
    	if(StringUtils.isEmpty(record.getPastmedicalHistoryCode()) || "0".equals(record.getPastmedicalHistoryCode()) || "-1".equals(record.getPastmedicalHistoryCode())) {
    		sb.append("既往体健。");
    	} else {
    		sb.append("患者有").append(record.getPastmedicalHistoryText()).append("。");
    	}
    	if(StringUtils.isEmpty(record.getOperationCode()) || "0".equals(record.getOperationCode())) {
    		sb.append("手术史不详。");
    	} else if(StringUtils.isNotEmpty(record.getOperationCode()) && "-1".equals(record.getOperationCode())) {
    		sb.append("无手术史。");
    	} else {
    		sb.append("有手术史。");
    	}
    	if(StringUtils.isEmpty(record.getAllergicHistoryCode()) || "0".equals(record.getAllergicHistoryCode())) {
    		sb.append("药物食物过敏不详。");
    	} else if(StringUtils.isNotEmpty(record.getAllergicHistoryCode()) && "-1".equals(record.getAllergicHistoryCode())) {
    		sb.append("无药物食物过敏。");
    	} else {
    		sb.append("患者对").append(record.getAllergicHistoryText()).append("过敏。");
    	}
    	String vaccinationHistoryText = record.getVaccinationHistoryText();
    	if(StringUtils.isEmpty(vaccinationHistoryText)) {
    		sb.append("无预防接种史");
    	} else if(VaccinationHistory.INTIME.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史按时完成。");
    	} else if(VaccinationHistory.UNTIMELY.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史未按时完成。");
    	} else if(VaccinationHistory.UNDO.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史未进行。");
    	} else if(VaccinationHistory.UNKNOWN.getText().equals(vaccinationHistoryText)) {
    		sb.append("预防接种史不详。");
    	}
    	if(StringUtils.isNotEmpty(record.getFertilityType()) || StringUtils.isNotEmpty(record.getGestationalAge()) || StringUtils.isNotEmpty(record.getFeedType())) {
    		sb.append("患儿为");
    		if(StringUtils.isNotEmpty(record.getGestationalAge())) {
    			sb.append(record.getGestationalAge());
    		}
    		if(StringUtils.isNotEmpty(record.getFertilityType())) {
    			sb.append(record.getFertilityType());
    		}
    		if(StringUtils.isNotEmpty(record.getFeedType())) {
    			sb.append("出生后").append(record.getFeedType()).append("。");
    		}
    	}
    	if("1".equals(record.getMenstrualPeriod())) {
    		sb.append("目前处于月经期内。");
    	}
    	this.pastmedicalHistoryText = sb.toString();
    }
}
