package com.sangdaero.walab.payment.controller;

import java.util.ArrayList;
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
import com.sangdaero.walab.payment.service.PaymentNoticeService;
import com.sangdaero.walab.payment.service.PaymentService;

@Controller
public class PaymentController {

	private PaymentService mPaymentService;
	private PaymentNoticeService mPaymentNoticeService;
	
	// constructor
	public PaymentController(PaymentService mPaymentService) {
		this.mPaymentService = mPaymentService;
	}
	
	@GetMapping("/payment") // list
	public String paymentList(Model model) { // transfer data to View with Model		

		// show lists
		List<PaymentDto> paymentList = mPaymentService.getPaymentList(); // read all payment records from database (service)
		model.addAttribute("paymentList", paymentList); // actual data transfer, with specific name
		
		// transfer page number
		
		// show paymentNotice contents
//		PaymentNoticeDto mPaymentNoticeDto = mPaymentNoticeService.getPaymentNoticeDto();
//		model.addAttribute("paymentNoticeDto", mPaymentNoticeDto);

		return "html/payment/payment.html";
	}
	
	
	// Search, sort by
	@GetMapping("/payment/search")
	public String search(@RequestParam(value="keyword") String keyword, @RequestParam(value="sortBy") Byte sortBy, Model model) {
		
		List<PaymentDto> paymentList = mPaymentService.searchPaymentList(keyword, sortBy);
		model.addAttribute("paymentList", paymentList);
		
		// 리스트 화면으로 돌아갔을 때 select가 유지되게 하는 데 필요한 변수
		model.addAttribute("sortBy", sortBy);
		
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
	public String paymentDetail(@PathVariable("no") Long no, Model model) {
		
		PaymentDto paymentDto = mPaymentService.getSinglePaymentById(no);
		model.addAttribute("paymentDto", paymentDto);

		return "html/payment/paymentDetail.html";
	}
	
	// go to update form
	@GetMapping("/payment/paymentUpdate/{no}") 
	public String update(@PathVariable("no") Long no, Model model) {
		
		PaymentDto paymentDto = mPaymentService.getSinglePaymentById(no);
		model.addAttribute("paymentDto", paymentDto);
		
		return "html/payment/paymentUpdate.html";
	}
	
	// update action
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
		
		// when at least one thing is checked,
		for (int i=0; i<paymentCheckBox.length; i++) { // for each PaymentDto, if user checked the checkBox,
			
			long id = Long.parseLong(paymentCheckBox[i]); // get the id
			 PaymentDto checkedPaymentDto = mPaymentService.getSinglePaymentById(id); // and with the id, get the data from DB
			 
			 // toggle the 'paymentCheck' status. (Byte value, 0 or 1)
			 if(checkedPaymentDto.getPaymentCheck() == (byte)1) {
				 checkedPaymentDto.setPaymentCheck((byte)0);
			 } else {
				 checkedPaymentDto.setPaymentCheck((byte)1);
			 }
			 mPaymentService.savePayment(checkedPaymentDto); // save the modified result in DB
		}
		
		return "redirect:/payment";
	}
	
	@DeleteMapping("/payment/{no}")
	public String delete(@PathVariable("no") Long no) {
		
		mPaymentService.deletePost(no);
		return "redirect:/payment";
	}

}
