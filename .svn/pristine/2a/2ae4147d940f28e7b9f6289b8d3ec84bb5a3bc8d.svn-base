package com.alpha.commons.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alpha.commons.exception.CommonException;

@ControllerAdvice(basePackages={"com.alpha.user.controller"})
public class ControllerEnhancerment {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler({CommonException.class})
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage processException(CommonException ex) {
		logger.error(ex.getMessage());
		return WebUtils.buildResponseMessage(com.alpha.commons.web.ResponseStatus.EXCEPTION, ex.getMessage());
	}
	

}
