package com.project.SpringBootAuthPro.exception;

import org.springframework.stereotype.Component;

@Component
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	UserNotFoundException(){}
	public UserNotFoundException(String msg){
		super(msg);
	}
}
