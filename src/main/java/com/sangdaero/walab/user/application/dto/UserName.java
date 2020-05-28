package com.sangdaero.walab.user.application.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserName {

    private Long id;

    @NotBlank
    @Length(min = 2)
    @Pattern(regexp = "^[가-힣]{2,4}$")
    private String name;

    @Builder
    public UserName(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
