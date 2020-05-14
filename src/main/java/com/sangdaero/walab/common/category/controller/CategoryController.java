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
	 		
	 		String path = currentPath(topCategory);
	 		
	 		System.out.println(path);
	        List<CategoryDto> categoryDtoList = mCategoryService.getCategory(topCategory);
	        
	        model.addAttribute("topCategory", topCategory);
	        model.addAttribute("path", path);
	        model.addAttribute("categoryList", categoryDtoList);
	 		
	        return "html/category/categoryList.html";
	    }
	 	
		// Writing community page
		@GetMapping("/category/add")
	    public String addCategory(Model model,
	    		@RequestParam(value = "path") String path) {
			
			model.addAttribute("path", path);
			if (path.equals("community")) {
				model.addAttribute("category", 2);
			} else if (path.equals("qna")) {
				model.addAttribute("category", 3);
			} else {
				model.addAttribute("category", 1);
			}
			
	        return "html/category/categoryAdd.html";
	    }

		// Execute when click save button
	    @PostMapping("/category/add")
	    public String addCategory(CategoryDto categoryDto) {
	    	String path = currentPath(categoryDto.getTopCategory());
	    	
	        mCategoryService.saveCategory(categoryDto);
	        
	        return "redirect:/"+path+"/category";
	    }
	    
	    // Detail page of community
	    @GetMapping("/category/detail/{no}")
	    public String detailCategory(
	    		@PathVariable("no") Long id, Model model,
	    		@RequestParam(value = "topCategory") Byte topCategory) {
	    	
	        CategoryDto categoryDto = mCategoryService.getCategoryDetail(id);
	        
	        String path = currentPath(topCategory);

	        model.addAttribute("categoryDto", categoryDto);
	        model.addAttribute("path", path);

	        return "html/category/categoryDetail.html";
	    }

	    // Edit page which through detail
	    @GetMapping("/category/edit/{no}")
	    public String editCategory(@PathVariable("no") Long id, Model model) {
	    	CategoryDto categoryDto = mCategoryService.getCategoryDetail(id);
	    	String path = currentPath(categoryDto.getTopCategory());

	        model.addAttribute("categoryDto", categoryDto);
	        model.addAttribute("path", path);
	        
	        return "html/category/categoryUpdate.html";
	    }

	    // Saving edit content
	    @PutMapping("/category/edit/{no}")
	    public String updateCategory(CategoryDto categoryDto) {
	        mCategoryService.updateCategory(categoryDto);
	        
	        String path = currentPath(categoryDto.getTopCategory());
	        
	        return "redirect:/"+path+"/category";
	    }

	    // Deleting community
	    @DeleteMapping("/category/delete/{no}")
	    public String deleteCategory(@PathVariable("no") Long id) {
	        mCategoryService.deleteCategory(id);

	        return "redirect:/notice/category";
	    }
	    
	    private String currentPath(Byte topCategory) {
	    	switch(topCategory) {
	 		case 1:
	 			return "notice";
	 		case 2:
	 			return "community";
	 		case 3:
	 			return "qna";
	 		default:
	 			return "notice";
	 		}
	    }
}
