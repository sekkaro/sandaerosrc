package com.sangdaero.walab.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangdaero.walab.activity.dto.ActivityDto;
import com.sangdaero.walab.activity.dto.EachUserActivity;
import com.sangdaero.walab.activity.service.ActivityService;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.common.push.MakeJSON;
import com.sangdaero.walab.common.push.Push;
import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.dto.InterestForm;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.application.dto.UserDetailDto;
import com.sangdaero.walab.user.application.service.UserService;
import com.sangdaero.walab.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService mUserService;
	private final InterestService mInterestService;
	private final UserRepository userRepository;
	private final InterestRepository mInterestRepository;
	private final ObjectMapper objectMapper;
	private final ActivityService mActivityService;

	@GetMapping("")
	public String userPage(Model model,
						   @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable,
						   @RequestParam(value = "keyword", defaultValue = "") String keyword,
						   @RequestParam(value = "condition", defaultValue = "0") Integer condition) {
		Page<User> userPageList = mUserService.getSimpleUserPageList(pageable, keyword, condition);
		Long totalNum = userPageList.getTotalElements();
		model.addAttribute("simpleUserList", userPageList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("condition",condition);
		model.addAttribute("totalNum", totalNum);

//		List<SimpleUser> simpleUsers = mUserService.getSimpleUserList();
//		model.addAttribute("simpleUserList", simpleUsers);
		return "html/user/user";
	}

	@GetMapping("/add")
	public String add(Model model) {
		List<InterestDto> interestDTOList = mInterestService.getInterestList(2);
		model.addAttribute(new UserDto());
		model.addAttribute("interestList", interestDTOList);
		return "html/user/add";
	}

	@PostMapping("/add")
	public String addUser(@Valid UserDto userDTO, Errors errors) {

		if(errors.hasErrors()) {
			return "html/user/add";
		}

		userDTO.setVolunteerTime(0);
		userDTO.setUserType((byte)0);
		userDTO.setIsDummy((byte)1);
		mUserService.addUser(userDTO);
		return "redirect:/user";
	}

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Long id, Model model) {
		UserDetailDto userDetailDTO = mUserService.getUser(id);

		model.addAttribute("userInfo", userDetailDTO);

		return "html/user/detail";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) throws JsonProcessingException {
		UserDetailDto userDetailDTO = mUserService.getUser(id);

		List<EachUserActivity> activities = mActivityService.getEachUserActivityList(id);

		model.addAttribute("userInfo", userDetailDTO);
		model.addAttribute("activities", activities);

		List<String> all = mInterestRepository.findAllByOn_offEquals((byte)1)
				.stream().map(InterestCategory::getName).collect(Collectors.toList());

		model.addAttribute("whitelist", objectMapper.writeValueAsString(all));

		return "html/user/update";
	}

	@PostMapping("/interest/add/{id}")
	@ResponseBody
	public ResponseEntity addInterest(@PathVariable Long id, @RequestBody InterestForm interestForm) {

		InterestCategory interest = mInterestRepository.findByName(interestForm.getInterestName());

		if(interest==null) {
			return ResponseEntity.badRequest().build();
		}

		mUserService.addInterest(id, interest);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/interest/remove/{id}")
	@ResponseBody
	public ResponseEntity removeInterest(@PathVariable Long id, @RequestBody InterestForm interestForm) {
		InterestCategory interest = mInterestRepository.findByName(interestForm.getInterestName());

		if(interest==null) {
			return ResponseEntity.badRequest().build();
		}

		mUserService.removeInterest(id, interest);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/edit/{id}")
	public String update(@RequestParam(value = "interest", required = false) List<String> interest, UserDto userDto, Errors errors) {

		if(errors.hasErrors()) {
			return "html/user/update";
		}

		if(interest!=null) {
			userDto.setUserInterestList(interest.toArray(new String[0]));
		}

		System.out.println(userDto.getVolunteerTime());

		mUserService.addUser(userDto);
		return "redirect:/user/detail/{id}";
	}

	@GetMapping("/data")
	public String generateTestData() {
		for(int i=0;i<35;i++) {
			String randomName = RandomString.make(3);
			String randomNickname = RandomString.make(5);
			double r = Math.random();
			Integer randomTime=(int)(r*100)+1;

			User user = User.builder()
					.name(randomName)
					.nickname(randomNickname)
					.userType((byte) 1)
					.volunteerTime(randomTime)
					.build();
			userRepository.save(user);
		}
		return "redirect:/";
	}

}
