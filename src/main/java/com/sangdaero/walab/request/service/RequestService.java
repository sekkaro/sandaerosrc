package com.sangdaero.walab.request.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.request.repository.RequestRepository;
import com.sangdaero.walab.request.dto.RequestDto;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.Request;
import com.sangdaero.walab.common.entity.UserEventMapper;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserEventMapperRepository;
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
	public RequestService(RequestRepository requestRepository, InterestRepository interestRepository, 
			UserRepository userRepository, UserEventMapperRepository userEventMapperRepository) {
		mRequestRepository = requestRepository;
		mInterestRepository = interestRepository;
		mUserRepository = userRepository;
		mUserEventMapperRepository = userEventMapperRepository;
	}
	
	// getRequestlist -> convertEntitytoDto
    public List<RequestDto> getRequestlist(Integer pageNum, String keyword, Integer interestType, Integer sortType) {
    	Page<Request> page;
    	
    	if(interestType == 0) {
    		page = mRequestRepository.findAllByTitleContaining(keyword, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    	}
    	else {
    		InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    		page = mRequestRepository.findAllByTitleContainingAndInterestCategory(keyword, interestCategory, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by((sortType==1)?Sort.Direction.DESC:Sort.Direction.ASC, "regDate")));
    	}
    	
    	
        List<Request> requests = page.getContent();
        List<RequestDto> requestDtoList = new ArrayList<>();

        for(Request request : requests) {
        	requestDtoList.add(this.convertRequestToDto(request));
        }
        
        return requestDtoList;
    }
    
 // getPageList -> getrequestCount
    public Integer[] getPageList(Integer curPageNum, String keyword, Integer interestType, Integer sortType) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUMCOUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getrequestCount(keyword, interestType));

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
    
    public Long getrequestCount(String keyword, Integer interestType) {
    	if(interestType == 0) {
    		return mRequestRepository.countByTitleContaining(keyword);
    	}
    	else {
    		InterestCategory interestCategory = mInterestRepository.findById(interestType.longValue()).orElse(null);
    			
    		return mRequestRepository.countByTitleContainingAndInterestCategory(keyword, interestCategory);
    	}
    	
    }
    
    public RequestDto getPost(Long id) {
    	Optional<Request> RequestWrapper = mRequestRepository.getById(id);
        Request request = RequestWrapper.get();
        
        RequestDto requestDto = this.convertRequestToDto(request);
    	
    	return requestDto;
	}
    
    public void setStatus(Long id, Byte status) {
		
    	Request request = mRequestRepository.findById(id).orElse(null);
    	
    	request.setStatus(status);
    	
    	mRequestRepository.save(request);
    	
	}
    
    public Long registerRequestToEvent(Long id) {
		
    	Request request = mRequestRepository.findById(id).orElse(null);
    	
    	UserEventMapper userEventMapper = new UserEventMapper();
    	userEventMapper.setEvent(request.getEvent());
    	userEventMapper.setUser(request.getClient());
    	userEventMapper.setStatus((byte) 1);
    	userEventMapper.setLocationAgree((byte) 1);
    	userEventMapper.setPhoneAgree((byte) 1);
    	userEventMapper.setUserType((byte) 1);
    	
    	mUserEventMapperRepository.save(userEventMapper);
    	
    	request.setStatus((byte) 1);
    	
    	mRequestRepository.save(request);
    	
		return request.getEvent().getId();
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
 					.regDate(request.getRegDate())
 					.modDate(request.getModDate())
 					.build();
 			
 			return requestDto;
 			
 		}

		

}
