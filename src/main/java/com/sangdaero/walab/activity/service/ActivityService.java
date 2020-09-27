package com.sangdaero.walab.activity.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.sangdaero.walab.activity.dto.*;
import com.sangdaero.walab.common.entity.*;
import com.sangdaero.walab.common.notification.repository.NotificationRepository;
import com.sangdaero.walab.common.push.MakeJSON;
import com.sangdaero.walab.common.push.Push;
import com.sangdaero.walab.mapper.repository.UserInterestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sangdaero.walab.activity.domain.repository.ActivityRepository;
import com.sangdaero.walab.common.file.repository.FileRepository;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserEventMapperRepository;
import com.sangdaero.walab.request.repository.RequestRepository;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.dto.UserDetailDto;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.domain.repository.UserRepository;

@Service
public class ActivityService {

	private ActivityRepository mActivityRepository;
	private InterestRepository mInterestRepository;
	private UserRepository mUserRepository;
	private UserEventMapperRepository mUserEventMapperRepository;
	private FileRepository mFileRepository;
	private RequestRepository mRequestRepository;
	private NotificationRepository mNotificationRepository;
	private UserInterestRepository mUserInterestRepository;

    private static final int PAGE_POSTCOUNT = 8;  // 한 페이지에 존재하는 게시글 수

	// constructor
	public ActivityService(ActivityRepository activityRepository, InterestRepository interestRepository, 
			UserRepository userRepository, UserEventMapperRepository userEventMapperRepository,
						   FileRepository fileRepository, RequestRepository requestRepository, NotificationRepository notificationRepository,
						   UserInterestRepository userInterestRepository) {
		mActivityRepository = activityRepository;
		mInterestRepository = interestRepository;
		mUserRepository = userRepository;
		mUserEventMapperRepository = userEventMapperRepository;
		mFileRepository = fileRepository;
		mRequestRepository = requestRepository;
		mNotificationRepository = notificationRepository;
		mUserInterestRepository = userInterestRepository;
	}
	
	
	// getActivitylist -> convertEntitytoDto
    public List<ActivityDto> getActivitylist(Integer pageNum, String keyword, Integer interestType, Integer status, Integer sortType) {
    	Page<EventEntity> page;
    	
    	if(status == 0) {
    		if(interestType == 0) {
				page = mActivityRepository.findAllByEventCategoryAndTitleContainingOrderByStatusAsc(0, keyword, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType!=3)?Sort.Direction.DESC:Sort.Direction.ASC, (sortType!=1)?"regDate":"modDate")));
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);

				page = mActivityRepository.findAllByEventCategoryAndTitleContainingAndInterestCategoryOrderByStatusAsc(0, keyword, interestCategory, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType!=3)?Sort.Direction.DESC:Sort.Direction.ASC, (sortType!=1)?"regDate":"modDate")));
			}
    	}
    	else {
    		if(interestType == 0) {
				page = mActivityRepository.findAllByEventCategoryAndTitleContainingAndStatus(0, keyword, (--status).byteValue(), PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType!=3)?Sort.Direction.DESC:Sort.Direction.ASC, (sortType!=1)?"regDate":"modDate")));
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);

				page = mActivityRepository.findAllByEventCategoryAndTitleContainingAndInterestCategoryAndStatus(0, keyword, interestCategory, (--status).byteValue(), PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType!=3)?Sort.Direction.DESC:Sort.Direction.ASC, (sortType!=1)?"regDate":"modDate")));
    		}
    	}
    	
        List<EventEntity> activities = page.getContent();
        List<ActivityDto> activityDtoList = new ArrayList<>();

        for(EventEntity activity : activities) {
        	activityDtoList.add(this.convertEventEntityToActivityDto(activity));
        }
        
        return activityDtoList;
    }
    
    public Long getActivityCount(String keyword, Integer interestType, Integer status) {
    	if(status == 0) {
    		if(interestType == 0) {
    			return mActivityRepository.countByEventCategoryAndTitleContaining(0, keyword);
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			return mActivityRepository.countByEventCategoryAndTitleContainingAndInterestCategory(0, keyword, interestCategory);
    		}
    	}
    	else {
    		if(interestType == 0) {
    			return mActivityRepository.countByEventCategoryAndTitleContainingAndStatus(0, keyword, (--status).byteValue());
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			return mActivityRepository.countByEventCategoryAndTitleContainingAndInterestCategoryAndStatus(0, keyword, interestCategory, (--status).byteValue());
    		}
    	}
    }

	public int getFirstPage(Integer curPageNum, String keyword, Integer interestType, Integer status) {
		// 총 게시글 수
		Double postsTotalCount = Double.valueOf(this.getActivityCount(keyword, interestType, status));

		// 총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
		Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POSTCOUNT)));

		if(curPageNum < 3) {
			return 1;
		}
		else if(curPageNum + 1 > totalLastPageNum) {
			return curPageNum-2;
		}
		else {
			return curPageNum-1;
		}

	}
    
 // Detail of id's notice
    public ActivityDto getPost(Long id) {
        Optional<EventEntity> ActivityWrapper = mActivityRepository.getById(id);
        EventEntity activity = ActivityWrapper.get();
        
        ActivityDto activityDto = this.convertEventEntityToActivityDto(activity);
        
        List<UserEventMapper> userEventList = mUserEventMapperRepository.findAllByEventId(id);
        
        Set<ActivityUserDto> activityUsers = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        Set<ActivityUserDto> activityVolunteers = new HashSet<>();
        Set<Long> volunteerIds = new HashSet<>();

		List<UserInterest> userInterestList = new ArrayList<>();
		Set<InterestCategory> interestList;
        
        for(UserEventMapper userEvent: userEventList) {
        	if(userEvent.getUserType() == 0) {
				userInterestList = mUserInterestRepository.findByUser_Id(userEvent.getUser().getId());
				interestList = new HashSet<>();

				for(UserInterest userInterest: userInterestList) {
					interestList.add(userInterest.getInterest());
				}

        		ActivityUserDto activityUser = new ActivityUserDto();
        		
        		activityUser.setUser(userEvent.getUser());
        		activityUser.setStatus(userEvent.getStatus());
        		activityUser.setPhoneAgree(userEvent.getPhoneAgree());
        		activityUser.setLocationAgree(userEvent.getLocationAgree());
        		activityUser.setStartImage(userEvent.getStartImage());
        		activityUser.setEndImage(userEvent.getEndImage());
				activityUser.setVolunteerTime(userEvent.getVolunteerTime());
				activityUser.setMemo(userEvent.getMemo());
				activityUser.setUserInterestList(interestList);
        		activityUser.setRegDate(userEvent.getRegDate());
        		activityUser.setModDate(userEvent.getModDate());
        		
        		activityUsers.add(activityUser);
        		userIds.add(userEvent.getUser().getId());
        	}
        	else {
        		ActivityUserDto activityVolunteer = new ActivityUserDto();
        		
        		activityVolunteer.setUser(userEvent.getUser());
        		activityVolunteer.setStatus(userEvent.getStatus());
        		activityVolunteer.setPhoneAgree(userEvent.getPhoneAgree());
        		activityVolunteer.setLocationAgree(userEvent.getLocationAgree());
        		activityVolunteer.setStartImage(userEvent.getStartImage());
        		activityVolunteer.setEndImage(userEvent.getEndImage());
				activityVolunteer.setVolunteerTime(userEvent.getVolunteerTime());
				activityVolunteer.setMemo(userEvent.getMemo());
        		activityVolunteer.setRegDate(userEvent.getRegDate());
        		activityVolunteer.setModDate(userEvent.getModDate());
        		activityVolunteer.setStartImgDate(userEvent.getStartImgDate());
				activityVolunteer.setEndImgDate(userEvent.getEndImgDate());
        		
        		activityVolunteers.add(activityVolunteer);
        		volunteerIds.add(userEvent.getUser().getId());
        	}
        }
        
        activityDto.setActivityUsers(activityUsers);
        activityDto.setUserIds(userIds);
        
        activityDto.setActivityVolunteers(activityVolunteers);
        activityDto.setVolunteerIds(volunteerIds);
        
        return activityDto;
    }
    
    @Transactional
    public Long saveActivity(String title, Long interestCategoryId, List<Long> userIdList, /*List<Byte> userStatusList,*/ Byte delivery, 
    		Long managerId, String startDate, String startTime, String endDate, String endTime, String place,
		 	String deadlineDate, String deadlineTime, String content, List<Long> volunteerIdList, List<Byte> volunteerStatusList, MultipartFile file, Long requestId, String requestFileName, String placeDetail) {
		
    	ActivityDto activityDto = new ActivityDto();
    	
    	activityDto.setTitle(title);
    	activityDto.setDeliveryFlag(delivery);
    	//activityDto.setPhoneAgree((phoneAgree==null)? (byte) 0:(byte) 1);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activityDto.setStartTime((startDate.isEmpty()||startTime.isEmpty())?null:LocalDateTime.parse(startDate + startTime, formatter));
    	activityDto.setEndTime((endDate.isEmpty()||endTime.isEmpty())?null:LocalDateTime.parse(endDate + endTime, formatter));
    	activityDto.setPlace(place);
		activityDto.setPlaceDetail(placeDetail);
    	activityDto.setDeadline((deadlineDate.isEmpty()||deadlineTime.isEmpty())?null:LocalDateTime.parse(deadlineDate + deadlineTime, formatter));
    	activityDto.setContent(content);
		activityDto.setType((requestId!=null)?0:1);
    	
    	InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
    	User manager = mUserRepository.findById(managerId).orElse(null);
    	
    	if(interestCategory.getName().contains("물건")) {
    		activityDto.setStatus((byte) 3);
    		if(manager!=null) {
    			sendAlarmToUser(manager, "활동 시작 알림", title + " 활동이 시작되었습니다", null);
    		}
    	}
    	else {
    		activityDto.setStatus((byte) 0);
    	}

    	EventEntity event = activityDto.toEntity();
    	
    	event.setInterestCategory(interestCategory);
    	event.setManager(manager);
    	
    	Long id = mActivityRepository.save(event).getId();
    	
    	if(userIdList!=null) {
    		
    		int index = 0;
    		
    		for(Long userId: userIdList) {
        		UserEventMapper userEventMapper = new UserEventMapper();
        		
        		User user = mUserRepository.findById(userId).orElse(null);
        		userEventMapper.setUser(user);
        		userEventMapper.setEvent(event);
        		userEventMapper.setUserType((byte) 0);
        		userEventMapper.setLocationAgree((byte) 1);
        		userEventMapper.setPhoneAgree((byte) 1);
        		userEventMapper.setStatus((byte) 1);
				userEventMapper.setVolunteerTime(0);
        		//userEventMapper.setLocationAgree((byte) ((userStatusList.get(index)==1)?1:0));
        		//userEventMapper.setPhoneAgree((byte) ((userStatusList.get(index)==1)?1:0));
        		//userEventMapper.setStatus(userStatusList.get(index));
        		
        		mUserEventMapperRepository.save(userEventMapper);
        		
        		sendAlarmToUser(user, "활동 등록 알림", title + "이 등록되었습니다", null);
        		
        		index++;
        	}
    		
    	}
    	
    	if(volunteerIdList != null) {
    		
    		int index = 0;
    		
    		for(Long volunteerId: volunteerIdList) {
        		UserEventMapper userEventMapper = new UserEventMapper();
        		
        		User volunteer = mUserRepository.findById(volunteerId).orElse(null);
        		userEventMapper.setUser(volunteer);
        		userEventMapper.setEvent(event);
        		userEventMapper.setUserType((byte) 1);
        		userEventMapper.setLocationAgree((byte) ((volunteerStatusList.get(index)==1)?1:0));
        		userEventMapper.setPhoneAgree((byte) ((volunteerStatusList.get(index)==1)?1:0));
        		userEventMapper.setStatus(volunteerStatusList.get(index));
				userEventMapper.setVolunteerTime(0);
        		
        		mUserEventMapperRepository.save(userEventMapper);
        		
        		sendAlarmToUser(volunteer, "활동 등록 알림", title + "이 등록되었습니다", null);
        		
        		index++;
        	}
    		
    	}
    		
    	Path currentPath = Paths.get("");
		Path absolutePath = currentPath.toAbsolutePath();

		String url = "/tomcat/webapps/ROOT/WEB-INF/classes/static/images/";

//    	String url = "/src/main/resources/static/images/";


		if(file!=null && !file.isEmpty()) {
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(absolutePath + url, fileName);
			try {
				Files.write(fileNameAndPath, file.getBytes());

				FileEntity fileEntity = new FileEntity();
				fileEntity.setEvent(event);
				fileEntity.setTitle(fileName);
				fileEntity.setUrl(fileNameAndPath.toString());

				mFileRepository.save(fileEntity);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(requestFileName != null) {
			Path fileNameAndPath = Paths.get(absolutePath + url, requestFileName);

			FileEntity fileEntity = new FileEntity();
			fileEntity.setEvent(event);
			fileEntity.setTitle(requestFileName);
			fileEntity.setUrl(fileNameAndPath.toString());

			mFileRepository.save(fileEntity);
		}
        
        if(requestId!=null) {
        	
        	Request request = mRequestRepository.findById(requestId).orElse(null);
        	
        	request.setEvent(event);
        	request.setStatus((byte) 1);
        	
        	mRequestRepository.save(request);
			
			sendAlarmToUser(request.getClient(), "활동 등록 알림", request.getTitle() + "이 등록되었습니다", request);
        	
        }
    	
    	
		return id;
	}
    
    public void updateActivity(Long id, String title, Byte status, Long interestCategoryId, List<Long> userIdList,
			Byte delivery, Long managerId, String startDate, String startTime, String endDate, 
			String endTime, String place, String deadlineDate, String deadlineTime, String content, 
			List<Long> volunteerIdList) {
    	
    	ActivityDto activityDto = new ActivityDto();
    	
    	activityDto.setId(id);
    	activityDto.setTitle(title);
    	activityDto.setStatus(status);
    	activityDto.setDeliveryFlag(delivery);
    	//activityDto.setPhoneAgree((phoneAgree==null)? (byte) 0:(byte) 1);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activityDto.setStartTime(LocalDateTime.parse(startDate + startTime, formatter));
    	activityDto.setEndTime(LocalDateTime.parse(endDate + endTime, formatter));
    	activityDto.setPlace(place);
    	activityDto.setDeadline(LocalDateTime.parse(deadlineDate + deadlineTime, formatter));
    	activityDto.setContent(content);
    	
    	InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
    	User manager = mUserRepository.findById(managerId).orElse(null);
    	
    	EventEntity event = activityDto.toEntity();
    	
    	event.setInterestCategory(interestCategory);
    	event.setManager(manager);
    	
    	Long eventId = mActivityRepository.save(event).getId();
    	
    	mUserEventMapperRepository.deleteByEventId(eventId);
    	
    	if(userIdList!=null) {
    		
    		for(Long userId: userIdList) {
        		UserEventMapper userEventMapper = new UserEventMapper();
        		
        		User user = mUserRepository.findById(userId).orElse(null);
        		userEventMapper.setUser(user);
        		userEventMapper.setEvent(event);
        		userEventMapper.setUserType((byte) 0);
        		
        		mUserEventMapperRepository.save(userEventMapper);
        	}
    		
    	}
    	
    	if(volunteerIdList != null) {
    		
    		for(Long volunteerId: volunteerIdList) {
        		UserEventMapper userEventMapper = new UserEventMapper();
        		
        		User volunteer = mUserRepository.findById(volunteerId).orElse(null);
        		userEventMapper.setUser(volunteer);
        		userEventMapper.setEvent(event);
        		userEventMapper.setUserType((byte) 1);
        		
        		mUserEventMapperRepository.save(userEventMapper);
        	}
    		
    	}
    	
	}
    
    public void setTitle(Long id, String title) {
		EventEntity activity = mActivityRepository.findById(id).orElse(null);
		
		activity.setTitle(title);
		
		mActivityRepository.save(activity);
		
	}
    
    public String setInterestCategory(Long id, Long interestCategoryId) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
    	
    	activity.setInterestCategory(interestCategory);
    	
    	mActivityRepository.save(activity);
    	
    	return (interestCategory!=null)?interestCategory.getName():"카테고리 없음";
	}
    
    public void setStatus(Long id, Byte status) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	if(status != activity.getStatus()) {
    		if(status == 3 && activity.getManager()!=null) {
    			sendAlarmToUser(activity.getManager(), "활동 시작 알림", activity.getTitle() + " 활동이 시작되었습니다", null);
    		}
    		else if(status == 4 && activity.getManager()!=null) {
    			sendAlarmToUser(activity.getManager(), "활동 종료 알림", activity.getTitle() + " 활동이 종료되었습니다", null);
    		}
    		
    		activity.setStatus(status);
        	
        	mActivityRepository.save(activity);
    	}
    	
	}
    
    public List<ActivityUserDto> setUsers(Long id, List<Long> userIdList, List<Byte> userStatusList, byte userType) {
    	EventEntity event = mActivityRepository.findById(id).orElse(null);
    	int index = 0;
    	
    	if(userIdList!=null) {
    		
    		for(Long userId: userIdList) {
        		UserEventMapper userEventMapper = mUserEventMapperRepository.findByEventIdAndUserIdAndUserType(id, userId, userType);
        		
        		if(userEventMapper == null) {
        			userEventMapper = new UserEventMapper();
        			User user = mUserRepository.findById(userId).orElse(null);
            		userEventMapper.setUser(user);
            		userEventMapper.setEvent(event);
            		userEventMapper.setUserType(userType);
            		userEventMapper.setLocationAgree((userStatusList==null)?(byte) 1:(byte) 0);
            		userEventMapper.setPhoneAgree((userStatusList==null)?(byte) 1:(byte) 0);
            		userEventMapper.setStatus((userStatusList==null)?(byte) 1:userStatusList.get(index));
					userEventMapper.setVolunteerTime(0);
        		}
        		else {
        			if(userType == 0) {
        				userEventMapper.setLocationAgree((byte) 1);
                		userEventMapper.setPhoneAgree((byte) 1);
                		userEventMapper.setStatus((byte) 1);
        			}
        			else {
        				userEventMapper.setStatus((userStatusList==null)?userEventMapper.getStatus():userStatusList.get(index));
        			}
        			
        		}
        		
        		mUserEventMapperRepository.save(userEventMapper);
        		
        		index++;
        	}
    		
    		List<UserEventMapper> userEventMapperList = mUserEventMapperRepository.findAllByEventIdAndUserType(id, userType);
    		
    		for(UserEventMapper userEventMapper: userEventMapperList) {
    			Long userId = userEventMapper.getUser().getId();
    			if(!userIdList.contains(userId)) {
    				mUserEventMapperRepository.deleteByEventIdAndUserIdAndUserType(id, userId, userType);
    			}
    			
    		}
    		
    	}
    	else {
    		mUserEventMapperRepository.deleteByEventIdAndUserType(id, userType);
    	}
    	
    	List<ActivityUserDto> userList = getActivityUsers(id, userIdList, userType);
    	
    	return userList;
	}
    
    public void setDelivery(Long id, Byte delivery) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	activity.setDeliveryFlag(delivery);
    	
    	mActivityRepository.save(activity);
	}
    
    /*public void setPhoneAgree(Long id, String phoneAgree) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	activity.setPhoneAgree((phoneAgree==null)? (byte) 0:(byte) 1);
    	
    	mActivityRepository.save(activity);
	}*/
    
    public void setVolunteerTime(Long id, String startDate, String startTime, String endDate, String endTime) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activity.setStartTime((startDate.isEmpty()||startTime.isEmpty())?null:LocalDateTime.parse(startDate + startTime, formatter));
    	activity.setEndTime((endDate.isEmpty()||endTime.isEmpty())?null:LocalDateTime.parse(endDate + endTime, formatter));
    	
    	mActivityRepository.save(activity);
	}
    
    public void setPlace(Long id, String place) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	activity.setPlace(place);
    	
    	mActivityRepository.save(activity);
	}
    
    public String setManager(Long id, Long managerId) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	User user = mUserRepository.findById(managerId).orElse(null);
    	
    	activity.setManager(user);
    	
    	mActivityRepository.save(activity);
    	
    	return (user!=null)?user.getName():"관리자 없음";
	}
    
    public void setContent(Long id, String content) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	activity.setContent(content);
    	
    	mActivityRepository.save(activity);
	}
    
    public void setDeadline(Long id, String deadlineDate, String deadlineTime) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activity.setDeadline((deadlineDate.isEmpty()||deadlineTime.isEmpty())?null:LocalDateTime.parse(deadlineDate + deadlineTime, formatter));
    	
    	mActivityRepository.save(activity);
	}
    
    public List<ActivityUserDto> getActivityUsers(Long id, List<Long> userIdList, byte userType){
    	List<ActivityUserDto> activityUsers = new ArrayList<>();
    	
    	if(userIdList!=null) {
    		for(Long userId: userIdList) {
       		 Optional<User> userWrapper = mUserRepository.getById(userId);
       		 UserEventMapper userEventMapper = mUserEventMapperRepository.findByEventIdAndUserId(id, userId);
       		 List<UserInterest> userInterestList = mUserInterestRepository.findByUser_Id(userId);
       		 Set<InterestCategory> interestList = new HashSet<>();

       		 for(UserInterest userInterest: userInterestList) {
				interestList.add(userInterest.getInterest());

       		 }
       	     User user = userWrapper.get();
       	     
       	     ActivityUserDto activityUser = new ActivityUserDto();
       	     
       	     activityUser.setUser(user);
       	     activityUser.setLocationAgree(userEventMapper.getLocationAgree());
       	     activityUser.setPhoneAgree(userEventMapper.getLocationAgree());
       	     activityUser.setStatus(userEventMapper.getStatus());
       	     activityUser.setStartImage(userEventMapper.getStartImage());
       	     activityUser.setEndImage(userEventMapper.getEndImage());
       	     activityUser.setRegDate(userEventMapper.getRegDate());
       	     activityUser.setModDate(userEventMapper.getModDate());
			 activityUser.setVolunteerTime(userEventMapper.getVolunteerTime());
			 activityUser.setMemo(userEventMapper.getMemo());
			 activityUser.setUserInterestList(interestList);
       	     
       	     activityUsers.add(activityUser);
       	     
    		}
    	}
    	
        
        return activityUsers;
    }
    
    public ActivityPeopleDto setPeople(Long id, List<Long> userIdList, /*List<Byte> userStatusList,*/ List<Long> volunteerIdList,
			List<Byte> volunteerStatusList, Long managerId) {
    	List<ActivityUserDto> users = setUsers(id, userIdList, null, (byte) 0);
    	List<ActivityUserDto> volunteers = setUsers(id, volunteerIdList, volunteerStatusList, (byte) 1);
    	String managerName = setManager(id, managerId);
    	
    	ActivityPeopleDto activityPeople = new ActivityPeopleDto();
    	activityPeople.setUsers(users);
    	activityPeople.setVolunteers(volunteers);
    	activityPeople.setManagerName(managerName);
    	
		return activityPeople;
	}
    
    public List<UserStatusDto> getUsersStatus(Long id, List<SimpleUser> userList) {
		List<UserStatusDto> userStatusList = new ArrayList<>();
		
		for(SimpleUser user: userList) {
			UserStatusDto userStatus = new UserStatusDto();
			userStatus.setId(user.getId());
			userStatus.setName(user.getName());
			userStatus.setPhone(user.getPhone());
			
			if(id != null) {
				UserEventMapper userEventMapper = mUserEventMapperRepository.findByEventIdAndUserId(id, user.getId());
				if(userEventMapper == null) {
					userStatus.setStatus(null);
					userStatus.setType(null);
				}
				else {
					userStatus.setStatus(userEventMapper.getStatus());
					userStatus.setType(userEventMapper.getUserType());
				}
			}
			else {
				userStatus.setStatus(null);
				userStatus.setType(null);
			}
			
			userStatusList.add(userStatus);
		
		}
    	
    	
		return userStatusList;
	}
    
    public void setTitleAndStatus(Long id, String title, Byte status) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
		
		activity.setTitle(title);
		activity.setStatus(status);
		
		if(status == 3 && activity.getManager()!=null) {
			sendAlarmToUser(activity.getManager(), "활동 시작 알림", activity.getTitle() + " 활동이 시작되었습니다", null);
		}
		else if(status == 4 && activity.getManager()!=null) {
			sendAlarmToUser(activity.getManager(), "활동 종료 알림", activity.getTitle() + " 활동이 종료되었습니다", null);
		}
		else if(status == 5) {
//			Request request = mRequestRepository.findByEventAndInterestCategory(activity, activity.getInterestCategory());

			List<UserEventMapper> byEventId = mUserEventMapperRepository.findByEventId(activity.getId());

			if(byEventId!=null) {
				for(UserEventMapper user : byEventId) {
					sendAlarmToUser(user.getUser(), "활동 취소 알림", activity.getTitle() + "이 관제사에 의해서 활동 취소되었습니다", null);
				}
			}
			
		}
		
		mActivityRepository.save(activity);
	}
    
    public String setInterestCategoryAndDeadline(Long id, Long interestCategoryId, String deadlineDate,
			String deadlineTime) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activity.setDeadline((deadlineDate.isEmpty()||deadlineTime.isEmpty())?null:LocalDateTime.parse(deadlineDate + deadlineTime, formatter));
    	
    	activity.setInterestCategory(interestCategory);
    	
    	mActivityRepository.save(activity);
    	
    	return (interestCategory!=null)?interestCategory.getName():"카테고리 없음";
	}
    
    public void setVolunteerTimeAndPlaceAndContent(Long id, String startDate, String startTime, String endDate,
												   String endTime, String place, String content, String placeDetail) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activity.setStartTime((startDate.isEmpty()||startTime.isEmpty())?null:LocalDateTime.parse(startDate + startTime, formatter));
    	activity.setEndTime((endDate.isEmpty()||endTime.isEmpty())?null:LocalDateTime.parse(endDate + endTime, formatter));
    	activity.setPlace(place);
		activity.setPlaceDetail(placeDetail);
    	activity.setContent(content);
    	
    	mActivityRepository.save(activity);
	}

    public void unregister(Long eventId, UserDto userDto) {
		UserEventMapper eventUserMapper = mUserEventMapperRepository.findByEventIdAndUserId(eventId, userDto.getId());
		eventUserMapper.setStatus((byte)2);
		mUserEventMapperRepository.save(eventUserMapper);
	}

	public List<ActivityDto> getActivitylist(Long interestCategoryId) {

		List<EventEntity> eventList = new ArrayList<>();
		if(interestCategoryId!=0) {
			InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
			eventList = mActivityRepository.findAllByEventCategoryAndInterestCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineAsc(0, interestCategory,(byte) 0, (byte) 1, (byte) 3);
			eventList.addAll(mActivityRepository.findAllByEventCategoryAndInterestCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineDesc(0, interestCategory,(byte) 0, (byte) 4, (byte) 6));
		}
		else {
			eventList = mActivityRepository.findAllByEventCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineAsc(0, (byte) 0, (byte) 1, (byte) 3);
			eventList.addAll(mActivityRepository.findAllByEventCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineDesc(0, (byte) 0, (byte) 4, (byte) 6));
		}
		List<ActivityDto> activityList = new ArrayList<>();
		Set<ActivityUserDto> activityUsers;
		Set<ActivityUserDto> activityVolunteers;
		ActivityUserDto activityUser;

		ActivityDto activity;
		List<UserEventMapper> userEventList;

		for(EventEntity event: eventList) {
			activity = convertEventEntityToActivityDto(event);
			userEventList = mUserEventMapperRepository.findAllByEventId(event.getId());
			activityUsers = new HashSet<>();
			activityVolunteers = new HashSet<>();

			for(UserEventMapper userEvent : userEventList) {
				activityUser = new ActivityUserDto();
				activityUser.setUser(userEvent.getUser());
				activityUser.setLocationAgree(userEvent.getLocationAgree());
				activityUser.setPhoneAgree(userEvent.getPhoneAgree());
				activityUser.setStatus(userEvent.getStatus());
				activityUser.setMemo((userEvent.getMemo()!=null)?userEvent.getMemo():null);

				if(userEvent.getUserType() == 1) {
					activityUser.setVolunteerTime(userEvent.getVolunteerTime());
					activityVolunteers.add(activityUser);
				}
				else {
					activityUsers.add(activityUser);
				}
			}
			activity.setActivityUsers(activityUsers);
			activity.setActivityVolunteers(activityVolunteers);
			activityList.add(activity);
		}

		return activityList;
	}

	public List<ActivityDto> getTop5Activitylist() {
		List<EventEntity> eventList = mActivityRepository.findTop5ByEventCategoryAndDeliveryFlagAndStatusGreaterThanOrderByStatusAscDeadlineAsc(0, (byte) 0, (byte) 0);
		List<ActivityDto> activityList = new ArrayList<>();
		Set<ActivityUserDto> activityUsers;
		Set<ActivityUserDto> activityVolunteers;
		ActivityUserDto activityUser;
		ActivityDto activity;
		List<UserEventMapper> userEventList;

		for(EventEntity event: eventList) {
			activity = convertEventEntityToActivityDto(event);
			userEventList = mUserEventMapperRepository.findAllByEventId(event.getId());
			activityUsers = new HashSet<>();
			activityVolunteers = new HashSet<>();
			for(UserEventMapper userEvent : userEventList) {
				activityUser = new ActivityUserDto();
				activityUser.setUser(userEvent.getUser());
				activityUser.setLocationAgree(userEvent.getLocationAgree());
				activityUser.setPhoneAgree(userEvent.getPhoneAgree());
				activityUser.setStatus(userEvent.getStatus());
				activityUser.setMemo((userEvent.getMemo()!=null)?userEvent.getMemo():null);

				if(userEvent.getUserType() == 1) {
					activityUser.setVolunteerTime(userEvent.getVolunteerTime());
					activityVolunteers.add(activityUser);
				}
				else {
					activityUsers.add(activityUser);
				}
			}
			activity.setActivityUsers(activityUsers);
			activity.setActivityVolunteers(activityVolunteers);
			activityList.add(activity);
		}
		return activityList;
	}

	public List<ActivityDto> getActivitylistForUser(UserDto user) {
		List<UserEventMapper> userEventMapperList = mUserEventMapperRepository.findAllByUserIdOrderByRegDateDesc(user.getId());
		List<ActivityDto> activityList = new ArrayList<>();
		Set<ActivityUserDto> activityUsers;
		Set<ActivityUserDto> activityVolunteers;
		ActivityUserDto activityUser;
		ActivityDto activity;
		List<UserEventMapper> userEventList;

		for(UserEventMapper userEventMapper : userEventMapperList) {
			EventEntity event = mActivityRepository.findById(userEventMapper.getEvent().getId()).orElse(null);
			if(event!=null) {
				activity = convertEventEntityToActivityDto(event);
				userEventList = mUserEventMapperRepository.findAllByEventId(event.getId());
				activityUsers = new HashSet<>();
				activityVolunteers = new HashSet<>();
				for(UserEventMapper userEvent : userEventList) {
					activityUser = new ActivityUserDto();
					activityUser.setUser(userEvent.getUser());
					activityUser.setLocationAgree(userEvent.getLocationAgree());
					activityUser.setPhoneAgree(userEvent.getPhoneAgree());
					activityUser.setStatus(userEvent.getStatus());
					activityUser.setMemo((userEvent.getMemo()!=null)?userEvent.getMemo():null);

					if(userEvent.getUserType() == 1) {
						activityUser.setVolunteerTime(userEvent.getVolunteerTime());
						activityVolunteers.add(activityUser);
					}
					else {
						activityUsers.add(activityUser);
					}
				}
				activity.setActivityUsers(activityUsers);
				activity.setActivityVolunteers(activityVolunteers);
				activityList.add(activity);
			}
		}
		return activityList;
	}

	public List<ActivityDto> getTop5ActivitylistForUser(UserDto user) {
//		List<UserEventMapper> userEventMapperList = mUserEventMapperRepository.findTop5ByUserIdOrderByRegDateDesc(user.getId());
		List<UserEventMapper> userEventMapperList = mUserEventMapperRepository.findTop5ByUserIdAndStatusOrderByRegDateDesc(user.getId(), (byte) 1);
		List<ActivityDto> activityList = new ArrayList<>();
		Set<ActivityUserDto> activityUsers;
		Set<ActivityUserDto> activityVolunteers;
		ActivityUserDto activityUser;
		ActivityDto activity;
		List<UserEventMapper> userEventList;

		for(UserEventMapper userEventMapper : userEventMapperList) {
			EventEntity event = mActivityRepository.findById(userEventMapper.getEvent().getId()).orElse(null);
			if(event!=null) {
				activity = convertEventEntityToActivityDto(event);
				userEventList = mUserEventMapperRepository.findAllByEventId(event.getId());
				activityUsers = new HashSet<>();
				activityVolunteers = new HashSet<>();
				for(UserEventMapper userEvent : userEventList) {
					activityUser = new ActivityUserDto();
					activityUser.setUser(userEvent.getUser());
					activityUser.setLocationAgree(userEvent.getLocationAgree());
					activityUser.setPhoneAgree(userEvent.getPhoneAgree());
					activityUser.setStatus(userEvent.getStatus());
					activityUser.setMemo((userEvent.getMemo()!=null)?userEvent.getMemo():null);

					if(userEvent.getUserType() == 1) {
						activityUser.setVolunteerTime(userEvent.getVolunteerTime());
						activityVolunteers.add(activityUser);
					}
					else {
						activityUsers.add(activityUser);
					}
				}
				activity.setActivityUsers(activityUsers);
				activity.setActivityVolunteers(activityVolunteers);
				activityList.add(activity);
			}
		}
		return activityList;
	}

	public void setVolunteerTime(Long id, Long volunteerId, Integer time) {
		UserEventMapper userEventMapper = mUserEventMapperRepository.findByEventIdAndUserIdAndUserType(id, volunteerId, (byte) 1);
		User user = userEventMapper.getUser();
		Integer prevTolTime = user.getVolunteerTime() - userEventMapper.getVolunteerTime();
		userEventMapper.setVolunteerTime(time);
		user.setVolunteerTime(prevTolTime + time);
		userEventMapper.setUser(user);
		mUserEventMapperRepository.save(userEventMapper);
		mUserRepository.save(user);
	}

	public void setMemoForUserEventMapper(Long eventId, Long userId, String memo) {
		UserEventMapper userEventMapper = mUserEventMapperRepository.findByEventIdAndUserId(eventId, userId);

		if(userEventMapper.getMemo()==null) {
			userEventMapper.setMemo(memo);
		}
		else {
			userEventMapper.setMemo(userEventMapper.getMemo() + "\n" + memo);
		}


	}
    
	// EventEntity -> ActivityDto conversion
		private ActivityDto convertEventEntityToActivityDto(EventEntity eventEntity) {
			
			ActivityDto activityDto = ActivityDto.builder()
					.id(eventEntity.getId())
					.title(eventEntity.getTitle())
					.status(eventEntity.getStatus())
					.interestCategory(eventEntity.getInterestCategory())
					.manager(eventEntity.getManager())
					.place(eventEntity.getPlace())
					.startTime(eventEntity.getStartTime())
					.endTime(eventEntity.getEndTime())
					.content(eventEntity.getContent())
					.deliveryFlag(eventEntity.getDeliveryFlag())
					.evaluate(eventEntity.getEvaluate())
					.deadline(eventEntity.getDeadline())
					.type(eventEntity.getType())
					.placeDetail(eventEntity.getPlaceDetail())
					.regDate(eventEntity.getRegDate())
					.modDate(eventEntity.getModDate())
					.build();
			
			return activityDto;
			
		}


	public List<EachUserActivity> getEachUserActivityList(Long id) {
		List<UserEventMapper> mapper = mUserEventMapperRepository.findAllByUserIdOrderByModDate(id);
		List<EachUserActivity> activities = new ArrayList<>();

		for(UserEventMapper m : mapper) {
			EventEntity event = m.getEvent();

			if(event.getStatus()!=4) continue;

			EachUserActivity activity = new EachUserActivity();

			activity.setId(event.getId());
			activity.setEventCategory(event.getEventCategory());
			activity.setTitle(event.getTitle());
			activity.setInterestCategory(event.getInterestCategory());
			activity.setCompleteTime(event.getEndTime());
			activity.setVolunteerTime(m.getVolunteerTime());

			activities.add(activity);
		}

		return activities;
	}

	public List<ActivityDto> getAllActivities() {

    	List<EventEntity> lists = mActivityRepository.findAllByOrderByStatusAscModDateDesc();
		List<ActivityDto> ret = new ArrayList<>();

		for(EventEntity activity : lists) {
			ret.add(this.convertEventEntityToActivityDto(activity));
		}

		return ret;
	}
	
	public void sendAlarmToUser(User user, String title, String content, Request request) {
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setMessage(title);
		
		if(request!=null) {
			notification.setRequest(request);
		}

		mNotificationRepository.save(notification);
		
		MakeJSON makeJson = new MakeJSON();
		Push push = new Push();
		String pushJson = "";
		List<Device> devices = user.getDevices();
		
		if(devices!=null) {
			for(Device device: devices) {
				pushJson = makeJson.makePush(device.getDeviceToken(), title, content); 
				push.sendPush(pushJson);
			}
		}
	}
}
