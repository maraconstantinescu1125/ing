package com.example.testING.services;

import com.example.testING.dto.StockDTO;
import com.example.testING.mapper.StockMapper;
import com.example.testING.model.Product;
import com.example.testING.model.Stock;
import com.example.testING.repository.ProductRepository;
import com.example.testING.repository.StockRepository;
import com.example.testING.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockMapper stockMapper;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    void testGetStockForProduct() {
        Long productId = 1L;
        Product mockProduct = new Product(1L, "ProductName1", "Description1", "Producer1", 10.0, null);
        Stock mockStock = new Stock(1L, mockProduct, 10, LocalDate.now());
        StockDTO expectedStockDTO = new StockDTO(1L,productId, 10, LocalDate.now());
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(mockStock));
        when(stockMapper.mapEntityToDTO(mockStock)).thenReturn(expectedStockDTO);

        StockDTO result = stockService.getStockForProduct(productId);

        assertEquals(expectedStockDTO, result);
        verify(stockRepository, times(1)).findByProductId(productId);
        verify(stockMapper, times(1)).mapEntityToDTO(mockStock);
    }

    @Test
    void testUpdateQuantityForProduct() {
        Long productId = 1L;
        int newQuantity = 20;
        Product mockProduct = new Product(1L, "ProductName1", "Description1", "Producer1", 10.0, null);
        Stock mockStock = new Stock(1L, mockProduct, 15, LocalDate.now());
        when(stockRepository.findByProductId(productId)).thenReturn(Optional.of(mockStock));

        stockService.updateQuantityForProduct(productId, newQuantity);

        verify(stockRepository, times(1)).save(mockStock);
        assertEquals(newQuantity, mockStock.getQuantity());
        assertEquals(LocalDate.now(), mockStock.getLastTimeUpdated());
    }

    @Test
    void testAddStockForProduct() {
        Long productId = 1L;
        StockDTO stockDTO = new StockDTO(1L, productId, 15, LocalDate.now());
        Product mockProduct = new Product(1L, "ProductName1", "Description1", "Producer1", 10.0, null);
        Stock newStock = new Stock(1L, mockProduct, 15, LocalDate.now());

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(stockMapper.mapDTOToEntity(stockDTO)).thenReturn(newStock);

        stockService.addStockForProduct(stockDTO);

        verify(stockRepository, times(1)).save(newStock);
        assertEquals(mockProduct, newStock.getProduct());
        assertEquals(LocalDate.now(), newStock.getLastTimeUpdated());
    }
}