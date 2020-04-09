package com.sangdaero.walab.request.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sangdaero.walab.request.dto.RequestDto;
import com.sangdaero.walab.request.service.RequestService;


@Controller
@RequestMapping("/request")
public class RequestController {
	
	private RequestService mRequestService;
	
	// constructor
	public RequestController(RequestService mRequestService) {
		this.mRequestService = mRequestService;
	}
	
	@GetMapping("")
	public String requestPage(Model model,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "type", defaultValue = "0") Integer searchType,
			@RequestParam(value = "sort", defaultValue = "1") Integer sortType) {
		
		List<RequestDto> requestDtoList = mRequestService.getRequestlist(pageNum, keyword, searchType, sortType);
        Integer[] pageList = mRequestService.getPageList(pageNum, keyword, searchType, sortType);

        model.addAttribute("requestList", requestDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", searchType);
        model.addAttribute("sort", sortType);

        return "html/request/request.html";
	}
	
	@GetMapping("/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        RequestDto requestDto = mRequestService.getPost(id);

        model.addAttribute("requestDto", requestDto);
        
        return "html/request/requestDetail.html";
    }

}
