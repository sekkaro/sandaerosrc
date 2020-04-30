package com.sangdaero.walab.common.comment.dto;

import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.Comment;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentDto {
	private Long id;
	private Long boardId;
	private String content;
	private String writer;
	private Byte isDeleted;
	private LocalDateTime regDate;
	private LocalDateTime modDate;

	public Comment toEntity() {
		Comment comment = Comment.builder()
				.id(id)
				.boardId(boardId)
				.content(content)
				.writer(writer)
				.isDeleted(isDeleted)
                .build();
		return comment;
	}
	
	@Builder
	public CommentDto(Long id, Long boardId, String content, String writer, Byte isDeleted, LocalDateTime regDate, LocalDateTime modDate) {
		this.id = id;
		this.boardId = boardId;
		this.content = content;
		this.writer = writer;
		this.isDeleted = isDeleted;
		this.regDate = regDate;
        this.modDate = modDate;
	}
}
