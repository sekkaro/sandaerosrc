package com.sangdaero.walab.payment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.FundraisingEntity;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.payment.domain.repository.FundraisingRepository;
import com.sangdaero.walab.payment.domain.repository.PaymentRepository;
import com.sangdaero.walab.payment.dto.FundraisingDto;
import com.sangdaero.walab.user.domain.repository.UserRepository;

@Service
public class FundraisingService {
	
	private FundraisingRepository mFundraisingRepository;
	private PaymentRepository mPaymentRepository;
	private UserRepository mUserRepository;

	// constructor
	public FundraisingService(FundraisingRepository mFundraisingRepository, PaymentRepository mPaymentRepository, UserRepository mUserRepository) {
		this.mFundraisingRepository = mFundraisingRepository;
		this.mPaymentRepository = mPaymentRepository;
		this.mUserRepository = mUserRepository;
	}
	
	// save
	public void save(FundraisingEntity fund) {
		mFundraisingRepository.save(fund);
	}
	
	// count all
	public Long getAllFundraisingCount() {
		return mFundraisingRepository.count();
	}
	
	// get all fundraising list
	public List<FundraisingEntity> getAllFundraisingList() {
		
		return mFundraisingRepository.findAll();
	}
	
	// find all fundraisingDto with a single 'event_id'. 
	@Transactional
	public List<FundraisingDto> findAllFundraisingDtoByEventId(Long eventId){
		
		List<FundraisingDto> fundraisingDtoList = new ArrayList<>();
		List<FundraisingEntity> fundraisingEntityList = new ArrayList<>();
		
		// used Optional to pick a specific entity, by using event_id value
		Optional<EventEntity> eventEntityWrapper = mPaymentRepository.findById(eventId);
		EventEntity eventEntity = eventEntityWrapper.get(); // got the event entity data
		
		fundraisingEntityList = mFundraisingRepository.findAllByEventId(eventEntity);
		
		// no matching records in DB
		if(fundraisingEntityList.isEmpty()) return fundraisingDtoList;
		
		for (FundraisingEntity fundraisingEntity : fundraisingEntityList) {
			fundraisingDtoList.add(this.convertFundraisingEntityToFundraisingDto(fundraisingEntity));
		}
		
		return fundraisingDtoList;
	}
	
	// find single fundraisingDto with composite key (eventId, userId).
	@Transactional
	public FundraisingDto getSingleFundraisingDtoByEventIdAndUserId (Long eventId, Long userId) {
		
		// used Optional to pick a specific entity, by using event_id value
		Optional<EventEntity> eventEntityWrapper = mPaymentRepository.findById(eventId);
		EventEntity event = eventEntityWrapper.get(); // got the event entity data
		
		Optional<User> userData = mUserRepository.findById(userId);
		User user = userData.get();
		
		FundraisingEntity fundraisingEntity = mFundraisingRepository.findByEventIdAndUserId(event, user);
		FundraisingDto fundraisingDto = convertFundraisingEntityToFundraisingDto(fundraisingEntity);
		
		return fundraisingDto;
	}
	
	
	
	// delete all fundraisingEntity with a single 'event_id'.
	public void deleteAllFundraisingByEventId(Long eventId) {
		
		Optional<EventEntity> eventEntityWrapper = mPaymentRepository.findById(eventId);
		EventEntity eventEntity = eventEntityWrapper.get(); // got the event entity data
		
		mFundraisingRepository.deleteAllByEventId(eventEntity);
	}
	
	// delete single fundraisingEntity with composite key (eventId, userId)
	@Transactional
	public void deleteSingleFundraisingByEventIdAndUserId(Long eventId, Long userId) {
		
		// used Optional to pick a specific entity, by using event_id value
		Optional<EventEntity> eventEntityWrapper = mPaymentRepository.findById(eventId);
		EventEntity event = eventEntityWrapper.get(); // got the event entity data
		
		Optional<User> userData = mUserRepository.findById(userId);
		User user = userData.get();
		
		mFundraisingRepository.deleteByEventIdAndUserId(event, user);
		
	}
	
	private FundraisingDto convertFundraisingEntityToFundraisingDto (FundraisingEntity fundraisingEntity) {
		
		FundraisingDto fundraisingDto = FundraisingDto.builder()
				.user(fundraisingEntity.getUserId())
				.title(fundraisingEntity.getTitle())
				.memo(fundraisingEntity.getMemo())
				.personalPayAmount(fundraisingEntity.getPersonalPayAmount())
				.donator(fundraisingEntity.getDonator())
				.billType(fundraisingEntity.getBillType())
				.paymentStatus(fundraisingEntity.getPaymentStatus())
				.donatorPhone(fundraisingEntity.getDonatorPhone())
				.businessPicture(fundraisingEntity.getBusinessPicture())
				.regDate(fundraisingEntity.getRegDate())
				.modDate(fundraisingEntity.getModDate())
				.build();
		
		return fundraisingDto;
		
	}
		
}
