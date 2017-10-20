package com.alpha.self.diagnosis.service.impl;

import com.alpha.commons.core.dao.DiagnosisDiseaseDao;
import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.commons.core.util.StringUtil;
import com.alpha.commons.util.DateUtils;
import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.service.DiagnosisDiseaseService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisMainsympQuestion;
import com.alpha.server.rpc.user.pojo.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     *
     * @param diseaseCodes
     * @return
     */
    public Map<String, DiagnosisDisease> mapDiagnosisDisease(Collection diseaseCodes, UserInfo userInfo) {
        Map<String, DiagnosisDisease> diagnosisDiseaseMap = new HashMap<>();
        if (diseaseCodes == null || diseaseCodes.size() == 0)
            return diagnosisDiseaseMap;

        Map<String, Object> params = new HashMap<>();
        params.put("diseaseCodes", diseaseCodes);
        List<DiagnosisDisease> dds = diagnosisDiseaseDao.listDiagnosisDisease(params);
        for (DiagnosisDisease dd : dds) {
            if (dd.getGender() != null && dd.getGender() > 0 && dd.getGender() != userInfo.getGender()) {
                continue;//过滤性别
            }
            if (dd.getSpecialPeriod() != null && dd.getSpecialPeriod() > 0 && StringUtils.isNotEmpty(userInfo.getSpecialPeriod()) &&userInfo.getSpecialPeriod().equals(dd.getSpecialPeriod()+"") ) {
                continue;//过滤特殊时期
            }
            float age = DateUtils.getAge(userInfo.getBirth());
            if ((dd.getMinAge() != null && dd.getMinAge() > age) || (dd.getMaxAge() != null && dd.getMaxAge() < age)) {
                continue;//过滤年龄
            }
            diagnosisDiseaseMap.put(dd.getDiseaseCode(), dd);
        }
        return diagnosisDiseaseMap;
    }

    @Override
    public List<DiseaseVo> findByDiseaseName(String diseaseName) {
        Map<String, Object> params = new HashMap<>();
        params.put("diseaseName", diseaseName);
        List<DiagnosisDisease> dds = diagnosisDiseaseDao.listDiagnosisDisease(params);
        return dds.stream().map(DiseaseVo::new).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        String a = "1";
        Integer b = 1;
        System.out.println(a.equalsIgnoreCase(b+""));
    }
}
