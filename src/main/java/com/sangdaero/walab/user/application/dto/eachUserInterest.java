package com.sangdaero.walab.user.application.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class eachUserInterest {

    private Long id;

    private String interestName;

    @Builder
    public eachUserInterest(Long id, String interestName) {
        this.id=id;
        this.interestName=interestName;
    }
}
