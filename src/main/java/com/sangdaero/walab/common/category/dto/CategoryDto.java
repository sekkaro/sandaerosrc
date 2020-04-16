package com.sangdaero.walab.common.category.dto;

import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.BoardCategory;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CategoryDto {
	private Long id;
	private Byte topCategory;
	private Byte status;
	private String memo;
	private String communityManager;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public BoardCategory toEntity() {
		BoardCategory boardCategory = BoardCategory.builder()
				.id(id)
				.topCategory(topCategory)
				.status(status)
				.memo(memo)
				.communityManager(communityManager)
                .build();
		return boardCategory;
	}
	
	@Builder
	public CategoryDto(Long id, Byte topCategory, Byte status, String memo, String communityManager, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.id = id;
		this.topCategory = topCategory;
		this.status = status;
		this.memo = memo;
		this.communityManager = communityManager;
		this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
	}
}
