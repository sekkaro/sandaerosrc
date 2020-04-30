package com.sangdaero.walab.mapper.id;

import java.io.Serializable;

public class UserEventId implements Serializable {
    private Long user;
    private Long event;

    public UserEventId() {
    }

    public UserEventId(Long user, Long event) {
        this.user = user;
        this.event = event;
    }
}
