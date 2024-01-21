package com.example.testING.service;

import com.example.testING.dto.StockDTO;
import com.example.testING.exception.InvalidQuantityException;
import com.example.testING.exception.ProductNotFoundException;
import com.example.testING.exception.StockNotFoundException;
import com.example.testING.mapper.StockMapper;
import com.example.testING.model.Product;
import com.example.testING.model.Stock;
import com.example.testING.repository.ProductRepository;
import com.example.testING.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class StockService {

    private final Logger logger = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final ProductRepository productRepository;

    @Autowired
    public StockService(StockRepository stockRepository, StockMapper stockMapper, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.productRepository = productRepository;
    }

    public StockDTO getStockForProduct(Long productId) {
        logger.info("Getting stock for product with id {}", productId);
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    logger.error("Stock not found for product with id {}", productId);
                    return new StockNotFoundException("Stock not found for product with id " + productId);
                });

        return stockMapper.mapEntityToDTO(stock);
    }

    public void updateQuantityForProduct(Long productId, int newQuantity) {
        logger.info("Updating quantity for product with id {} to {}", productId, newQuantity);
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    logger.error("Stock not found for product with id {}", productId);
                    return new StockNotFoundException("Stock not found for product with id " + productId);
                });

        if (newQuantity < 0) {
            logger.warn("Attempted to set negative quantity for product with id {}", productId);
            throw new InvalidQuantityException("Quantity cannot be negative");
        }

        stock.setQuantity(newQuantity);
        stock.setLastTimeUpdated(LocalDate.now());

        stockRepository.save(stock);
        logger.info("Quantity updated successfully for product with id {} to {}", productId, newQuantity);
    }

    public void addStockForProduct(StockDTO stockDTO) {
        logger.info("Adding stock for product with id {}", stockDTO.productId());
        Product product = productRepository.findById(stockDTO.productId())
                .orElseThrow(() -> {
                    logger.warn("Product not found with id {}", stockDTO.productId());
                    return new ProductNotFoundException("Product not found with id " + stockDTO.productId());
                });

        Stock newStock = stockMapper.mapDTOToEntity(stockDTO);
        newStock.setProduct(product);
        newStock.setLastTimeUpdated(LocalDate.now());

        stockRepository.save(newStock);
        logger.info("Stock added successfully for product with id {}", stockDTO.productId());
    }
}