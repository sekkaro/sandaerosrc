package com.sangdaero.walab.payment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.FundraisingEntity;
import com.sangdaero.walab.payment.domain.repository.FundraisingRepository;
import com.sangdaero.walab.payment.dto.FundraisingDto;
import com.sangdaero.walab.payment.dto.PaymentDto;

@Service
public class FundraisingService {
	
	private FundraisingRepository mFundraisingRepository;

	// constructor
	public FundraisingService(FundraisingRepository mFundraisingRepository) {
		this.mFundraisingRepository = mFundraisingRepository;
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
	public List<FundraisingDto> findByEventId(EventEntity eventId){
		
		List<FundraisingDto> fundraisingDtoList = new ArrayList<>();
		List<FundraisingEntity> fundraisingEntityList = new ArrayList<>();
		
		fundraisingEntityList = mFundraisingRepository.findByEventId(eventId);
		
		// no matching records in DB
		if(fundraisingEntityList.isEmpty()) return fundraisingDtoList;
		
		for (FundraisingEntity fundraisingEntity : fundraisingEntityList) {
			fundraisingDtoList.add(this.convertFundraisingEntityToFundraisingDto(fundraisingEntity));
		}
		
		return fundraisingDtoList;
	}
	
	private FundraisingDto convertFundraisingEntityToFundraisingDto (FundraisingEntity fundraisingEntity) {
		
		FundraisingDto fundraisingDto = FundraisingDto.builder()
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
