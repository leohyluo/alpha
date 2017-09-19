package com.alpha.self.diagnosis.pojo.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.alpha.self.diagnosis.pojo.BasicAnswer;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisPastmedicalHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSubpastmedicalHistory;
import com.alpha.server.rpc.user.pojo.UserMember;

public class BasicAnswerVo implements IAnswerVo {

	

	/**
	 * 答案值
	 */
	private String answerValue;

	/**
     * 问题内容
     */
	private String answerTitle;

	/**
	 * 描述
	 */
	private String description ;
	/**
	 * 序号
	 */
	private Integer defaultOrder;

	public BasicAnswerVo(BasicAnswer basicAnswer) {
		BeanUtils.copyProperties(basicAnswer, this);
	}
	public BasicAnswerVo() {

	}
	
	public BasicAnswerVo(UserMember userMember) {
		this.answerTitle = userMember.getMemberName();
		this.answerValue = userMember.getUserId()+"";
//		this.userId = userMember.getUserId().toString();
	}

	public BasicAnswerVo(DiagnosisPastmedicalHistory disease) {
		this.answerValue = disease.getDiseaseCode();
		this.answerTitle = disease.getDiseaseName();
		this.description = disease.getDescription();
	}

	public BasicAnswerVo(DiagnosisSubpastmedicalHistory disease) {
		this.answerValue = disease.getDiseaseCode();
		this.answerTitle = disease.getDiseaseName();
		this.description = disease.getDescription();
	}

	public BasicAnswerVo(DiagnosisAllergicHistory disease) {
		this.answerValue = disease.getDiseaseCode();
		this.answerTitle = disease.getDiseaseName();
		this.description = disease.getDescription();
	}

	public BasicAnswerVo(DiagnosisSuballergicHistory disease) {
		this.answerValue = disease.getDiseaseCode();
		this.answerTitle = disease.getDiseaseName();
		this.description = disease.getDescription();
	}


	public String getAnswerTitle() {
		return answerTitle;
	}

	public void setAnswerTitle(String answerTitle) {
		this.answerTitle = answerTitle;
	}

//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	public String getAnswerValue() {
		return answerValue;
	}
	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}


	/**
	 * 医学答案转换
	 * @param dqAnswers
	 * @return
	 */
	public static List<IAnswerVo> convertListMedicineAnswerVo(Collection<DiagnosisQuestionAnswer > dqAnswers){
		List<IAnswerVo> answerVos = new ArrayList<>();
		for(DiagnosisQuestionAnswer dqAnswer:dqAnswers){
			BasicAnswerVo answerVo = new BasicAnswerVo(dqAnswer);
			answerVo.setDefaultOrder(dqAnswer.getDefaultOrder());
			answerVos.add(answerVo);
		}
		return answerVos;
	}

	/**
	 * 医学答案转换
	 * @param dqAnswer
	 */
	public BasicAnswerVo(DiagnosisQuestionAnswer dqAnswer ){
		this.answerTitle = dqAnswer.getContent();
		this.description = dqAnswer.getPopuContent();
		this.answerValue = dqAnswer.getAnswerCode();
		this.defaultOrder = dqAnswer.getDefaultOrder();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BasicAnswerVo)) return false;

		BasicAnswerVo answerVo = (BasicAnswerVo) o;

		return answerValue.equals(answerVo.answerValue);
	}

	@Override
	public int hashCode() {
		int result = answerValue.hashCode();
		return result;
	}
}
