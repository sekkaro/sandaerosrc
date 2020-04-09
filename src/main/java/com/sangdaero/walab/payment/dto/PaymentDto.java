package com.sangdaero.walab.payment.dto;

import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.EventEntity;

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
	private String donator;
	private Integer donationPrice;
	private String content;
	private Integer manager;
	private Byte selectSupport; // entertainment, support
	private Byte billType; // business receipt y/n
	private String donatorPhone; // personal or company phone number
	private String businessPicture; // business registration certificate
	private Byte paymentCheck; // payment waiting, payment fulfilled
	
	private Byte status; // opened fundraising or closed fundraising
	private Integer eventCategory;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	


	@Builder
	public PaymentDto(Long id, String title, String donator, Integer donationPrice, String content, Integer manager,
			Byte selectSupport, Byte billType, String donatorPhone, String businessPicture,
			Byte paymentCheck, Byte status, Integer eventCategory, LocalDateTime regDate, LocalDateTime modDate) {
		super();
		this.id = id;
		this.title = title;
		this.donator = donator;
		this.donationPrice = donationPrice;
		this.content = content;
		this.manager = manager;
		this.selectSupport = selectSupport;
		this.billType = billType;
		this.donatorPhone = donatorPhone;
		this.businessPicture = businessPicture;
		this.paymentCheck = paymentCheck;
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
				.userVolunteer(null)
				.manager(manager)
				.place(null)
				.startTime(null)
				.endTime(null)
				.content(content)
				.deliveryFlag(null)
				.phoneAgree(null)
				.userName(null)
				.donator(donator)
				.selectSupport(selectSupport)
				.donationPrice(donationPrice)
				.billType(billType)
				.paymentCheck(paymentCheck)
				.status(status)
				.donatorPhone(donatorPhone)
				.businessPicture(businessPicture)
				.eventCategory(2)
				.evaluate(null)
				.deadline(null)
				.build();
		return eventEntity;
	}

}
