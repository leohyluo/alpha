package com.alpha.self.diagnosis.service;

import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.server.rpc.user.pojo.UserInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/11.
 */
public interface DiagnosisDiseaseService {

    /**
     * 查询所有的疾病
     *
     * @param diseaseCodes
     * @return
     */
    Map<String, DiagnosisDisease> mapDiagnosisDisease(Collection diseaseCodes,UserInfo userInfo);

    List<DiseaseVo> findByDiseaseName(String diseaseName);
}
