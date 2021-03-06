package com.sangdaero.walab.user.application.dto;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDetailDto {
    private Long id;

    private String name;

    @NotBlank
    @Length(min = 2)
    private String nickname;
    private String socialId;

    @NotBlank
    private String phone;
    private Byte userType;
    private Integer volunteerTime;
    private String[] userInterestList;
    private Set<String> interestName = new HashSet<>();
    private Set<InterestCategory> interests = new HashSet<>();
    private Byte isDummy;
    private Byte phoneAgree;

    public User toEntity() {
        User user = User.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .socialId(socialId)
                .phone(phone)
                .userType(userType)
                .volunteerTime(volunteerTime)
                .isDummy(isDummy)
                .phoneAgree(phoneAgree)
                .build();
        return user;
    }

    @Builder
    public UserDetailDto(Long id, String name, String nickname, String socialId, String phone,
                         Byte userType, Integer volunteerTime, Set<String> interestName, Byte isDummy,
                         Byte phoneAgree) {
        this.id=id;
        this.name=name;
        this.nickname=nickname;
        this.socialId=socialId;
        this.phone=phone;
        this.userType=userType;
        this.volunteerTime=volunteerTime;
        this.interestName=interestName;
        this.isDummy=isDummy;
        this.phoneAgree = phoneAgree;
    }

}