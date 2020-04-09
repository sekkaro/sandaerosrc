package com.sangdaero.walab.request.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.UserEventMapper;

public interface RequestRepository extends JpaRepository<EventEntity, Long> {

	Page<EventEntity> findAllByTitleContainingAndEventCategory(String keyword, int eventCategory, Pageable page);

	Page<EventEntity> findAllByUserNameContainingAndEventCategory(String keyword, int eventCategory, Pageable page);

	Page<EventEntity> findAllByEventCategory(int eventCategory, Pageable page);

	Long countByEventCategory(int eventCategory);

	Long countByTitleContainingAndEventCategory(String keyword, int eventCategory);

	Long countByUserNameContainingAndEventCategory(String keyword, int eventCategory);
	
	@EntityGraph(attributePaths = { "volunteers", "interestCategory", "userTaker" })
	Optional<EventEntity> getById(Long id);

}
