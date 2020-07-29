package com.sangdaero.walab.request.repository;

import java.util.Optional;

import com.sangdaero.walab.common.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.common.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

	@EntityGraph(attributePaths = { "interestCategory", "client", "event" })
	Page<Request> findAllByTitleContaining(String keyword, Pageable page);

	@EntityGraph(attributePaths = { "interestCategory", "client", "event" })
	Page<Request> findAllByTitleContainingAndInterestCategory(String keyword, InterestCategory interestCategory,
			Pageable page);

	@EntityGraph(attributePaths = { "interestCategory", "client", "event" })
	Page<Request> findAllByTitleContainingAndStatus(String keyword, Byte status, Pageable page);

	@EntityGraph(attributePaths = { "interestCategory", "client", "event" })
	Page<Request> findAllByTitleContainingAndInterestCategoryAndStatus(String keyword,
																	   InterestCategory interestCategory, Byte status, Pageable page);

	Long countByTitleContaining(String keyword);

	Long countByTitleContainingAndInterestCategory(String keyword, InterestCategory interestCategory);

	Long countByTitleContainingAndStatus(String keyword, Byte status);

	Long countByTitleContainingAndInterestCategoryAndStatus(String keyword, InterestCategory interestCategory,
															Byte status);

	Long countByStatus(Byte status);
	
	@EntityGraph(attributePaths = { "interestCategory", "client" })
	Optional<Request> getById(Long id);

	@EntityGraph(attributePaths = { "client" })
	Request findByEventAndInterestCategory(EventEntity activity, InterestCategory interestCategory);
}
