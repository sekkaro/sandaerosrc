package com.sangdaero.walab.app;

import com.sangdaero.walab.activity.domain.repository.ActivityRepository;
import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppRestController {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

//    @GetMapping
//    @ResponseBody
//    public ResponseEntity getActivityList() {
//        List<EventEntity> allActivity = activityRepository.findAll();
//
//
//
//    }

//    @PostMapping
//    public ResponseEntity applyActivity() {
//
//    }
//
//    @PostMapping
//    public ResponseEntity createActivity() {
//
//    }
//
//    @PostMapping
//    public ResponseEntity cancleActivity() {
//
//    }


    @GetMapping("/test/get")
    public ResponseEntity testGetFromApp() {
        User user = new User();
        user.setName("App유저(GET)");
        user.setNickname("react-native");
        user.setPhone("010-1111-1111");
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/test/post")
    public ResponseEntity testPostFromApp() {
        User user = new User();
        user.setName("App유저(POST)");
        user.setNickname("react-native");
        user.setPhone("010-2222-2222");
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
