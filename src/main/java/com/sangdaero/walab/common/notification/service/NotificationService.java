package com.sangdaero.walab.common.notification.service;

import com.sangdaero.walab.common.entity.Notification;
import com.sangdaero.walab.common.notification.dto.NotificationForUserDto;
import com.sangdaero.walab.common.notification.repository.NotificationRepository;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    private NotificationRepository mNotificationRepository;
    private UserRepository mUserRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        mNotificationRepository = notificationRepository;
        mUserRepository = userRepository;
    }

    public List<NotificationForUserDto> getNotificationForUser(UserDto userDto) {
        List<Notification> notifications = mNotificationRepository.findAllByUserOrderByRegDateDesc(userDto.toEntity());
        List<NotificationForUserDto> notificationList = new ArrayList<>();

        for(Notification notification : notifications) {
            notificationList.add(NotificationForUserDto.builder()
                    .id(notification.getId())
                    .message(notification.getMessage())
                    .regDate(notification.getRegDate())
                    .build()
            );
        }

        return notificationList;

    }
}
