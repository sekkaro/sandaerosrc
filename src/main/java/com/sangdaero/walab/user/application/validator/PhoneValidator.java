package com.sangdaero.walab.user.application.validator;

import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.user.application.dto.UserPhone;
import com.sangdaero.walab.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PhoneValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UserPhone.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserPhone userPhone = (UserPhone)o;

        User user = userRepository.findByPhone(userPhone.getPhone());

        if(user!=null && !user.getId().equals(userPhone.getId()) && userRepository.existsByPhone(userPhone.getPhone())) {
            errors.rejectValue("phone", "invalid.phone", new Object[]{userPhone.getPhone()}, "이미 사용중인 전화번호입니다.");
        }

    }
}
