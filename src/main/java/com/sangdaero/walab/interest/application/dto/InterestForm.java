package com.sangdaero.walab.interest.application.dto;

import com.sangdaero.walab.common.entity.InterestCategory;
import lombok.Data;

@Data
public class InterestForm {

    private String interestName;

    public InterestCategory getInterest() {
        return InterestCategory.builder().name(this.getInterestName()).build();
    }
}
