package com.alpha.self.diagnosis.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alpha.self.diagnosis.dao.BasicWeightInfoDao;
import com.alpha.self.diagnosis.pojo.BasicWeightInfo;
import com.alpha.self.diagnosis.service.BasicWeightInfoService;

@Service
public class BasicWeightInfoServiceImpl implements BasicWeightInfoService {
	
	@Resource
	private BasicWeightInfoDao dao;

	@Override
	public List<BasicWeightInfo> query(Map<String, Object> param) {
		return dao.query(param);
	}

}
