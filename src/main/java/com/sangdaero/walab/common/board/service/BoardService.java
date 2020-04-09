package com.sangdaero.walab.common.board.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.board.domain.entity.Board;
import com.sangdaero.walab.common.board.domain.repository.BoardRepository;
import com.sangdaero.walab.common.board.dto.BoardDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private BoardRepository mBoardRepository;
    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POST_COUNT = 4;  // 한 페이지에 존재하는 게시글 수

    public BoardService(BoardRepository boardRepository) {
        mBoardRepository = boardRepository;
    }

    public Long savePost(BoardDto boardDto) {
        return mBoardRepository.save(boardDto.toEntity()).getId();
    }

    public List<BoardDto> getBoardlist(Integer pageNum) {

        Page<Board> page = mBoardRepository
                .findAll(PageRequest
                        .of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));

        //List<Board> boards = boardRepository.findAll();
        List<Board> boards = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : boards) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    public Integer[] getPageList(Integer curPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        // 총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;

        // 페이지 번호 할당
        for(int val=curPageNum, i=0;val<=blockLastPageNum;val++, i++) {
            pageList[i] = val;
        }

        return pageList;
    }

    public Long getBoardCount() {
        return mBoardRepository.count();
    }

    public BoardDto getPost(Long id) {
        Optional<Board> boardWrapper = mBoardRepository.findById(id);
        Board board = boardWrapper.get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getRegdate())
                .build();

        return boardDto;
    }

    public void deletePost(Long id) {
        mBoardRepository.deleteById(id);
    }

    public List<BoardDto> searchPosts(String keyword) {
        List<Board> boards = mBoardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if(boards.isEmpty()) return boardDtoList;

        for(Board board : boards) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    private BoardDto convertEntityToDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getRegdate())
                .build();
    }
}
