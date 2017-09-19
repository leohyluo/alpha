package com.alpha.self.diagnosis.service.impl;

import com.alpha.commons.core.sql.DataSet;
import com.alpha.self.diagnosis.dao.DiagnosisMainSymptomsDao;
import com.alpha.self.diagnosis.pojo.enums.QuestionEnum;
import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.self.diagnosis.service.MedicineAnswerService;
import com.alpha.self.diagnosis.service.SymptomMainService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainSymptoms;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/11.
 * 主症状提问操作类
 */
@Service
public class SymptomMainServiceImpl implements SymptomMainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SymptomMainServiceImpl.class);

    @Resource
    private DiagnosisMainSymptomsDao diagnosisMainSymptomsDao;
    @Resource
    private MedicineAnswerService medicineAnswerService;


    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    public BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId, UserInfo userInfo) {
    	LOGGER.info("getMainSymptomsQuestion start with {}", diagnosisId);
        DataSet<DiagnosisMainSymptoms> dmsDateSet = diagnosisMainSymptomsDao.selectLimit(1, 1000);
        List<DiagnosisMainSymptoms> dmses = dmsDateSet.getRows();
        BasicQuestionVo questionVo = new BasicQuestionVo();
        List<IAnswerVo> basicAnswers = new ArrayList<>();
        for (DiagnosisMainSymptoms dms : dmses) {
            BasicAnswerVo basicAnswer = new BasicAnswerVo();
            basicAnswer.setAnswerValue(dms.getSympCode());
            basicAnswer.setAnswerTitle(dms.getSympName());
            basicAnswer.setDefaultOrder(dms.getDefaultOrder());
            basicAnswer.setDescription(dms.getPopuName());
            basicAnswers.add(basicAnswer);
        }
        questionVo.setAnswers(basicAnswers);
        questionVo.setQuestionTitle("【xxx】的基本情况我已经清楚了解，现在告诉我最不舒服的是什么，我将调动全身的每一个细胞进行运算！");
        questionVo.setQuestionCode("9999");
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setType(QuestionEnum.主症状.getValue());
        questionVo.setDisplayType("radio");
        questionVo.setSympCode("");
        // 保存正向反向特异性疾病
        medicineAnswerService.saveDiagnosisAnswer(questionVo);
        return questionVo;
    }
    
    /**
     * 生成主症状问题
     * 获取主症状数据
     *
     * @return
     */
    public BasicQuestionVo getMainSymptomsQuestion(Long diagnosisId, List<DiagnosisMainSymptoms> list) {
    	LOGGER.info("getMainSymptomsQuestion start with {}", diagnosisId);
        
        BasicQuestionVo questionVo = new BasicQuestionVo();
        List<IAnswerVo> basicAnswers = new ArrayList<>();
        for (DiagnosisMainSymptoms dms : list) {
            BasicAnswerVo basicAnswer = new BasicAnswerVo();
            basicAnswer.setAnswerValue(dms.getSympCode());
            basicAnswer.setAnswerTitle(dms.getSympName());
            basicAnswer.setDefaultOrder(dms.getDefaultOrder());
            basicAnswer.setDescription(dms.getPopuName());
            basicAnswers.add(basicAnswer);
        }
        questionVo.setAnswers(basicAnswers);
        questionVo.setQuestionTitle("【xxx】的基本情况我已经清楚了解，现在告诉我最不舒服的是什么，我将调动全身的每一个细胞进行运算！");
        questionVo.setQuestionCode("9999");
        questionVo.setDiagnosisId(diagnosisId);
        questionVo.setType(QuestionEnum.主症状.getValue());
        questionVo.setDisplayType("match_mainSymptoms");
        questionVo.setSympCode("");
        // 保存正向反向特异性疾病
        medicineAnswerService.saveDiagnosisAnswer(questionVo);
        return questionVo;
    }


	@Override
	public List<DiagnosisMainSymptoms> query(Map<String, Object> param) {
		return diagnosisMainSymptomsDao.query(param);
	}
}