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
public class UserDto {
    private Long id;

    @NotBlank
    @Length(min = 2)
    private String name;

    @NotBlank
    @Length(min = 3, max = 10)
    private String nickname;

    @NotBlank
    private String phone;
    private String socialId;
    private Byte userType;
    private Byte status;
    private Integer volunteerTime;
    private String[] userInterestList;
    private Set<InterestCategory> interests = new HashSet<>();
    private Byte isDummy;

    public User toEntity() {
        User user = User.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .phone(phone)
                .socialId(socialId)
                .userType(userType)
                .status(status)
                .volunteerTime(volunteerTime)
                .isDummy(isDummy)
                .locationAgree((byte) 0)
                .phoneAgree((byte) 0)
                .build();
        return user;
    }

    @Builder
    public UserDto(Long id, String name, String nickname, String phone, String socialId, Byte userType, Byte status, Integer volunteerTime, Byte isDummy) {
        this.id=id;
        this.name=name;
        this.nickname=nickname;
        this.phone=phone;
        this.socialId = socialId;
        this.userType = userType;
        this.status = status;
        this.volunteerTime = volunteerTime;
        this.isDummy = isDummy;
    }

}
