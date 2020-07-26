package com.sangdaero.walab.activity.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.sangdaero.walab.activity.dto.ActivityForm;
import com.sangdaero.walab.common.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        List<InterestDto> interestList = mInterestService.getInterestList(2);
		Integer firstPage = mActivityService.getFirstPage(pageNum, keyword, interestType, status);

		Long totalNum = mActivityService.getActivityCount(keyword, interestType, status);

        model.addAttribute("activityList", activityDtoList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("interestType", interestType);
        model.addAttribute("status", status);
        model.addAttribute("sort", sortType);
        model.addAttribute("interests", interestList);

        model.addAttribute("currentPage", pageNum);
		model.addAttribute("firstPage", firstPage);
        model.addAttribute("totalNum", totalNum);

        return "html/activity/activity.html";
	}

	@GetMapping("/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        ActivityDto activityDto = mActivityService.getPost(id);
        List<InterestDto> interestList = mInterestService.getInterestList(2);
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
		List<InterestDto> interestList = mInterestService.getInterestList(2);
		List<SimpleUser> managerList = mUserService.getSimpleUserList("manager");
		List<SimpleUser> userList = mUserService.getSimpleUserList();

		model.addAttribute("interests", interestList);
		model.addAttribute("managers", managerList);
		model.addAttribute("users", userList);
		model.addAttribute("activityForm", new ActivityForm());

        return "html/activity/activityForm.html";
    }

    @GetMapping(value = "/downloadExcel", produces = "application/vnd.ms-excel")
	public String downloadExcel(Map<String,Object> modelMap) {

		List<ActivityDto> allActivities = mActivityService.getAllActivities();

		modelMap.put("activities", allActivities);

		return "activityExcel";
	}

	@GetMapping("/activityForm/edit/{no}")
	public String editActivity(@PathVariable("no") Long id, Model model) {
		ActivityDto activityDto = mActivityService.getPost(id);
		List<InterestDto> interestList = mInterestService.getInterestList(2);
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
	public String setActivityForm(Model model, @Valid ActivityForm activityForm, BindingResult bindingResult) {

		if(bindingResult.hasErrors()) {
			if(activityForm.getFiles()!=null && !activityForm.getFiles().isEmpty()) {
				String fileName =  mFileService.saveFile(activityForm.getFiles());
				model.addAttribute("productImage", fileName);
			}
			List<InterestDto> interestList = mInterestService.getInterestList(2);
			List<SimpleUser> managerList = mUserService.getSimpleUserList("manager");
			List<SimpleUser> userList = mUserService.getSimpleUserList();

			model.addAttribute("interests", interestList);
			model.addAttribute("managers", managerList);
			model.addAttribute("users", userList);
			model.addAttribute("activityForm", activityForm);

			return "html/activity/activityForm.html";
		}

		mActivityService.saveActivity(activityForm.getTitle(),activityForm.getInterestCategoryId(), activityForm.getUserId(),
				activityForm.getDelivery(), activityForm.getManagerId(), activityForm.getStartDate(), activityForm.getStartTime(),
				activityForm.getEndDate(), activityForm.getEndTime(), activityForm.getPlace(), activityForm.getDeadlineDate(),
				activityForm.getDeadlineTime(), activityForm.getContent(), activityForm.getVolunteerId(),
				activityForm.getVolunteerStatus(), activityForm.getFiles(), activityForm.getRequestId(), activityForm.getFile(),
				activityForm.getPlaceDetail());

		return "redirect:/activity";
	}

}
