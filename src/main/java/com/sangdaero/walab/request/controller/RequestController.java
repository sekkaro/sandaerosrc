package com.sangdaero.walab.request.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.request.dto.RequestDto;
import com.sangdaero.walab.request.service.RequestService;
import com.sangdaero.walab.user.application.dto.SimpleUser;
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
			@RequestParam(value = "interestType", defaultValue = "0") Integer interestType,
			@RequestParam(value = "status", defaultValue = "0") Integer status,
			@RequestParam(value = "sort", defaultValue = "1") Integer sortType) {
		
		List<RequestDto> requestDtoList = mRequestService.getRequestlist(pageNum, keyword, interestType, status, sortType);
        Integer[] pageList = mRequestService.getPageList(pageNum, keyword, interestType, sortType, status);
        List<InterestDto> interestList = mInterestService.getInterestList();

        model.addAttribute("requestList", requestDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("interestType", interestType);
        model.addAttribute("status", status);
        model.addAttribute("sort", sortType);
        model.addAttribute("interests", interestList);

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
	
	@GetMapping("/requestForm/edit/{no}")
	public String editRequest(@PathVariable("no") Long id, Model model) {
		RequestDto requestDto = mRequestService.getPost(id);
		List<InterestDto> interestList = mInterestService.getInterestList();
		List<SimpleUser> managerList = mUserService.getSimpleUserList("manager");
		List<SimpleUser> userList = mUserService.getSimpleUserList();
		
		model.addAttribute("requestDto", requestDto);
		model.addAttribute("interests", interestList);
		model.addAttribute("managers", managerList);
		model.addAttribute("users", userList);
		
		return "html/request/requestUpdate.html";
	}
	
	@PutMapping("/requestForm/edit/{no}") 
	public String updateRequest(Long id,String title, Long interestCategoryId, Byte status, @RequestParam(value="userId", required=false) List<Long> userIdList, 
			Byte delivery, @RequestParam(value = "phoneAgree", required = false) String phoneAgree, 
			@RequestParam(value = "locationAgree", required = false) String locationAgree, Long managerId, String startDate, 
			String startTime, String endDate, String endTime, String place, String deadlineDate, String deadlineTime, 
			String content, @RequestParam(value="volunteerId", required=false) List<Long> volunteerIdList) {
		
		mRequestService.updateRequest(id,title, status, interestCategoryId, userIdList, delivery, phoneAgree, locationAgree, managerId, startDate, startTime, endDate, endTime, place, deadlineDate, deadlineTime, content, volunteerIdList);
			
		return "redirect:/request";
	}
	
	
	@PostMapping("/requestForm") 
	public String setRequestForm(String title, Long interestCategoryId, @RequestParam(value="userId", required=false) List<Long> userIdList, 
			Byte delivery, @RequestParam(value = "phoneAgree", required = false) String phoneAgree, 
			@RequestParam(value = "locationAgree", required = false) String locationAgree, Long managerId, String startDate, 
			String startTime, String endDate, String endTime, String place, String deadlineDate, String deadlineTime, 
			String content, @RequestParam(value="volunteerId", required=false) List<Long> volunteerIdList) {
		
		mRequestService.saveRequest(title,interestCategoryId, userIdList, delivery, phoneAgree, locationAgree, managerId, startDate, startTime, endDate, endTime, place, deadlineDate, deadlineTime, content, volunteerIdList);
			
		return "redirect:/request";
	}

}
