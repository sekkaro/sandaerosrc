package com.sangdaero.walab.user.application.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VolunteerRanking {

    private Long id;

    private String name;

    private Integer volunteerTime;

    @Builder
    public VolunteerRanking(Long id, String name, Integer volunteerTime) {
        this.id = id;
        this.name = name;
        this.volunteerTime = volunteerTime;
    }
}
