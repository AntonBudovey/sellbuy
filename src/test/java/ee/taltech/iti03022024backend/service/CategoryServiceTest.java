package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.CategoryRepository;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.mapper.CategoryMapper;
import ee.taltech.iti03022024backend.web.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductService productService;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private ProductMapper productMapper;
    private Category category;
    private Category savedCategory;
    private CategoryDto categoryDto;
    private CategoryDto savedCategoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category(null, "name", null);
        savedCategory = new Category(1L, "name", new ArrayList<Product>());
        categoryDto = new CategoryDto(null, "name");
        savedCategoryDto = new CategoryDto(1L, "name");
    }

    @Test
    void testCreateCategorySuccess() {
        Mockito.when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);
        Mockito.when(categoryRepository.save(category)).thenReturn(savedCategory);
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        assertEquals(savedCategoryDto, createdCategory);
    }

    @Test
    void testGetAllCategoriesSuccess() {
        List<CategoryDto> categoryDtos = List.of(savedCategoryDto);
        List<Category> categories = List.of(savedCategory);
        Mockito.when(categoryMapper.toDto(categories)).thenReturn(categoryDtos);
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        List<CategoryDto> result = categoryService.getAllCategories();
        assertEquals(categoryDtos, result);
    }

    @Test
    void testDeleteCategorySuccess() {
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(true);
        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategoryThatNotExist() {
        Mockito.when(categoryRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(0)).deleteById(1L);
    }

    @Test
    void testAddProductToCategorySuccess() {
        Product product = new Product(1L, "Title", "Description", 5.0, false
                , null, null, new HashSet<Category>());
        ProductDto productDto = new ProductDto(1L, "Title", "Description", 5.0, false, null, null);
        Mockito.when(productService.getProductById(1L)).thenReturn(productDto);
        Mockito.when(productMapper.toDto(product)).thenReturn(productDto);
        Mockito.when(productMapper.toEntity(productDto)).thenReturn(product);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(savedCategory));
        categoryService.addProductToCategory(1L, 1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(productService, times(1)).updateProduct(any(ProductDto.class));
    }

    @Test
    void testAddProductToCategoryWithCategoryNotExist() {
        Product product = new Product(1L, "Title", "Description", 5.0, false
                , null, null, new HashSet<Category>());
        ProductDto productDto = new ProductDto(1L, "Title", "Description", 5.0, false, null, null);
        Mockito.when(productService.getProductById(1L)).thenReturn(productDto);
        Mockito.when(productMapper.toDto(product)).thenReturn(productDto);
        Mockito.when(productMapper.toEntity(productDto)).thenReturn(product);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.addProductToCategory(1L, 1L));
        verify(categoryRepository, times(0)).save(any(Category.class));
        verify(productService, times(0)).updateProduct(any(ProductDto.class));
    }

    @Test
    void testAddProductToCategoryWithProductNotExist() {
        Product product = new Product(1L, "Title", "Description", 5.0, false
                , null, null, new HashSet<Category>());
        ProductDto productDto = new ProductDto(1L, "Title", "Description", 5.0, false, null, null);
        Mockito.when(productService.getProductById(1L)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(productMapper.toDto(product)).thenReturn(productDto);
        Mockito.when(productMapper.toEntity(productDto)).thenReturn(product);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(savedCategory));
        assertThrows(ResourceNotFoundException.class, () -> categoryService.addProductToCategory(1L, 1L));
        verify(categoryRepository, times(0)).save(any(Category.class));
        verify(productService, times(0)).updateProduct(any(ProductDto.class));
    }
}