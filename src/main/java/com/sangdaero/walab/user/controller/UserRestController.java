package com.sangdaero.walab.user.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sangdaero.walab.user.application.dto.*;
import com.sangdaero.walab.user.application.validator.NicknameValidator;
import com.sangdaero.walab.user.application.validator.PhoneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.sangdaero.walab.user.application.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RestController
@RequestMapping("/userdata")
@RequiredArgsConstructor
public class UserRestController {
	
	private final UserService mUserService;
	private final NicknameValidator nicknameValidator;
	private final PhoneValidator phoneValidator;

	@InitBinder("userNickname")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(nicknameValidator); // validator 추가
	}

	@InitBinder("userPhone")
	public void initBinder2(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(phoneValidator); // validator 추가
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
	public Map<String, Object> changeName(@Valid UserName userName, Errors errors) {
		Map<String, Object> map = new HashMap<String, Object>();

		if(errors.hasErrors()) {
			map.put("message", "올바르지 않은 형식입니다");
			return map;
		}

		mUserService.changeName(userName.getId(), userName.getName());
		map.put("name", userName.getName());

		return map;
	}

	@PostMapping("/nickname")
	public Map<String, Object> changeNickname(@Valid UserNickname userNickname, Errors errors) {
		Map<String, Object> map = new HashMap<String, Object>();

		if(errors.hasErrors()) {
			map.put("message", "이미 존재하거나, 올바르지 않은 형식입니다");
			return map;
		}

		mUserService.changeNickname(userNickname.getId(), userNickname.getNickname());
		map.put("nickname", userNickname.getNickname());
		return map;
	}

	@PostMapping("/phone")
	public Map<String, Object> changePhone(@Valid UserPhone userPhone, Errors errors) {
		Map<String, Object> map = new HashMap<String, Object>();

		if(errors.hasErrors()) {
			map.put("message", "이미 존재하거나, 올바르지 않은 전화번호입니다.");
			return map;
		}

		mUserService.changePhone(userPhone.getId(), userPhone.getPhone());
		map.put("phone", userPhone.getPhone());
		return map;
	}

	@PostMapping("/usertype")
	public void changeUserType(@RequestParam("id") Long id, @RequestParam("type") Byte type) {
		mUserService.changeUserType(id, type);
	}

}
