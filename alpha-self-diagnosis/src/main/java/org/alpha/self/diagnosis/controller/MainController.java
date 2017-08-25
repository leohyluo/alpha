package org.alpha.self.diagnosis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {

	@GetMapping("/test")
	public String test() {
		System.out.println("invoke test");
		return "return from self-diagnosis";
	}
}
