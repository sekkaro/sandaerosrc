package com.sangdaero.walab.volunteer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VolunteerController {
	
	@GetMapping("/volunteer")
	public String volunteerPage() {
		return "html/volunteer.html";
	}

}
