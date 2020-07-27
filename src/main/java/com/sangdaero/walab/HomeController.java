package com.sangdaero.walab;

import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final UserService mUserService;

	@GetMapping("/")
	public String homePage(@AuthenticationPrincipal OAuth2User principal) {
		UserDto userDto = mUserService.getUser(principal);

		return (userDto.getUserType()==1)?"redirect:/activity":"html/index.html";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "html/login.html";
	}

//	@GetMapping("/test/map")
//	public String naverMap() {
//		return "html/request/naverMap";
//	}

	@GetMapping("/test/juso")
	public String jusoSearch() {
		return "html/activity/jusoSearch";
	}

	@GetMapping("/test/tagify")
	public String testTagify() {
		return "html/test";
	}

	@GetMapping("/test/addAct")
	public String addAct() {
		return "html/test/insertAct";
	}
}
