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
	
	private Byte selectSupport; // 지정후원, 비지정후원
	private Byte billType; // 사업자영수증 y/n
	private String donatorPhone; // 개인/사업체 연락번호
	private String businessPicture; // 사업자등록증
	private Byte paymentCheck; // 대기중, 입금완료
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	


	@Builder
	public PaymentDto(Long id, String title, String donator, Integer donationPrice, String content, Integer manager,
			Byte selectSupport, Byte billType, String donatorPhone, String businessPicture,
			Byte paymentCheck, LocalDateTime regDate, LocalDateTime modDate) {
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
		this.regDate = regDate;
		this.modDate = modDate;
	}
	
	// payment uses 'event' table.
	public EventEntity toEntity() {

		EventEntity eventEntity = EventEntity.builder()
				.id(id)
				.title(title)
				.status(null)
				.userVolunteer(null)
				.manager(manager)
				.place(null)
				.startTime(null)
				.endTime(null)
				.content(content)
				.deliveryFlag(null)
				.phoneAgree(null)
				.donator(donator)
				.selectSupport(selectSupport)
				.donationPrice(donationPrice)
				.billType(billType)
				.paymentCheck(paymentCheck)
				.donatorPhone(donatorPhone)
				.businessPicture(businessPicture)
				.evaluate(null)
				.deadline(null)
				.build();
		return eventEntity;
	}

}
