package com.alpha.self.diagnosis.dao;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.diagnosis.pojo.vo.MedicineQuestionVo;

import java.util.Collection;
import java.util.List;

/**
 * Created by xc.xiong on 2017/9/5.
 */
public interface DiagnosisQuestionAnswerDao extends IBaseDao<DiagnosisQuestionAnswer, Long> {

    /**
     * 根据问题编号查询所有的答案
     *
     * @param questionCode
     * @return
     */
    List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String questionCode);


    /**
     * 根据答案编号查询答案
     *
     * @param questionCode
     * @param answerCode
     * @return
     */
    DiagnosisQuestionAnswer getDiagnosisQuestionAnswer(String questionCode, String answerCode);

    /**
     * 根据答案编号查询答案
     *
     * @param answerCodes
     * @return
     */
    List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String questionCode, Collection<String> answerCodes);

    /**
     * 根据答案编号查询答案
     *
     * @param questionCodes
     * @param answerCodes
     * @return
     */
     List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(Collection<String> questionCodes, Collection<String> answerCodes);

    /**
     * 根据答案编号查询答案
     *
     * @param answerCodes
     * @param questionCodes
     * @return
     */
    List<MedicineQuestionVo> listMedicineQuestionVo(Collection<String> questionCodes, Collection<String> answerCodes);
}
