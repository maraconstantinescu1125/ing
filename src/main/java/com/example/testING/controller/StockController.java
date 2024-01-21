package com.example.testING.controller;

import com.example.testING.dto.StockDTO;
import com.example.testING.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StockDTO> getStockForProduct(@PathVariable Long productId) {
        StockDTO stockDTO = stockService.getStockForProduct(productId);
        return new ResponseEntity<>(stockDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addStockForProduct(@RequestBody StockDTO stockDTO) {
        stockService.addStockForProduct(stockDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/update-quantity/{productId}/{newQuantity}")
    public ResponseEntity<Void> updateQuantityForProduct(
            @PathVariable Long productId, @PathVariable int newQuantity) {
        stockService.updateQuantityForProduct(productId, newQuantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}