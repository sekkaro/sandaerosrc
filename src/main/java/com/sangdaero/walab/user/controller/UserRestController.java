package com.sangdaero.walab.user.controller;

import java.util.*;

import com.sangdaero.walab.activity.dto.AppRequest;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.UserInterest;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserInterestRepository;
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
	private final InterestRepository mInterestRepository;
	private final UserInterestRepository mUserInterestRepository;

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
    public boolean setUserStatus(@RequestParam("isOn") Boolean isOn, @AuthenticationPrincipal OAuth2User principal) {
		boolean res = mUserService.setStatus(principal, isOn);
		return isOn;
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

	@PostMapping("/email")
	public Map<String, Object> changeEmail(@Valid UserEmail userEmail, Errors errors) {
		Map<String, Object> map = new HashMap<>();

		if(errors.hasErrors()) {
			map.put("message", "올바르지 않은 형식입니다.");
			return map;
		}

		mUserService.changeEmail(userEmail.getId(), userEmail.getSocialId());
		map.put("socialId", userEmail.getSocialId());

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

	@PostMapping("/addtime")
	public void addVolunteerTime() {

	}

	@GetMapping("/getUser")
	public UserDetailDto getUser(@RequestParam("name") String name, @RequestParam("email") String email) {

		UserDto userDto = mUserService.createUser(email, name);

		return mUserService.getUser(userDto.getId());
	}

	@PostMapping("/modifyNickName")
	public void modifyNickName(@RequestBody AppRequest userForm) {
		UserDto userDto = mUserService.createUser(userForm.getEmail(), userForm.getName());

		mUserService.changeNickname(userDto.getId(), userForm.getNickname());
	}

	@GetMapping("/checkNewUser")
	public Boolean checkNewUser(@RequestParam("name") String name, @RequestParam("email") String email) {
		return mUserService.checkNewUser(email, name);
	}

	@PostMapping("/setPhoneAgree")
	public void setPhoneAgree(@RequestBody AppRequest userForm) {
		UserDto userDto = mUserService.createUser(userForm.getEmail(), userForm.getName());

		mUserService.setPhoneAgree(userDto.getId(), userForm.getPhoneAgree());
	}

	@PostMapping("/setBasicInfo")
	public void setBasicInfo(@RequestBody AppRequest userForm) {
		UserDto userDto = mUserService.createUser(userForm.getEmail(), userForm.getName());

		mUserService.setBasicInfo(userDto.getId(), userForm.getPhone(), userForm.getNickname(), userForm.getPhoneAgree());
	}

	@PostMapping("/addInterest")
	public void addInterest(@RequestBody AppRequest userForm) {
		InterestCategory interest = mInterestRepository.findById(userForm.getInterestId()).orElse(null);
		UserDto userDto = mUserService.createUser(userForm.getEmail(), userForm.getName());

		mUserService.addInterest(userDto.getId(), interest);
	}

	@PostMapping("/removeInterest")
	public void removeInterest(@RequestBody AppRequest userForm) {
		InterestCategory interest = mInterestRepository.findById(userForm.getInterestId()).orElse(null);
		UserDto userDto = mUserService.createUser(userForm.getEmail(), userForm.getName());

		mUserService.removeInterest(userDto.getId(), interest);
	}

	@GetMapping("/eachUserInterest")
	public Set<eachUserInterest> eachUserInterest(@RequestParam("name") String name, @RequestParam("email") String email) {

		UserDto userDto = mUserService.createUser(email, name);

		List<UserInterest> byUser_id = mUserInterestRepository.findByUser_Id(userDto.getId());

		Set<eachUserInterest> interests = new HashSet<>();

		for(UserInterest e : byUser_id) {
			Optional<InterestCategory> byId = mInterestRepository.findById(e.getInterest().getId());
			eachUserInterest test = new eachUserInterest();
			test.setId(byId.get().getId());
			test.setInterestName(byId.get().getName());
			interests.add(test);
		}

		return interests;
	}

	@PostMapping("/unregisterUser")
	public void unregisterUser(@RequestBody AppRequest userForm) {
		UserDto userDto = mUserService.createUser(userForm.getEmail(), userForm.getName());

		mUserService.changeUserType(userDto.getId(), (byte) 2);
	}
}
