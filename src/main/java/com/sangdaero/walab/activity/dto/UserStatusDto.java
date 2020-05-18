package com.sangdaero.walab.activity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserStatusDto {
	
	private Long id;
	private String name;
	private Byte status;
	private Byte type;
	
	public UserStatusDto(Long id, String name, Byte status, Byte type) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.type = type;
	}

	
}
