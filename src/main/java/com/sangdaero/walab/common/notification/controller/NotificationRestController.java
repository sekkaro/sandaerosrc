package com.sangdaero.walab.common.notification.controller;

import com.sangdaero.walab.common.notification.dto.NotificationForUserDto;
import com.sangdaero.walab.common.notification.service.NotificationService;
import com.sangdaero.walab.user.application.dto.UserDto;
import com.sangdaero.walab.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notificationdata")
@RequiredArgsConstructor
public class NotificationRestController {
    private final NotificationService mNotificationService;
    private final UserService mUserService;

    @GetMapping("/getNotificationsForUser")
    public List<NotificationForUserDto> getNotificationForUser(@RequestParam("name") String name,
                                                               @RequestParam("email") String email){
        UserDto userDto = mUserService.createUser(email, name);
        List<NotificationForUserDto> notifications = mNotificationService.getNotificationForUser(userDto);

        return notifications;
    }
}
