package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product, Long userId);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Product product);

    void deleteProduct(Long id);


}
