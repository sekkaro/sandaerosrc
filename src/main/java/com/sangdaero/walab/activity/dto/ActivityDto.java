package com.sangdaero.walab.activity.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
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
public class ActivityDto {
	
	private Long id;
	private String title;
	private Byte status;
	private Integer eventCategory;
	private InterestCategory interestCategory;
	private Set<ActivityUserDto> activityUsers;
	private Set<Long> userIds;
	private Set<ActivityUserDto> activityVolunteers;
	private Set<Long> volunteerIds;
	private User manager;
	private String place;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String content;
	private Byte deliveryFlag;
	private String evaluate;
	private LocalDateTime deadline;
	private Integer type;
	private String placeDetail;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	@Builder
	public ActivityDto(Long id, String title, Byte status, Integer eventCategory, InterestCategory interestCategory, 
			Set<ActivityUserDto> activityUsers, Set<Long> userIds, Set<ActivityUserDto> activityVolunteers, Set<Long> volunteerIds, User manager, String place, LocalDateTime startTime, 
			LocalDateTime endTime, String content, Byte deliveryFlag, String evaluate,
					   LocalDateTime deadline, Integer type, String placeDetail, LocalDateTime regDate, LocalDateTime modDate) {
		this.id = id;
		this.title = title;
		this.status = status;
		this.eventCategory = eventCategory;
		this.interestCategory = interestCategory;
		this.activityUsers = activityUsers;
		this.userIds = userIds;
		this.activityVolunteers = activityVolunteers;
		this.volunteerIds = volunteerIds;
		this.manager = manager;
		this.place = place;
		this.startTime = startTime;
		this.endTime = endTime;
		this.content = content;
		this.deliveryFlag = deliveryFlag;
		this.evaluate = evaluate;
		this.deadline = deadline;
		this.type = type;
		this.placeDetail = placeDetail;
		this.regDate = regDate;
		this.modDate = modDate;
	}
	
	// request uses 'event' table.
		public EventEntity toEntity() {

			EventEntity eventEntity = EventEntity.builder()
					.id(id)
					.title(title)
					.status(status)
					.eventCategory(0)
					.place(place)
					.startTime(startTime)
					.endTime(endTime)
					.content(content)
					.deliveryFlag(deliveryFlag)
					.donator(null)
					.selectSupport(null)
					.donationPrice(null)
					.billType(null)
					.paymentCheck(null)
					.donatorPhone(null)
					.businessPicture(null)
					.evaluate(evaluate)
					.deadline(deadline)
					.type(type)
					.placeDetail(placeDetail)
					.build();
			
			return eventEntity;
		}
		
}
