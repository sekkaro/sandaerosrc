package com.sangdaero.walab.user.application.service;

import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.*;

import com.sangdaero.walab.activity.domain.repository.ActivityRepository;
import com.sangdaero.walab.common.entity.*;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserEventMapperRepository;
import com.sangdaero.walab.mapper.repository.UserInterestRepository;
import com.sangdaero.walab.ranking.service.RankingService;

import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.dto.UserDetailDto;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.application.dto.VolunteerRanking;
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

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService extends OidcUserService {
	
	private final InterestRepository mInterestRepository;
    private final UserRepository mUserRepository;
    private final UserInterestRepository mUserInterestRepository;
    private final ActivityRepository mActivityRepository;
    private final UserEventMapperRepository mUserEventMapperRepository;
	
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
            userDto.setPhone("010-9291-2788");
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
                .isDummy(user.getIsDummy())
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

    public List<VolunteerRanking> getVolunteerRankingList() {
        List<User> userList = mUserRepository.findTop5ByOrderByVolunteerTimeDesc();
//        List<SimpleUser> userRankingList = mUserRepository.findTop5ByOrderByVolunteerTimeDesc();

        List<VolunteerRanking> result = new ArrayList<>();

        for(User u : userList) {
            VolunteerRanking build = VolunteerRanking.builder()
                    .id(u.getId())
                    .name(u.getName())
                    .volunteerTime(u.getVolunteerTime())
                    .build();
            result.add(build);
        }

        return result;
    }

//    public List<SimpleUser> getUserRankingList() {
//        List<SimpleUser> userRankingList = mUserRepository.findTop5ByOrderByVolunteerTimeDesc();
//
//        return userRankingList;
//    }

    public List<VolunteerRanking> getRanking(int scope) {
        LocalDateTime currentDate=LocalDateTime.now();
        LocalDateTime endDate=LocalDateTime.now();

        if(scope==3) {
            currentDate = LocalDateTime.parse(RankingService.getCurMonday()+"T00:00:00");
            endDate = LocalDateTime.parse(RankingService.getCurSunday()+"T23:59:59");
        } else if(scope==2) {
            currentDate = LocalDateTime.parse(RankingService.getFirstDayMonth()+"T00:00:00");
            endDate = LocalDateTime.parse(RankingService.getLastDayMonth()+"T23:59:59");
        }

        List<EventEntity> list = mActivityRepository.findAllByStatusAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual((byte)scope, currentDate, endDate);
        Map<Long, Integer> map = new HashMap<>();

        for(EventEntity event : list) {
            // 이벤트 아이디로 -> 봉사자 리스트 가져오기
            Long id = event.getId();

            List<UserEventMapper> eventList = mUserEventMapperRepository.findAllByUserTypeAndEvent_id((byte) 1, id);

            // 가져온 봉사자 리스트 없는 유저면 map에 추가
            // 있는 유저면 누적합
            for(UserEventMapper a : eventList) {
                LocalTime start = a.getEvent().getStartTime().toLocalTime();
                LocalTime end = a.getEvent().getEndTime().toLocalTime();
                Integer plus = end.getHour()-start.getHour();

                map.computeIfPresent(a.getUser().getId(), (k, v)-> map.get(a.getUser().getId())+plus);
                map.putIfAbsent(a.getUser().getId(), plus);
            }
        }

        List<VolunteerRanking> result = new ArrayList<>();

        for(Long id : map.keySet()) {
            VolunteerRanking build = VolunteerRanking.builder()
                    .id(id)
                    .name(mUserRepository.findById(id).get().getName())
                    .volunteerTime(mUserRepository.findById(id).get().getVolunteerTime())
                    .build();
            result.add(build);
        }

        return result;
    }

    public User findUserEntity(Long id) {
        Optional<User> userData = mUserRepository.findById(id);
        User user = userData.get();

        return user;
    }

    public void addInterest(Long id, InterestCategory interest) {
        User user = mUserRepository.getOne(id);
        UserInterest userInterest = new UserInterest();
        userInterest.setUser(user);
        userInterest.setInterest(interest);

        mUserInterestRepository.save(userInterest);
    }

    public void removeInterest(Long id, InterestCategory interest) {
        mUserInterestRepository.deleteByUser_IdAndInterestId(id, interest.getId());
    }

    public void changeVolunteerTime(Long id, Integer time) {
        Optional<User> byId = mUserRepository.findById(id);
        byId.ifPresent(a->a.setVolunteerTime(time));
    }

    public void changeNickname(Long id, String nickname) {
        Optional<User> byId = mUserRepository.findById(id);
        byId.ifPresent(a->a.setNickname(nickname));
    }

    public void changeName(Long id, String name) {
        Optional<User> byId = mUserRepository.findById(id);
        byId.ifPresent(a->a.setName(name));
    }

    public void changeEmail(Long id, String socialId) {
        Optional<User> byId = mUserRepository.findById(id);
        byId.ifPresent(a->a.setSocialId(socialId));
    }

    public void changePhone(Long id, String phone) {
        Optional<User> byId = mUserRepository.findById(id);
        byId.ifPresent(a->a.setPhone(phone));
    }

    public void changeUserType(Long id, Byte type) {
        Optional<User> byId = mUserRepository.findById(id);
        byId.ifPresent(a->a.setUserType(type));
    }
    
    public List<UserDetailDto> findUsers(String keyword) {
		List<User> users = mUserRepository.findAllByNameContaining(keyword);
		List<UserDetailDto> userList = new ArrayList<>();
		
		for(User user: users) {
			UserDetailDto userDetailDTO = UserDetailDto.builder()
   	                .id(user.getId())
   	                .name(user.getName())
   	                .nickname(user.getNickname())
   	                .socialId(user.getSocialId())
   	                .phone(user.getPhone())
   	                .userType(user.getUserType())
   	                .volunteerTime(user.getVolunteerTime())
   	                .interestName(null)
   	                .build();
		
			userList.add(userDetailDTO);
		}
				
		return userList;
	}

    public UserDto createUser(String email, String name) {
        UserDto userDto = new UserDto();
        userDto.setSocialId(email);
        userDto.setName(name);

        updateUser(userDto);

        User user = mUserRepository.findBySocialId(email);

        return convertEntityToDto(user);

    }

    public List<SimpleUser> getSimpleUserListWithInterestOnOff(Long interestCategoryId) {
        List<SimpleUser> simpleUserList = mUserRepository.findAllByOrderByName();

        if(interestCategoryId!=0) {
            Iterator<SimpleUser> iter = simpleUserList.iterator();
            while (iter.hasNext()) {
                SimpleUser user = iter.next();
                UserInterest userInterest = mUserInterestRepository.findByUserIdAndInterestId(user.getId(), interestCategoryId);

                if(userInterest!=null) {
                    Byte on_Off = userInterest.getOn_off();

                    if(on_Off == 0) {
                        iter.remove();
                    }
                }

            }
        }

        return simpleUserList;
    }


    @Transactional
    public void setStartImage(Long id, UserDto userDto, String fileDownloadUri) {
        UserEventMapper list = mUserEventMapperRepository.findByEventIdAndUserId(id, userDto.getId());
        list.setStartImage(fileDownloadUri);
    }

    public void setEndImage(Long id, UserDto userDto, String fileDownloadUri) {
        UserEventMapper list = mUserEventMapperRepository.findByEventIdAndUserId(id, userDto.getId());
        list.setEndImage(fileDownloadUri);
    }


}
