package com.sangdaero.walab.request.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.request.domain.repository.RequestRepository;
import com.sangdaero.walab.request.dto.RequestDto;

@Service
public class RequestService {

	private RequestRepository mRequestRepository;
	private static final int BLOCK_PAGE_NUMCOUNT = 6; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POSTCOUNT = 3;  // 한 페이지에 존재하는 게시글 수

	// constructor
	public RequestService(RequestRepository requestRepository) {
		mRequestRepository = requestRepository;
	}
	
	
	// getRequestlist -> convertEntitytoDto
    public List<RequestDto> getRequestlist(Integer pageNum, String keyword, Integer searchType, Integer sortType) {
    	Page<EventEntity> page;

   		switch(searchType) {
    		// Search by Content
    		case 1:
    			if(sortType == 1) {
    				page = mRequestRepository.findAllByTitleContainingAndEventCategory(keyword, 0, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			}
    			else {
    				page = mRequestRepository.findAllByTitleContainingAndEventCategory(keyword, 0, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "userName")));
    			}
    			break;
    		// Search by Writer
    		case 2:
    			if(sortType == 1) {
    				page = mRequestRepository.findAllByUserNameContainingAndEventCategory(keyword, 0, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			}
    			else {
    				page = mRequestRepository.findAllByUserNameContainingAndEventCategory(keyword, 0, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "userName")));
    			}
    			break;
    		// Requests without search
    		default:
    			if(sortType == 1) {
    				page = mRequestRepository.findAllByEventCategory(0, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			}
    			else {
    				page = mRequestRepository.findAllByEventCategory(0, PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "userName")));
    			}
    			break;
    	}
    	
        List<EventEntity> requests = page.getContent();
        List<RequestDto> requestDtoList = new ArrayList<>();

        for(EventEntity request : requests) {
        	requestDtoList.add(this.convertEventEntityToRequestDto(request));
        }
        
        return requestDtoList;
    }
    
 // getPageList -> getRequestCount
    public Integer[] getPageList(Integer curPageNum, String keyword, Integer searchType, Integer sortType) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUMCOUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getRequestCount(keyword, searchType));

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
    
    public Long getRequestCount(String keyword, Integer searchType) {
    	switch(searchType) {
    		case 1:
    	    	return mRequestRepository.countByTitleContainingAndEventCategory(keyword , 0);
    		case 2:
    			return mRequestRepository.countByUserNameContainingAndEventCategory(keyword, 0);
    		default:
    			return mRequestRepository.countByEventCategory(0);
    	}
    }
    
 // Detail of id's notice
    public RequestDto getPost(Long id) {
        Optional<EventEntity> RequestWrapper = mRequestRepository.getById(id);
        EventEntity request = RequestWrapper.get();

        RequestDto requestDto = this.convertEventEntityToRequestDto(request);
        
        return requestDto;
    }
	
	// EventEntity -> RequestDto conversion
		private RequestDto convertEventEntityToRequestDto(EventEntity eventEntity) {
			
			RequestDto requestDto = RequestDto.builder()
					.id(eventEntity.getId())
					.title(eventEntity.getTitle())
					.status(eventEntity.getStatus())
					.userName(eventEntity.getUserName())
					.userTaker(eventEntity.getUserTaker())
					.interestCategory(eventEntity.getInterestCategory())
					.volunteers(eventEntity.getVolunteers())
					.userVolunteer(eventEntity.getUserVolunteer())
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
