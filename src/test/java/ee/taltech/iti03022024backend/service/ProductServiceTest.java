package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ProductRepository;
import ee.taltech.iti03022024backend.repository.specification.ProductSpecification;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.pagination.ProductSearchCriteria;
import ee.taltech.iti03022024backend.web.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    private Product product;
    private Product savedProduct;
    private List<ProductDto> productDtos;
    private ProductDto productDto;
    private ProductDto savedProductDto;
    private List<Product> products;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.product = new Product(null, "Title", "Description", null, false, null, null, null);
        this.savedProduct = new Product(1L, "Title", "Description", null, false, null, null, null);
        this.productDto = new ProductDto(null, "Title", "Description", null, false, null, null);
        this.savedProductDto = new ProductDto(1L, "Title", "Description", null, false, null, null);
        this.productDtos = List.of(savedProductDto);
        this.products = List.of(savedProduct);
    }

    @Test
    void testGetProductsByUserIdSuccess() {
        // Mock
        when(productRepository.findByUserId(1L)).thenReturn(products);
        when(productMapper.toDto(products)).thenReturn(productDtos);


        List<ProductDto> selectedProducts = productService.getProductsByUserId(1L);
        assertEquals(productDtos, selectedProducts);

    }

    @Test
    void testCreateProductSuccess() {
        //Mock
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productMapper.toDto(savedProduct)).thenReturn(savedProductDto);
        when(productRepository.save(product)).thenReturn(savedProduct);

        ProductDto result = productService.createProduct(productDto, 1L);
        assertEquals(savedProductDto, result);
    }

    @Test
    void testGetAllProducts_success() {
        // Arrange
        ProductSearchCriteria criteria = new ProductSearchCriteria(0, 10, 5.0, 20.0, "ASC");



        Page<Product> productPage = new PageImpl<>(products);


        when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), any(PageRequest.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(savedProduct)).thenReturn(savedProductDto);


        // Act
        Page<ProductDto> result = productService.getAllProducts(criteria);



        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Title", result.getContent().get(0).getTitle());
        verify(productMapper, times(1)).toDto(savedProduct);

    }

    @Test
    void testGetProductByIdSuccess() {
        //Mock
        Mockito.when(productMapper.toDto(savedProduct)).thenReturn(savedProductDto);
        Mockito.when(productRepository.findWithReviewsById(1L)).thenReturn(Optional.of(savedProduct));

        ProductDto result = productService.getProductById(1L);
        assertEquals(savedProductDto, result);

    }
    @Test
    void testGetProductByIdWithIdNotExist() {
        //Mock
        Mockito.when(productMapper.toDto(savedProduct)).thenReturn(savedProductDto);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> productService.getProductById(1L));

    }

    @Test
    void testUpdateProduct() {
        Mockito.when(productRepository.existsById(1L)).thenReturn(true);
        Mockito.when(productMapper.toEntity(savedProductDto)).thenReturn(savedProduct);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));
        Mockito.when(productRepository.save(savedProduct)).thenReturn(savedProduct);
        Mockito.when(productMapper.toDto(savedProduct)).thenReturn(savedProductDto);

        ProductDto result = productService.updateProduct(savedProductDto);
        assertEquals(savedProductDto, result);
    }

    @Test
    void testUpdateProductThatDoesNotExist() {
        Mockito.when(productRepository.existsById(1L)).thenReturn(false);
        Mockito.when(productMapper.toEntity(savedProductDto)).thenReturn(savedProduct);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(productRepository.save(savedProduct)).thenReturn(savedProduct);
        Mockito.when(productMapper.toDto(savedProduct)).thenReturn(savedProductDto);

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(savedProductDto));
    }


    @Test
    void testDeleteProductSuccess() {
        Mockito.when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteProductWithNotExistingId() {
        Mockito.when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, times(0)).deleteById(1L);
    }

    @Test
    void testExistsByIdSuccess() {
        Mockito.when(productRepository.existsById(1L)).thenReturn(true);
        assertTrue(productService.existsById(1L));
    }
}