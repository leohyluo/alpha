package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.Level1AnswerVo;
import com.alpha.self.diagnosis.pojo.vo.QuestionRequestVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.diagnosis.pojo.SyDiagnosisAnswer;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.*;

/**
 * Created by xc.xiong on 2017/9/11.
 */
public interface MedicineAnswerService {
    /**
     * 记录用户的答案
     *
     * @param questionVo
     */
    void updateDiagnosisAnswer(QuestionRequestVo questionVo, UserInfo userInfo);

    /**
     * 保存问题答案
     *
     * @param questionVo
     */
    void saveDiagnosisAnswer(BasicQuestionVo questionVo, UserInfo userInfo);

    /**
     * 获取所有的答案，并过来年龄，性别
     *
     * @param questionCode
     * @param userInfo
     * @return
     */
    List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String mainSympCode, String questionCode, UserInfo userInfo);

    /**
     * 获取所有的答案，并过滤年龄，性别
     *
     * @param questionCodes
     * @param userInfo
     * @return
     */
    List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String mainSympCode, Collection<String> questionCodes, UserInfo userInfo);

    /**
     * 根据答案查询所有的答案，计算特异性
     */
    Map<Integer, Set<String>> mapAnswerSpec(String mainSympCode, String questionCode, Collection<String> answerCodes, Collection<String> hiddenAnswerCodes);

    /**
     * 查询正向特异性的疾病编码
     *
     * @param diagnosisId
     * @return
     */
    Set<String> listSpecCode(Long diagnosisId);

    /**
     * 查询正向特异性的答案
     *
     * @param dqAnswers
     * @param diagnosisId
     * @return
     */
    LinkedHashSet<IAnswerVo> getSpecAnswer(List<DiagnosisQuestionAnswer> dqAnswers, Long diagnosisId);

    /**
     * 所有疾病下的所有答案
     *
     * @return
     */
    Map<String, List<DiagnosisQuestionAnswer>> mapAnswers(Collection<String> questionCodes, Collection<String> answerCodes);
    
    /**
	 * 根据答案大类编码找小类答案或同义词等
	 * @param connCode
	 * @param userInfo
	 * @return
	 */
	List<SyDiagnosisAnswer> listSyDiagnosisAnswer(String connCode, UserInfo userInfo);

	/**
	 * 建立答案大小类关系
	 * @param answerList
	 * @return
	 */
	LinkedHashSet<IAnswerVo> mapAnswerLevel(List<IAnswerVo> answerList);
}
