
package com.sangdaero.walab.interest.controller;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.interest.application.validator.InterestValidator;
import com.sangdaero.walab.user.application.dto.SimpleUser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/interest")
@RequiredArgsConstructor
public class InterestController {

	private final InterestService mInterestService;
	private final InterestValidator mInterestValidator;

	@InitBinder("interestDto")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(mInterestValidator); // validator 추가
	}

	@GetMapping("")
	public String interestPage(Model model) {
//		List<InterestDTO> interestDTOList = mInterestService.getInterestList(type);
		List<InterestDto> interestDTOList = mInterestService.getInterestList(1);
		model.addAttribute("interestList", interestDTOList);
		model.addAttribute(new InterestDto());
//		model.addAttribute("mod_interest", new InterestDto());
		return "html/interest/interest";
	}

//	@GetMapping("/add")
//	public String add() {
//		return "html/interest/interest";
//	}

	@PostMapping("/add")
	public String addInterest(@Valid InterestDto interestDto, Errors errors, Model model) {

		if(errors.hasErrors()) {
			List<InterestDto> interestDTOList = mInterestService.getInterestList(1);
			model.addAttribute("interestList", interestDTOList);
			return "html/interest/interest";
		}

		mInterestService.addInterest(interestDto);
		return "redirect:/interest";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		InterestDto interestDTO = mInterestService.getInterest(id);

		model.addAttribute("interestDTO", interestDTO);
		return "html/interest/update";
	}

	@PutMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid InterestDto interestDTO, Model model, Errors errors) throws Exception {

		if(errors.hasErrors()) {
			return "redirect:/interest";
		}
		mInterestService.update(id, interestDTO.getName());
		System.out.println(interestDTO);
//		mInterestService.addInterest(interestDTO);

//		List<InterestDto> interestDTOList = mInterestService.getInterestList();
//		model.addAttribute("interestList", interestDTOList);
//		model.addAttribute(new InterestDto());
//		model.addAttribute("mod_interest", new InterestDto());
//		return "html/interest/interest";
		
		return "redirect:/interest";
	}

	@PutMapping("/{id}")
	public String disabled(@PathVariable("id") Long id) throws Exception {
		mInterestService.setOnOff(id);
//		mInterestService.deleteInterest(id);
		return "redirect:/interest";
	}
}