package com.sangdaero.walab.ask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AskController {

	@GetMapping("/ask")
	public String developingPage() {
		return "html/developing";
	}
	
//	@GetMapping("/ask")
//	public String askPage() {
//		return "html/ask.html";
//	}

}
