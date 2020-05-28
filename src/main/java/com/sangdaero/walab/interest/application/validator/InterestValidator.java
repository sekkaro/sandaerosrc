package com.sangdaero.walab.interest.application.validator;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class InterestValidator implements Validator {

    private final InterestRepository interestRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(InterestDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        InterestDto interestDto = (InterestDto)o;

        if(interestRepository.existsByName(interestDto.getName().trim())) {
            errors.rejectValue("name", "invalid.name", new Object[]{interestDto.getName()}, "이미 사용중인 이름입니다.");
        }
    }
}
