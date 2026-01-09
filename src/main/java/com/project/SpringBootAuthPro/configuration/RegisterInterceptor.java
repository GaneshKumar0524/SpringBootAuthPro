package com.project.SpringBootAuthPro.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.SpringBootAuthPro.interceptor.LoggingInterceptor;


@Configuration
public class RegisterInterceptor  implements WebMvcConfigurer{

	@Autowired
	LoggingInterceptor loggingInterceptor;

	public  void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor)
		.addPathPatterns("/auth/**");
		}
}
