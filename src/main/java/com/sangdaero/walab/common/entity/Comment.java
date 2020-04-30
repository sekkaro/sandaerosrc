package com.sangdaero.walab.common.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert

public class Comment extends TimeEntity {
	@Id
    @GeneratedValue
    private Long id;
	
	@Column(name="board_id")
	private Long boardId;
	
	@Column(length = 255, nullable = false)
	private String content;
	
	@Column(length = 10, nullable = false)
    private String writer;
	
	@Column(name="is_deleted", columnDefinition="TINYINT", length = 1)
    @ColumnDefault("1")
	private Byte isDeleted;
	
	@Builder
	public Comment(Long id, Long boardId, String content, String writer, Byte isDeleted) {
		this.id = id;
		this.boardId = boardId;
		this.content = content;
		this.writer = writer;
		this.isDeleted = isDeleted;
	}
}
