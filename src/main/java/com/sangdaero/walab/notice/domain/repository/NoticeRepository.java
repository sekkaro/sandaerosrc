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
	
	// Get communities without deleted
	Page<Board> findAllByStatusNotAndTopCategoryEquals(Byte status, Byte topCategory, Pageable page);
	// Get communities deleted
	Page<Board> findAllByStatusAndTopCategoryEquals(Byte status, Byte topCategory, Pageable page);
	// Get communities which fit to categoryId
	Page<Board> findAllByStatusNotAndCategoryIdAndTopCategoryEquals(Byte status, Long categoryId, Byte topCategory, Pageable page);
	// Searching communities without deleted
	Page<Board> findAllByTitleContainingAndStatusNotAndTopCategoryEquals(String title, Byte status, Byte topCategory, Pageable page);
	// Searching communities deleted
	Page<Board> findAllByTitleContainingAndStatusAndTopCategoryEquals(String title, Byte status, Byte topCategory, Pageable page);
	// Searching communities which fit to categoryId
	Page<Board> findAllByTitleContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(String title, Byte status, Long categoryId, Byte topCategory, Pageable page);
	
	Page<Board> findAllByContentContainingAndStatusNotAndTopCategoryEquals(String content, Byte status, Byte topCategory, Pageable page);
	
	Page<Board> findAllByContentContainingAndStatusAndTopCategoryEquals(String content, Byte status, Byte topCategory, Pageable page);
	
	Page<Board> findAllByContentContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(String content, Byte status, Long categoryId, Byte topCategory, Pageable page);
	
	Page<Board> findAllByWriterContainingAndStatusNotAndTopCategoryEquals(String writer, Byte status, Byte topCategory, Pageable page);
	
	Page<Board> findAllByWriterContainingAndStatusAndTopCategoryEquals(String writer, Byte status, Byte topCategory, Pageable page);
	
	Page<Board> findAllByWriterContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(String writer, Byte status, Long categoryId, Byte topCategory, Pageable page);
	// Get count of communities without deleted
	Long countByStatusNotAndTopCategoryEquals(Byte status, Byte topCategory);
	// Get count of communities deleted
	Long countByStatusAndTopCategoryEquals(Byte status, Byte topCategory);
	// Get count of communities which fit to categoryId
	Long countByStatusNotAndCategoryIdAndTopCategoryEquals(Byte status, Long categoryId, Byte topCategory);
	// Get count of searched communities without deleted
	Long countByTitleContainingAndStatusNotAndTopCategoryEquals(String title, Byte status, Byte topCategory);
	// Get count of searched communities deleted
	Long countByTitleContainingAndStatusAndTopCategoryEquals(String title, Byte status, Byte topCategory);
	// Get count of searched communities which fit to categoryId
	Long countByTitleContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(String title, Byte status, Long categoryId, Byte topCategory);
	
	Long countByContentContainingAndStatusNotAndTopCategoryEquals(String content, Byte status, Byte topCategory);
	
	Long countByContentContainingAndStatusAndTopCategoryEquals(String content, Byte status, Byte topCategory);
	
	Long countByContentContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(String content, Byte status, Long categoryId, Byte topCategory);
	
	Long countByWriterContainingAndStatusNotAndTopCategoryEquals(String writer, Byte status, Byte topCategory);
	
	Long countByWriterContainingAndStatusAndTopCategoryEquals(String writer, Byte status, Byte topCategory);
	
	Long countByWriterContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(String writer, Byte status, Long categoryId, Byte topCategory);
	// Increasing view count when click community
	@Transactional
	@Modifying
	@Query(value="UPDATE board SET view=:view WHERE id=:id", nativeQuery = true)
	void updateViewCount(@Param("view") Long view, @Param("id") Long id);
	
	// Updating categoryId
	@Transactional
	@Modifying
	@Query(value="UPDATE board SET status=:status WHERE id=:id", nativeQuery = true)
	void updateCommunityCategoryId(@Param("status") Byte status, @Param("id") Long id);

	List<Board> findAllByTopCategoryOrderByRegDateDesc(byte topCategory);

	List<Board> findTop5ByTopCategoryOrderByRegDateDesc(byte topCategory);

//	Long countByTopCategory(Long category);
}
