package com.sangdaero.walab.user.application.dto;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.User;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String nickname;
    private String phone;
    private String socialId;
    private Byte userType;
    private Byte status;
    private String[] userInterestList;
    private Set<InterestCategory> interests = new HashSet<>();

    public User toEntity() {
        User user = User.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .phone(phone)
                .socialId(socialId)
                .userType(userType)
                .status(status)
                .build();
        return user;
    }

    @Builder
    public UserDto(Long id, String name, String nickname, String phone, String socialId, Byte userType, Byte status) {
        this.id=id;
        this.name=name;
        this.nickname=nickname;
        this.phone=phone;
        this.socialId = socialId;
        this.userType = userType;
        this.status = status;
    }

}
