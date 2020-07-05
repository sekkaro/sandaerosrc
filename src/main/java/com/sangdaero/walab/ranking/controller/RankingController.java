package com.sangdaero.walab.ranking.controller;

import com.sangdaero.walab.user.application.dto.VolunteerRanking;
import com.sangdaero.walab.user.application.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/ranking")
public class RankingController {

	private UserService mUserService;

	public RankingController(UserService mUserService) {
		this.mUserService = mUserService;
	}

	@GetMapping("")
	public String developingPage() {
		return "html/developing";
	}

//	@GetMapping("")
//	public String rankingPage(Model model, @RequestParam(value = "scope", defaultValue = "1") int scope) {
////		List<SimpleUser> userRanking = mUserService.getUserRankingList();
//		List<VolunteerRanking> userRanking = mUserService.getVolunteerRankingList();
//
//		// 월간
//		if(scope==2 || scope==3) {
//			userRanking = mUserService.getRanking(scope);
//		}
//
//		model.addAttribute("userRanking", userRanking);
//		return "html/ranking/ranking";
//	}

}
