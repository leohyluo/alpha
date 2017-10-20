package com.alpha.treatscheme.service.impl;

import com.alpha.commons.core.dao.DiagnosisDiseaseDao;
import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.treatscheme.dao.DiagnosisDiseaseCheckDao;
import com.alpha.treatscheme.dao.DiagnosisDiseasePhysicalexamDao;
import com.alpha.treatscheme.dao.DiagnosisDiseaseTreatoptionsDao;
import com.alpha.treatscheme.pojo.DiagnosisDiseaseCheck;
import com.alpha.treatscheme.pojo.DiagnosisDiseasePhysicalexam;
import com.alpha.treatscheme.pojo.DiagnosisDiseaseTreatoptions;
import com.alpha.treatscheme.pojo.vo.TreatSchemeVo;
import com.alpha.treatscheme.service.TreatSchemeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TreatSchemeServiceImpl implements TreatSchemeService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiagnosisDiseaseCheckDao diagnosisDiseaseCheckDao;

    @Autowired
    private DiagnosisDiseasePhysicalexamDao diagnosisDiseasePhysicalexamDao;

    @Autowired
    private DiagnosisDiseaseTreatoptionsDao diagnosisDiseaseTreatoptionsDao;

    @Autowired
    private DiagnosisDiseaseDao diagnosisDiseaseDao;


    /**
     * 获取治疗方案
     *
     * @param diseaseCode
     */
    public TreatSchemeVo getTreatScheme(String diseaseCode) {
        if (StringUtils.isEmpty(diseaseCode)) {
            return null;
        }
        DiagnosisDisease disease = diagnosisDiseaseDao.getDiagnosisDisease(diseaseCode);
        if (disease == null) {
            return null;
        }
        DiagnosisDiseaseTreatoptions diagnosisDiseaseTreatoptions = diagnosisDiseaseTreatoptionsDao.getDiagnosisDiseaseTreatoptions(diseaseCode);
        if (diagnosisDiseaseTreatoptions == null) {
            return null;
        }
        List<DiagnosisDiseaseCheck> diagnosisDiseaseChecks = diagnosisDiseaseCheckDao.listDiagnosisDiseaseCheck(diseaseCode);
        List<DiagnosisDiseasePhysicalexam> diagnosisDiseasePhysicalexams = diagnosisDiseasePhysicalexamDao.listDiagnosisDiseasePhysicalexam(diseaseCode);
        return packgeTreatScheme(disease, diagnosisDiseaseTreatoptions, diagnosisDiseaseChecks, diagnosisDiseasePhysicalexams);

    }

    /**
     * 获取治疗方案相关内容
     *
     * @param disease
     * @param diagnosisDiseaseTreatoptions
     * @param diagnosisDiseaseChecks
     * @param diagnosisDiseasePhysicalexams
     */
    public TreatSchemeVo packgeTreatScheme(DiagnosisDisease disease, DiagnosisDiseaseTreatoptions diagnosisDiseaseTreatoptions,
                                           List<DiagnosisDiseaseCheck> diagnosisDiseaseChecks, List<DiagnosisDiseasePhysicalexam> diagnosisDiseasePhysicalexams) {
        TreatSchemeVo treatSchemeVo = new TreatSchemeVo();
        treatSchemeVo.setDiseaseCode(disease.getDiseaseCode());
        treatSchemeVo.setDiseaseName(disease.getDiseaseName());
        treatSchemeVo.setDiseaseDefinition(disease.getDefinition());
        treatSchemeVo.setSchemeCode(diagnosisDiseaseTreatoptions.getOptionCode());
        treatSchemeVo.setSchemeName(diagnosisDiseaseTreatoptions.getOptionName());
        treatSchemeVo.setSchemeContent(diagnosisDiseaseTreatoptions.getOptionContent());
        List<TreatSchemeVo.Check> checks = new ArrayList<>();
        List<TreatSchemeVo.Physicalexam> physicalexams = new ArrayList<>();
        for (DiagnosisDiseaseCheck ddc : diagnosisDiseaseChecks) {
            TreatSchemeVo.Check check = new TreatSchemeVo.Check();
            check.setCheckCode(ddc.getCheckCode());
            check.setCheckName(ddc.getCheckName());
            checks.add(check);
        }
        for (DiagnosisDiseasePhysicalexam ddp : diagnosisDiseasePhysicalexams) {
            TreatSchemeVo.Physicalexam physicalexam = new TreatSchemeVo.Physicalexam();
            physicalexam.setExamCode(ddp.getExamCode());
            physicalexam.setExamName(ddp.getExamName());
            physicalexams.add(physicalexam);
        }
        treatSchemeVo.setChecks(checks);
        treatSchemeVo.setPhysicalexams(physicalexams);
        return treatSchemeVo;
    }

}
