package com.sangdaero.walab.common.board.controller;

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
import com.sangdaero.walab.common.board.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {

    private BoardService mBoardService;

    public BoardController(BoardService boardService) {
        mBoardService = boardService;
    }

    @GetMapping("")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        List<BoardDto> boardDtoList = mBoardService.getBoardlist(pageNum);
        Integer[] pageList = mBoardService.getPageList(pageNum);

        model.addAttribute("boardList", boardDtoList);
        model.addAttribute("pageList", pageList);

        return "board/list.html";
    }

    @GetMapping("/post")
    public String write() {
        return "board/write.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        mBoardService.savePost(boardDto);
        return "redirect:/board";
    }

    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        BoardDto boardDto = mBoardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/detail.html";
    }

    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long id, Model model) {
        BoardDto boardDto = mBoardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/update.html";
    }

    @PutMapping("/post/edit/{no}")
    public String update(BoardDto boardDto) {
        mBoardService.savePost(boardDto);
        return "redirect:/board";
    }

    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long id) {
        mBoardService.deletePost(id);

        return "redirect:/board";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = mBoardService.searchPosts(keyword);
        model.addAttribute("boardList", boardDtoList);

        return "board/list.html";
    }
}
