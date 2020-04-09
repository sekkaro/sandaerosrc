package com.sangdaero.walab.payment.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sangdaero.walab.common.entity.EventEntity;


// A /\ (B \/ C) <=> (A /\ B) \/ (A /\ C)
// A and (B or C) <=> (A and B) or (A and C)
public interface PaymentRepository extends JpaRepository<EventEntity, Integer> {

	Optional<EventEntity> findById(Long id);
	List<EventEntity> deleteById(Long id);

	// search with keyword, donator
	List<EventEntity> findByTitleContainingIgnoreCaseOrDonatorContainingIgnoreCase(String keyword, String donator, Pageable pageable);
	
	Long countByTitleContainingIgnoreCaseOrDonatorContainingIgnoreCase(String keyword, String donator);

	// search with keyword, donator, paymentCheck
	@Query(nativeQuery = true, value="SELECT * FROM event WHERE payment_check = :paymentCheck AND (title like %:title% OR donator like %:donator%)")
	List<EventEntity> findByPaymentCheckAndTitleOrDonator(@Param("paymentCheck")Byte paymentCheck ,@Param("title")String title, @Param("donator")String donator, Pageable pageable);
	
	@Query(nativeQuery = true, value="SELECT COUNT(*) FROM event WHERE payment_check = :paymentCheck AND (title like %:title% OR donator like %:donator%)")
	Long countByPaymentCheckAndTitleOrDonator(@Param("paymentCheck")Byte paymentCheck ,@Param("title")String title, @Param("donator")String donator);
	
}
