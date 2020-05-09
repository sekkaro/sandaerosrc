package com.sangdaero.walab;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/summernoteImage/**")
				.addResourceLocations("file:///C:/Users/hn122/git/sangdaero/sangdaero/src/main/resources/static/img/");
	}
}
