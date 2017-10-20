package com.alpha.self.diagnosis.service;

import com.alpha.self.diagnosis.pojo.Synonym;

import java.util.List;
import java.util.Map;

public interface SynonymService {

    /**
     * 根据短语查询同义词
     *
     * @param param
     * @return
     */
    List<Synonym> query(Map<String, Object> param);
}
