package com.backendbloggingapp.blog_app.service.Impl;

import com.backendbloggingapp.blog_app.exceptions.ResourceNotFoundException;
import com.backendbloggingapp.blog_app.models.Category;
import com.backendbloggingapp.blog_app.payloads.CategoryDto;
import com.backendbloggingapp.blog_app.repository.CategoryRepository;
import com.backendbloggingapp.blog_app.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category cat = this.modelMapper.map(categoryDto, Category.class);
        Category addedCat = this.categoryRepository.save(cat);
        return this.modelMapper.map(addedCat, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

        Category cat = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category ", "Category Id", categoryId));

        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedcat = this.categoryRepository.save(cat);

        return this.modelMapper.map(updatedcat, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {

        Category cat = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category ", "category id", categoryId));
        this.categoryRepository.delete(cat);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {
        Category cat = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

        return this.modelMapper.map(cat, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryDto> catDtos = categories.stream().map((cat) -> this.modelMapper.map(cat, CategoryDto.class))
                .collect(Collectors.toList());

        return catDtos;
    }
}
