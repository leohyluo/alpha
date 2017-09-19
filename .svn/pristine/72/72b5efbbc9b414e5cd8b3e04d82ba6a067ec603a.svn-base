package com.alpha.user.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.commons.pojo.UserToken;
import com.alpha.commons.web.ResponseMessage;
import com.alpha.commons.web.WebUtils;
import com.alpha.user.pojo.vo.UserVo;
import com.alpha.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private UserService userService;

	@ApiOperation(value = "用户登录", notes="用户登录", produces="application/json")
	@GetMapping("/test")
	public String test() {
		return userService.test();
	}
	
	@ApiOperation(value = "忘记密码", notes="忘记密码", produces="application/json")
	@GetMapping("/test2")
	public String test2() {
		String content = userService.test2();
		System.out.println("content="+content);
		return content;
	}
	
	@ApiOperation(value = "用户列表", notes="用户列表", produces="application/json")
	@GetMapping("/test3")
	public ResponseMessage test3() {
		logger.info("test3 started");
		List<UserVo> userList = userService.test3();
		int size = userList.size();
		System.out.println("size="+size);
		logger.info("test3 end");
		return WebUtils.buildSuccessResponseMessage(userList);
	}
	
	@ApiOperation(value = "用户Token列表", notes="用户Token列表", produces="application/json")
	@GetMapping("/test4")
	public String test4() {
		List<UserToken> tokenList = userService.test4();
		int size = tokenList.size();
		String cont = "token size : " + size;
		return cont;
	}
}
