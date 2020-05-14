package com.sangdaero.walab.user.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sangdaero.walab.user.application.dto.UserDetailDto;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.application.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/userdata")
public class UserRestController {
	
	private UserService mUserService;
	
	public UserRestController(UserService userService) {
		mUserService = userService;
	}
	
	@GetMapping("/getBasicInfo")
    public Map<String, Object> getUserData(@AuthenticationPrincipal OAuth2User principal) {
		UserDto userDto = mUserService.getUser(principal);
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("name", principal.getAttribute("name"));
		result.put("status", userDto.getStatus());
		result.put("userType", userDto.getUserType());
		
        return result;
    }
	
	@PostMapping("/setStatus")
    public String setUserStatus(@RequestParam("isOn") Boolean isOn, @AuthenticationPrincipal OAuth2User principal) {
		mUserService.setStatus(principal, isOn);
		return "done";
    }

    @PostMapping("/change")
	public void changeTime(@RequestParam("id") Long id, Integer time) {
		mUserService.changeVolunteerTime(id, time);
	}

	@PostMapping("/name")
	public String changeName(@RequestParam("id") Long id, @RequestParam("name") String name, RedirectAttributes attributes) {
		mUserService.changeName(id, name);
		return name;
	}

	@PostMapping("/nickname")
	public String changeNickname(@RequestParam("id") Long id, @RequestParam("nickname") String nickname) {
		mUserService.changeNickname(id, nickname);
		return nickname;
	}

	@PostMapping("/phone")
	public String changePhone(@RequestParam("id") Long id, @RequestParam("phone") String phone) {
		mUserService.changePhone(id, phone);
		return phone;
	}

	@PostMapping("/usertype")
	public void changeUserType(@RequestParam("id") Long id, @RequestParam("type") Byte type) {
		mUserService.changeUserType(id, type);
	}

}
