package com.sangdaero.walab.ranking.controller;

import com.sangdaero.walab.user.application.dto.SimpleUser;
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
	public String rankingPage(Model model, @RequestParam(value = "scope", defaultValue = "1") int scope) {
		List<SimpleUser> userRanking = mUserService.getUserRankingList();

		// 월간
		if(scope==2) {
			userRanking = mUserService.getMonthlyRanking(scope);
		} else if(scope==3) { // 주간
			userRanking = mUserService.getWeeklyRanking(scope);
		}

		model.addAttribute("userRanking", userRanking);
		return "html/ranking/ranking";
	}

}
