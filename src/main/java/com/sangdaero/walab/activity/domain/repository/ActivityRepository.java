package com.sangdaero.walab.activity.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.InterestCategory;

public interface ActivityRepository extends JpaRepository<EventEntity, Long> {

	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	Page<EventEntity> findAllByEventCategoryAndTitleContainingOrderByStatusAsc(int eventCategory, String keyword, Pageable page);
	
	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	Page<EventEntity> findAllByEventCategoryAndTitleContainingAndInterestCategoryOrderByStatusAsc(int eventCategory, String keyword, InterestCategory interestCategory,
			Pageable page);
	
	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	Page<EventEntity> findAllByEventCategoryAndTitleContainingAndStatus(int eventCategory, String keyword, Byte status,
			Pageable page);
	
	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	Page<EventEntity> findAllByEventCategoryAndTitleContainingAndInterestCategoryAndStatus(int eventCategory, String keyword,
			InterestCategory interestCategory, Byte status, Pageable page);
	
	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	Optional<EventEntity> getById(Long id);

	Long countByEventCategoryAndTitleContaining(int eventCategory, String keyword);

	Long countByEventCategoryAndTitleContainingAndInterestCategory(int eventCategory, String keyword,
			InterestCategory interestCategory);

	Long countByEventCategoryAndTitleContainingAndStatus(int eventCategory, String keyword, Byte status);

	Long countByEventCategoryAndTitleContainingAndInterestCategoryAndStatus(int eventCategory, String keyword,
			InterestCategory interestCategory, Byte status);

	List<EventEntity> findAllByStatusAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(byte scope,
			LocalDateTime currentDate, LocalDateTime endDate);

	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	List<EventEntity> findTop5ByEventCategoryAndDeliveryFlagAndStatusGreaterThanOrderByStatusAscDeadlineAsc(int eventCategory, Byte deliveryFlag, Byte status);

	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	List<EventEntity> findAllByEventCategoryAndInterestCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineAsc(int eventCategory,
																														  InterestCategory interestCategory, Byte deliveryFlag, Byte status1, Byte status2);

	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	List<EventEntity> findAllByEventCategoryAndInterestCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineDesc(int eventCategory,
																														   InterestCategory interestCategory, Byte deliveryFlag, Byte status1, Byte status2);

	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	List<EventEntity> findAllByEventCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineAsc(int eventCategory, Byte deliveryFlag, Byte status1, Byte status2);

	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	List<EventEntity> findAllByEventCategoryAndDeliveryFlagAndStatusBetweenOrderByStatusAscDeadlineDesc(int eventCategory, Byte deliveryFlag, Byte status1, Byte status2);

	Long countByEventCategory(int eventCategory);

	List<EventEntity> findAllByOrderByStatusAscModDateDesc();

}
