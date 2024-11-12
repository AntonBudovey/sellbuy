package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ProductRepository;
import ee.taltech.iti03022024backend.repository.specification.ProductSpecification;
import ee.taltech.iti03022024backend.web.dto.pagination.ProductSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService{
    private final ProductRepository productRepository;
    @Transactional
    public Product createProduct(Product product, Long userId) {
        Product createdProduct = productRepository.save(product);
        productRepository.assignProductToUser(createdProduct.getId(), userId);
        return createdProduct;
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(ProductSearchCriteria criteria) {
        Sort.Direction direction = criteria.getSortDirection() != null? Sort.Direction.valueOf(criteria.getSortDirection().toUpperCase()) : Sort.Direction.ASC;
        int pageNum = criteria.getPageNum() != null? criteria.getPageNum() : 0;
        int pageSize = criteria.getPageSize() != null? criteria.getPageSize() : 10;
        Sort sort = Sort.by(direction,"price");
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sort);
        return productRepository.findAll(ProductSpecification.priceInRange(criteria.getMinPrice()
                , criteria.getMaxPrice()), pageRequest);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findWithReviewsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }
    @Transactional
    public Product updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product with id " + product.getId() + " not found");
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);

    }

    public boolean existsById(Long productId) {
        return productRepository.existsById(productId);
    }
}
