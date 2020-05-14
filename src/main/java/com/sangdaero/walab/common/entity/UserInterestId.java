package com.sangdaero.walab.common.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInterestId implements Serializable {
	
    private Long user;
    private Long interest;

    public UserInterestId() {
    }

    public UserInterestId(Long user, Long interest) {
        this.user = user;
        this.interest = interest;
    }

}

