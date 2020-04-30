package com.sangdaero.walab.payment.dto;

import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentDto {

	private Long id;
	private String title;
	private Integer donationPrice;
	private String content;
	private User manager;
	private Byte selectSupport; // entertainment, support
	private Byte status; // opened fundraising or closed fundraising
	private Integer eventCategory;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	@Builder
	public PaymentDto(Long id, String title, Integer donationPrice, String content, User manager,
			Byte selectSupport,
			Byte status, Integer eventCategory, LocalDateTime regDate, LocalDateTime modDate) {
		super();
		this.id = id;
		this.title = title;
		this.donationPrice = donationPrice;
		this.content = content;
		this.manager = manager;
		this.selectSupport = selectSupport;
		this.status = status;
		this.regDate = regDate;
		this.eventCategory = eventCategory;
		this.modDate = modDate;
		this.status = status;
	}
	
	// payment uses 'event' table.
	public EventEntity toEntity() {

		EventEntity eventEntity = EventEntity.builder()
				.id(id)
				.title(title)
				.status(null)
				//.userTaker(null)
				.manager(manager)
				.place(null)
				.startTime(null)
				.endTime(null)
				.content(content)
				.deliveryFlag(null)
				.phoneAgree(null)
				.donator(null)
				.selectSupport(selectSupport)
				.donationPrice(donationPrice)
				.status(status)
				.eventCategory(2)
				.evaluate(null)
				.deadline(null)
				.build();
		return eventEntity;
	}

}
