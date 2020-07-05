package com.sangdaero.walab.notice.service;


import com.sangdaero.walab.notice.dto.NoticeAppDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.category.repository.CategoryRepository;
import com.sangdaero.walab.common.category.service.CategoryService;
import com.sangdaero.walab.common.entity.Board;
import com.sangdaero.walab.common.entity.BoardCategory;
import com.sangdaero.walab.notice.domain.repository.NoticeRepository;
import com.sangdaero.walab.notice.dto.NoticeDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService extends CategoryService {

    private NoticeRepository mNoticeRepository;
    private static final int BLOCK_PAGE_NUMCOUNT = 6; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POSTCOUNT = 8;  // 한 페이지에 존재하는 게시글 수
    private static final Byte topCategory = 1;

    public NoticeService(NoticeRepository noticeRepository, CategoryRepository categoryRepository) {
    	super(categoryRepository);
        this.mNoticeRepository = noticeRepository;
    }
    
    // Convert Entity to DTO
    private NoticeDto convertEntityToDto(Board notice) {
        return NoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getWriter())
                .view(notice.getView())
                .status(notice.getStatus())
                .topCategory(notice.getTopCategory())
                .regDate(notice.getRegDate())
                .modDate(notice.getModDate())
                .build();
    }

    // Save
    public Long savePost(NoticeDto noticeDto, Long categoryId) {
        return mNoticeRepository.save(noticeDto.toEntity()).getId();
    }
    
    // Update
    public Long updatePost(NoticeDto noticeDto) {
        return mNoticeRepository.save(noticeDto.toEntity()).getId();
    }
    
    // Delete
    public void deletePost(Long id) {
    	Byte status = 0;
    	mNoticeRepository.updateCommunityCategoryId(status, id);
    }

    // getNoticelist -> convertEntitytoDto
    public List<NoticeDto> getNoticelist(Integer pageNum, Long categoryId, String keyword, Integer searchType) {
    	Page<Board> page;
    	
    	Byte deleted = 0;

   		switch(searchType) {
   	// Search by Title
		case 1:
			if (categoryId == 0) {
        		page = mNoticeRepository.findAllByTitleContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			} else if (categoryId == -1) {
        		page = mNoticeRepository.findAllByTitleContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			} else {
				page = mNoticeRepository.findAllByTitleContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory,
        				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			}
			break;
		// Search by Content
		case 2:
			if (categoryId == 0) {
        		page = mNoticeRepository.findAllByContentContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			} else if (categoryId == -1) {
        		page = mNoticeRepository.findAllByContentContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			} else {
				page = mNoticeRepository.findAllByContentContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory,
        				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			}
			break;
		// Search by Writer
		case 3:
			if (categoryId == 0) {
        		page = mNoticeRepository.findAllByWriterContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			} else if (categoryId == -1) {
        		page = mNoticeRepository.findAllByWriterContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			} else {
				page = mNoticeRepository.findAllByWriterContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory,
        				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
			}
			break;
		// Communities without search
		default:
			if (categoryId == 0) {
        		page = mNoticeRepository.findAllByStatusNotAndTopCategoryEquals(deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
        	} else if (categoryId == -1) {
        		page = mNoticeRepository.findAllByStatusAndTopCategoryEquals(deleted, topCategory,
        				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
        	} else {
        		page = mNoticeRepository.findAllByStatusNotAndCategoryIdAndTopCategoryEquals(deleted, categoryId, topCategory,
        				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
        	}
			break;
    	}
    	
        List<Board> notices = page.getContent();
        List<NoticeDto> noticeDtoList = new ArrayList<>();

        for(Board notice : notices) {
        	noticeDtoList.add(this.convertEntityToDto(notice));
        }
        
        return noticeDtoList;
    }
    
    
    // getPageList -> getNoticeCount
    public Integer[] getPageList(Integer curPageNum, Long categoryId, String keyword, Integer searchType) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUMCOUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getNoticeCount(categoryId, keyword, searchType));

        // 총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POSTCOUNT)));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUMCOUNT)
                ? curPageNum + BLOCK_PAGE_NUMCOUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;

        // 페이지 번호 할당
        for(int val=curPageNum, i=0;val<=blockLastPageNum;val++, i++) {
            pageList[i] = val;
        }

        return pageList;
    }
    

    public Long getNoticeCount(Long categoryId, String keyword, Integer searchType) {
    	Byte deleted = 0;
    	
    	switch(searchType) {
		case 1:
			if (categoryId == 0) {
	    		return mNoticeRepository.countByTitleContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory);
	    	} else if (categoryId == -1) {
	    		return mNoticeRepository.countByTitleContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory);
	    	} else {
	    		return mNoticeRepository.countByTitleContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory);
	    	}
		case 2:
			if (categoryId == 0) {
	    		return mNoticeRepository.countByContentContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory);
	    	} else if (categoryId == -1) {
	    		return mNoticeRepository.countByContentContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory);
	    	} else {
	    		return mNoticeRepository.countByContentContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory);
	    	}
		case 3:
			if (categoryId == 0) {
	    		return mNoticeRepository.countByWriterContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory);
	    	} else if (categoryId == -1) {
	    		return mNoticeRepository.countByWriterContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory);
	    	} else {
	    		return mNoticeRepository.countByWriterContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory);
	    	}
		default:
			if (categoryId == 0) {
	    		return mNoticeRepository.countByStatusNotAndTopCategoryEquals(deleted, topCategory);
	    	} else if (categoryId == -1) {
	    		return mNoticeRepository.countByStatusAndTopCategoryEquals(deleted, topCategory);
	    	} else {
	    		return mNoticeRepository.countByStatusNotAndCategoryIdAndTopCategoryEquals(deleted, categoryId, topCategory);
	    	}
    	}
    }
    
    // Detail of id's notice
    public NoticeDto getPost(Long id) {
        Optional<Board> NoticeWrapper = mNoticeRepository.findById(id);
        Board notice = NoticeWrapper.get();
        
        mNoticeRepository.updateViewCount(notice.getView() + 1, id);

        NoticeDto noticeDto = NoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getWriter())
                .view(notice.getView()+ 1)
                .status(notice.getStatus())
                .topCategory(notice.getTopCategory())
                .categoryId(notice.getCategoryId())
                .regDate(notice.getRegDate())
                .modDate(notice.getModDate())
                .build();

        return noticeDto;
    }

	public List<NoticeAppDto> getNotices() {
		List<Board> notices = mNoticeRepository.findAllByTopCategoryOrderByRegDateDesc((byte) 1);
		List<NoticeAppDto> noticeAppDtoList = new ArrayList<>();

		for(Board notice: notices) {
			BoardCategory category = mCategoryRepository.findById(notice.getCategoryId()).orElse(null);

			noticeAppDtoList.add(convertEntityToAppDto(notice, category));
		}



		return noticeAppDtoList;
	}

	public List<NoticeAppDto> getTop5Notices() {
		List<Board> notices = mNoticeRepository.findTop5ByTopCategoryOrderByRegDateDesc((byte) 1);
		List<NoticeAppDto> noticeAppDtoList = new ArrayList<>();

		for(Board notice: notices) {
			BoardCategory category = mCategoryRepository.findById(notice.getCategoryId()).orElse(null);

			noticeAppDtoList.add(convertEntityToAppDto(notice, category));
		}

		return noticeAppDtoList;
	}

	public NoticeAppDto convertEntityToAppDto(Board notice, BoardCategory category) {
		return NoticeAppDto.builder()
				.id(notice.getId())
				.title(notice.getTitle())
				.content(notice.getContent())
				.writer(notice.getWriter())
				.view(notice.getView()+ 1)
				.status(notice.getStatus())
				.topCategory(notice.getTopCategory())
				.categoryId(notice.getCategoryId())
				.regDate(notice.getRegDate())
				.modDate(notice.getModDate())
				.memo(category.getMemo())
				.build();
	}

//    public Long getAllNoticeNum(Long category) {
//    	return mNoticeRepository.countByTopCategory(category);
//    }
}
