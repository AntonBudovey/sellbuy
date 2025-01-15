package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ProductRepository;
import ee.taltech.iti03022024backend.repository.specification.ProductSpecification;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.pagination.ProductSearchCriteria;
import ee.taltech.iti03022024backend.web.mapper.ProductMapper;
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
    private final ProductMapper productMapper;

    // have to change
    public List<ProductDto> getProductsByUserId(Long userId) {
        return productMapper.toDto(productRepository.findByUserId(userId));
    }

    @Transactional
    public ProductDto createProduct(ProductDto product, Long userId) {
        log.info("Attempting to create product for userId: {}", userId);
        Product productEntity = productMapper.toEntity(product);
        Product createdProduct = productRepository.save(productEntity);
        productRepository.assignProductToUser(createdProduct.getId(), userId);
        log.info("Successfully created product with id: {} and assigned to userId: {}", createdProduct.getId(), userId);
        return productMapper.toDto(createdProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(ProductSearchCriteria criteria) {
        log.info("Fetching products with search criteria: {}", criteria);

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
        return products.map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        log.info("Fetching product by id: {}", id);
        try {
            Product product = productRepository.findWithReviewsById(id)
                    .orElseThrow(() -> {
                        log.warn("Product with given id {} not found", id);
                        return new ResourceNotFoundException("Product with id " + id + " not found in getProductById");
                    });
            log.info("Successfully fetched product with id: {}", id);
            return productMapper.toDto(product);
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage(), e);
            throw e;
        }
    }


    @Transactional
    public ProductDto updateProduct(ProductDto product) {
        log.info("Attempting to update product with id: {}", product.getId());
        if (!productRepository.existsById(product.getId())) {
            log.warn("Product with id {}" + UserService.NOT_FOUND, product.getId());
            throw new ResourceNotFoundException("Product to update with id " + product.getId() + " not found");
        }
        Product productEntity = productMapper.toEntity(product);
        Product oldProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> {
                    log.warn("Product with id {} not found", product.getId());
                    return new ResourceNotFoundException("Product to update with id " + product.getId() + " not found in updateProduct");
                });
        productEntity.setUser(oldProduct.getUser());
        Product updatedProduct = productRepository.save(productEntity);
        log.info("Successfully updated product with id: {}", updatedProduct.getId());
        return productMapper.toDto(updatedProduct);

    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Attempting to delete product with id: {}", id);
        if (!productRepository.existsById(id)) {
            log.warn("Product with id {} not found", id);
            throw new ResourceNotFoundException("Product to delete with id " + id + " not found");
        }
        productRepository.deleteById(id);
        log.info("Successfully deleted product with id: {}", id);
    }

    public boolean existsById(Long productId) {
        log.info("Checking if product exists by id: {}", productId);
        boolean exists = productRepository.existsById(productId);
        log.info("Product with id {} exists: {}", productId, exists);
        return exists;
    }
}
