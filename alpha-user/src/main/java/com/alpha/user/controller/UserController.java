package com.alpha.user.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.server.rpc.user.SelfDiagnosisFeign;
import com.alpha.user.domain.User;
import com.alpha.user.mapper.UserMapper;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private SelfDiagnosisFeign selfDiagnosisFeign;
	
	@Resource
	private UserMapper userMapper;

	@GetMapping("/test")
	public String test() {
		return "return from alpha-user";
	}
	
	@GetMapping("/test2")
	public String test2() {
		String content = selfDiagnosisFeign.getUserInfo();
		System.out.println("content="+content);
		return content;
	}
	
	@GetMapping("/test3")
	public String test3() {
		User u = new User();
		List<User> userList = userMapper.queryUser();
		int size = userList.size();
		System.out.println("size="+size);
		return "size="+size;
	}
}
