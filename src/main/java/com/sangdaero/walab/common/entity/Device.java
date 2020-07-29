package com.sangdaero.walab.common.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Device extends TimeEntity{

    @Id @GeneratedValue
    private Long id;

    private String deviceToken;

    @Builder
    public Device(Long id, String deviceToken) {
        this.id = id;
        this.deviceToken = deviceToken;
    }
}
