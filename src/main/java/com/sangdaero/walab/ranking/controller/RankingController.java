package com.sangdaero.walab.ranking.controller;

import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.user.application.dto.VolunteerRanking;
import com.sangdaero.walab.user.application.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
	public String rankingPage(Model model,
							  @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 10) Pageable pageable) {

		Page<User> userRankingPage = mUserService.getAllUserRankingPageList(pageable);
		Long totalNum = userRankingPage.getTotalElements();

		model.addAttribute("userRanking", userRankingPage);
		model.addAttribute("totalNum", totalNum);

		return "html/ranking/ranking";
	}

//  주간 & 월간
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
