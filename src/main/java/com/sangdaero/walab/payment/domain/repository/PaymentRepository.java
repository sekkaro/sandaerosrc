package com.sangdaero.walab.payment.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.sangdaero.walab.common.entity.EventEntity;


// A /\ (B \/ C) <=> (A /\ B) \/ (A /\ C)
// A and (B or C) <=> (A and B) or (A and C)
public interface PaymentRepository extends PagingAndSortingRepository<EventEntity, Integer> {

	// List<EventEntity> findByPaymentCheckAndTitleOrDonatorContainingIgnoreCase(Byte paymentCheck, String keyword, String donator);
	
	List<EventEntity> findAllById(Long id, Pageable pageable);
	List<EventEntity> deleteById(Long id, Pageable pageable);
	
	List<EventEntity> findByTitleContainingIgnoreCaseOrDonatorContainingIgnoreCase(String keyword, String donator);
	
	@Query(nativeQuery = true, value="SELECT * FROM event WHERE payment_check = :paymentCheck AND (title like %:title% OR donator like %:donator%)")
    List<EventEntity> findByPaymentCheckAndTitleOrDonator(@Param("paymentCheck")Byte paymentCheck ,@Param("title")String title, @Param("donator")String donator);
	
}
