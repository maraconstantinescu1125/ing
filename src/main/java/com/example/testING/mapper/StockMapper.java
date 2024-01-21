package com.example.testING.mapper;

import com.example.testING.dto.StockDTO;
import com.example.testING.model.Stock;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(target = "productId", source = "product.id")
    StockDTO mapEntityToDTO(Stock stock);

    @InheritInverseConfiguration
    Stock mapDTOToEntity(StockDTO stockDTO);
}