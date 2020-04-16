package com.sangdaero.walab.request.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.common.entity.UserEventMapper;

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
	private Byte status;
	private Integer eventCategory;
	private InterestCategory interestCategory;
	private String userName;
	private User userTaker;
	private Integer userVolunteer;
	private List<UserEventMapper> volunteers;
	private Long manager;
	private String place;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String content;
	private Byte deliveryFlag;
	private Byte phoneAgree;
	private String evaluate;
	private LocalDateTime deadline;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	@Builder
	public RequestDto(Long id, String title, Byte status, Integer eventCategory, InterestCategory interestCategory, List<UserEventMapper> volunteers, String userName, User userTaker, Long manager,
			String place, LocalDateTime startTime, LocalDateTime endTime, String content, Byte deliveryFlag,
			Byte phoneAgree, String evaluate, LocalDateTime deadline, LocalDateTime regDate, LocalDateTime modDate) {
		this.id = id;
		this.title = title;
		this.status = status;
		this.eventCategory = eventCategory;
		this.interestCategory = interestCategory;
		this.volunteers = volunteers;
		this.userName = userName;
		this.userTaker = userTaker;
		this.manager = manager;
		this.place = place;
		this.startTime = startTime;
		this.endTime = endTime;
		this.content = content;
		this.deliveryFlag = deliveryFlag;
		this.phoneAgree = phoneAgree;
		this.evaluate = evaluate;
		this.deadline = deadline;
		this.regDate = regDate;
		this.modDate = modDate;
	}
	
	// payment uses 'event' table.
		public EventEntity toEntity() {

			EventEntity eventEntity = EventEntity.builder()
					.id(id)
					.title(title)
					.status(status)
					.eventCategory(0)
					.userName(userName)
					.manager(manager)
					.place(place)
					.startTime(startTime)
					.endTime(endTime)
					.content(content)
					.deliveryFlag(deliveryFlag)
					.phoneAgree(phoneAgree)
					.donator(null)
					.selectSupport(null)
					.donationPrice(null)
					.billType(null)
					.paymentCheck(null)
					.donatorPhone(null)
					.businessPicture(null)
					.evaluate(evaluate)
					.deadline(deadline)
					.build();
			
			return eventEntity;
		}
		
}
