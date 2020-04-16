package com.sangdaero.walab.request.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.request.dto.RequestDto;
import com.sangdaero.walab.request.service.RequestService;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.application.service.UserService;


@Controller
@RequestMapping("/request")
public class RequestController {
	
	private RequestService mRequestService;
	private InterestService mInterestService;
	private UserService mUserService;
	
	// constructor
	public RequestController(RequestService requestService, InterestService interestService, UserService userService) {
		mRequestService = requestService;
		mInterestService = interestService;
		mUserService = userService;
	}
	
	@GetMapping("")
	public String requestPage(Model model,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "type", defaultValue = "0") Integer searchType,
			@RequestParam(value = "sort", defaultValue = "1") Integer sortType) {
		
		List<RequestDto> requestDtoList = mRequestService.getRequestlist(pageNum, keyword, searchType, sortType);
        Integer[] pageList = mRequestService.getPageList(pageNum, keyword, searchType, sortType);

        model.addAttribute("requestList", requestDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", searchType);
        model.addAttribute("sort", sortType);

        return "html/request/request.html";
	}
	
	@GetMapping("/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        RequestDto requestDto = mRequestService.getPost(id);

        model.addAttribute("requestDto", requestDto);
        
        return "html/request/requestDetail.html";
    }
	
	@GetMapping("/requestForm")
    public String getRequestForm(Model model) {
		List<InterestDto> interestList = mInterestService.getInterestList();
		List<SimpleUser> managerList = mUserService.getSimpleUserList("manager");
		List<SimpleUser> userList = mUserService.getSimpleUserList();
		
		model.addAttribute("interests", interestList);
		model.addAttribute("managers", managerList);
		model.addAttribute("users", userList);
		
        return "html/request/requestForm.html";
    }
	
	
	@PostMapping("/requestForm") 
	public String setRequestForm(String title, Long interestCategoryId, Long userId, Byte delivery, Long managerId, String startTime, String endTime, String place, String deadline, String content) {
		
		mRequestService.saveRequest(title,interestCategoryId, userId, delivery, managerId, startTime, endTime, place, deadline, content);
			
		return "redirect:/request";
	}

}
