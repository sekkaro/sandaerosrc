package com.sangdaero.walab;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String homePage() {
		return "html/index.html";
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
}
