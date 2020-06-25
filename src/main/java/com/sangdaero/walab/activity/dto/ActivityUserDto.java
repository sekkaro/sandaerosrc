package com.sangdaero.walab.activity.dto;

import java.time.LocalDateTime;

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
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	public ActivityUserDto(User user, Byte status, Byte phoneAgree, Byte locationAgree, String startImage, String endImage,
						   Integer volunteerTime, LocalDateTime regDate, LocalDateTime modDate) {
		this.user = user;
		this.status = status;
		this.phoneAgree = phoneAgree;
		this.locationAgree = locationAgree;
		this.startImage = startImage;
		this.endImage = endImage;
		this.volunteerTime = volunteerTime;
		this.regDate = regDate;
		this.modDate = modDate;
	}

}
