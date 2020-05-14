package com.sangdaero.walab.common.comment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.comment.dto.CommentDto;
import com.sangdaero.walab.common.comment.repository.CommentRepository;
import com.sangdaero.walab.common.entity.Comment;

@Service
public class CommentService {
	protected CommentRepository mCommentRepository;
	
	public CommentService(CommentRepository commentRepository) {
		this.mCommentRepository = commentRepository;
	}
	
    // Convert Comment Entity to DTO
    private CommentDto convertEntityToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .content(comment.getContent())
                .writer(comment.getWriter())
                .isDeleted(comment.getIsDeleted())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();
    }
	
	// Save comment
    public Long saveComment(CommentDto commentDto) {
        return mCommentRepository.save(commentDto.toEntity()).getId();
    }
    
    // Update comment
    public Long updateComment(CommentDto commentDto) {
        return mCommentRepository.save(commentDto.toEntity()).getId();
    }
    
    // Delete comment
    public void deleteComment(Long id) {
    	Byte delete = 0;
		mCommentRepository.updateCommentStatus(delete, id);
    }
    
    public List<CommentDto> getComment(Long boardId) {
    	List<Comment> comments;
    	
    	comments = mCommentRepository.findAllByBoardId(boardId);
    	
    	List<CommentDto> commentDtoList = new ArrayList<CommentDto>();
    	
    	for(Comment comment : comments) {
    		commentDtoList.add(this.convertEntityToDto(comment));
    	}
    	
    	return commentDtoList;
    	
    }
    
    // Detail of id's comment
    public CommentDto getCommentDetail(Long id) {
        Optional<Comment> CommentWrapper = mCommentRepository.findById(id);

        Comment comment = CommentWrapper.get();

        CommentDto commentDto = CommentDto.builder()
        		.id(comment.getId())
                .boardId(comment.getBoardId())
                .content(comment.getContent())
                .writer(comment.getWriter())
                .isDeleted(comment.getIsDeleted())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();

        return commentDto;
    }
}
