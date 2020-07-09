package com.sangdaero.walab.activity.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ActivityUserDto {
	
	private User user;
	private Byte status;
	private Byte phoneAgree;
	private Byte locationAgree;
	private String startImage;
	private String endImage;
	private Integer volunteerTime;
	private String memo;
	private Set<InterestCategory> userInterestList;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	public ActivityUserDto(User user, Byte status, Byte phoneAgree, Byte locationAgree, String startImage, String endImage,
						   Integer volunteerTime, String memo, Set<InterestCategory> userInterestList, LocalDateTime regDate, LocalDateTime modDate) {
		this.user = user;
		this.status = status;
		this.phoneAgree = phoneAgree;
		this.locationAgree = locationAgree;
		this.startImage = startImage;
		this.endImage = endImage;
		this.volunteerTime = volunteerTime;
		this.memo = memo;
		this.userInterestList = userInterestList;
		this.regDate = regDate;
		this.modDate = modDate;
	}

}
