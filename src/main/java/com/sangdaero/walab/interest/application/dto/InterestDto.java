package com.sangdaero.walab.interest.application.dto;

import com.sangdaero.walab.common.entity.InterestCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InterestDto {
    private Long id;
    private String name;
    private Byte type;
    private Byte on_off;
    private Long count;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public InterestCategory toEntity() {
        InterestCategory build = InterestCategory.builder()
                .id(id)
                .name(name)
                .type(type)
                .on_off(on_off)
                .build();
        return build;
    }

    @Builder
    public InterestDto(Long id, String name, Byte type, Byte on_off, Long count, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.on_off = on_off;
        this.count = count;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
