package com.sangdaero.walab.community.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sangdaero.walab.common.entity.Board;

public interface CommunityRepository extends JpaRepository<Board, Long> {
	
	// Get communities without deleted
	Page<Board> findAllByCategoryIdNotAndTopCategoryEquals(Long categoryId, Byte topCategory, Pageable page);
	// Get communities which fit to categoryId
	Page<Board> findAllByCategoryIdAndTopCategoryEquals(Long categoryId, Byte topCategory, Pageable page);
	// Searching communities without deleted
	Page<Board> findAllByTitleContainingAndCategoryIdNotAndTopCategoryEquals(String title, Long categoryId, Byte topCategory, Pageable page);
	// Searching communities which fit to categoryId
	Page<Board> findAllByTitleContainingAndCategoryIdAndTopCategoryEquals(String title, Long categoryId, Byte topCategory, Pageable page);
	
	Page<Board> findAllByContentContainingAndCategoryIdNotAndTopCategoryEquals(String content, Long categoryId, Byte topCategory, Pageable page);
	
	Page<Board> findAllByContentContainingAndCategoryIdAndTopCategoryEquals(String content, Long categoryId, Byte topCategory, Pageable page);
	
	Page<Board> findAllByWriterContainingAndCategoryIdNotAndTopCategoryEquals(String writer, Long categoryId, Byte topCategory, Pageable page);
	
	Page<Board> findAllByWriterContainingAndCategoryIdAndTopCategoryEquals(String writer, Long categoryId, Byte topCategory, Pageable page);
	// Get count of communities without deleted
	Long countByCategoryIdNotAndTopCategoryEquals(Long categoryId, Byte topCategory);
	// Get count of communities which fit to categoryId
	Long countByCategoryIdAndTopCategoryEquals(Long categoryId, Byte topCategory);
	// Get count of searched communities without deleted
	Long countByTitleContainingAndCategoryIdNotAndTopCategoryEquals(String title, Long categoryId, Byte topCategory);
	// Get count of searched communities which fit to categoryId
	Long countByTitleContainingAndCategoryIdAndTopCategoryEquals(String title, Long categoryId, Byte topCategory);
	
	Long countByContentContainingAndCategoryIdNotAndTopCategoryEquals(String content, Long categoryId, Byte topCategory);
	
	Long countByContentContainingAndCategoryIdAndTopCategoryEquals(String content, Long categoryId, Byte topCategory);
	
	Long countByWriterContainingAndCategoryIdNotAndTopCategoryEquals(String writer, Long categoryId, Byte topCategory);
	
	Long countByWriterContainingAndCategoryIdAndTopCategoryEquals(String writer, Long categoryId, Byte topCategory);
	// Increasing view count when click community
	@Transactional
	@Modifying
	@Query(value="UPDATE board SET view=:view WHERE id=:id", nativeQuery = true)
	void updateViewCount(@Param("view") Long view, @Param("id") Long id);
	
	// Updating categoryId
	@Transactional
	@Modifying
	@Query(value="UPDATE board SET category_id=:categoryId WHERE id=:id", nativeQuery = true)
	void updateCommunityCategoryId(@Param("categoryId") Long categoryId, @Param("id") Long id);
}
