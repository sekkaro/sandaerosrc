package com.sangdaero.walab.common.category.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sangdaero.walab.common.category.dto.CategoryDto;
import com.sangdaero.walab.common.category.service.CategoryService;

public class CategoryController {
		private CategoryService mCategoryService;
		
		public CategoryController(CategoryService categoryService) {
			this.mCategoryService = categoryService;
		}
	
		// Category list page
	 	@GetMapping("/category")
	    public String category(
	    		Model model,
				@RequestParam(value = "topCategory", defaultValue = "0") Byte topCategory,
				@RequestParam(value = "keyword", defaultValue = "") String keyword,
				@RequestParam(value = "type", defaultValue = "0") Integer searchType) {
	 		
	 		String path;
	 		
	 		switch(topCategory) {
	 		case 1:
	 			path = "/notice";
	 			break;
	 		case 2:
	 			path = "/community";
	 			break;
	 		case 3:
	 			path = "/qna";
	 			break;
	 		default:
	 			path = "";
	 			break;
	 		}
	 		
	 		System.out.println(path);
	        List<CategoryDto> categoryDtoList = mCategoryService.getCategory(topCategory);
	        
	        model.addAttribute("topCategory", topCategory);
	        model.addAttribute("path", path);
	        model.addAttribute("categoryList", categoryDtoList);
	 		
	        return "html/community/categoryList.html";
	    }
	 	
		// Writing community page
		@GetMapping("/category/add")
	    public String addCategory() {
	        return "html/community/categoryAdd.html";
	    }

		// Execute when click save button
	    @PostMapping("/category/add")
	    public String addCategory(CategoryDto categoryDto) {
	        mCategoryService.saveCategory(categoryDto);
	        return "redirect:/community/category";
	    }
	    
	    // Detail page of community
	    @GetMapping("/category/detail/{no}")
	    public String detailCategory(@PathVariable("no") Long id, Model model) {
	        CategoryDto categoryDto = mCategoryService.getCategoryDetail(id);

	        model.addAttribute("categoryDto", categoryDto);

	        return "html/community/categoryDetail.html";
	    }

	    // Edit page which through detail
	    @GetMapping("/category/edit/{no}")
	    public String editCategory(@PathVariable("no") Long id, Model model) {
	    	CategoryDto categoryDto = mCategoryService.getCategoryDetail(id);

	        model.addAttribute("categoryDto", categoryDto);
	        
	        return "html/community/categoryUpdate.html";
	    }

	    // Saving edit content
	    @PutMapping("/category/edit/{no}")
	    public String updateCategory(CategoryDto categoryDto) {
	        mCategoryService.updateCategory(categoryDto);
	        return "redirect:/community/category";
	    }

	    // Deleting community
	    @DeleteMapping("/category/delete/{no}")
	    public String deleteCategory(@PathVariable("no") Long id) {
	        mCategoryService.deleteCategory(id);

	        return "redirect:/community/category";
	    }
}
