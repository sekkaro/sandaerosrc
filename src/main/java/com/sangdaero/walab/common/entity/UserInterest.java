package com.sangdaero.walab.common.entity;

import com.sangdaero.walab.mapper.id.UserInterestId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@IdClass(UserInterestId.class)
public class UserInterest {

    @PrePersist
    public void prePersist() {
        this.on_off = this.on_off == null ? 1 : this.on_off;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "interest_id")
    private InterestCategory interest;


    private Byte on_off;
}
