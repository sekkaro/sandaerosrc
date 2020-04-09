package com.sangdaero.walab.payment.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.PaymentNoticeEntity;
import com.sangdaero.walab.payment.domain.repository.PaymentNoticeRepository;
import com.sangdaero.walab.payment.dto.PaymentNoticeDto;

@Service
public class PaymentNoticeService {

	private PaymentNoticeRepository mPaymentNoticeRepository;

	// constructor
	public PaymentNoticeService(PaymentNoticeRepository mPaymentNoticeRepository) {
		this.mPaymentNoticeRepository = mPaymentNoticeRepository;
	}
	
	// create & update notice
	@Transactional
	public Long savePaymentNotice(PaymentNoticeDto paymentNoticeDto) {
		return mPaymentNoticeRepository.save(paymentNoticeDto.toEntity()).getId();
	}
	
	// read single(and the only) notice
	@Transactional
	public PaymentNoticeDto getPaymentNoticeDto() {

		List<PaymentNoticeEntity> paymentNoticeEntityList = mPaymentNoticeRepository.findAll();
		PaymentNoticeEntity paymentNoticeEntity = paymentNoticeEntityList.get(0);
		
		PaymentNoticeDto paymentNoticeDto = PaymentNoticeDto.builder()
				.id(paymentNoticeEntity.getId())
				.content(paymentNoticeEntity.getContent())
				.regDate(paymentNoticeEntity.getRegDate())
				.modDate(paymentNoticeEntity.getModDate())
				.build();
				
		return paymentNoticeDto;
	}
	
}
