package com.sangdaero.walab.notice.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sangdaero.walab.common.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	List<Notice> findByTitleContaining(String keyword);
	
	Page<Notice> findAllBySubCategoryNot(Long category, Pageable page);
	
	Page<Notice> findAllBySubCategory(Long category, Pageable page);
	
	Page<Notice> findAllByTitleContainingAndSubCategoryNot(String title, Long category, Pageable page);
	
	Page<Notice> findAllByTitleContainingAndSubCategory(String title, Long category, Pageable page);
	
	Page<Notice> findAllByContentContainingAndSubCategoryNot(String content, Long category, Pageable page);
	
	Page<Notice> findAllByContentContainingAndSubCategory(String content, Long category, Pageable page);
	
	Page<Notice> findAllByWriterContainingAndSubCategoryNot(String writer, Long category, Pageable page);
	
	Page<Notice> findAllByWriterContainingAndSubCategory(String writer, Long category, Pageable page);
	
	Long countBySubCategoryNot(Long category);
	
	Long countBySubCategory(Long category);
	
	Long countByTitleContainingAndSubCategoryNot(String title, Long category);
	
	Long countByTitleContainingAndSubCategory(String title, Long category);
	
	Long countByContentContainingAndSubCategoryNot(String content, Long category);
	
	Long countByContentContainingAndSubCategory(String content, Long category);
	
	Long countByWriterContainingAndSubCategoryNot(String writer, Long category);
	
	Long countByWriterContainingAndSubCategory(String writer, Long category);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE notice SET view=:view WHERE id=:id", nativeQuery = true)
	void updateViewCount(@Param("view") Long view, @Param("id") Long id);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE notice SET sub_category=:sub_category WHERE id=:id", nativeQuery = true)
	void updateNoticeSubCategory(@Param("sub_category") Long subCategory, @Param("id") Long id);
}
