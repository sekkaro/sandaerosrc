package com.sangdaero.walab.notice.dto;



import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.Board;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoticeDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long view;
    private Byte topCategory;
    private Long categoryId;  
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public Board toEntity() {
        Board notice = Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .view(view)
                .topCategory(topCategory)
                .categoryId(categoryId)
                .build();
        return notice;
    }

    @Builder
    public NoticeDto(Long id, String title, String content, String writer, Long view, Byte topCategory, Long categoryId, LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.view = view;
        this.topCategory = topCategory;
        this.categoryId = categoryId;
        this.regDate = regDate;
        this.modDate = modDate;
    }
}
