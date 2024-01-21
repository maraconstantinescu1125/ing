package com.example.testING.service;

import com.example.testING.dto.ProductDTO;
import com.example.testING.exception.InvalidPriceException;
import com.example.testING.exception.InvalidProductException;
import com.example.testING.exception.ProductNotFoundException;
import com.example.testING.mapper.ProductMapper;
import com.example.testING.model.Category;
import com.example.testING.model.Product;
import com.example.testING.repository.CategoryRepository;
import com.example.testING.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        logger.info("Adding product {}", productDTO);

        Product product = productMapper.mapDTOToEntity(productDTO);
        productValidation(product);
        Optional<Category> category = categoryRepository.findById(productDTO.categoryId());
        if (category.isEmpty()) {
            logger.error("Category with id {} not found.", productDTO.categoryId());
            throw new InvalidProductException("Invalid category id");
        } else {
            product.setCategory(category.get());
            Product savedProduct = productRepository.save(product);
            logger.info("Product added successfully with id {}", savedProduct.getId());
            return productMapper.mapEntityToDTO(savedProduct);
        }
    }

    public ProductDTO findProductById(Long productId) {
        logger.info("Finding product by id {}", productId);
        return productRepository.findById(productId)
                .map(productMapper::mapEntityToDTO)
                .orElseThrow(() -> {
                    logger.warn("Product not found with id {}", productId);
                    return new ProductNotFoundException("Product not found with id " + productId);
                });
    }

    public List<ProductDTO> getAllProducts() {
        logger.info("Getting all products");
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long productId) {
        logger.info("Deleting product with id {}", productId);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.warn("Product not found with id {}", productId);
                    return new ProductNotFoundException("Product not found with id " + productId);
                });
        productRepository.delete(existingProduct);
        logger.info("Product deleted successfully");
    }

    public ProductDTO changeProductPrice(Long productId, double newPrice) {
        logger.info("Changing price for product with id {} to {}", productId, newPrice);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.warn("Product not found with id {}", productId);
                    return new ProductNotFoundException("Product not found with id " + productId);
                });

            product.setPrice(newPrice);
            productValidation(product);
            Product updatedProduct = productRepository.save(product);
            logger.info("Price changed successfully for product with id {} to {}", productId, newPrice);
            return productMapper.mapEntityToDTO(updatedProduct);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        logger.info("Getting products by category with id {}", categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(productMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByProducer(String producer) {
        logger.info("Getting products by producer {}", producer);
        List<Product> products = productRepository.findByProducer(producer);
        return products.stream()
                .map(productMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByPriceRange(double minPrice, double maxPrice) {
        logger.info("Getting products by price range {} - {}", minPrice, maxPrice);
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream()
                .map(productMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    private void productValidation(Product product){
        if(product.getPrice()<0){
                logger.warn("Invalid price provided");
                throw new InvalidPriceException("Price cannot be less than 0");
        }

    }
}

