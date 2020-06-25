package com.sangdaero.walab.notice.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeAppDto {
	 	private Long id;
	    private String title;
	    private String content;
	    private String writer;
	    private Long view;
	    private Byte status;
	    private Byte topCategory;
	    private Long categoryId;  
	    private String memo;
	    private LocalDateTime regDate;
	    private LocalDateTime modDate;
	    
	    @Builder
		public NoticeAppDto(Long id, String title, String content, String writer, Long view, Byte status,
				Byte topCategory, Long categoryId, String memo, LocalDateTime regDate, LocalDateTime modDate) {
			this.id = id;
			this.title = title;
			this.content = content;
			this.writer = writer;
			this.view = view;
			this.status = status;
			this.topCategory = topCategory;
			this.categoryId = categoryId;
			this.memo = memo;
			this.regDate = regDate;
			this.modDate = modDate;
		}
	    
	    
}
