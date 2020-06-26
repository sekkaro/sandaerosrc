package com.sangdaero.walab.common.notification.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NotificationForUserDto {
    private Long id;
    private String message;
    private LocalDateTime regDate;

    @Builder
    public NotificationForUserDto(Long id, String message, LocalDateTime regDate) {
        this.id = id;
        this.message = message;
        this.regDate = regDate;
    }
}
