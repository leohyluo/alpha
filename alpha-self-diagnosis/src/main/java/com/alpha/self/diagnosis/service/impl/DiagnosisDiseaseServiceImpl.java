package com.alpha.self.diagnosis.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alpha.commons.core.pojo.DiagnosisDisease;
import com.alpha.commons.util.DateUtils;
import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.pojo.vo.TreatAdviceVo;
import com.alpha.self.diagnosis.service.DiagnosisDiseaseService;
import com.alpha.server.rpc.user.pojo.UserInfo;
import com.alpha.treatscheme.dao.DiagnosisDiseaseDao;

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
    public Boolean filterByUserInfo(String diseaseCode, UserInfo userInfo) {
        DiagnosisDisease disease = diagnosisDiseaseDao.getDiagnosisDisease(diseaseCode);
        if(disease == null) {
            return false;
        }
        //过滤性别
        if (disease.getGender() != null && disease.getGender() > 0 && disease.getGender() != userInfo.getGender()) {
           return false;
        }
        //过滤特殊时期
        if (disease.getSpecialPeriod() != null && disease.getSpecialPeriod() > 0 && StringUtils.isNotEmpty(userInfo.getSpecialPeriod()) &&userInfo.getSpecialPeriod().equals(disease.getSpecialPeriod()+"") ) {
            return false;
        }
        //过滤年龄
        float age = DateUtils.getAge(userInfo.getBirth());
        if ((disease.getMinAge() != null && disease.getMinAge() > age) || (disease.getMaxAge() != null && disease.getMaxAge() < age)) {
            return false;
        }
        return true;
    }

    @Override
    public List<DiseaseVo> findByDiseaseName(String diseaseName) {
        Map<String, Object> params = new HashMap<>();
        params.put("diseaseName", diseaseName);
        List<DiagnosisDisease> dds = diagnosisDiseaseDao.listDiagnosisDisease(params);
        //疾病名称重复的数据标注儿童版、成人版
        Map<String, Long> map = dds.stream().collect(Collectors.groupingBy(DiagnosisDisease::getDiseaseName, Collectors.counting()));
        Consumer<DiagnosisDisease> consumer = (e)-> {
        	String name = e.getDiseaseName();
        	if(map.get(name).longValue() > 1) {
        		Double minAge = e.getMinAge();
        		Double maxAge = e.getMaxAge();
        		if(minAge >= 0 && maxAge <= 18) {
        			name = name.concat("(儿童版)");
        		} else if(minAge >= 0 && maxAge <= 150) {
        			name = name.concat("(成人版)");
        		} else {
        			name = name.concat("(版本未明)");
        		}
        		e.setDiseaseName(name);
        	}
        };
        dds = dds.stream().peek(consumer).collect(Collectors.toList());
        List<DiseaseVo> diseasevoList = dds.stream().map(DiseaseVo::new).collect(Collectors.toList());
        return diseasevoList;
    }

	@Override
	public TreatAdviceVo queryTreatAdvice(String diseaseCode) {
		DiagnosisDisease disease = diagnosisDiseaseDao.getDiagnosisDisease(diseaseCode);
		TreatAdviceVo treatAdvice = null;
		if(disease != null) {
			//更新用户选择次数
			Integer userSelectCount = disease.getUserSelectCount() == null ? 0 : disease.getUserSelectCount();
			userSelectCount++;
			disease.setUserSelectCount(userSelectCount);
			diagnosisDiseaseDao.update(disease);
			
			treatAdvice = new TreatAdviceVo(disease);
		}
		return treatAdvice;
	}
}
