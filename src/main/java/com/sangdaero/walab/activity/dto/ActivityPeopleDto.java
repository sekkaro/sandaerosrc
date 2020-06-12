package com.sangdaero.walab.activity.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ActivityPeopleDto {
	
	List<ActivityUserDto> users;
	List<ActivityUserDto> volunteers;
	String managerName;
	
	public ActivityPeopleDto(List<ActivityUserDto> users, List<ActivityUserDto> volunteers, String managerName) {
		this.users = users;
		this.volunteers = volunteers;
		this.managerName = managerName;
	}

}
