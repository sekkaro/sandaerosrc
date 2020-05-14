package com.sangdaero.walab.common.category.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sangdaero.walab.common.entity.BoardCategory;

public interface CategoryRepository extends JpaRepository<BoardCategory, Long> {
	List<BoardCategory> findAllByTopCategory(Byte topCategory);
	
	// Updating categoryId
	@Transactional
	@Modifying
	@Query(value="UPDATE board_category SET status=:status WHERE id=:id", nativeQuery = true)
	void updateCommunityCategoryId(@Param("status") Byte status, @Param("id") Long id);
}
