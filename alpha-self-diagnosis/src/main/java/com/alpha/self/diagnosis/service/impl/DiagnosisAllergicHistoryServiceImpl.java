package com.alpha.self.diagnosis.service.impl;

import com.alpha.self.diagnosis.dao.DiagnosisAllergicHistoryDao;
import com.alpha.self.diagnosis.pojo.vo.DiseaseVo;
import com.alpha.self.diagnosis.service.DiagnosisAllergicHistoryService;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisAllergicHistory;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisSuballergicHistory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DiagnosisAllergicHistoryServiceImpl implements DiagnosisAllergicHistoryService {

    @Resource
    private DiagnosisAllergicHistoryDao dao;

    @Override
    public List<DiagnosisAllergicHistory> queryAllergicHistory(Map<String, Object> param) {
        return dao.queryAllergicHistory(param);
    }

    @Override
    public List<DiagnosisSuballergicHistory> querySubAllergicHistory(Map<String, Object> param) {
        // TODO Auto-generated method stub
        return dao.querySubAllergicHistory(param);
    }

    @Override
    public List<DiseaseVo> querySelectedAllergicHistory(Map<String, Object> param) {
        // TODO Auto-generated method stub
        return dao.querySelectedAllergicHistory(param);
    }

    @Override
    public void updateUserSelectCount(Map<String, Object> param) {
        dao.updateUserSelectCount(param);
    }


}
