package com.sangdaero.walab.payment.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.FundraisingEntity;
import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.interest.application.service.InterestService;
import com.sangdaero.walab.payment.dto.FundraisingDto;
import com.sangdaero.walab.payment.dto.PaymentDto;
import com.sangdaero.walab.payment.service.FundraisingService;
import com.sangdaero.walab.payment.service.PaymentService;
import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.service.UserService;

@Controller
public class PaymentController {

	private PaymentService mPaymentService;
	private FundraisingService mFundraisingService;
	
	// user mapping part
	private UserService mUserService;
	private InterestService mInterestService;
	
	// constructor
	public PaymentController(PaymentService mPaymentService, FundraisingService mFundraisingService,
			UserService mUserService, InterestService mInterestService) {
		
		this.mPaymentService = mPaymentService;
		this.mFundraisingService = mFundraisingService;
		this.mUserService = mUserService;
		this.mInterestService = mInterestService;
	}
	
	// - - - - - - - - - - - - Fundraising Part - - - - - - - - - - - - 
	@GetMapping("/fundraising")
	public String fundraisingList() {
		return "html/payment/fundraising/fundraising.html";
	}

	@GetMapping("/fundraisingForm")
	public String fundraisingWrite() {
		return "html/payment/fundraising/fundraisingWrite.html";
	}
	
	@PostMapping("/fundraisingWrite")
	public String fundraisingWrite(FundraisingDto fundraisingDto) {
//		mFundraisingService.saveFundraising(fundraisingDto);
		
		return "redirect:/fundraising";
	}
	
	
	// - - - - - - - - - - - - Payment Part - - - - - - - - - - - - 
	@GetMapping("/payment") // list
	public String paymentList(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
			@RequestParam(value="keyword", defaultValue="") String keyword,
			@RequestParam(value="sortBy", defaultValue="2") Byte sortBy, 
			Model model) { // transfer data to View with Model		
				
		List<PaymentDto> paymentList = mPaymentService.getSearchPaymentList(pageNum, keyword, sortBy); // read all payment records from database (service)
		model.addAttribute("paymentList", paymentList); // actual data transfer, with specific name

		// page numbers
		Integer[] pageList =  mPaymentService.getPageList(pageNum, keyword, sortBy);		
		model.addAttribute("pageList", pageList);
		
		// pass get parameters
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("pageNum", pageNum);

		return "html/payment/payment.html";
	}
	
	
	// Search, sort by
	@GetMapping("/payment/search")
	public String search(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
			@RequestParam(value="keyword", defaultValue="") String keyword, 
			@RequestParam(value="sortBy", defaultValue="2") Byte sortBy, 
			Model model) {
		
		List<PaymentDto> paymentList = mPaymentService.getSearchPaymentList(pageNum, keyword, sortBy);
		model.addAttribute("paymentList", paymentList);
		
		Integer[] pageList =  mPaymentService.getPageList(pageNum, keyword, sortBy);		
		model.addAttribute("pageList", pageList);
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("pageNum", pageNum);
		
		return "html/payment/payment.html";
	}
	
	// go to write form
	@GetMapping("/paymentForm") 
	public String paymentWrite(Model model) {
		
		List<SimpleUser> managers = mUserService.getSimpleUserList("manager");
		model.addAttribute("managers", managers);

		return "html/payment/paymentForm.html";
	}
	
	// write action
	@PostMapping("/paymentForm") 
	public String paymentWrite(PaymentDto paymentDto, Long managerId) {
		
		User user = mUserService.findUserEntity(managerId);
		paymentDto.setManager(user);
		
		mPaymentService.savePayment(paymentDto);
		
		return "redirect:/payment";
	}
	
	// detail
	@GetMapping("paymentDetail/{no}")
	public String paymentDetail(@PathVariable("no") Long no, @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
			@RequestParam(value="keyword", defaultValue="") String keyword, 
			@RequestParam(value="sortBy", defaultValue="2") Byte sortBy, Model model) {
		
		PaymentDto paymentDto = mPaymentService.getSinglePaymentById(no);
		model.addAttribute("paymentDto", paymentDto);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("pageNum", pageNum);
		
		// user mapping part
		List<SimpleUser> simpleUsers = mUserService.getSimpleUserList();
		model.addAttribute("simpleUserList", simpleUsers);
		
		// all fundraising data as list
		List<FundraisingDto> fundraisingDtoList = mFundraisingService.findAllFundraisingDtoByEventId(no);
		model.addAttribute("fundraisingList", fundraisingDtoList);
		
		// iterate to delete redundant data between "Users to be registered" and "Users that are already registered".
//		if (!simpleUsers.isEmpty() && !fundraisingDtoList.isEmpty()) {
//			for (int i=0; i<simpleUsers.size(); i++) {
//				
//				Long unRegistered = simpleUsers.get(i).getId();
//				for (int k=0; k<fundraisingDtoList.size(); k++) {
//					
//				}
//			}
//		}
		
		return "html/payment/paymentDetail.html";
	}
	
	// go to update form for attribute 'paymentCheck'
	@GetMapping("/payment/paymentUpdate/{no}") 
	public String update(@PathVariable("no") Long no, Model model) {
		
		PaymentDto paymentDto = mPaymentService.getSinglePaymentById(no);
		model.addAttribute("paymentDto", paymentDto);
		
		List<SimpleUser> managers = mUserService.getSimpleUserList("manager");
		model.addAttribute("managers", managers);
		
		return "html/payment/paymentUpdate.html";
	}
	
	// update action for attribute 'paymentCheck'
	@PutMapping("/payment/paymentUpdate/{no}") 
	public String update(@PathVariable("no") Long no, PaymentDto paymentDto, Long managerId) { 

		User user = mUserService.findUserEntity(managerId);
		paymentDto.setManager(user);
		
		mPaymentService.savePayment(paymentDto);
		
		return "redirect:/payment";
	}

	// toggle 'paymentCheck' status for checked paymentDto
	@GetMapping("payment/paymentUpdateChecked")
	public String updateChecked(String[] paymentCheckBox) {
		
		// when nothing is checked, just redirect
		if (paymentCheckBox == null) {
			return "redirect:/payment";
		}
		mPaymentService.paymentCheckBoxToggle(paymentCheckBox);
		
		return "redirect:/payment";
	}
	
	// match User - Payment(fundraising)
	@GetMapping("payment/paymentUserMatching/{no}")
	public String matchUserAndPayment(@PathVariable("no") Long no, String[] userMatchingCheckBox) {
		
		String redirection = "redirect:/paymentDetail/" + no; // go back to a detail page, with id={no}
		
		if (userMatchingCheckBox == null) {
			return redirection;
		}
		
		// checked user's ids are now in String[] userMatchingCheckBox.
		// also, @PathVariable "no" is EventEntity's id.
		mPaymentService.fundraisingUserMatching(userMatchingCheckBox, no);
		
		return redirection;
	}
	
	@DeleteMapping("/payment/{no}")
	public String delete(@PathVariable("no") Long no) {
		
		mPaymentService.deletePost(no);
		return "redirect:/payment";
	}
	
}
