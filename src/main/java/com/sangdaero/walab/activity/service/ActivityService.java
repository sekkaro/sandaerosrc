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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sangdaero.walab.activity.domain.repository.ActivityRepository;
import com.sangdaero.walab.activity.dto.ActivityDto;
import com.sangdaero.walab.activity.dto.ActivityPeopleDto;
import com.sangdaero.walab.activity.dto.ActivityUserDto;
import com.sangdaero.walab.activity.dto.UserStatusDto;
import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.FileEntity;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.Request;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.common.entity.UserEventMapper;
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
	
	private static final int BLOCK_PAGE_NUMCOUNT = 12; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POSTCOUNT = 3;  // 한 페이지에 존재하는 게시글 수

	// constructor
	public ActivityService(ActivityRepository activityRepository, InterestRepository interestRepository, 
			UserRepository userRepository, UserEventMapperRepository userEventMapperRepository,
			FileRepository fileRepository, RequestRepository requestRepository) {
		mActivityRepository = activityRepository;
		mInterestRepository = interestRepository;
		mUserRepository = userRepository;
		mUserEventMapperRepository = userEventMapperRepository;
		mFileRepository = fileRepository;
		mRequestRepository = requestRepository;
	}
	
	
	// getActivitylist -> convertEntitytoDto
    public List<ActivityDto> getActivitylist(Integer pageNum, String keyword, Integer interestType, Integer status, Integer sortType) {
    	Page<EventEntity> page;
    	
    	if(status == 0) {
    		if(interestType == 0) {
    			page = mActivityRepository.findAllByEventCategoryAndTitleContaining(0, keyword, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			page = mActivityRepository.findAllByEventCategoryAndTitleContainingAndInterestCategory(0, keyword, interestCategory, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    	}
    	else {
    		if(interestType == 0) {
    			page = mActivityRepository.findAllByEventCategoryAndTitleContainingAndStatus(0, keyword, (--status).byteValue(), PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			page = mActivityRepository.findAllByEventCategoryAndTitleContainingAndInterestCategoryAndStatus(0, keyword, interestCategory, (--status).byteValue(), PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    	}
    	
        List<EventEntity> activities = page.getContent();
        List<ActivityDto> activityDtoList = new ArrayList<>();

        for(EventEntity activity : activities) {
        	activityDtoList.add(this.convertEventEntityToActivityDto(activity));
        }
        
        return activityDtoList;
    }
    
 // getPageList -> getActivityCount
    public Integer[] getPageList(Integer curPageNum, String keyword, Integer interestType, Integer sortType, Integer status) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUMCOUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getActivityCount(keyword, interestType, status));

        // 총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POSTCOUNT)));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUMCOUNT)
                ? curPageNum + BLOCK_PAGE_NUMCOUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;

        // 페이지 번호 할당
        for(int val=curPageNum, i=0;val<=blockLastPageNum;val++, i++) {
            pageList[i] = val;
        }

        return pageList;
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
        
        for(UserEventMapper userEvent: userEventList) {
        	if(userEvent.getUserType() == 0) {
        		ActivityUserDto activityUser = new ActivityUserDto();
        		
        		activityUser.setUser(userEvent.getUser());
        		activityUser.setStatus(userEvent.getStatus());
        		activityUser.setPhoneAgree(userEvent.getPhoneAgree());
        		activityUser.setLocationAgree(userEvent.getLocationAgree());
        		activityUser.setStartImage(userEvent.getStartImage());
        		activityUser.setEndImage(userEvent.getEndImage());
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
        		activityVolunteer.setRegDate(userEvent.getRegDate());
        		activityVolunteer.setModDate(userEvent.getModDate());
        		
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
		    String deadlineDate, String deadlineTime, String content, List<Long> volunteerIdList, List<Byte> volunteerStatusList, MultipartFile[] files, Long requestId, String requestFileName) {
		
    	ActivityDto activityDto = new ActivityDto();
    	
    	activityDto.setTitle(title);
    	activityDto.setDeliveryFlag(delivery);
    	//activityDto.setPhoneAgree((phoneAgree==null)? (byte) 0:(byte) 1);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activityDto.setStartTime((startDate.isEmpty()||startTime.isEmpty())?null:LocalDateTime.parse(startDate + startTime, formatter));
    	activityDto.setEndTime((endDate.isEmpty()||endTime.isEmpty())?null:LocalDateTime.parse(endDate + endTime, formatter));
    	activityDto.setPlace(place);
    	activityDto.setDeadline((deadlineDate.isEmpty()||deadlineTime.isEmpty())?null:LocalDateTime.parse(deadlineDate + deadlineTime, formatter));
    	activityDto.setContent(content);
    	
    	InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
    	User manager = mUserRepository.findById(managerId).orElse(null);
    	
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
        		//userEventMapper.setLocationAgree((byte) ((userStatusList.get(index)==1)?1:0));
        		//userEventMapper.setPhoneAgree((byte) ((userStatusList.get(index)==1)?1:0));
        		//userEventMapper.setStatus(userStatusList.get(index));
        		
        		mUserEventMapperRepository.save(userEventMapper);
        		
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
        		
        		mUserEventMapperRepository.save(userEventMapper);
        		
        		index++;
        	}
    		
    	}
    		
    	Path currentPath = Paths.get("");
		Path absolutePath = currentPath.toAbsolutePath();

		String url = "/tomcat/webapps/ROOT/WEB-INF/classes/static/images/";

//    	String url = "/src/main/resources/static/images/";
    		
        for(MultipartFile file: files) {
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
    	
    	activity.setStatus(status);
    	
    	mActivityRepository.save(activity);
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
			String endTime, String place, String content) {
    	EventEntity activity = mActivityRepository.findById(id).orElse(null);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	activity.setStartTime((startDate.isEmpty()||startTime.isEmpty())?null:LocalDateTime.parse(startDate + startTime, formatter));
    	activity.setEndTime((endDate.isEmpty()||endTime.isEmpty())?null:LocalDateTime.parse(endDate + endTime, formatter));
    	activity.setPlace(place);
    	activity.setContent(content);
    	
    	mActivityRepository.save(activity);
	}

    public void unregister(Long eventId, UserDto userDto) {
		UserEventMapper eventUserMapper = mUserEventMapperRepository.findByEventIdAndUserId(eventId, userDto.getId());
		eventUserMapper.setStatus((byte)2);
		mUserEventMapperRepository.save(eventUserMapper);
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
					.regDate(eventEntity.getRegDate())
					.modDate(eventEntity.getModDate())
					.build();
			
			return activityDto;
			
		}
		
}
