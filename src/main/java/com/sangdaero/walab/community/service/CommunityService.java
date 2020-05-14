package com.sangdaero.walab.community.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.category.dto.CategoryDto;
import com.sangdaero.walab.common.category.repository.CategoryRepository;
import com.sangdaero.walab.common.category.service.CategoryService;
import com.sangdaero.walab.common.entity.Board;
import com.sangdaero.walab.common.entity.BoardCategory;
import com.sangdaero.walab.community.domain.repository.CommunityRepository;
import com.sangdaero.walab.community.dto.CommunityDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityService extends CategoryService {

    private CommunityRepository mCommunityRepository;
    private static final int BLOCK_PAGE_NUMCOUNT = 6; // 블럭에 존재하는 페이지 수
    private static final int PAGE_POSTCOUNT = 3;  // 한 페이지에 존재하는 게시글 수
    private static final Byte topCategory = 2;

    public CommunityService(CommunityRepository communityRepository, CategoryRepository categoryRepository) {
    	super(categoryRepository);
    	this.mCommunityRepository = communityRepository;
    }
    
    // Convert Community Entity to DTO
    private CommunityDto convertEntityToDto(Board community) {
        return CommunityDto.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .writer(community.getWriter())
                .view(community.getView())
                .status(community.getStatus())
                .topCategory(community.getTopCategory())
                .regDate(community.getRegDate())
                .modDate(community.getModDate())
                .build();
    }

    // Save post
    public Long savePost(CommunityDto communityDto, Long categoryId) {
    	return mCommunityRepository.save(communityDto.toEntity()).getId();
    }
    
    // Update post
    public Long updatePost(CommunityDto communityDto) {
        return mCommunityRepository.save(communityDto.toEntity()).getId();
    }
    
    // Delete post
    public void deletePost(Long id) {
    	Byte status = 0;
    	mCommunityRepository.updateCommunityCategoryId(status, id);
    }

    // getCommunitylist -> convertEntitytoDto
    public List<CommunityDto> getCommunitylist(Integer pageNum, Long categoryId, String keyword, Integer searchType) {
    	Page<Board> page;

    	Byte deleted = 0;
    	
   		switch(searchType) {
   			// Search by Title
    		case 1:
    			if (categoryId == 0) {
            		page = mCommunityRepository.findAllByTitleContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			} else if (categoryId == -1) {
            		page = mCommunityRepository.findAllByTitleContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			} else {
    				page = mCommunityRepository.findAllByTitleContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory,
            				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			}
    			break;
    		// Search by Content
    		case 2:
    			if (categoryId == 0) {
            		page = mCommunityRepository.findAllByContentContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			} else if (categoryId == -1) {
            		page = mCommunityRepository.findAllByContentContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			} else {
    				page = mCommunityRepository.findAllByContentContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory,
            				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			}
    			break;
    		// Search by Writer
    		case 3:
    			if (categoryId == 0) {
            		page = mCommunityRepository.findAllByWriterContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			} else if (categoryId == -1) {
            		page = mCommunityRepository.findAllByWriterContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			} else {
    				page = mCommunityRepository.findAllByWriterContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory,
            				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
    			}
    			break;
    		// Communities without search
    		default:
    			if (categoryId == 0) {
            		page = mCommunityRepository.findAllByStatusNotAndTopCategoryEquals(deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
            	} else if (categoryId == -1) {
            		page = mCommunityRepository.findAllByStatusAndTopCategoryEquals(deleted, topCategory,
            				PageRequest.of(pageNum-1,PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
            	} else {
            		System.out.println("\n\n"+categoryId+"\n\n");
            		page = mCommunityRepository.findAllByStatusNotAndCategoryIdAndTopCategoryEquals(deleted, categoryId, topCategory,
            				PageRequest.of(pageNum-1, PAGE_POSTCOUNT, Sort.by(Sort.Direction.DESC, "regDate")));
            	}
    			break;
    	}
    	
        List<Board> communities = page.getContent();
        List<CommunityDto> communityDtoList = new ArrayList<>();

        for(Board community : communities) {
        	communityDtoList.add(this.convertEntityToDto(community));
        }
        
        return communityDtoList;
    }
    
    
    // getPageList -> getCommunityCount
    public Integer[] getPageList(Integer curPageNum, Long categoryId, String keyword, Integer searchType) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUMCOUNT];

        // 총 게시글 수
        Double postsTotalCount = Double.valueOf(this.getCommunityCount(categoryId, keyword, searchType));

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
    

    public Long getCommunityCount(Long categoryId, String keyword, Integer searchType) {
    	Byte deleted = 0;
    	
    	switch(searchType) {
    		case 1:
    			if (categoryId == 0) {
    	    		return mCommunityRepository.countByTitleContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory);
    	    	} else if (categoryId == -1) {
    	    		return mCommunityRepository.countByTitleContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory);
    	    	} else {
    	    		return mCommunityRepository.countByTitleContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory);
    	    	}
    		case 2:
    			if (categoryId == 0) {
    	    		return mCommunityRepository.countByContentContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory);
    	    	} else if (categoryId == -1) {
    	    		return mCommunityRepository.countByContentContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory);
    	    	} else {
    	    		return mCommunityRepository.countByContentContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory);
    	    	}
    		case 3:
    			if (categoryId == 0) {
    	    		return mCommunityRepository.countByWriterContainingAndStatusNotAndTopCategoryEquals(keyword, deleted, topCategory);
    	    	} else if (categoryId == -1) {
    	    		return mCommunityRepository.countByWriterContainingAndStatusAndTopCategoryEquals(keyword, deleted, topCategory);
    	    	} else {
    	    		return mCommunityRepository.countByWriterContainingAndStatusNotAndCategoryIdAndTopCategoryEquals(keyword, deleted, categoryId, topCategory);
    	    	}
    		default:
    			if (categoryId == 0) {
    	    		return mCommunityRepository.countByStatusNotAndTopCategoryEquals(deleted, topCategory);
    	    	} else if (categoryId == -1) {
    	    		return mCommunityRepository.countByStatusAndTopCategoryEquals(deleted, topCategory);
    	    	} else {
    	    		return mCommunityRepository.countByStatusNotAndCategoryIdAndTopCategoryEquals(deleted, categoryId, topCategory);
    	    	}
    	}
    }
    
    // Detail of id's community
    public CommunityDto getPost(Long id) {
        Optional<Board> CommunityWrapper = mCommunityRepository.findById(id);
        Board community = CommunityWrapper.get();
        
        mCommunityRepository.updateViewCount(community.getView() + 1, id);

        CommunityDto communityDto = CommunityDto.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .writer(community.getWriter())
                .view(community.getView()+ 1)
                .status(community.getStatus())
                .topCategory(community.getTopCategory())
                .categoryId(community.getCategoryId())
                .regDate(community.getRegDate())
                .modDate(community.getModDate())
                .build();

        return communityDto;
    }
}
