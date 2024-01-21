package com.example.testING.services;

import com.example.testING.dto.CategoryDTO;
import com.example.testING.exception.CategoryNotFoundException;
import com.example.testING.mapper.CategoryMapper;
import com.example.testING.model.Category;
import com.example.testING.repository.CategoryRepository;
import com.example.testING.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testFindCategoryById() {
        Long categoryId = 1L;
        Category category = new Category(categoryId, "CategoryName", "CategoryDescription");
        CategoryDTO expectedCategoryDTO = new CategoryDTO(categoryId, "CategoryName", "CategoryDescription");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.mapEntityToDTO(category)).thenReturn(expectedCategoryDTO);

        CategoryDTO result = categoryService.findCategoryById(categoryId);

        assertEquals(expectedCategoryDTO, result);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).mapEntityToDTO(category);
    }

    @Test
    public void testFindCategoryByIdNotFound() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryById(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, never()).mapEntityToDTO(any());
    }

    @Test
    public void testGetAllCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "Category1", "Description1"),
                new Category(2L, "Category2", "Description2")
        );
        List<CategoryDTO> expectedCategoryDTOs = Arrays.asList(
                new CategoryDTO(1L, "Category1", "Description1"),
                new CategoryDTO(2L, "Category2", "Description2")
        );

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.mapEntityToDTO(any())).thenReturn(expectedCategoryDTOs.get(0), expectedCategoryDTOs.get(1));

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(expectedCategoryDTOs, result);
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(2)).mapEntityToDTO(any());
    }

    @Test
    public void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(null, "CategoryName", "CategoryDescription");
        Category category = new Category(null, "CategoryName", "CategoryDescription");
        Category savedCategory = new Category(1L, "CategoryName", "CategoryDescription");

        when(categoryMapper.mapDTOToEntity(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.mapEntityToDTO(savedCategory)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.addCategory(categoryDTO);

        assertEquals(categoryDTO, result);
        verify(categoryMapper, times(1)).mapDTOToEntity(categoryDTO);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).mapEntityToDTO(savedCategory);
    }

    @Test
    public void testUpdateCategory() {
        Long categoryId = 1L;
        CategoryDTO updatedCategoryDTO = new CategoryDTO(categoryId, "UpdatedCategory", "UpdatedDescription");
        Category existingCategory = new Category(categoryId, "CategoryName", "CategoryDescription");
        Category updatedCategory = new Category(categoryId, "UpdatedCategory", "UpdatedDescription");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.mapEntityToDTO(updatedCategory)).thenReturn(updatedCategoryDTO);

        CategoryDTO result = categoryService.updateCategory(categoryId, updatedCategoryDTO);

        assertEquals(updatedCategoryDTO, result);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(existingCategory);
        verify(categoryMapper, times(1)).mapEntityToDTO(updatedCategory);
    }

    @Test
    public void testUpdateCategoryNotFound() {
        Long categoryId = 1L;
        CategoryDTO updatedCategoryDTO = new CategoryDTO(categoryId, "UpdatedCategory", "UpdatedDescription");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(categoryId, updatedCategoryDTO));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any());
        verify(categoryMapper, never()).mapEntityToDTO(any());
    }

    @Test
    public void testDeleteCategory() {
        Long categoryId = 1L;
        Category existingCategory = new Category(categoryId, "CategoryName", "CategoryDescription");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).delete(existingCategory);
    }

    @Test
    public void testDeleteCategoryNotFound() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).delete(any());
    }
}
