package com.example.testING.controller;

import com.example.testING.dto.ProductDTO;
import com.example.testING.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.addProduct(productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productService.findProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{productId}/change-price/{newPrice}")
    public ResponseEntity<ProductDTO> changeProductPrice(@PathVariable Long productId, @PathVariable double newPrice) {
        ProductDTO updatedProduct = productService.changeProductPrice(productId, newPrice);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/producer/{producer}")
    public ResponseEntity<List<ProductDTO>> getProductsByProducer(@PathVariable String producer) {
        List<ProductDTO> products = productService.getProductsByProducer(producer);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/price-range/{minPrice}/{maxPrice}")
    public ResponseEntity<List<ProductDTO>> getProductsByPriceRange(
            @PathVariable double minPrice, @PathVariable double maxPrice) {
        List<ProductDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}