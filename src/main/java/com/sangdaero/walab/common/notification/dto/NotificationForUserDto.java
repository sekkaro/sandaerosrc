package com.sangdaero.walab.common.notification.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NotificationForUserDto {
	
	private Long id;
	private String message;
	private LocalDateTime regDate;
	
	@Builder
	public NotificationForUserDto(Long id, String message, LocalDateTime regDate) {
		this.id = id;
		this.message = message;
		this.regDate = regDate;
	}

}
