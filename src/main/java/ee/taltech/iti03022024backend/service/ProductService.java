package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ProductRepository;
import ee.taltech.iti03022024backend.repository.specification.ProductSpecification;
import ee.taltech.iti03022024backend.web.dto.pagination.ProductSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    // have to change
    public List<Product> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    @Transactional
    public Product createProduct(Product product, Long userId) {
        log.info("Attempting to create product for userId: {}", userId);
        try {
            Product createdProduct = productRepository.save(product);
            productRepository.assignProductToUser(createdProduct.getId(), userId);
            log.info("Successfully created product with id: {} and assigned to userId: {}", createdProduct.getId(), userId);
            return createdProduct;
        } catch (Exception e) {
            log.error("Error occurred while creating product for userId: {}", userId, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(ProductSearchCriteria criteria) {
        log.info("Fetching products with search criteria: {}", criteria);
        try {
            Sort.Direction direction = criteria.getSortDirection() != null
                    ? Sort.Direction.valueOf(criteria.getSortDirection().toUpperCase())
                    : Sort.Direction.ASC;
            int pageNum = criteria.getPageNum() != null ? criteria.getPageNum() : 0;
            int pageSize = criteria.getPageSize() != null ? criteria.getPageSize() : 10;
            Sort sort = Sort.by(direction, "price");
            PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sort);
            Page<Product> products = productRepository.findAll(
                    ProductSpecification.priceInRange(criteria.getMinPrice(), criteria.getMaxPrice()),
                    pageRequest
            );
            log.info("Successfully fetched {} products", products.getTotalElements());
            return products;
        } catch (Exception e) {
            log.error("Error occurred while fetching products with criteria: {}", criteria, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        log.info("Fetching product by id: {}", id);
        try {
            Product product = productRepository.findWithReviewsById(id)
                    .orElseThrow(() -> {
                        log.warn("Product with given id {} not found", id);
                        return new ResourceNotFoundException("Product with id " + id + " not found in getProductById");
                    });
            log.info("Successfully fetched product with id: {}", id);
            return product;
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching product by id: {}", id, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Product getProductByIdWithCategories(Long id) {
        log.info("Fetching product with categories by id: {}", id);
        try {
            Product product = productRepository.findWithCategoriesById(id)
                    .orElseThrow(() -> {
                        log.warn("Product with id {} not found ", id);
                        return new ResourceNotFoundException("Product with categories with id " + id + " not found in getProductByIdWithCategories");
                    });
            log.info("Successfully fetched product with categories for id: {}", id);
            return product;
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching product with categories by id: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public Product updateProduct(Product product) {
        log.info("Attempting to update product with id: {}", product.getId());
        if (!productRepository.existsById(product.getId())) {
            log.warn("Product with id {} not found", product.getId());
            throw new ResourceNotFoundException("Product to update with id " + product.getId() + " not found");
        }
        try {
            Product updatedProduct = productRepository.save(product);
            log.info("Successfully updated product with id: {}", updatedProduct.getId());
            return updatedProduct;
        } catch (Exception e) {
            log.error("Error occurred while updating product with id: {}", product.getId(), e);
            throw e;
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Attempting to delete product with id: {}", id);
        if (!productRepository.existsById(id)) {
            log.warn("Product with id {} not found", id);
            throw new ResourceNotFoundException("Product to delete with id " + id + " not found");
        }
        try {
            productRepository.deleteById(id);
            log.info("Successfully deleted product with id: {}", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting product with id: {}", id, e);
            throw e;
        }
    }

    public boolean existsById(Long productId) {
        log.info("Checking if product exists by id: {}", productId);
        boolean exists = productRepository.existsById(productId);
        log.info("Product with id {} exists: {}", productId, exists);
        return exists;
    }
}
