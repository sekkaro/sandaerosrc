package com.sangdaero.walab.notice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sangdaero.walab.notice.domain.repository.NoticeRepository;
import com.sangdaero.walab.notice.dto.NoticeAppDto;
import com.sangdaero.walab.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/noticedata")
@RequiredArgsConstructor
public class NoticeRestController {

	private final NoticeService mNoticeService;
	
	@GetMapping("/getNotices")
	public List<NoticeAppDto> getNotices(){
		return mNoticeService.getNotices();
	}
	
	@GetMapping("/getTop5Notices")
	public List<NoticeAppDto> getTop5Notices(){
		return mNoticeService.getTop5Notices();
	}
	
}
