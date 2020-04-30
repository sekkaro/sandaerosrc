package com.sangdaero.walab.user.application.service;

import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.*;

import com.sangdaero.walab.common.entity.*;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserEventMapperRepository;
import com.sangdaero.walab.mapper.repository.UserInterestRepository;
import com.sangdaero.walab.request.domain.repository.RequestRepository;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.dto.UserDetailDto;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService extends OidcUserService {
	
	private final InterestRepository mInterestRepository;
    private final UserRepository mUserRepository;
    private final UserInterestRepository mUserInterestRepository;
    private final RequestRepository mRequestRepository;
    private final UserEventMapperRepository mUserEventRepository;
	
	@Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
        UserDto userDto = new UserDto();
        userDto.setName((String) attributes.get("name"));
        userDto.setSocialId((String) attributes.get("email"));
        
        
        updateUser(userDto);
        return oidcUser;
    }

    private void updateUser(UserDto userDto) {
        User user = mUserRepository.findBySocialId(userDto.getSocialId());
        if(user == null) {
        	userDto.setNickname("닉네임");
        	userDto.setPhone("010-XXXX-XXXX");
            userDto.setUserType((byte) 1); // TODO 나중에 0(이용자)로 바꿔야 함
            userDto.setStatus((byte) 1);
            user = userDto.toEntity(); 
            user.setLocationAgree((byte) 0);
            user.setPhoneAgree((byte) 0);
            user.setVolunteerTime(0);
        }
        
        user.setLastLogin(LocalDateTime.now());
        
		mUserRepository.save(user);
    }
    
    public UserDto getUser(@AuthenticationPrincipal OAuth2User principal) {
    	User user = mUserRepository.findBySocialId(principal.getAttribute("email"));
    	
    	UserDto userDto = convertEntityToDto(user);
    	
    	return userDto;
    }
    
    public void setStatus(@AuthenticationPrincipal OAuth2User principal, Boolean isOn) {
    	User user = mUserRepository.findBySocialId(principal.getAttribute("email"));
    	
    	if(isOn) {
    		user.setStatus((byte) 1);
    	}
    	else {
    		user.setStatus((byte) 0);
    	}
    	
    	mUserRepository.save(user);
    	
    }

    public void addUser(UserDto userDTO) {

        mUserInterestRepository.deleteByUser_Id(userDTO.getId());

        if(userDTO.getUserInterestList()!=null) {

            for (String e :userDTO.getUserInterestList()) {
                InterestCategory interestList = mInterestRepository.findByNameEquals(e);
                userDTO.getInterests().add(interestList);
            }
        }

        User user = mUserRepository.save(userDTO.toEntity());

        for(InterestCategory interest : userDTO.getInterests()) {
            UserInterest userInterest = new UserInterest();

            userInterest.setInterest(interest);
            userInterest.setUser(user);

            mUserInterestRepository.save(userInterest);
        }
    }

    public Page<User> getSimpleUserPageList(Pageable pageable, String keyword, Integer condition) {
        Page<User> simpleUserPage;

        if(condition==1) {
            simpleUserPage = mUserRepository.findAllByNameContaining(keyword, pageable);
        } else if(condition==2) {
            simpleUserPage = mUserRepository.findAllByNicknameContaining(keyword, pageable);
        } else {
            simpleUserPage = mUserRepository.findAll(pageable);
        }

        return simpleUserPage;
    }

    public List<SimpleUser> getSimpleUserList() {
        List<SimpleUser> simpleUserList = mUserRepository.findAllByOrderByName();
        return simpleUserList;
    }
    
    public List<SimpleUser> getSimpleUserList(String type){
    	List<SimpleUser> simpleUserList = new ArrayList<>();
    	
    	if(type=="manager") {
    		simpleUserList = mUserRepository.findAllByUserTypeOrderByName((byte) 1);
    	}
    	else {
    		simpleUserList = mUserRepository.findAllByUserTypeOrderByName((byte) 0);
    	}
    	
    	return simpleUserList;
    }

    public UserDetailDto getUser(Long id) {
        Optional<User> userWrapper = mUserRepository.findById(id);

        User user = userWrapper.get();

        List<UserInterest> byUser_id = mUserInterestRepository.findByUser_Id(user.getId());

        Set<String> interestName = new HashSet<>();

        for(UserInterest e : byUser_id) {
            Optional<InterestCategory> byId = mInterestRepository.findById(e.getInterest().getId());
            interestName.add(byId.get().getName());
        }

        UserDetailDto userDetailDTO = UserDetailDto.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .socialId(user.getSocialId())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .volunteerTime(user.getVolunteerTime())
                .interestName(interestName)
                .build();

        return userDetailDTO;
    }
    
    private UserDto convertEntityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .socialId(user.getSocialId())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .status(user.getStatus())
                .build();
    }

    public List<SimpleUser> getUserRankingList() {
        List<SimpleUser> userRankingList = mUserRepository.findTop5ByOrderByVolunteerTimeDesc();

        return userRankingList;
    }

    public List<SimpleUser> getMonthlyRanking(int scope) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);

        List<EventEntity> list = mRequestRepository.findAllByStatusAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual((byte)scope, currentDate, endDate);
        Map<Long, Integer> map = new HashMap<>();

        for(EventEntity event : list) {
            // TODO 이벤트 아이디로 -> 봉사자 리스트 가져오기
            Long id = event.getId();

            List<UserEventMapper> eventList = mUserEventRepository.findAllByUserTypeAndEvent_id((byte) 1, id);

            // TODO 가져온 봉사자 리스트 없는 유저면 map에 추가
            // TODO 있는 유저면 누적합
            for(UserEventMapper a : eventList) {
                LocalTime start = a.getEvent().getStartTime().toLocalTime();
                LocalTime end = a.getEvent().getEndTime().toLocalTime();
                Integer plus = end.getHour()-start.getHour();

                map.computeIfPresent(a.getUser().getId(), (k, v)-> map.get(a.getUser().getId())+plus);
                map.putIfAbsent(a.getUser().getId(), plus);
            }
        }

        List<SimpleUser> result = new ArrayList<>();

//        for(Long id : map.keySet()) {
//            SimpleUser simpleUser = new SimpleUser();
//            simpleUser.setVolunteerTime(map.get(id));
//            simpleUser.setId(id);
//            result.add(simpleUser);
//        }

        return result;
    }

    public List<SimpleUser> getWeeklyRanking(Integer scope) {
        return getUserRankingList();
    }
    
    // added
    public User findUserEntity(Long id) {
       Optional<User> userData = mUserRepository.findById(id);
       User user = userData.get();
       
       return user;
    }
}
