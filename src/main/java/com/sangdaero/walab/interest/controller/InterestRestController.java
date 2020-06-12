package com.sangdaero.walab.interest.controller;

import com.sangdaero.walab.activity.dto.ActivityDto;
import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.dto.InterestName;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.interest.application.validator.InterestNameValidator;
import com.sangdaero.walab.interest.application.validator.InterestValidator;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/interestdata")
@RequiredArgsConstructor
public class InterestRestController {

    private final InterestService mInterestService;
    private final InterestNameValidator mInterestNameValidator;
    private final InterestRepository mInterestRepository;

    @InitBinder("interestName")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(mInterestNameValidator); // validator 추가
    }

    @PostMapping("/edit")
    public Map<String, Object> setTitle(@Valid InterestName interestName, Errors errors) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        if(errors.hasErrors()) {
            map.put("message", "이미 존재하거나, 형식에 맞지 않습니다.");

//            model.addAttribute("msg", "hello");
//            System.out.println("===============================");
//            System.out.println("===============================");
//
//            throw new IllegalArgumentException("중복되는 이름입니다.");
//            return "error";
            return map;
        }

        mInterestService.update(interestName.getId(), interestName.getName());
        map.put("name", interestName.getName());
        return map;
//        return interestName.getName();
    }

    @GetMapping("/getAll")
    public List<InterestName> getActivities(){
//        List<InterestName> result = mInterestService.getAllInterest();
        return mInterestService.getAllInterest();
    }
//    @PutMapping("/test")
//    public String update(@PathVariable Long id, @Valid InterestDto interestDTO, Model model, Errors errors) throws Exception {
//
//        if(errors.hasErrors()) {
//            return "redirect:/interest";
//        }
//        mInterestService.update(id, interestDTO.getName());
//        System.out.println(interestDTO);
//
//        return "redirect:/interest";
//    }

}
