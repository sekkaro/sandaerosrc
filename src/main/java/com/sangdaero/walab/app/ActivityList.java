package com.sangdaero.walab.app;

import com.sangdaero.walab.common.entity.InterestCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityList {

    private Long id;

    private String title;

    private InterestCategory interestCategory;

    private String place;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
