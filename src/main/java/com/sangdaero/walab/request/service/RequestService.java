package com.sangdaero.walab.request.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.sangdaero.walab.common.file.repository.FileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.request.repository.RequestRepository;
import com.sangdaero.walab.request.dto.RequestDto;
import com.sangdaero.walab.activity.domain.repository.ActivityRepository;
import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.Request;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.common.entity.UserEventMapper;
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
	private static final int BLOCK_PAGE_NUMCOUNT = 6; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POSTCOUNT = 3;  // 한 페이지에 존재하는 게시글 수

	// constructor
	public RequestService(RequestRepository requestRepository, InterestRepository interestRepository, 
			UserRepository userRepository, UserEventMapperRepository userEventMapperRepository,
		    ActivityRepository activityRepository, FileRepository fileRepository) {
		mRequestRepository = requestRepository;
		mInterestRepository = interestRepository;
		mUserRepository = userRepository;
		mUserEventMapperRepository = userEventMapperRepository;
		mActivityRepository = activityRepository;
		mFileRepository = fileRepository;
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

	public void createRequest(Long eventId, Long interestCategoryId, UserDto userDto, MultipartFile multipartFile) {
		Request request = new Request();
		
		User client = mUserRepository.findById(userDto.getId()).orElse(null);
		EventEntity event = (eventId != null)?mActivityRepository.findById(eventId).orElse(null):null;
		InterestCategory interestCategory = (eventId != null)?event.getInterestCategory():mInterestRepository.findById(interestCategoryId).orElse(null);
		
		request.setClient(client);
		request.setEvent(event);
		request.setInterestCategory(interestCategory);
		request.setStatus((byte)0);
		request.setTitle((eventId!=null)?userDto.getName() + "님이 " + event.getTitle() + "을/를 봉사자로 참여하기 원하십니다":userDto.getName() + "님이 새로운 활동으로 봉사를 하기 원하십니다");

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
				System.out.println("hello");
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
 					.regDate(request.getRegDate())
 					.modDate(request.getModDate())
 					.build();
 			
 			return requestDto;
 			
 		}


	public Long getAllRequestNum() {
		return mRequestRepository.count();
	}
}
