package com.sangdaero.walab.interest.application.validator;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.dto.InterestName;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class InterestNameValidator implements Validator {

    private final InterestRepository interestRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(InterestName.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        InterestName interestName = (InterestName) o;

        InterestCategory byName = interestRepository.findByName(interestName.getName());

        if(byName!=null && !byName.getId().equals(interestName.getId()) && interestRepository.existsByName(interestName.getName().trim())) {
            errors.rejectValue("name", "invalid.name", new Object[]{interestName.getName()}, "이미 사용중인 이름입니다.");
        }
    }
}
