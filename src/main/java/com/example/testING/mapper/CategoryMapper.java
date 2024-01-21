package com.example.testING.mapper;


import com.example.testING.dto.CategoryDTO;
import com.example.testING.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO mapEntityToDTO(Category category);

    Category mapDTOToEntity(CategoryDTO categoryDTO);
}