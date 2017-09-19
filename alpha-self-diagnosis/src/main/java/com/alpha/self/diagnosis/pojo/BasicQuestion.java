package com.alpha.self.diagnosis.pojo;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.commons.util.CollectionUtils;
import com.alpha.commons.util.DateUtils;
import com.alpha.commons.util.StringUtils;
import com.alpha.self.diagnosis.pojo.vo.AnswerRequestVo;
import com.alpha.server.rpc.user.pojo.UserInfo;

public class BasicQuestion implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 问题内容
     */
    private String title;

    /**
     * 问题编码
     */
    private String questionCode;

    /**
     * 问题类型
     */
    private String displayType;

    /**
     * 问题序号
     */
    private Integer defaultOrder;

    /**
     * 所属入口类型
     */
    private Integer inType;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 最小年龄
     */
    private Integer minAge;

    /**
     * 最大年龄
     */
    private Integer maxAge;

    /**
     * 是否多选
     */
    private Boolean isMulti;

    private static final long serialVersionUID = 1L;

    public BasicQuestion() {}
    
    public BasicQuestion(Integer defaultOrder, String questionCode, Integer gender, Integer minAge, Integer maxAge) {
    	this.defaultOrder = defaultOrder;
    	this.questionCode = questionCode;
    	this.gender = gender;
    	this.minAge = minAge;
    	this.maxAge = maxAge;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode == null ? null : questionCode.trim();
    }


    public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

    public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public Integer getInType() {
        return inType;
    }

    public void setInType(Integer inType) {
        this.inType = inType;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Boolean getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Boolean isMulti) {
        this.isMulti = isMulti;
    }

    public void merge(UserInfo userInfo, List<AnswerRequestVo> answerList) throws ParseException {
    	AnswerRequestVo answer = CollectionUtils.isNotEmpty(answerList) ? answerList.get(0) : null;
    	if(answer == null) {
    		return;
    	}
    	String content = answer.getContent();
    	BasicQuestionType questionType = BasicQuestionType.findByValue(this.questionCode);
    	if(questionType == null || StringUtils.isEmpty(content)) {
    		return;
    	}
    	if(questionType == BasicQuestionType.BORN) {
    		Date birth = DateUtils.string2Date(content);
    		userInfo.setBirth(birth);
    	}
    	if(questionType == BasicQuestionType.BOY_OR_GIRL || questionType == BasicQuestionType.MAN_OR_WOMAN) {
    		userInfo.setGender(Integer.parseInt(content));
    	}
    	if(questionType == BasicQuestionType.MENSTRUAL_PERIOD || questionType == BasicQuestionType.SPECIAL_PERIOD) {
    		userInfo.setSpecialPeriod(content);
    	}
    	if(questionType == BasicQuestionType.PAST_MEDICAL_HISTORY) {
    		String pastmedicalHistoryCode = answerList.stream().map(AnswerRequestVo::getContent).collect(Collectors.joining(","));
    		userInfo.setPastmedicalHistoryCode(pastmedicalHistoryCode);
    	}
    	if(questionType == BasicQuestionType.ALLERGIC_HISTORY) {
    		String allergicHistoryCode = answerList.stream().map(AnswerRequestVo::getContent).collect(Collectors.joining(","));
    		userInfo.setAllergicHistoryCode(allergicHistoryCode);
    	}
    	if(questionType == BasicQuestionType.FERTILITY_TYPE) {
    		userInfo.setFertilityType(content);
    	}
    	if(questionType == BasicQuestionType.GESTATIONAL_AGE) {
    		userInfo.setGestationalAge(content);
    	}
    	if(questionType == BasicQuestionType.FEED_TYPE) {
    		userInfo.setFeedType(content);
    	}
    	if(questionType == BasicQuestionType.HEIGHT) {
    		userInfo.setHeight(content);
    	}
    	if(questionType == BasicQuestionType.WEIGHT) {
    		userInfo.setWeight(content);
    	}
    	if(questionType == BasicQuestionType.LIVER_RENAL) {
    		try {
    			if(answerList.size() == 2) {
    				AnswerRequestVo liver = answerList.get(0);
    				AnswerRequestVo renal = answerList.get(1);
    				userInfo.setLiverFunc(Integer.parseInt(liver.getContent()));
    				userInfo.setRenalFunc(Integer.parseInt(renal.getContent()));
    			}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
}