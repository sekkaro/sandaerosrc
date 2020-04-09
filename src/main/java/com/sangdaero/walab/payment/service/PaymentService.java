package com.sangdaero.walab.payment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.payment.domain.repository.PaymentRepository;
import com.sangdaero.walab.payment.dto.PaymentDto;

@Service
public class PaymentService {

	private PaymentRepository mPaymentRepository;

	// for paging
	private static final int NUMBER_OF_CONTENTS_IN_ONE_PAGE = 5; // 6 contents are shown, per page.
	private static final int PAGE_NUMBERS_PER_BLOCK = 5; // ex) 1 2 3 4 5 

	// constructor
	public PaymentService(PaymentRepository mPaymentRepository) {
		this.mPaymentRepository = mPaymentRepository;
	}

	// Count All
	public Long getAllPaymentCount() {
		return mPaymentRepository.count();
	}

	// return page list as array of integers, according to the specific search query
	public Integer[] getPageList(Integer pageNum, String keyword, Byte sortBy) {

		Integer[] pageList = new Integer[NUMBER_OF_CONTENTS_IN_ONE_PAGE]; // returning result

		Double totalNumberOfRecords = Double.valueOf(this.getSearchPaymentCount(keyword, sortBy)); // # of total contents found

		Integer LastPageNumber = (int)(Math.ceil(totalNumberOfRecords/NUMBER_OF_CONTENTS_IN_ONE_PAGE));// 1. calculating last page number
		Integer blockLastPageNumber = (LastPageNumber > pageNum + PAGE_NUMBERS_PER_BLOCK) ? pageNum + NUMBER_OF_CONTENTS_IN_ONE_PAGE : LastPageNumber;

		// adjust current starting page number
		pageNum = (pageNum <= 3) ? 1 : pageNum - 2;

		for (int curPageNum = pageNum, index=0; curPageNum<=blockLastPageNumber; curPageNum++, index++) {
			pageList[index] = curPageNum;
		}

		return pageList;
	}

	// Create
	// * JPA uses same method for create and update.
	// if there is an existing id value, it is update, else it is a new create.
	@Transactional
	public Long savePayment(PaymentDto paymentDto) {
		return mPaymentRepository.save(paymentDto.toEntity()).getId();
	}

	// Read All, with conditions { search with 1)sortBy, 2)keyword } 
	public List<PaymentDto> getSearchPaymentList(Integer pageNum, String keyword, Byte sortBy){

		// get only 5(NUM_OF_CONTENTS_IN_ONE_PAGE) records to display at payment.html
		Pageable pageable = PageRequest.of(pageNum-1, NUMBER_OF_CONTENTS_IN_ONE_PAGE); // Pageable's page index starts from 0.

		List<EventEntity> eventEntitiyList = new ArrayList<>();
		List<PaymentDto> paymentDtoList = new ArrayList<>();

		if (sortBy == (byte)2) { // select all status
			eventEntitiyList = mPaymentRepository.findByTitleContainingIgnoreCaseOrDonatorContainingIgnoreCase(keyword, keyword, pageable);
		} else {
			eventEntitiyList = mPaymentRepository.findByPaymentCheckAndTitleOrDonator(sortBy, keyword, keyword, pageable); 
		}
		// no matching records in DB
		if(eventEntitiyList.isEmpty()) return paymentDtoList;

		for (EventEntity eventEntity : eventEntitiyList) {
			paymentDtoList.add(this.convertEventEntityToPaymentDto(eventEntity));
		}
			
		return paymentDtoList;
	}

	// Count All, with conditions { search with 1)sortBy, 2)keyword } 
	public Long getSearchPaymentCount(String keyword, Byte sortBy) {

		Long searchCount = (long)0;

		if (sortBy == (byte)2) { // select all status
			searchCount = mPaymentRepository.countByTitleContainingIgnoreCaseOrDonatorContainingIgnoreCase(keyword, keyword);
		} else {
			searchCount = mPaymentRepository.countByPaymentCheckAndTitleOrDonator(sortBy, keyword, keyword); 
		}
		return searchCount;
	}

	// Read single record
	@Transactional
	public PaymentDto getSinglePaymentById(Long id) {

		// used Optional to pick a specific entity, by using id value
		Optional<EventEntity> eventEntityWrapper = mPaymentRepository.findById(id); // original
		EventEntity eventEntity = eventEntityWrapper.get();

		PaymentDto result = convertEventEntityToPaymentDto(eventEntity);
		return result;
	}

	// delete single record
	@Transactional
	public void deletePost(Long id) {
		mPaymentRepository.deleteById(id); // original
	}

	// EventEntity -> PaymentDto conversion
	private PaymentDto convertEventEntityToPaymentDto(EventEntity eventEntity) {

		PaymentDto paymentDto = PaymentDto.builder()
				
				.id(eventEntity.getId())
				.title(eventEntity.getTitle())
				.donationPrice(eventEntity.getDonationPrice())
				.eventCategory(eventEntity.getEventCategory())
				.donator(eventEntity.getDonator())
				.content(eventEntity.getContent())
				.manager(eventEntity.getManager())
				.selectSupport(eventEntity.getSelectSupport())
				.billType(eventEntity.getBillType())
				.donatorPhone(eventEntity.getDonatorPhone())
				.businessPicture(eventEntity.getBusinessPicture())
				.paymentCheck(eventEntity.getPaymentCheck())
				.status(eventEntity.getStatus())
				.regDate(eventEntity.getRegDate())
				.modDate(eventEntity.getModDate())
				.build();

		return paymentDto;

	}

	public void paymentCheckBoxToggle(String[] paymentCheckBox) {

		if (paymentCheckBox == null) {
			return ;
		}
		// when at least one thing is checked,
		for (int i=0; i<paymentCheckBox.length; i++) { // for each PaymentDto, if user checked the checkBox,

			long id = Long.parseLong(paymentCheckBox[i]); // get the id
			PaymentDto checkedPaymentDto = this.getSinglePaymentById(id); // and with the id, get the data from DB

			// toggle the 'paymentCheck' status. (Byte value, 0 or 1)
			if(checkedPaymentDto.getPaymentCheck() == (byte)1) {
				checkedPaymentDto.setPaymentCheck((byte)0);
			} else {
				checkedPaymentDto.setPaymentCheck((byte)1);
			}
			this.savePayment(checkedPaymentDto); // save the modified result in DB
		}
	}

	public void paymentStatusToggle(PaymentDto paymentDto) {
		
		Byte currentStatus = paymentDto.getStatus();
		
		if (currentStatus == (byte)0) {
			paymentDto.setStatus((byte)1);
		} else if (currentStatus == (byte)1) {
			paymentDto.setStatus((byte)0);
		}
		this.savePayment(paymentDto);
	}
	
	//	// Read all, not used
	//	@Transactional
	//	public List<PaymentDto> getPaymentList(Pageable pageable){
	//
	//		// get all 'event' records from database
	//		Page<EventEntity> eventEntityAsPage = mPaymentRepository.findAll(pageable); 
	//		
	//		List<EventEntity> eventEntityList = eventEntityAsPage.getContent();
	//
	//		// payment DTO to save selected data from 'event' records
	//		List<PaymentDto> paymentDtoList = new ArrayList<>();
	//
	//		// extract selected data from EventEntity into PaymentDto
	//		for ( EventEntity eventEntity : eventEntityList) {
	//
	//			PaymentDto paymentDto = this.convertEventEntityToPaymentDto(eventEntity);
	//			paymentDtoList.add(paymentDto);
	//		}
	//		return paymentDtoList;
	//	}
}
