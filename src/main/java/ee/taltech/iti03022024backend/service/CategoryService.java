package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.CategoryRepository;
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

    @Transactional
    public Category createCategory(Category category) {
        log.info("Attempting to create a category with name: {}", category.getName());
        try {
            Category result = categoryRepository.save(category);
            log.info("Successfully created category with id: {}", result.getId());
            return result;
        } catch (Exception e) {
            log.error("Error occurred while creating category with name: {}", category.getName(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        log.info("Fetching all categories");
        try {
            List<Category> categories = categoryRepository.findAll();
            log.info("Successfully fetched {} categories", categories.size());
            return categories;
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

            Product product = productService.getProductById(productId);
            category.getProducts().add(product);
            product.getCategories().add(category);
            log.info("Added product with id {} to category with id {}", productId, categoryId);

            categoryRepository.save(category);
            productService.updateProduct(product);
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
