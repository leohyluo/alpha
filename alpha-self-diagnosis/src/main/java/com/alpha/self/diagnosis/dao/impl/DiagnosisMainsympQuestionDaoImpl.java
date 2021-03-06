package com.alpha.self.diagnosis.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.self.diagnosis.dao.DiagnosisMainsympQuestionDao;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 */
@Repository
public class DiagnosisMainsympQuestionDaoImpl extends BaseDao<DiagnosisMainsympQuestion, Long> implements DiagnosisMainsympQuestionDao {


    @Autowired
    public DiagnosisMainsympQuestionDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<DiagnosisMainsympQuestion> getClz() {
        return DiagnosisMainsympQuestion.class;
    }

    /**
     * 查询主症状下的所有问题
     *
     * @param mainSympCode
     * @return
     */
    public List<DiagnosisMainsympQuestion> listDiagnosisMainsympQuestion(String mainSympCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("mainSympCode", mainSympCode);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.queryDiagnosisMainsympQuestion", params);
        List<DiagnosisMainsympQuestion> dmQuestions = new ArrayList<>();
        dmQuestions = JavaBeanMap.convertListToJavaBean(datas, DiagnosisMainsympQuestion.class);
        return dmQuestions;
    }

    /**
     * 查询主症状的下一个问题
     *
     * @param mainSympCode
     * @return
     */
    public DiagnosisMainsympQuestion getNextQuestion(String mainSympCode, int defaultOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("mainSympCode", mainSympCode);
        params.put("defaultOrder", defaultOrder);
        DiagnosisMainsympQuestion dmQuestions = super.selectOne("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.getDiagnosisMainsympQuestion", params);
        return dmQuestions;
    }

    /**
     * 查询主症状的未回答问题
     *
     * @param mainSympCode
     * @return
     */
    public List<DiagnosisMainsympQuestion> listNextAllQuestion(String mainSympCode, int defaultOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("mainSympCode", mainSympCode);
        params.put("defaultOrder", defaultOrder);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.queryUntreatedQuestion", params);
        List<DiagnosisMainsympQuestion> dmQuestions = new ArrayList<>();
        dmQuestions = JavaBeanMap.convertListToJavaBean(datas, DiagnosisMainsympQuestion.class);
        return dmQuestions;
    }

    /**
     * 查询主症状的自动计算问题
     *
     * @param mainSympCode
     * @return
     */
    public List<DiagnosisMainsympQuestion> listAutoQuestion(String mainSympCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("mainSympCode", mainSympCode);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.queryAutoQuestion", params);
        List<DiagnosisMainsympQuestion> dmQuestions = new ArrayList<>();
        dmQuestions = JavaBeanMap.convertListToJavaBean(datas, DiagnosisMainsympQuestion.class);
        return dmQuestions;
    }

    /**
     * 查询主症状下所有疾病
     *
     * @param mainSympCode
     * @return
     */
    public List<DiagnosisMainsympQuestion> listDiseaseQuestion(String mainSympCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("mainSympCode", mainSympCode);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.queryDiseaseQuestion", params);
        List<DiagnosisMainsympQuestion> dmQuestions = new ArrayList<>();
        dmQuestions = JavaBeanMap.convertListToJavaBean(datas, DiagnosisMainsympQuestion.class);
        return dmQuestions;
    }

    /**
     * 查询主症状下疾病下的所有问题，并计算答案数量
     *
     * @param mainSympCode
     * @return
     */
    public List<DiagnosisMainsympQuestion> listAnswerCount(String mainSympCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("mainSympCode", mainSympCode);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.queryAnswerCount", params);
        List<DiagnosisMainsympQuestion> dmQuestions = new ArrayList<>();
        dmQuestions = JavaBeanMap.convertListToJavaBean(datas, DiagnosisMainsympQuestion.class);
        return dmQuestions;
    }

    @Override
	public List<DiagnosisMainsympQuestion> listConcSymptomCount(String mainSympCode) {
    	Map<String, Object> params = new HashMap<>();
        params.put("mainSympCode", mainSympCode);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.queryConcSymptomCount", params);
        List<DiagnosisMainsympQuestion> dmQuestions = new ArrayList<>();
        dmQuestions = JavaBeanMap.convertListToJavaBean(datas, DiagnosisMainsympQuestion.class);
        return dmQuestions;
	}
    

    /**
     * 查询主症状的单个问题
     *
     * @param questionCode
     * @return
     */
    @Override
    public DiagnosisMainsympQuestion getDiagnosisMainsympQuestion(String questionCode, String mainSympCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("questionCode", questionCode);
        params.put("mainSympCode", mainSympCode);
        DataRecord dataRecord = super.selectOne("com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion.getQuestionByCode", params);
        if (dataRecord == null)
            return null;
        DiagnosisMainsympQuestion dmQuestions = new DiagnosisMainsympQuestion();
        JavaBeanMap.convertMapToJavaBean(dataRecord, dmQuestions);
        return dmQuestions;
    }

}
