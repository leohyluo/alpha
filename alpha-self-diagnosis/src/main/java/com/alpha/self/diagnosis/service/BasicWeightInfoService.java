package com.alpha.self.diagnosis.service;

import java.util.List;
import java.util.Map;

import com.alpha.self.diagnosis.pojo.BasicWeightInfo;

public interface BasicWeightInfoService {

	List<BasicWeightInfo> query(Map<String, Object> param);
}
