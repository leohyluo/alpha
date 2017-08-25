package org.alpha.user.controller;

import javax.annotation.Resource;

import org.alpha.user.rpc.SelfDiagnosisFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private SelfDiagnosisFeign selfDiagnosisFeign;

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
	
}
