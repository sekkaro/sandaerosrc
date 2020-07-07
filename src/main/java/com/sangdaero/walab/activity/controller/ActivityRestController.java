package com.sangdaero.walab.activity.controller;

import java.io.IOException;
import java.util.List;

import com.sangdaero.walab.common.entity.UserEventMapper;
import com.sangdaero.walab.file.payload.FileUploadResponse;
import com.sangdaero.walab.file.service.FileUploadDownloadService;
import com.sangdaero.walab.mapper.repository.UserEventMapperRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sangdaero.walab.activity.dto.ActivityDto;
import com.sangdaero.walab.activity.dto.ActivityPeopleDto;
import com.sangdaero.walab.activity.dto.ActivityUserDto;
import com.sangdaero.walab.activity.dto.AppRequest;
import com.sangdaero.walab.activity.dto.UserStatusDto;
import com.sangdaero.walab.activity.service.ActivityService;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.dto.UserDetailDto;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.application.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/activitydata")
@RequiredArgsConstructor
public class ActivityRestController {

	private final ActivityService mActivityService;
	private final UserService mUserService;
	private final FileUploadDownloadService fileUploadDownloadService;

	@PostMapping("/setTitle")
	public String setTitle(@RequestParam("id") Long id, @RequestParam("title") String title) {
		mActivityService.setTitle(id, title);
		return title;
	}

	@PostMapping("/setCategory")
	public String setCategory(@RequestParam("id") Long id, @RequestParam("category") Long interestCategoryId) {
		String interestCategoryName = mActivityService.setInterestCategory(id, interestCategoryId);
		return interestCategoryName;
	}

	@PostMapping("/setStatus")
	public Byte setStatus(@RequestParam("id") Long id, @RequestParam("status") Byte status) {
		mActivityService.setStatus(id, status);
		return status;
	}

	@PostMapping("/setTitleAndStatus")
	public String setTitleAndStatus(@RequestParam("id") Long id, @RequestParam("title") String title, @RequestParam("status") Byte status) {
		mActivityService.setTitleAndStatus(id, title, status);
		return title + "|" + status;
	}

	@PostMapping("/setCategoryAndDeadline")
	public String setCategoryAndDeadline(@RequestParam("id") Long id, @RequestParam("category") Long interestCategoryId,
			@RequestParam(value="deadlineDate") String deadlineDate, @RequestParam(value="deadlineTime") String deadlineTime) {
		String interestCategoryName = mActivityService.setInterestCategoryAndDeadline(id, interestCategoryId, deadlineDate, deadlineTime);
		return interestCategoryName + "." + deadlineDate + "." + deadlineTime;
	}

	@GetMapping("/getUsers")
	public List<UserStatusDto> getUsers(@RequestParam("id") Long id, @RequestParam("category") Long interestCategoryId) {
		List<SimpleUser> userList = mUserService.getSimpleUserListWithInterestOnOff(interestCategoryId);
		List<UserStatusDto>	userStatusList = mActivityService.getUsersStatus(id, userList);
		return userStatusList;
	}
	
	/*@PostMapping("/setUsers")
	public List<ActivityUserDto> setUsers(@RequestParam("id") Long id, @RequestParam(value="users[]", required=false) List<Long> userIdList) {
		List<ActivityUserDto> userList = mActivityService.setUsers(id, userIdList, (byte) 0);
		return userList;
	}
	
	@PostMapping("/setVolunteers")
	public List<ActivityUserDto> setVolunteers(@RequestParam("id") Long id, @RequestParam(value="volunteers[]", required=false) List<Long> volunteerIdList) {
		List<ActivityUserDto> userList = mActivityService.setUsers(id, volunteerIdList, (byte) 1);
		return userList;
	}*/

	@PostMapping("/setDelivery")
	public Byte setDelivery(@RequestParam("id") Long id, @RequestParam("delivery") Byte delivery) {
		mActivityService.setDelivery(id, delivery);
		return delivery;
	}
	
	/*@PostMapping("/setPhoneAgree")
	public String setPhoneAgree(@RequestParam("id") Long id, @RequestParam(value="phoneAgree", required=false) String phoneAgree) {
		mActivityService.setPhoneAgree(id, phoneAgree);
		return phoneAgree;
	}*/

//	@PostMapping("/setVolunteerTime")
//	public String setVolunteerTime(@RequestParam("id") Long id,
//			@RequestParam("startDate") String startDate, @RequestParam("startTime") String startTime,
//			@RequestParam("endDate") String endDate, @RequestParam("endTime") String endTime) {
//		mActivityService.setVolunteerTime(id, startDate, startTime, endDate, endTime);
//		return startDate + "." + startTime + "." + endDate + "." + endTime;
//	}

	@PostMapping("/setVolunteerTime")
	public Integer setVolunteerTime(@RequestParam("id") Long id, @RequestParam("volunteerId") Long volunteerId, @RequestParam("time") Integer time) {
		mActivityService.setVolunteerTime(id, volunteerId, time);
		return time;
	}

	@PostMapping("/setVolunteerTimeAndPlaceAndContent")
	public String setVolunteerTimeAndPlaceAndContent(@RequestParam("id") Long id,
			@RequestParam("startDate") String startDate, @RequestParam("startTime") String startTime,
			@RequestParam("endDate") String endDate, @RequestParam("endTime") String endTime,
			@RequestParam("place") String place, @RequestParam("content") String content) {
		mActivityService.setVolunteerTimeAndPlaceAndContent(id, startDate, startTime, endDate, endTime, place, content);
		return place + "|" + content +"|" +startDate + "|" + startTime + "|" + endDate + "|" + endTime;
	}

	@PostMapping("/setPlace")
	public String setPlace(@RequestParam("id") Long id, @RequestParam("place") String place) {
		mActivityService.setPlace(id, place);
		return place;
	}

	@PostMapping("/setManager")
	public String setManager(@RequestParam("id") Long id, @RequestParam("manager") Long managerId) {
		String managerName = mActivityService.setManager(id, managerId);
		return managerName;
	}

	@PostMapping("/setPeople")
	public ActivityPeopleDto setPeople(@RequestParam("id") Long id,
			@RequestParam(value="users[]", required=false) List<Long> userIdList,
			/*@RequestParam(value="userStatusList[]", required=false) List<Byte> userStatusList,*/
			@RequestParam(value="volunteers[]", required=false) List<Long> volunteerIdList,
			@RequestParam(value="volunteerStatusList[]", required=false) List<Byte> volunteerStatusList,
			@RequestParam("manager") Long managerId) {
		ActivityPeopleDto people = mActivityService.setPeople(id, userIdList, /*userStatusList,*/ volunteerIdList, volunteerStatusList, managerId);
		return people;
	}

	@PostMapping("/setContent")
	public String setContent(@RequestParam("id") Long id, @RequestParam("content") String content) {
		mActivityService.setContent(id, content);
		return content;
	}

	@PostMapping("/setDeadline")
	public String setDeadline(@RequestParam("id") Long id,
			@RequestParam(value="deadlineDate") String deadlineDate, @RequestParam(value="deadlineTime") String deadlineTime) {
		mActivityService.setDeadline(id, deadlineDate, deadlineTime);
		return deadlineDate + "." + deadlineTime;
	}

	@GetMapping("/getActivities")
	public List<ActivityDto> getActivities(@RequestParam("id") Long interestCategoryId){
		return mActivityService.getActivitylist(interestCategoryId);
	}

	@GetMapping("/getTop5Activities")
	public List<ActivityDto> getTop5Activities(){
		return mActivityService.getTop5Activitylist();
	}

	@GetMapping("/getActivitiesForUser")
	public List<ActivityDto> getActivitiesForUser(@RequestParam("name") String name, @RequestParam("email") String email){
		UserDto userDto = mUserService.createUser(email, name);
		return mActivityService.getActivitylistForUser(userDto);
	}

	@GetMapping("/getTop5ActivitiesForUser")
	public List<ActivityDto> getTop5ActivitiesForUser(@RequestParam("name") String name, @RequestParam("email") String email){
		UserDto userDto = mUserService.createUser(email, name);
		return mActivityService.getTop5ActivitylistForUser(userDto);
	}

	@PostMapping("/unregister")
	public String unregister(@RequestBody AppRequest unregisterForm) {
		UserDto userDto = mUserService.createUser(unregisterForm.getEmail(), unregisterForm.getName());
		mActivityService.unregister(unregisterForm.getId(), userDto);
		return "success";
	}

	@RequestMapping(path = "/uploadStartImg", method = RequestMethod.POST,
			consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public void uploadStartImage(@RequestParam("id") Long id, @RequestParam("name") String name,
											 @RequestParam("email") String email, @RequestParam("image") MultipartFile image) {

		String fileName = fileUploadDownloadService.storeFile(image);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/activitydata/downloadFile/")
				.path(fileName)
				.toUriString();

		UserDto userDto = mUserService.createUser(email, name);
		mUserService.setStartImage(id, userDto, fileDownloadUri);
	}

	@RequestMapping(path = "/uploadEndImg", method = RequestMethod.POST,
			consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public void uploadEndImage(@RequestParam("id") Long id, @RequestParam("name") String name,
						   @RequestParam("email") String email, @RequestParam("image") MultipartFile image) {

		String fileName = fileUploadDownloadService.storeFile(image);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/activitydata/downloadFile/")
				.path(fileName)
				.toUriString();

		UserDto userDto = mUserService.createUser(email, name);
		mUserService.setEndImage(id, userDto, fileDownloadUri);
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
		// Load file as Resource
		Resource resource = fileUploadDownloadService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
//			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
