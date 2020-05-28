package com.sangdaero.walab.user.application.validator;

import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.user.application.dto.UserNickname;
import com.sangdaero.walab.user.application.dto.UserPhone;
import com.sangdaero.walab.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UserNickname.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserNickname userNickname = (UserNickname)o;

        User user = userRepository.findByNickname(userNickname.getNickname());

        if(user!=null && !user.getId().equals(userNickname.getId()) && userRepository.existsByNickname(userNickname.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{userNickname.getNickname()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
