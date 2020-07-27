package com.sangdaero.walab.request.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.sangdaero.walab.activity.dto.ActivityForm;
import com.sangdaero.walab.common.entity.*;
import com.sangdaero.walab.common.file.repository.FileRepository;
import com.sangdaero.walab.common.notification.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.request.repository.RequestRepository;
import com.sangdaero.walab.request.dto.RequestDto;
import com.sangdaero.walab.activity.domain.repository.ActivityRepository;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserEventMapperRepository;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.domain.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RequestService {
	
	private RequestRepository mRequestRepository;
	private InterestRepository mInterestRepository;
	private UserRepository mUserRepository;
	private ActivityRepository mActivityRepository;
	private UserEventMapperRepository mUserEventMapperRepository;
	private FileRepository mFileRepository;
	private NotificationRepository mNotificationRepository;

    private static final int PAGE_POSTCOUNT = 8;  // 한 페이지에 존재하는 게시글 수

	// constructor
	public RequestService(RequestRepository requestRepository, InterestRepository interestRepository, 
			UserRepository userRepository, UserEventMapperRepository userEventMapperRepository,
						  ActivityRepository activityRepository, FileRepository fileRepository,
						  NotificationRepository notificationRepository) {
		mRequestRepository = requestRepository;
		mInterestRepository = interestRepository;
		mUserRepository = userRepository;
		mUserEventMapperRepository = userEventMapperRepository;
		mActivityRepository = activityRepository;
		mFileRepository = fileRepository;
		mNotificationRepository = notificationRepository;
	}
	
	// getRequestlist -> convertEntitytoDto
    public List<RequestDto> getRequestlist(Integer pageNum, String keyword, Integer interestType, Integer sortType) {
    	Page<Request> page;
    	
    	if(interestType == 0) {
			page = mRequestRepository.findAllByTitleContainingOrderByStatusAsc(keyword, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    	}
    	else {
    		InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);

			page = mRequestRepository.findAllByTitleContainingAndInterestCategoryOrderByStatusAsc(keyword, interestCategory, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    	}
    	
    	
        List<Request> requests = page.getContent();
        List<RequestDto> requestDtoList = new ArrayList<>();

        for(Request request : requests) {
        	requestDtoList.add(this.convertRequestToDto(request));
        }
        
        return requestDtoList;
    }

	public Long getRequestCount(String keyword, Integer interestType) {
    	if(interestType == 0) {
    		return mRequestRepository.countByTitleContaining(keyword);
    	}
    	else {
    		InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    		return mRequestRepository.countByTitleContainingAndInterestCategory(keyword, interestCategory);
    	}
    	
    }

	public int getFirstPage(Integer curPageNum, String keyword, Integer interestType) {
		// 총 게시글 수
		Double postsTotalCount = Double.valueOf(this.getRequestCount(keyword, interestType));

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
    
    public RequestDto getPost(Long id) {
    	Optional<Request> RequestWrapper = mRequestRepository.getById(id);
        Request request = RequestWrapper.get();
        
        RequestDto requestDto = this.convertRequestToDto(request);
    	
    	return requestDto;
	}

	public RequestDto setStatus(Long id, Byte status) {
		
    	Request request = mRequestRepository.findById(id).orElse(null);
    	
    	request.setStatus(status);

		Notification notification;

		if(status == 0) {
			notification = mNotificationRepository.findByRequestAndMessageContaining(request, "거절");

			mNotificationRepository.deleteById(notification.getId());
		}
		else if(status == 2) {
			notification = new Notification();

			notification.setUser(request.getClient());
			notification.setRequest(request);
			notification.setMessage(request.getTitle() + "이 거절되었습니다");
			mNotificationRepository.save(notification);
		}

		mRequestRepository.save(request);

		return convertRequestToDto(request);
	}
    
    public Long registerRequestToEvent(Long id) {
		
    	Request request = mRequestRepository.findById(id).orElse(null);
    	
    	UserEventMapper userEventMapper = new UserEventMapper();
    	userEventMapper.setEvent(request.getEvent());
    	userEventMapper.setUser(request.getClient());
    	userEventMapper.setStatus((byte) 1);
    	userEventMapper.setLocationAgree((byte) 1);
    	userEventMapper.setPhoneAgree((byte) 1);
		userEventMapper.setUserType(request.getUserType());
		userEventMapper.setVolunteerTime(0);
    	
    	mUserEventMapperRepository.save(userEventMapper);

		Notification notification = new Notification();

    	request.setStatus((byte) 1);

		notification.setUser(request.getClient());
		notification.setRequest(request);
		notification.setMessage(request.getTitle() + "이 승인되었습니다");
    	
    	mRequestRepository.save(request);
		mNotificationRepository.save(notification);
    	
		return request.getEvent().getId();
	}

	public void createRequest(Long eventId, Long interestCategoryId, UserDto userDto, MultipartFile multipartFile, Byte userType,
							  String startTime, String endTime, String title, String memo) {
		Request request = new Request();
		
		User client = mUserRepository.findById(userDto.getId()).orElse(null);
		EventEntity event = (eventId != null)?mActivityRepository.findById(eventId).orElse(null):null;
		InterestCategory interestCategory = (eventId != null)?event.getInterestCategory():mInterestRepository.findById(interestCategoryId).orElse(null);
		
		request.setClient(client);
		request.setEvent(event);
		request.setInterestCategory(interestCategory);
		request.setStatus((byte)0);
		if(title==null) {
			if(eventId==null) {
				request.setTitle(userDto.getName() + "님의 나눔 신청");
			}
			else {
				if(userType==1) {
					request.setTitle(userDto.getName() + "님의 " + event.getTitle() + " 봉사자 참여 신청");
				}
				else if(userType==0) {
					request.setTitle(userDto.getName() + "님의 " + event.getTitle() + " 이용자 참여 신청");
				}
			}

		}
		else {
			request.setTitle(title);
		}
		request.setUserType(userType);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		if(startTime!=null) {
			request.setStartTime(LocalDateTime.parse(startTime, formatter));
		}
		if(endTime!=null) {
			request.setEndTime(LocalDateTime.parse(endTime, formatter));
		}
		request.setContent(memo);

		if(multipartFile!=null && !multipartFile.isEmpty()) {
			Path currentPath = Paths.get("");
			Path absolutePath = currentPath.toAbsolutePath();
//			String url = "/src/main/resources/static/images/";	//로컬 용
			String url = "/tomcat/webapps/ROOT/WEB-INF/classes/static/images/";
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + multipartFile.getOriginalFilename();
			Path fileNameAndPath = Paths.get(absolutePath + url, fileName);
			try {
				Files.write(fileNameAndPath, multipartFile.getBytes());

				request.setProductImage(fileName);


				mRequestRepository.save(request);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			mRequestRepository.save(request);
		}
		
	}
    
 // Request -> RequestDto conversion
 		private RequestDto convertRequestToDto(Request request) {
 			
 			RequestDto requestDto = RequestDto.builder()
 					.id(request.getId())
 					.title(request.getTitle())
 					.interestCategory(request.getInterestCategory())
 					.client(request.getClient())
 					.status(request.getStatus())
 					.event(request.getEvent())
					.productImage(request.getProductImage())
					.userType(request.getUserType())
					.startTime(request.getStartTime())
					.endTime(request.getEndTime())
					.content(request.getContent())
 					.regDate(request.getRegDate())
 					.modDate(request.getModDate())
 					.build();
 			
 			return requestDto;
 			
 		}


	public Long getAllRequestNum() {
		return mRequestRepository.count();
	}

	public ActivityForm getActivityForm(RequestDto requestDto) {

		ActivityForm activityForm = new ActivityForm();
		List<Long> userId = new ArrayList<>();
		List<Byte> userStatus = new ArrayList<>();
		String interestName = requestDto.getInterestCategory().getName();

		activityForm.setTitle(requestDto.getTitle());
		activityForm.setContent(requestDto.getContent());
		activityForm.setStartDate((requestDto.getStartTime()!=null && !interestName.contains("나눔"))?requestDto.getStartTime().toLocalDate().toString():null);
		activityForm.setStartTime((requestDto.getStartTime()!=null && !interestName.contains("나눔"))?requestDto.getStartTime().toLocalTime().toString():null);
		activityForm.setEndDate((requestDto.getEndTime()!=null && !interestName.contains("나눔"))?requestDto.getEndTime().toLocalDate().toString():null);
		activityForm.setEndTime((requestDto.getEndTime()!=null && !interestName.contains("나눔"))?requestDto.getEndTime().toLocalTime().toString():null);
		activityForm.setRequestId(requestDto.getId());
		activityForm.setFile(requestDto.getProductImage());

		userId.add(requestDto.getClient().getId());
		userStatus.add((byte) 1);

		if(!interestName.contains("나눔")) {
			if(requestDto.getUserType() == 1) {
				activityForm.setVolunteerId(userId);
				activityForm.setVolunteerStatus(userStatus);
			}
			else {
				activityForm.setUserId(userId);
			}
		}
		else {
			InterestCategory interestCategory = mInterestRepository.findByNameContaining("전달");
			if(interestCategory!=null) {
				activityForm.setInterestCategoryId(interestCategory.getId());
			}
			else {
				activityForm.setInterestCategoryId(requestDto.getInterestCategory().getId());

			}
		}

		return activityForm;
	}
}
