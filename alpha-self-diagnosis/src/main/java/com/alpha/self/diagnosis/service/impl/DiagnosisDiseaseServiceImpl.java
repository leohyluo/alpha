package com.alpha.self.diagnosis.service.impl;

import com.alpha.self.diagnosis.dao.DiagnosisDiseaseDao;
import com.alpha.self.diagnosis.service.DiagnosisDiseaseService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisDisease;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/11.
 * 主症状提问操作类
 */
@Service
public class DiagnosisDiseaseServiceImpl implements DiagnosisDiseaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosisDiseaseServiceImpl.class);

    @Resource
    private DiagnosisDiseaseDao diagnosisDiseaseDao;

    /**
     * 查询所有的疾病
     * @param diseaseCodes
     * @return
     */
    public Map<String,DiagnosisDisease> mapDiagnosisDisease(Collection diseaseCodes) {
        Map<String, DiagnosisDisease> diagnosisDiseaseMap = new HashMap<>();
        if(diseaseCodes==null||diseaseCodes.size()==0)
            return diagnosisDiseaseMap;
        List<DiagnosisDisease> dds = diagnosisDiseaseDao.listDiagnosisDisease(diseaseCodes);
        for(DiagnosisDisease dd:dds){
            diagnosisDiseaseMap.put(dd.getDiseaseCode(), dd);
        }
        return diagnosisDiseaseMap;
    }

}
