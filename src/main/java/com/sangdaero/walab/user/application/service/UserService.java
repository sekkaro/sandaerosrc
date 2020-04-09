package com.sangdaero.walab.user.application.service;

import java.time.LocalDateTime;

import java.util.*;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.common.entity.UserInterest;
import com.sangdaero.walab.mapper.repository.UserInterestRepository;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.dto.UserDetailDto;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.domain.repository.UserRepository;
import com.sangdaero.walab.common.entity.User;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends OidcUserService {
	
	private InterestRepository mInterestRepository;
    private UserRepository mUserRepository;
    private UserInterestRepository mUserInterestRepository;

    public UserService(UserRepository mUserRepository, InterestRepository mInterestRepository, UserInterestRepository mUserInterestRepository) {
        this.mUserRepository = mUserRepository;
        this.mInterestRepository = mInterestRepository;
        this.mUserInterestRepository = mUserInterestRepository;
    }
	
	@Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
        UserDto userDto = new UserDto();
        userDto.setName((String) attributes.get("name"));
        userDto.setSocialId((String) attributes.get("sub"));
        
        
        updateUser(userDto);
        return oidcUser;
    }

    private void updateUser(UserDto userDto) {
        User user = mUserRepository.findBySocialId(userDto.getSocialId());
        if(user == null) {
        	userDto.setNickname("닉네임");
        	userDto.setPhone("010-XXXX-XXXX");
            userDto.setUserType((byte) 0);
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
    	User user = mUserRepository.findBySocialId(principal.getAttribute("sub"));
    	
    	UserDto userDto = convertEntityToDto(user);
    	
    	return userDto;
    }
    
    public void setStatus(@AuthenticationPrincipal OAuth2User principal, Boolean isOn) {
    	User user = mUserRepository.findBySocialId(principal.getAttribute("sub"));
    	
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

        for (String e :userDTO.getUserInterestList()) {
            InterestCategory interestList = mInterestRepository.findByNameEquals(e);
            userDTO.getInterests().add(interestList);
        }

        User user = mUserRepository.save(userDTO.toEntity());

        for(InterestCategory interest : userDTO.getInterests()) {
            UserInterest userInterest = new UserInterest();

            userInterest.setInterest(interest);
            userInterest.setUser(user);

            mUserInterestRepository.save(userInterest);
        }
    }


    public List<SimpleUser> getSimpleUserList() {
        List<SimpleUser> simpleUserList = mUserRepository.findAllByOrderByName();
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

}
