package com.sangdaero.walab.common.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sangdaero.walab.common.entity.UserInterest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User extends TimeEntity {
	
	// 2020-05-11 added
	@OneToMany(fetch= FetchType.LAZY, mappedBy="userId")
	@JsonIgnore
	private Set<FundraisingEntity> fundraising = new HashSet<FundraisingEntity>();
	
	@Id @GeneratedValue
    private Long id;

    private String name;

    private String nickname;

    private String profile;

    private String socialId;

    private String phone;

    private Byte userType;

    private Byte status;

    private Integer volunteerTime;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserInterest> userInterestList = new ArrayList<>();

    private String service;

    private String memo;

    private Byte locationAgree;

    private Byte phoneAgree;

    private String community;

    private LocalDateTime lastLogin;

    private Byte isDelete;

    private Byte isDummy;
	
    @Builder
    public User(Long id, String name, String nickname, String profile, String socialId,
                String phone, Byte userType, Byte status, Integer volunteerTime,
//                Set<InterestCategory> interests,
                String service, String memo, Byte locationAgree, Byte phoneAgree, String community,
                LocalDateTime lastLogin, Byte isDelete, Byte isDummy) {

        this.id=id;
        this.name=name;
        this.nickname=nickname;
        this.profile=profile;
        this.socialId=socialId;
        this.phone=phone;
        this.userType=userType;
        this.status=status;
        this.volunteerTime=volunteerTime;
        //this.interests=interests;
        this.service=service;
        this.memo=memo;
        this.locationAgree=locationAgree;
        this.phoneAgree=phoneAgree;
        this.community=community;
        this.lastLogin=lastLogin;
        this.isDelete=0;
        this.isDummy=isDummy;
    }
	
	
}
