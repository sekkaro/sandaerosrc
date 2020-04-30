package com.sangdaero.walab.payment.dto;

import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.FundraisingEntity;
import com.sangdaero.walab.common.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FundraisingDto {
	
	private User user;
	
	private String title;
	private String memo;
	private Integer personalPayAmount;
	private String donator;
	private Byte billType;
	private Byte paymentStatus;
	private String donatorPhone;
	private String businessPicture;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	
	@Builder
	public FundraisingDto(User user, String title, String memo, Integer personalPayAmount,
			String donator, Byte billType, Byte paymentStatus, String donatorPhone, String businessPicture,
			LocalDateTime regDate, LocalDateTime modDate) {
		super();
		
		this.user = user;
		this.title = title;
		this.memo = memo;
		this.personalPayAmount = personalPayAmount;
		this.donator = donator;
		this.billType = billType;
		this.paymentStatus = paymentStatus;
		this.donatorPhone = donatorPhone;
		this.businessPicture = businessPicture;
		this.regDate = regDate;
		this.modDate = modDate;
	}
	
	public FundraisingEntity toEntity() {
		
		FundraisingEntity fundraisingEntity = FundraisingEntity.builder()
//				.eventId()
				.userId(user)
				.title(title)
				.memo(memo)
				.personalPayAmount(personalPayAmount)
				.donator(donator)
				.billType(billType)
				.paymentStatus(paymentStatus)
				.donatorPhone(donatorPhone)
				.businessPicture(businessPicture)
				.build();
		
		return fundraisingEntity;
	}

}
