package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.service.ProductService;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import ee.taltech.iti03022024backend.web.mapper.ProductMapper;
import ee.taltech.iti03022024backend.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/{userId}")
    ProductDto createProduct(@Validated(OnCreate.class) @RequestBody ProductDto dto
            , @PathVariable Long userId) {
        Product product = productMapper.toEntity(dto);
        Product createdProduct = productService.createProduct(product, userId);

        return productMapper.toDto(createdProduct);
    }

    @PutMapping
    ProductDto updateProduct(@Validated(OnUpdate.class) @RequestBody ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        Product updatedProduct = productService.updateProduct(product);

        return productMapper.toDto(updatedProduct);
    }

    @GetMapping
    List<ProductDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return productMapper.toDto(products);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
