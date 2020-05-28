package com.sangdaero.walab.user.application.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserPhone {

    private Long id;

    @NotBlank
    @Pattern(regexp =  "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$")
    private String phone;

    @Builder
    public UserPhone(Long id, String phone) {
        this.id = id;
        this.phone = phone;
    }
}
