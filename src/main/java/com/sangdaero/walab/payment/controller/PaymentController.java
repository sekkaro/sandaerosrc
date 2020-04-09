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

import com.sangdaero.walab.payment.dto.PaymentDto;
import com.sangdaero.walab.payment.service.PaymentService;

@Controller
public class PaymentController {

	private PaymentService mPaymentService;
	
	// constructor
	public PaymentController(PaymentService mPaymentService) {
		this.mPaymentService = mPaymentService;
	}
	
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
			@RequestParam(value="status", defaultValue="2") Byte status,
			Model model) {
		
		List<PaymentDto> paymentList = mPaymentService.getSearchPaymentList(pageNum, keyword, sortBy);
		model.addAttribute("paymentList", paymentList);
		
		Integer[] pageList =  mPaymentService.getPageList(pageNum, keyword, sortBy);		
		model.addAttribute("pageList", pageList);
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("status", status);
		
		return "html/payment/payment.html";
	}
	
	// go to write form
	@GetMapping("/paymentForm") 
	public String paymentWrite() {

		return "html/payment/paymentForm.html";
	}
	
	// write action
	@PostMapping("/paymentForm") 
	public String paymentWrite(PaymentDto paymentDto) {
		
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

		return "html/payment/paymentDetail.html";
	}
	
	// update action for attribute 'status' 
	@PutMapping("paymentStatusUpdate/{no}")
	public String paymentStatusUpdate(@PathVariable("no") Long no) {
		
		PaymentDto paymentDto = mPaymentService.getSinglePaymentById(no);
		mPaymentService.paymentStatusToggle(paymentDto);
		System.out.println("controller : status update : "+paymentDto.getStatus());

		return "redirect:/payment";
	}
	
	// go to update form for attribute 'paymentCheck'
	@GetMapping("/payment/paymentUpdate/{no}") 
	public String update(@PathVariable("no") Long no, Model model) {
		
		PaymentDto paymentDto = mPaymentService.getSinglePaymentById(no);
		model.addAttribute("paymentDto", paymentDto);
		
		return "html/payment/paymentUpdate.html";
	}
	
	// update action for attribute 'paymentCheck'
	@PutMapping("/payment/paymentUpdate/{no}") 
	public String update(PaymentDto paymentDto) { 
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
	
	@DeleteMapping("/payment/{no}")
	public String delete(@PathVariable("no") Long no) {
		
		mPaymentService.deletePost(no);
		return "redirect:/payment";
	}
	
}
