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

import com.sangdaero.walab.common.category.controller.CategoryController;
import com.sangdaero.walab.common.category.dto.CategoryDto;
import com.sangdaero.walab.notice.dto.NoticeDto;
import com.sangdaero.walab.notice.service.NoticeService;

@Controller
@RequestMapping("/notice")
public class NoticeController extends CategoryController {
	
	private NoticeService mNoticeService;
	
	public NoticeController(NoticeService noticeService) {
		super(noticeService);
		this.mNoticeService = noticeService;
	}
	
	// Notice list page
	@GetMapping("")
	public String list(
			Model model,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "category", defaultValue = "0") Long category,
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@RequestParam(value = "type", defaultValue = "0") Integer searchType) {
		
        List<NoticeDto> noticeDtoList = mNoticeService.getNoticelist(pageNum, category, keyword, searchType);
        List<CategoryDto> categoryDtoList = mNoticeService.getCategory((byte)1, "", 0);
        Integer firstPage = mNoticeService.getFirstPage(pageNum, category, keyword, searchType);
        Long totalNum = mNoticeService.getNoticeCount(category, keyword, searchType);

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("noticeList", noticeDtoList);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", searchType);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("totalNum", totalNum);

        return "html/notice/list.html";
    }

	// Writing notice page
	@GetMapping("/post")
    public String write(Model model) {
		List<CategoryDto> categoryDtoList = mNoticeService.getCategory((byte)1, "", 0);
		
		model.addAttribute("categoryDto", categoryDtoList);
		
        return "html/notice/write.html";
    }

	// Execute when click save button
    @PostMapping("/post")
    public String write(NoticeDto noticeDto, @RequestParam(value="categoryId")Long categoryId) {
        mNoticeService.savePost(noticeDto, categoryId);
        return "redirect:/notice";
    }

    // Detail page of notice
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        NoticeDto noticeDto = mNoticeService.getPost(id);
        String category = super.mCategoryService.getCategoryMemo(noticeDto.getCategoryId());
        
        model.addAttribute("noticeDto", noticeDto);
        model.addAttribute("category", category);

        return "html/notice/detail.html";
    }

    @GetMapping("/mobile/detail/{no}")
    public String mobile_detail(@PathVariable("no") Long id, Model model) {
        NoticeDto noticeDto = mNoticeService.getPost(id);
        String category = super.mCategoryService.getCategoryMemo(noticeDto.getCategoryId());

        model.addAttribute("noticeDto", noticeDto);
        model.addAttribute("category", category);

        return "html/notice/mobile_detail";
    }

    // Edit page which through detail
    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long id, Model model) {
        NoticeDto noticeDto = mNoticeService.getPost(id);
        List<CategoryDto> categoryDtoList = mNoticeService.getCategory((byte)1, "", 0);
		
        System.out.println("\n\n"+noticeDto+"\n\n");
        
        model.addAttribute("noticeDto", noticeDto);
        model.addAttribute("categoryDto", categoryDtoList);
        return "html/notice/update.html";
    }

    // Saving edit content
    @PutMapping("/post/edit/{no}")
    public String update(NoticeDto noticeDto) {
        mNoticeService.updatePost(noticeDto);
        return "redirect:/notice";
    }

    // Deleting notice
    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long id) {
        mNoticeService.deletePost(id);

        return "redirect:/notice";
    }
}
