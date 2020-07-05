package com.sangdaero.walab.user.application.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;


@Data
public class UserEmail {

    private Long id;

    @Email
    private String socialId;

    @Builder
    public UserEmail(Long id, String socialId) {
        this.id=id;
        this.socialId=socialId;
    }

}
