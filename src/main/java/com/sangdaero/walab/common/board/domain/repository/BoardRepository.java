package com.sangdaero.walab.common.board.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sangdaero.walab.common.board.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findByTitleContaining(String keyword);
}
