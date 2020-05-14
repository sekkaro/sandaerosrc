package com.sangdaero.walab.request.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sangdaero.walab.request.service.RequestService;

@RestController
@RequestMapping("/requestdata")
public class RequestRestController {

	private RequestService mRequestService;
	
	public RequestRestController(RequestService requestService) {
		mRequestService = requestService;
	}
	
	@PostMapping("/setStatus")
	public Byte setStatus(@RequestParam("id") Long id, @RequestParam("status") Byte status) {
		mRequestService.setStatus(id, status);
		
		return status;
	}
	
}
