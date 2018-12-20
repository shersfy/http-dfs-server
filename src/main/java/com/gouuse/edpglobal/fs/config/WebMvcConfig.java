package com.gouuse.edpglobal.fs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gouuse.edpglobal.fs.filter.ViewFilesInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Autowired
	private ViewFilesInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//拦截规则
		registry.addInterceptor(interceptor).addPathPatterns(ViewFilesInterceptor.patterns);

	}
}
