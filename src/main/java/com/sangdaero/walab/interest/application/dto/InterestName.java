package com.sangdaero.walab.interest.application.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class InterestName {

    private Long id;

    @NotBlank
    @Length(min = 2)
    private String name;

    @Builder
    public InterestName(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
