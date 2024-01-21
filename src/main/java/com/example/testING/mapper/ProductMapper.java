package com.example.testING.mapper;

import com.example.testING.dto.ProductDTO;
import com.example.testING.model.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO mapEntityToDTO(Product product);

    @InheritInverseConfiguration
    Product mapDTOToEntity(ProductDTO productDTO);
}