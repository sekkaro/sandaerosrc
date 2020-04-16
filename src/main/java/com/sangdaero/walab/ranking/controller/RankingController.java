package com.sangdaero.walab.ranking.controller;

import com.sangdaero.walab.user.application.dto.SimpleUser;
import com.sangdaero.walab.user.application.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ranking")
public class RankingController {

	private UserService mUserService;

	public RankingController(UserService mUserService) {
		this.mUserService = mUserService;
	}

	@GetMapping("")
	public String rankingPage(Model model) {
		List<SimpleUser> userRanking = mUserService.getUserRankingList();
		model.addAttribute("userRanking", userRanking);
		return "html/ranking/ranking";
	}

}
