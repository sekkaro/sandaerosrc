package com.sangdaero.walab.interest.controller;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/interest")
@RequiredArgsConstructor
public class InterestRestController {

    private final InterestService mInterestService;

    @PostMapping("/edit")
    public String setTitle(@RequestParam("id") Long id, @Valid InterestDto interestDto, Errors errors) throws Exception {
        mInterestService.update(id, interestDto.getName());
        return interestDto.getName();
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
