package com.sangdaero.walab.activity.dto;

import com.sangdaero.walab.common.entity.InterestCategory;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EachUserActivity {

    private Long id;
    private String title;
    private Integer eventCategory;
    private InterestCategory interestCategory;
    private LocalDateTime completeTime;
    private Integer volunteerTime;

    @Builder
    public EachUserActivity(Long id, String title, Integer eventCategory,
                            InterestCategory interestCategory, LocalDateTime completeTime, Integer volunteerTime) {
        this.id = id;
        this.title = title;
        this.eventCategory = eventCategory;
        this.interestCategory = interestCategory;
        this.completeTime = completeTime;
        this.volunteerTime = volunteerTime;
    }
}
