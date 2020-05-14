package com.sangdaero.walab.common.entity;

import com.sangdaero.walab.common.entity.UserInterest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public InterestCategory update(String name) {
        this.setName(name);
        this.setModDate(LocalDateTime.now());
        return this;
    }
}
