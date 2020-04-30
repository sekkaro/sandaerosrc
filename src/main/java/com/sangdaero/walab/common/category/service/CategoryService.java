package com.sangdaero.walab.common.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sangdaero.walab.common.category.dto.CategoryDto;
import com.sangdaero.walab.common.category.repository.CategoryRepository;
import com.sangdaero.walab.common.entity.BoardCategory;

public class CategoryService {
	protected CategoryRepository mCategoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.mCategoryRepository = categoryRepository;
	}
	
    // Convert Category Entity to DTO
    private CategoryDto convertEntityToDto(BoardCategory category) {
        return CategoryDto.builder()
                .id(category.getId())
                .topCategory(category.getTopCategory())
                .status(category.getStatus())
                .memo(category.getMemo())
                .communityManager(category.getCommunityManager())
                .regDate(category.getRegDate())
                .modDate(category.getModDate())
                .build();
    }
	
	// Save category
    public Long saveCategory(CategoryDto categoryDto) {
        return mCategoryRepository.save(categoryDto.toEntity()).getId();
    }
    
    // Update category
    public Long updateCategory(CategoryDto categoryDto) {
        return mCategoryRepository.save(categoryDto.toEntity()).getId();
    }
    
    // Delete category
    public void deleteCategory(Long id) {
    	Byte delete = 0;
		mCategoryRepository.updateCommunityCategoryId(delete, id);
    }
    
    public List<CategoryDto> getCategory(Byte topCategory) {
    	List<BoardCategory> boardCategories;
    	
    	if (topCategory != 0) {
    		boardCategories = mCategoryRepository.findAllByTopCategory(topCategory);
    	} else {
    		boardCategories = mCategoryRepository.findAll();
    	}
    	
    	List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();
    	
    	for(BoardCategory boardCategory : boardCategories) {
    		categoryDtoList.add(this.convertEntityToDto(boardCategory));
    	}
    	
    	return categoryDtoList;
    	
    }
    
    // Detail of id's category
    public CategoryDto getCategoryDetail(Long id) {
        Optional<BoardCategory> CategoryWrapper = mCategoryRepository.findById(id);

        BoardCategory category = CategoryWrapper.get();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .topCategory(category.getTopCategory())
                .status(category.getStatus())
                .memo(category.getMemo())
                .communityManager(category.getCommunityManager())
                .regDate(category.getRegDate())
                .modDate(category.getModDate())
                .build();

        return categoryDto;
    }
}
