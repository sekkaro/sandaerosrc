package com.sangdaero.walab.payment.dto;

import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.PaymentNoticeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentNoticeDto {
	
	private Long id;
	private String content;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	@Builder
	public PaymentNoticeDto(Long id, String content, LocalDateTime regDate, LocalDateTime modDate) {
		this.id = id;
		this.content = content;
		this.regDate = regDate;
		this.modDate = modDate;
	}
	
	public PaymentNoticeEntity toEntity() {
		
		PaymentNoticeEntity paymentNoticeEntity = PaymentNoticeEntity.builder()
				.id(id)
				.content(content)
				.build();
		return paymentNoticeEntity;
	}

}
