package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.CategoryRepository;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import ee.taltech.iti03022024backend.web.mapper.CategoryMapper;
import ee.taltech.iti03022024backend.web.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Transactional
    public CategoryDto createCategory(CategoryDto category) {
        log.info("Attempting to create a category with name: {}", category.getName());
        try {
            Category categoryEntity = categoryMapper.toEntity(category);
            Category result = categoryRepository.save(categoryEntity);
            log.info("Successfully created category with id: {}", result.getId());
            return categoryMapper.toDto(result);
        } catch (Exception e) {
            log.error("Error occurred while creating category with name: {}", category.getName(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        log.info("Fetching all categories");
        try {
            List<Category> categories = categoryRepository.findAll();
            log.info("Successfully fetched {} categories", categories.size());
            return categoryMapper.toDto(categories);
        } catch (Exception e) {
            log.error("Error occurred while fetching all categories", e);
            throw e;
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Attempting to delete category with id: {}", id);
        if (!categoryRepository.existsById(id)) {
            log.warn("Category with given id {} not found", id);
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        try {
            categoryRepository.deleteById(id);
            log.info("Successfully deleted category with id: {}", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting category with id: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public void addProductToCategory(Long productId, Long categoryId) {
        log.info("Attempting to add product with id {} to category with id {}", productId, categoryId);
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
                log.warn("Category with id {} not found", categoryId);
                return new ResourceNotFoundException("Category with id " + categoryId + " not found");
            });

            Product product = productMapper.toEntity(productService.getProductById(productId));
            category.getProducts().add(product);
            product.getCategories().add(category);
            log.info("Added product with id {} to category with id {}", productId, categoryId);

            categoryRepository.save(category);
            productService.updateProduct(productMapper.toDto(product));
            log.info("Successfully saved category and updated product");
        } catch (ResourceNotFoundException e) {
            log.error("Error: Resource not found - {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while adding product with id {} to category with id {}", productId, categoryId, e);
            throw e;
        }
        log.info("Added product with id {} to category with id {}", productId, categoryId);
    }
}
