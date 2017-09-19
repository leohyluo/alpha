package com.alpha.user.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alpha.commons.mapper.UserTokenMapper;
import com.alpha.commons.pojo.UserToken;
import com.alpha.server.rpc.user.SelfDiagnosisFeign;
import com.alpha.user.mapper.UserMapper;
import com.alpha.user.pojo.vo.UserVo;
import com.alpha.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private SelfDiagnosisFeign selfDiagnosisFeign;
	
	@Resource
	private UserMapper userMapper;
	
	@Resource
	private UserTokenMapper userTokenMapper;

	public String test() {
		System.out.println(1/0);
		return "return from alpha-user";
	}
	
	public String test2() {
		String content = selfDiagnosisFeign.getUserInfo();
		System.out.println("content="+content);
		return content;
	}
	
	public List<UserVo> test3() {
		return userMapper.queryUser().stream().map(UserVo::new).collect(toList());
	}
	
	public List<UserToken> test4() {
		List<UserToken> tokenList = userTokenMapper.queryAll();
		return tokenList;
	}
}
