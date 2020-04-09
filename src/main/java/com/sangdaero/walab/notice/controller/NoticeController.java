package com.sangdaero.walab.notice.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sangdaero.walab.common.board.dto.BoardDto;
import com.sangdaero.walab.notice.dto.NoticeDto;
import com.sangdaero.walab.notice.service.NoticeService;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	private NoticeService mNoticeService;
	
	public NoticeController(NoticeService noticeService) {
		this.mNoticeService = noticeService;
	}
	
	@GetMapping("")
	public String list(
			Model model,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "category", defaultValue = "1") Long category,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "type", defaultValue = "0") Integer searchType) {
        List<NoticeDto> noticeDtoList = mNoticeService.getNoticelist(pageNum, category, keyword, searchType);
        Integer[] pageList = mNoticeService.getPageList(pageNum, category, keyword, searchType);

        model.addAttribute("noticeList", noticeDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", searchType);

        return "html/notice/list.html";
    }

	@GetMapping("/post")
    public String write() {
        return "html/notice/write.html";
    }

    @PostMapping("/post")
    public String write(NoticeDto noticeDto) {
        mNoticeService.savePost(noticeDto);
        return "redirect:/notice";
    }

    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        NoticeDto noticeDto = mNoticeService.getPost(id);
        
        String category;
        
        switch(noticeDto.getSubCategory().toString()) {
	        case "1":
	        	category = "전체";
	        	break;
	        case "2":
	        	category = "자원봉사자";
	        	break;
	        case "3":
	        	category = "이용자";
	        	break;
	        default:
	        	category = "에러";
	        	break;
        }

        model.addAttribute("noticeDto", noticeDto);
        model.addAttribute("category", category);
        
        
        return "html/notice/detail.html";
    }

    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long id, Model model) {
        NoticeDto noticeDto = mNoticeService.getPost(id);
        model.addAttribute("noticeDto", noticeDto);
        return "html/notice/update.html";
    }

    @PutMapping("/post/edit/{no}")
    public String update(NoticeDto noticeDto) {
    	System.out.println("\n\n\n");
    	System.out.println(noticeDto);
    	System.out.println("\n\n\n");
        mNoticeService.updatePost(noticeDto);
        return "redirect:/notice";
    }

    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long id) {
        mNoticeService.deletePost(id);

        return "redirect:/notice";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword, @RequestParam(value = "type") int searchType, Model model) {
        List<NoticeDto> noticeDtoList = mNoticeService.searchPosts(keyword, searchType);
        model.addAttribute("noticeList", noticeDtoList);

        return "html/notice/list.html";
    }
	
}
