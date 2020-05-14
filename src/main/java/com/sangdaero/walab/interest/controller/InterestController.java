
package com.sangdaero.walab.interest.controller;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.user.application.dto.SimpleUser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/interest")
public class InterestController {

	private InterestService mInterestService;

	public InterestController(InterestService interestService) {
		this.mInterestService = interestService;
	}

	@GetMapping("")
	public String interestPage(Model model) {
//		List<InterestDTO> interestDTOList = mInterestService.getInterestList(type);
		List<InterestDto> interestDTOList = mInterestService.getInterestList();
		model.addAttribute("interestList", interestDTOList);
		model.addAttribute(new InterestDto());
		return "html/interest/interest";
	}

//	@GetMapping("/add")
//	public String add() {
//		return "html/interest/interest";
//	}

	@PostMapping("/add")
	public String addInterest(@Valid InterestDto interestDto, Errors errors) {

		if(errors.hasErrors()) {
			return "redirect:/interest";
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
	public String update(InterestDto interestDTO) {
		System.out.println(interestDTO);
		mInterestService.addInterest(interestDTO);
		return "redirect:/interest";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") Long id) {
		mInterestService.deleteInterest(id);
		return "redirect:/interest";
	}
}