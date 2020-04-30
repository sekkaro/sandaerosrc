package com.sangdaero.walab.common.comment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sangdaero.walab.common.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByBoardId(Long boardId);
	
	// Updating categoryId
	@Transactional
	@Modifying
	@Query(value="UPDATE comment SET status=:status WHERE id=:id", nativeQuery = true)
	void updateCommentStatus(@Param("status") Byte status, @Param("id") Long id);
}
