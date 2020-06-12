package com.sangdaero.walab.request.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.sangdaero.walab.activity.dto.ActivityDto;
import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.Request;
import com.sangdaero.walab.common.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RequestDto {
	
	private Long id;
	private String title;
	private InterestCategory interestCategory;
	private User client;
	private Byte status;
	private EventEntity event;
	private String productImage;
	private Byte userType;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	@Builder
	public RequestDto(Long id, String title, InterestCategory interestCategory, User client, Byte status, Byte userType,
					  EventEntity event, String productImage, LocalDateTime regDate, LocalDateTime modDate) {
		this.id = id;
		this.title = title;
		this.interestCategory = interestCategory;
		this.client = client;
		this.status = status;
		this.event = event;
		this.productImage = productImage;
		this.userType = userType;
		this.regDate = regDate;
		this.modDate = modDate;
	}
	
	public Request toEntity() {

		Request request = Request.builder()
				.id(id)
				.title(title)
				.interestCategory(interestCategory)
				.client(client)
				.event(event)
				.productImage(productImage)
				.userType(userType)
				.build();
		
		return request;
	}
	

}
