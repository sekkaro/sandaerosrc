package com.sangdaero.walab.notice.domain.repository;

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

public interface NoticeRepository extends JpaRepository<Board, Long> {
	
	// Get notices without deleted
	Page<Board> findAllByCategoryIdNotAndTopCategoryEquals(Long subCategory, Byte topCategory, Pageable page);
	// Get notices which fit to subCategory
	Page<Board> findAllByCategoryIdAndTopCategoryEquals(Long subCategory, Byte topCategory, Pageable page);
	// Searching notices without deleted
	Page<Board> findAllByTitleContainingAndCategoryIdNotAndTopCategoryEquals(String title, Long subCategory, Byte topCategory, Pageable page);
	// Searching notices which fit to subCategory
	Page<Board> findAllByTitleContainingAndCategoryIdAndTopCategoryEquals(String title, Long subCategory, Byte topCategory, Pageable page);
	
	Page<Board> findAllByContentContainingAndCategoryIdNotAndTopCategoryEquals(String content, Long subCategory, Byte topCategory, Pageable page);
	
	Page<Board> findAllByContentContainingAndCategoryIdAndTopCategoryEquals(String content, Long subCategory, Byte topCategory, Pageable page);
	
	Page<Board> findAllByWriterContainingAndCategoryIdNotAndTopCategoryEquals(String writer, Long subCategory, Byte topCategory, Pageable page);
	
	Page<Board> findAllByWriterContainingAndCategoryIdAndTopCategoryEquals(String writer, Long subCategory, Byte topCategory, Pageable page);
	// Get count of notices without deleted
	Long countByCategoryIdNotAndTopCategoryEquals(Long subCategory, Byte topCategory);
	// Get count of notices which fit to subCategory
	Long countByCategoryIdAndTopCategoryEquals(Long subCategory, Byte topCategory);
	// Get count of searched notices without deleted
	Long countByTitleContainingAndCategoryIdNotAndTopCategoryEquals(String title, Long subCategory, Byte topCategory);
	// Get count of searched notices which fit to subCategory
	Long countByTitleContainingAndCategoryIdAndTopCategoryEquals(String title, Long subCategory, Byte topCategory);
	
	Long countByContentContainingAndCategoryIdNotAndTopCategoryEquals(String content, Long subCategory, Byte topCategory);
	
	Long countByContentContainingAndCategoryIdAndTopCategoryEquals(String content, Long subCategory, Byte topCategory);
	
	Long countByWriterContainingAndCategoryIdNotAndTopCategoryEquals(String writer, Long subCategory, Byte topCategory);
	
	Long countByWriterContainingAndCategoryIdAndTopCategoryEquals(String writer, Long subCategory, Byte topCategory);
	// Increasing view count when click notice
	@Transactional
	@Modifying
	@Query(value="UPDATE board SET view=:view WHERE id=:id", nativeQuery = true)
	void updateViewCount(@Param("view") Long view, @Param("id") Long id);
	// Updating subCategory
	@Transactional
	@Modifying
	@Query(value="UPDATE board SET category_id=:categoryId WHERE id=:id", nativeQuery = true)
	void updateNoticeCategoryId(@Param("categoryId") Long subCategory, @Param("id") Long id);
}
