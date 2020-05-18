package com.sangdaero.walab.activity.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sangdaero.walab.activity.dto.ActivityDto;
import com.sangdaero.walab.activity.service.ActivityService;
import com.sangdaero.walab.common.file.service.FileService;
import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.request.service.RequestService;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.service.UserService;


@Controller
@RequestMapping("/activity")
public class ActivityController {
	
	private ActivityService mActivityService;
	private InterestService mInterestService;
	private UserService mUserService;
	private FileService mFileService;
	
	// constructor
	public ActivityController(ActivityService activityService, InterestService interestService, 
			UserService userService, FileService fileService) {
		mActivityService = activityService;
		mInterestService = interestService;
		mUserService = userService;
		mFileService = fileService;
	}
	
	@GetMapping("")
	public String activityPage(Model model,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "interestType", defaultValue = "0") Integer interestType,
			@RequestParam(value = "status", defaultValue = "0") Integer status,
			@RequestParam(value = "sort", defaultValue = "1") Integer sortType) {
		
		List<ActivityDto> activityDtoList = mActivityService.getActivitylist(pageNum, keyword, interestType, status, sortType);
        Integer[] pageList = mActivityService.getPageList(pageNum, keyword, interestType, sortType, status);
        List<InterestDto> interestList = mInterestService.getInterestList();

        model.addAttribute("activityList", activityDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("interestType", interestType);
        model.addAttribute("status", status);
        model.addAttribute("sort", sortType);
        model.addAttribute("interests", interestList);

        return "html/activity/activity.html";
	}
	
	@GetMapping("/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        ActivityDto activityDto = mActivityService.getPost(id);
        List<InterestDto> interestList = mInterestService.getInterestList();
        List<SimpleUser> managerList = mUserService.getSimpleUserList("manager");
		List<SimpleUser> userList = mUserService.getSimpleUserList();
		List<String> fileNameList = mFileService.getFiles(id);
		
        model.addAttribute("activityDto", activityDto);
        model.addAttribute("interests", interestList);
        model.addAttribute("managers", managerList);
		model.addAttribute("users", userList);
        model.addAttribute("files", fileNameList);
		
        return "html/activity/activityDetail.html";
    }
	
	@GetMapping("/activityForm")
    public String getActivityForm(Model model) {
		List<InterestDto> interestList = mInterestService.getInterestList();
		List<SimpleUser> managerList = mUserService.getSimpleUserList("manager");
		List<SimpleUser> userList = mUserService.getSimpleUserList();
		
		model.addAttribute("interests", interestList);
		model.addAttribute("managers", managerList);
		model.addAttribute("users", userList);
		
        return "html/activity/activityForm.html";
    }
	
	@GetMapping("/activityForm/edit/{no}")
	public String editActivity(@PathVariable("no") Long id, Model model) {
		ActivityDto activityDto = mActivityService.getPost(id);
		List<InterestDto> interestList = mInterestService.getInterestList();
		List<SimpleUser> managerList = mUserService.getSimpleUserList("manager");
		List<SimpleUser> userList = mUserService.getSimpleUserList();
		
		model.addAttribute("activityDto", activityDto);
		model.addAttribute("interests", interestList);
		model.addAttribute("managers", managerList);
		model.addAttribute("users", userList);
		
		return "html/activity/activityUpdate.html";
	}
	
	@PutMapping("/activityForm/edit/{no}") 
	public String updateActivity(Long id,String title, Long interestCategoryId, Byte status, @RequestParam(value="userId", required=false) List<Long> userIdList, 
			Byte delivery, /*@RequestParam(value = "phoneAgree", required = false) String phoneAgree, 
			@RequestParam(value = "locationAgree", required = false) String locationAgree,*/ Long managerId, String startDate, 
			String startTime, String endDate, String endTime, String place, String deadlineDate, String deadlineTime, 
			String content, @RequestParam(value="volunteerId", required=false) List<Long> volunteerIdList) {
		
		mActivityService.updateActivity(id,title, status, interestCategoryId, userIdList, delivery, managerId, startDate, startTime, endDate, endTime, place, deadlineDate, deadlineTime, content, volunteerIdList);
			
		return "redirect:/activity";
	}
	
	
	@PostMapping("/activityForm") 
	public String setActivityForm(@RequestParam(value = "requestId", required = false) Long requestId, 
			String title, Long interestCategoryId, @RequestParam(value="userId", required=false) List<Long> userIdList, 
			Byte delivery,  @RequestParam(value = "userStatus", required = false) List<Byte> userStatusList, 
			@RequestParam(value = "volunteerStatus", required = false) List<Byte> volunteerStatusList, Long managerId, 
			@RequestParam(value = "startDate", required = false) String startDate, 
			@RequestParam(value = "startTime", required = false)String startTime, 
			String endDate, String endTime, String place, String deadlineDate, String deadlineTime, 
			String content, @RequestParam(value="volunteerId", required=false) List<Long> volunteerIdList,
			@RequestParam(value="files", required=false) MultipartFile[] files) {
		
		mActivityService.saveActivity(title,interestCategoryId, userIdList, userStatusList, delivery, managerId, startDate, startTime, endDate, endTime, place, deadlineDate, deadlineTime, content, volunteerIdList, volunteerStatusList, files, requestId);
			
		return "redirect:/activity";
	}

}
