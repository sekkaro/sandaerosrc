package com.sangdaero.walab;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String homePage() {
		return "redirect:/activity";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "html/login.html";
	}

//	@GetMapping("/test/map")
//	public String naverMap() {
//		return "html/request/naverMap";
//	}

	@GetMapping("/test/juso")
	public String jusoSearch() {
		return "html/activity/jusoSearch";
	}

	@GetMapping("/test/tagify")
	public String testTagify() {
		return "html/test";
	}

	@GetMapping("/test/addAct")
	public String addAct() {
		return "html/test/insertAct";
	}
}
