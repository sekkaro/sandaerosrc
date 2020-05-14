package com.sangdaero.walab.request.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.common.entity.UserEventMapper;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserEventMapperRepository;
import com.sangdaero.walab.request.domain.repository.RequestRepository;
import com.sangdaero.walab.request.dto.RequestDto;
import com.sangdaero.walab.user.domain.repository.UserRepository;

@Service
public class RequestService {

	private RequestRepository mRequestRepository;
	private InterestRepository mInterestRepository;
	private UserRepository mUserRepository;
	private UserEventMapperRepository mUserEventMapperRepository;
	private static final int BLOCK_PAGE_NUMCOUNT = 6; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POSTCOUNT = 3;  // 한 페이지에 존재하는 게시글 수

	// constructor
	public RequestService(RequestRepository requestRepository, InterestRepository interestRepository, UserRepository userRepository, UserEventMapperRepository userEventMapperRepository) {
		mRequestRepository = requestRepository;
		mInterestRepository = interestRepository;
		mUserRepository = userRepository;
		mUserEventMapperRepository = userEventMapperRepository;
	}
	
	
	// getRequestlist -> convertEntitytoDto
    public List<RequestDto> getRequestlist(Integer pageNum, String keyword, Integer interestType, Integer status, Integer sortType) {
    	Page<EventEntity> page;
    	
    	if(status == 0) {
    		if(interestType == 0) {
    			page = mRequestRepository.findAllByEventCategoryAndTitleContaining(0, keyword, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			page = mRequestRepository.findAllByEventCategoryAndTitleContainingAndInterestCategory(0, keyword, interestCategory, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    	}
    	else {
    		if(interestType == 0) {
    			page = mRequestRepository.findAllByEventCategoryAndTitleContainingAndStatus(0, keyword, (--status).byteValue(), PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			page = mRequestRepository.findAllByEventCategoryAndTitleContainingAndInterestCategoryAndStatus(0, keyword, interestCategory, (--status).byteValue(), PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    		}
    	}
    	
        List<EventEntity> requests = page.getContent();
        List<RequestDto> requestDtoList = new ArrayList<>();

        for(EventEntity request : requests) {
        	requestDtoList.add(this.convertEventEntityToRequestDto(request));
        }
        
        return requestDtoList;
    }
    
 // getPageList -> getRequestCount
    public Integer[] getPageList(Integer curPageNum, String keyword, Integer interestType, Integer sortType, Integer status) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUMCOUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getRequestCount(keyword, interestType, status));

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
    
    public Long getRequestCount(String keyword, Integer interestType, Integer status) {
    	if(status == 0) {
    		if(interestType == 0) {
    			return mRequestRepository.countByEventCategoryAndTitleContaining(0, keyword);
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			return mRequestRepository.countByEventCategoryAndTitleContainingAndInterestCategory(0, keyword, interestCategory);
    		}
    	}
    	else {
    		if(interestType == 0) {
    			return mRequestRepository.countByEventCategoryAndTitleContainingAndStatus(0, keyword, (--status).byteValue());
    		}
    		else {
    			InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    			return mRequestRepository.countByEventCategoryAndTitleContainingAndInterestCategoryAndStatus(0, keyword, interestCategory, (--status).byteValue());
    		}
    	}
    }
    
 // Detail of id's notice
    public RequestDto getPost(Long id) {
        Optional<EventEntity> RequestWrapper = mRequestRepository.getById(id);
        EventEntity request = RequestWrapper.get();
        
        RequestDto requestDto = this.convertEventEntityToRequestDto(request);
        
        List<UserEventMapper> userEventList = mUserEventMapperRepository.findAllByEventId(id);
        
        Set<User> users = new HashSet<>();
        Set<Long> userIds = new HashSet<>();
        Set<User> volunteers = new HashSet<>();
        Set<Long> volunteerIds = new HashSet<>();
        
        for(UserEventMapper userEvent: userEventList) {
        	if(userEvent.getUserType() == 0) {
        		users.add(userEvent.getUser());
        		userIds.add(userEvent.getUser().getId());
        	}
        	else {
        		volunteers.add(userEvent.getUser());
        		volunteerIds.add(userEvent.getUser().getId());
        	}
        }
        
        requestDto.setUsers(users);
        requestDto.setUserIds(userIds);
        
        requestDto.setVolunteers(volunteers);
        requestDto.setVolunteerIds(volunteerIds);
        
        return requestDto;
    }
    
    @Transactional
    public Long saveRequest(String title, Long interestCategoryId, List<Long> userIdList, Byte delivery, String phoneAgree, 
    		String locationAgree, Long managerId, String startDate, String startTime, String endDate, String endTime, String place, 
    		String deadlineDate, String deadlineTime, String content, List<Long> volunteerIdList) {
		
    	RequestDto requestDto = new RequestDto();
    	
    	requestDto.setTitle(title);
    	requestDto.setDeliveryFlag(delivery);
    	requestDto.setPhoneAgree((phoneAgree==null)? (byte) 0:(byte) 1);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	requestDto.setStartTime(LocalDateTime.parse(startDate + startTime, formatter));
    	requestDto.setEndTime(LocalDateTime.parse(endDate + endTime, formatter));
    	requestDto.setPlace(place);
    	requestDto.setDeadline(LocalDateTime.parse(deadlineDate + deadlineTime, formatter));
    	requestDto.setContent(content);
    	
    	InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
    	User manager = mUserRepository.findById(managerId).orElse(null);
    	
    	EventEntity event = requestDto.toEntity();
    	
    	event.setInterestCategory(interestCategory);
    	event.setManager(manager);
    	
    	Long id = mRequestRepository.save(event).getId();
    	
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
    	
		return id;
	}
    
    public void updateRequest(Long id, String title, Byte status, Long interestCategoryId, List<Long> userIdList,
			Byte delivery, String phoneAgree, String locationAgree, Long managerId, String startDate,
			String startTime, String endDate, String endTime, String place, String deadlineDate,
			String deadlineTime, String content, List<Long> volunteerIdList) {
    	
    	RequestDto requestDto = new RequestDto();
    	
    	requestDto.setId(id);
    	requestDto.setTitle(title);
    	requestDto.setStatus(status);
    	requestDto.setDeliveryFlag(delivery);
    	requestDto.setPhoneAgree((phoneAgree==null)? (byte) 0:(byte) 1);
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm");
    	
    	requestDto.setStartTime(LocalDateTime.parse(startDate + startTime, formatter));
    	requestDto.setEndTime(LocalDateTime.parse(endDate + endTime, formatter));
    	requestDto.setPlace(place);
    	requestDto.setDeadline(LocalDateTime.parse(deadlineDate + deadlineTime, formatter));
    	requestDto.setContent(content);
    	
    	InterestCategory interestCategory = mInterestRepository.findById(interestCategoryId).orElse(null);
    	User manager = mUserRepository.findById(managerId).orElse(null);
    	
    	EventEntity event = requestDto.toEntity();
    	
    	event.setInterestCategory(interestCategory);
    	event.setManager(manager);
    	
    	Long eventId = mRequestRepository.save(event).getId();
    	
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
	
	// EventEntity -> RequestDto conversion
		private RequestDto convertEventEntityToRequestDto(EventEntity eventEntity) {
			
			RequestDto requestDto = RequestDto.builder()
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
					.phoneAgree(eventEntity.getPhoneAgree())
					.evaluate(eventEntity.getEvaluate())
					.deadline(eventEntity.getDeadline())
					.regDate(eventEntity.getRegDate())
					.modDate(eventEntity.getModDate())
					.build();
			
			return requestDto;
			
		}
		
}
