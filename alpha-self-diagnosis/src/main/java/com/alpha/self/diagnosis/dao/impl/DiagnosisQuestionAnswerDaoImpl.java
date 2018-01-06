package com.alpha.self.diagnosis.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.DiagnosisQuestionAnswerDao;
import com.alpha.self.diagnosis.pojo.enums.SyAnswerType;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.diagnosis.pojo.vo.MedicineQuestionVo;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 */
@Repository
public class DiagnosisQuestionAnswerDaoImpl extends BaseDao<DiagnosisQuestionAnswer, Long> implements DiagnosisQuestionAnswerDao {


    @Autowired
    public DiagnosisQuestionAnswerDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<DiagnosisQuestionAnswer> getClz() {
        return DiagnosisQuestionAnswer.class;
    }


    /**
     * 根据问题编号查询所有的答案
     *
     * @param questionCodes
     * @return
     */
    public List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(Collection<String> questionCodes) {
        Map<String, Object> params = new HashMap<>();
        params.put("questionCodes", questionCodes);
        params.put("wordsProperty", SyAnswerType.PARENT_ANSWER.getValue());
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer.queryDiagnosisQuestionAnswer", params);
        List<DiagnosisQuestionAnswer> dqAnswers = JavaBeanMap.convertListToJavaBean(datas, DiagnosisQuestionAnswer.class);
        return dqAnswers;
    }

    /**
     * 根据答案编号查询答案
     *
     * @param questionCode
     * @param answerCode
     * @return
     */
    public DiagnosisQuestionAnswer getDiagnosisQuestionAnswer(String questionCode, String answerCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("answerCode", answerCode);
        params.put("questionCode", questionCode);
        DataRecord data = super.selectOne("com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer.getDiagnosisQuestionAnswer", params);
        if (data == null)
            return null;
        DiagnosisQuestionAnswer dqAnswer = new DiagnosisQuestionAnswer();
        JavaBeanMap.convertMapToJavaBean(data, dqAnswer);
        return dqAnswer;
    }

    /**
     * 根据答案编号查询答案
     *
     * @param answerCodes
     * @return
     */
    public List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(String questionCode, Collection<String> answerCodes, Collection<String> hiddenAnswerCodes) {
        Map<String, Object> params = new HashMap<>();
        answerCodes.addAll(hiddenAnswerCodes);
        params.put("questionCode", questionCode);
        params.put("answerCodes", answerCodes);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer.queryByAnswerCodes", params);
        List<DiagnosisQuestionAnswer> dqAnswers = JavaBeanMap.convertListToJavaBean(datas, DiagnosisQuestionAnswer.class);
        return dqAnswers;
    }

    /**
     * 根据答案编号查询答案
     *
     * @param answerCodes
     * @param questionCodes
     * @return
     */
    public List<DiagnosisQuestionAnswer> listDiagnosisQuestionAnswer(Collection<String> questionCodes, Collection<String> answerCodes) {
        Map<String, Object> params = new HashMap<>();
        params.put("questionCodes", questionCodes);
        params.put("answerCodes", answerCodes);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer.queryByQuestionCodesAndAnswerCodes", params);
        List<DiagnosisQuestionAnswer> dqAnswers = JavaBeanMap.convertListToJavaBean(datas, DiagnosisQuestionAnswer.class);
        return dqAnswers;
    }

    /**
     * 根据答案编号查询答案
     *
     * @param answerCodes
     * @param questionCodes
     * @return
     */
    public List<MedicineQuestionVo> listMedicineQuestionVo(Collection<String> questionCodes, Collection<String> answerCodes) {
        Map<String, Object> params = new HashMap<>();
        params.put("questionCodes", questionCodes);
        params.put("answerCodes", answerCodes);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer.queryAnswers", params);
        List<MedicineQuestionVo> mqvAnswers = JavaBeanMap.convertListToJavaBean(datas, MedicineQuestionVo.class);
        return mqvAnswers;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<DiagnosisQuestionAnswer> listHiddenAnswers(String questionCode) {
		Map<String, Object> params = new HashMap<>();
        params.put("questionCode", questionCode);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer.queryHiddenAnswers", params);
        List<DiagnosisQuestionAnswer> dqAnswers = JavaBeanMap.convertListToJavaBean(datas, DiagnosisQuestionAnswer.class);
        return dqAnswers;
	}


}
