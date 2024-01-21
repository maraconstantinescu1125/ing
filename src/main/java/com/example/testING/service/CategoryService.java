package com.example.testING.service;

import com.example.testING.dto.CategoryDTO;
import com.example.testING.exception.CategoryNotFoundException;
import com.example.testING.mapper.CategoryMapper;
import com.example.testING.model.Category;
import com.example.testING.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDTO findCategoryById(Long categoryId) {
        logger.info("Finding category by id {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + categoryId));

        return categoryMapper.mapEntityToDTO(category);
    }

    public List<CategoryDTO> getAllCategories() {
        logger.info("Getting all categories");
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        logger.info("Adding category {}", categoryDTO);
        Category category = categoryMapper.mapDTOToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);

        logger.info("Category added successfully with id {}", savedCategory.getId());
        return categoryMapper.mapEntityToDTO(savedCategory);
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO updatedCategoryDTO) {
        logger.info("Updating category with id {}", categoryId);
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + categoryId));

        existingCategory.setName(updatedCategoryDTO.name());
        existingCategory.setDescription(updatedCategoryDTO.description());
        Category updatedCategory = categoryRepository.save(existingCategory);

        logger.info("Category updated successfully with id {}", updatedCategory.getId());
        return categoryMapper.mapEntityToDTO(updatedCategory);
    }

    public void deleteCategory(Long categoryId) {
        logger.info("Deleting category with id: {}", categoryId);
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + categoryId));

        categoryRepository.delete(existingCategory);

        logger.info("Category deleted successfully");
    }
}

