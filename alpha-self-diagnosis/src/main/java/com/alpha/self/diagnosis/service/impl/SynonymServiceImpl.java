package com.alpha.self.diagnosis.service.impl;

import com.alpha.self.diagnosis.dao.SynonymDao;
import com.alpha.self.diagnosis.pojo.Synonym;
import com.alpha.self.diagnosis.service.SynonymService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SynonymServiceImpl implements SynonymService {

    @Resource
    private SynonymDao synonyDao;

    @Override
    public List<Synonym> query(Map<String, Object> param) {
        return synonyDao.query(param);
    }

}
