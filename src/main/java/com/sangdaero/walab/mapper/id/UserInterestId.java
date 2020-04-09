package com.sangdaero.walab.mapper.id;

import java.io.Serializable;

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
