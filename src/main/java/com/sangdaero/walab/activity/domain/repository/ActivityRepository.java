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
	Page<EventEntity> findAllByEventCategoryAndTitleContaining(int eventCategory, String keyword, Pageable page);
	
	@EntityGraph(attributePaths = { "interestCategory", "manager" })
	Page<EventEntity> findAllByEventCategoryAndTitleContainingAndInterestCategory(int eventCategory, String keyword, InterestCategory interestCategory,
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

}
