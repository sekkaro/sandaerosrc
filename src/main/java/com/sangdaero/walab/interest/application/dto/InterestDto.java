package com.sangdaero.walab.interest.application.dto;

import com.sangdaero.walab.common.entity.InterestCategory;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InterestDto {
    private Long id;

    @NotBlank
    @Length(min = 2)
    private String name;

    private Byte type;
    private Long count;
    private Byte on_off;
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
    public InterestDto(Long id, String name, Byte type, Long count, Byte on_off, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.count = count;
        this.on_off = on_off;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
