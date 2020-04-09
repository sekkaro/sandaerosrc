package com.sangdaero.walab.notice.dto;



import java.time.LocalDateTime;

import com.sangdaero.walab.common.entity.Notice;

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
    private Long subCategory;  
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Notice toEntity() {
        Notice notice = Notice.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .view(view)
                .topCategory(topCategory)
                .subCategory(subCategory)
                .build();
        return notice;
    }

    @Builder
    public NoticeDto(Long id, String title, String content, String writer, Long view, Byte topCategory, Long subCategory, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.view = view;
        this.topCategory = topCategory;
        this.subCategory = subCategory;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
