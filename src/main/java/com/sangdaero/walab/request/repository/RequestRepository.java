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
	Page<Request> findAllByTitleContainingOrderByStatusAsc(String keyword, Pageable page);

	@EntityGraph(attributePaths = { "interestCategory", "client", "event" })
	Page<Request> findAllByTitleContainingAndInterestCategoryOrderByStatusAsc(String keyword, InterestCategory interestCategory,
			Pageable page);

	Long countByTitleContaining(String keyword);

	Long countByTitleContainingAndInterestCategory(String keyword, InterestCategory interestCategory);
	
	@EntityGraph(attributePaths = { "interestCategory", "client" })
	Optional<Request> getById(Long id);

	@EntityGraph(attributePaths = { "client" })
	Request findByEventAndInterestCategory(EventEntity activity, InterestCategory interestCategory);
}
