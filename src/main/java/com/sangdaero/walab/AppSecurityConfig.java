package com.sangdaero.walab;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.sangdaero.walab.user.application.service.UserService;

@Configuration
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private UserService mUserService;
	
	public AppSecurityConfig(UserService userService) {
		mUserService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*http
			.antMatcher("/**").authorizeRequests()
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
			.and()
			.oauth2Login();*/
		
		http
        .authorizeRequests(a -> a
				.antMatchers("/error","/test/**", "/interestdata/**", "/requestdata/**", "/activitydata/**","/noticedata/**", "/notification/**", "/notificationdata/**", "/userdata/**", "/downloadFile/**").permitAll()
        		.anyRequest().authenticated()
        )
        .logout(l -> l
				.logoutSuccessUrl("/login").permitAll()
        )
        .csrf().disable()
		.oauth2Login().loginPage("/login")
        .userInfoEndpoint()
        .oidcUserService(mUserService);
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.mvcMatchers("/node_modules/**")
				.mvcMatchers("/img/**")
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
}
