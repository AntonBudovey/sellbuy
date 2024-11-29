package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.service.ProductService;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.pagination.PageResponse;
import ee.taltech.iti03022024backend.web.dto.pagination.ProductSearchCriteria;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@Validated
@Tag(name = "Product controller", description = "Product API")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "get product by id")
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "create new product and as owner login user")
    ProductDto createProduct(@Validated(OnCreate.class) @RequestBody ProductDto dto
            , Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return productService.createProduct(dto, userId);
    }

    @PutMapping("/update")
    @Operation(summary = "update product(can product owner and admin)")
    ProductDto updateProduct(@Validated(OnUpdate.class) @RequestBody ProductDto dto) {
        return productService.updateProduct(dto);
    }

    @GetMapping("/all")
    @Operation(summary = "get all products with sorting and pagination")
    PageResponse<ProductDto> getAllProducts(@ModelAttribute ProductSearchCriteria criteria) {
        Page<ProductDto> products = productService.getAllProducts(criteria);
        return new PageResponse<>(products.getContent()
                , products.getNumber()
                , products.getSize()
                , products.getTotalPages()
                , products.getTotalElements());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "get all products by user id")
    public List<ProductDto> getProductsByUserId(@PathVariable Long userId) {
        return productService.getProductsByUserId(userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete product(can product owner and admin)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
