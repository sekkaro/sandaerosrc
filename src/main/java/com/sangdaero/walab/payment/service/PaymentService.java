package com.sangdaero.walab.payment.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.payment.domain.repository.PaymentRepository;
import com.sangdaero.walab.payment.dto.PaymentDto;

@Service
public class PaymentService {

	private PaymentRepository mPaymentRepository;

	// constructor
	public PaymentService(PaymentRepository mPaymentRepository) {
		this.mPaymentRepository = mPaymentRepository;
	}

	// Create
	// * JPA uses same method for create and update.
	// if there is an existing id value, it is update, else it is a new create.
	@Transactional
	public Long savePayment(PaymentDto paymentDto) {
		return mPaymentRepository.save(paymentDto.toEntity()).getId();
	}
	
	// Read all
	@Transactional
	public List<PaymentDto> getPaymentList(){

		// get all 'event' records from database
		Pageable pageable = PageRequest.of(0, 10);
		Page<EventEntity> eventEntityAsPage = mPaymentRepository.findAll(pageable); 
		List<EventEntity> eventEntityList = eventEntityAsPage.getContent();

		// payment DTO to save selected data from 'event' records
		List<PaymentDto> paymentDtoList = new ArrayList<>();

		// extract selected data from EventEntity into PaymentDto
		for ( EventEntity eventEntity : eventEntityList) {

			PaymentDto paymentDto = this.convertEventEntityToPaymentDto(eventEntity);
			paymentDtoList.add(paymentDto);
		}
		return paymentDtoList;
	}
	
	// Read Specific records (sort, keyword search)
	public List<PaymentDto> searchPaymentList(String keyword, Byte sortBy){
		
		List<EventEntity> eventEntitiyList = new ArrayList<>();
		List<PaymentDto> paymentDtoList = new ArrayList<>();
		
		
		if (sortBy == (byte)2) { // select all status
			// key가 제목 혹은 입금자명에 하나라도 있으면 찾는다. 예를 들어서, 제목이 aaa이고 입금자명이 bbb이면, a로 검색해도 글이 뜨고 b로 검색해도 글이 뜬다.
			eventEntitiyList = mPaymentRepository.findByTitleContainingIgnoreCaseOrDonatorContainingIgnoreCase(keyword, keyword);
		} else {
			eventEntitiyList = mPaymentRepository.findByPaymentCheckAndTitleOrDonator(sortBy, keyword, keyword); 
		}
		// no matching records in DB
		if(eventEntitiyList.isEmpty()) return paymentDtoList;
		
		for (EventEntity eventEntity : eventEntitiyList) {
			System.out.println(eventEntity.getPaymentCheck());
			paymentDtoList.add(this.convertEventEntityToPaymentDto(eventEntity));
		}
		
		return paymentDtoList;
	}
	
	// Read single record
	@Transactional
	public PaymentDto getSinglePaymentById(Long id) {
		
		// used Optional to pick a specific entity, by using id value
//		Optional<EventEntity> eventEntityWrapper = mPaymentRepository.findById(id); // original
//		EventEntity eventEntity = eventEntityWrapper.get();
		
		Pageable pageable = PageRequest.of(0, 1);
	
		EventEntity eventEntity = mPaymentRepository.findAllById(id, pageable).get(0);
		
		return this.convertEventEntityToPaymentDto(eventEntity);
	}
	
	// delete single record
	@Transactional
	public void deletePost(Long id) {
//		mPaymentRepository.deleteById(id); // original
		Pageable pageable = PageRequest.of(0, 1);
		mPaymentRepository.deleteById(id, pageable).get(0);

	}
	
	// EventEntity -> PaymentDto conversion
	private PaymentDto convertEventEntityToPaymentDto(EventEntity eventEntity) {
		
		PaymentDto paymentDto = PaymentDto.builder()
				.id(eventEntity.getId())
				.title(eventEntity.getTitle())
				.donationPrice(eventEntity.getDonationPrice())
				.donator(eventEntity.getDonator())
				.content(eventEntity.getContent())
				.manager(eventEntity.getManager())
				.selectSupport(eventEntity.getSelectSupport())
				.billType(eventEntity.getBillType())
				.donatorPhone(eventEntity.getDonatorPhone())
				.businessPicture(eventEntity.getBusinessPicture())
				.paymentCheck(eventEntity.getPaymentCheck())
				.regDate(eventEntity.getRegDate())
				.modDate(eventEntity.getModDate())
				.build();
		
		return paymentDto;
		
	}
}
