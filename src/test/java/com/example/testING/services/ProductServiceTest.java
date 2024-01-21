package com.example.testING.services;

import com.example.testING.dto.ProductDTO;
import com.example.testING.exception.InvalidProductException;
import com.example.testING.exception.ProductNotFoundException;
import com.example.testING.mapper.ProductMapper;
import com.example.testING.model.Category;
import com.example.testING.model.Product;
import com.example.testING.repository.CategoryRepository;
import com.example.testING.repository.ProductRepository;
import com.example.testING.service.ProductService;
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
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testAddProduct() {
        ProductDTO productDTO = new ProductDTO(null, "ProductName", "Description", "Producer", 10.0, 1L);
        Product product = new Product(null, "ProductName", "Description", "Producer", 10.0, null);
        Category category = new Category(1L, "CategoryName", "CategoryDescription");
        Product savedProduct = new Product(1L, "ProductName", "Description", "Producer", 10.0, category);
        ProductDTO savedProductDTO = new ProductDTO(1L, "ProductName", "Description", "Producer", 10.0, 1L);

        when(productMapper.mapDTOToEntity(productDTO)).thenReturn(product);
        when(categoryRepository.findById(productDTO.categoryId())).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.mapEntityToDTO(savedProduct)).thenReturn(savedProductDTO);

        ProductDTO result = productService.addProduct(productDTO);

        assertEquals(savedProductDTO, result);
        verify(productMapper, times(1)).mapDTOToEntity(productDTO);
        verify(categoryRepository, times(1)).findById(productDTO.categoryId());
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).mapEntityToDTO(savedProduct);
    }

    @Test
    public void testAddProductInvalidCategory() {
        ProductDTO productDTO = new ProductDTO(null, "ProductName", "Description", "Producer", 10.0, 1L);

        when(productMapper.mapDTOToEntity(productDTO)).thenReturn(new Product());
        when(categoryRepository.findById(productDTO.categoryId())).thenReturn(Optional.empty());

        assertThrows(InvalidProductException.class, () -> productService.addProduct(productDTO));

        verify(productMapper, times(1)).mapDTOToEntity(productDTO);
        verify(categoryRepository, times(1)).findById(productDTO.categoryId());
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).mapEntityToDTO(any());
    }

    @Test
    public void testFindProductById() {
        Long productId = 1L;
        Product product = new Product(productId, "ProductName", "Description", "Producer", 10.0, null);
        ProductDTO expectedProductDTO = new ProductDTO(productId, "ProductName", "Description", "Producer", 10.0, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.mapEntityToDTO(product)).thenReturn(expectedProductDTO);

        ProductDTO result = productService.findProductById(productId);

        assertEquals(expectedProductDTO, result);
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).mapEntityToDTO(product);
    }

    @Test
    public void testFindProductByIdNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));

        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, never()).mapEntityToDTO(any());
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "ProductName1", "Description1", "Producer1", 10.0, null),
                new Product(2L, "ProductName2", "Description2", "Producer2", 20.0, null)
        );
        List<ProductDTO> expectedProductDTOs = Arrays.asList(
                new ProductDTO(1L, "ProductName1", "Description1", "Producer1", 10.0, null),
                new ProductDTO(2L, "ProductName2", "Description2", "Producer2", 20.0, null)
        );

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.mapEntityToDTO(any())).thenReturn(expectedProductDTOs.get(0), expectedProductDTOs.get(1));

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(expectedProductDTOs, result);
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(2)).mapEntityToDTO(any());
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;
        Product existingProduct = new Product(productId, "ProductName", "Description", "Producer", 10.0, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(existingProduct);
    }

    @Test
    public void testDeleteProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(productId));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).delete(any());
    }

    @Test
    public void testChangeProductPrice() {
        Long productId = 1L;
        double newPrice = 20.0;
        Product product = new Product(productId, "ProductName", "Description", "Producer", 10.0, null);
        Product updatedProduct = new Product(productId, "ProductName", "Description", "Producer", newPrice, null);
        ProductDTO updatedProductDTO = new ProductDTO(productId, "ProductName", "Description", "Producer", newPrice, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(updatedProduct);
        when(productMapper.mapEntityToDTO(updatedProduct)).thenReturn(updatedProductDTO);

        ProductDTO result = productService.changeProductPrice(productId, newPrice);

        assertEquals(updatedProductDTO, result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).mapEntityToDTO(updatedProduct);
    }

    @Test
    public void testChangeProductPriceNotFound() {
        Long productId = 1L;
        double newPrice = 20.0;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.changeProductPrice(productId, newPrice));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).mapEntityToDTO(any());
    }

    @Test
    public void testGetProductsByCategory() {
        Long categoryId = 1L;
        List<Product> products = Arrays.asList(
                new Product(1L, "ProductName1", "Description1", "Producer1", 10.0, null),
                new Product(2L, "ProductName2", "Description2", "Producer2", 20.0, null)
        );
        List<ProductDTO> expectedProductDTOs = Arrays.asList(
                new ProductDTO(1L, "ProductName1", "Description1", "Producer1", 10.0, null),
                new ProductDTO(2L, "ProductName2", "Description2", "Producer2", 20.0, null)
        );

        when(productRepository.findByCategoryId(categoryId)).thenReturn(products);
        when(productMapper.mapEntityToDTO(any())).thenReturn(expectedProductDTOs.get(0), expectedProductDTOs.get(1));

        List<ProductDTO> result = productService.getProductsByCategory(categoryId);

        assertEquals(expectedProductDTOs, result);
        verify(productRepository, times(1)).findByCategoryId(categoryId);
        verify(productMapper, times(2)).mapEntityToDTO(any());
    }

    @Test
    public void testGetProductsByProducer() {
        String producer = "Producer1";
        List<Product> products = Arrays.asList(
                new Product(1L, "ProductName1", "Description1", producer, 10.0, null),
                new Product(2L, "ProductName2", "Description2", producer, 20.0, null)
        );
        List<ProductDTO> expectedProductDTOs = Arrays.asList(
                new ProductDTO(1L, "ProductName1", "Description1", producer, 10.0, null),
                new ProductDTO(2L, "ProductName2", "Description2", producer, 20.0, null)
        );

        when(productRepository.findByProducer(producer)).thenReturn(products);
        when(productMapper.mapEntityToDTO(any())).thenReturn(expectedProductDTOs.get(0), expectedProductDTOs.get(1));

        List<ProductDTO> result = productService.getProductsByProducer(producer);

        assertEquals(expectedProductDTOs, result);
        verify(productRepository, times(1)).findByProducer(producer);
        verify(productMapper, times(2)).mapEntityToDTO(any());
    }

    @Test
    public void testGetProductsByPriceRange() {
        double minPrice = 10.0;
        double maxPrice = 20.0;
        List<Product> products = Arrays.asList(
                new Product(1L, "ProductName1", "Description1", "Producer1", 15.0, null),
                new Product(2L, "ProductName2", "Description2", "Producer2", 18.0, null)
        );
        List<ProductDTO> expectedProductDTOs = Arrays.asList(
                new ProductDTO(1L, "ProductName1", "Description1", "Producer1", 15.0, null),
                new ProductDTO(2L, "ProductName2", "Description2", "Producer2", 18.0, null)
        );

        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(products);
        when(productMapper.mapEntityToDTO(any())).thenReturn(expectedProductDTOs.get(0), expectedProductDTOs.get(1));

        List<ProductDTO> result = productService.getProductsByPriceRange(minPrice, maxPrice);

        assertEquals(expectedProductDTOs, result);
        verify(productRepository, times(1)).findByPriceBetween(minPrice, maxPrice);
        verify(productMapper, times(2)).mapEntityToDTO(any());
    }

}