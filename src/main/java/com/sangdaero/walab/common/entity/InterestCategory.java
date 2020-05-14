package com.sangdaero.walab.common.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InterestCategory extends TimeEntity {

    @Id @GeneratedValue
    private Long id;

    @NotEmpty
    private String name;

    private Byte type;

    private Byte on_off;

    @OneToMany(mappedBy = "interest")
    private List<UserInterest> userInterestList = new ArrayList<>();

    @Builder
    public InterestCategory(Long id, String name, Byte type, Byte on_off) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.on_off = 1;
    }
}
