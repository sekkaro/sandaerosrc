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
	List<EventEntity> findByEventCategoryAndTitleContainingIgnoreCase(Integer eventCategory, String keyword, Pageable pageable);
	
	Long countByEventCategoryAndTitleContainingIgnoreCase(Integer eventCategory, String keyword);

	// search with keyword, donator, paymentCheck
	@Query(nativeQuery = true, value="SELECT * FROM event WHERE event_category = :eventCategory AND status = :status AND (title like %:title%)")
	List<EventEntity> findByEventCategoryAndStatusAndTitle(@Param("eventCategory")Integer eventCategory, @Param("status")Byte status ,@Param("title")String title, Pageable pageable);
	
	@Query(nativeQuery = true, value="SELECT COUNT(*) FROM event WHERE event_category = :eventCategory AND status = :status AND (title like %:title%)")
	Long countByEventCategoryAndStatusAndTitle(@Param("eventCategory")Integer eventCategory, @Param("status")Byte status ,@Param("title")String title);
	
}
