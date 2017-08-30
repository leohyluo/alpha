package com.alpha.api.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zuul")
public class ZuulController {

	@GetMapping("/test")
	public String test() {
		return "return form api-gateway";
	}
}
