package com.sangdaero.walab.activity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppRequest {

    private String email;

    private String name;

    private Long id;

    private Byte type;

    private String title;

    private String startTime;

    private String endTime;

    private String memo;

    private String nickname;

    private Boolean phoneAgree;

    private String phone;

    private Long interestId;

}
