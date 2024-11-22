package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.repository.CategoryRepository;
import ee.taltech.iti03022024backend.service.CategoryService;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.pagination.PageResponse;
import ee.taltech.iti03022024backend.web.dto.pagination.ProductSearchCriteria;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.mapper.CategoryMapper;
import ee.taltech.iti03022024backend.web.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;


    @PostMapping()
    @Operation(summary = "create new category of product")
    CategoryDto createCategory(@Validated(OnCreate.class) @RequestBody CategoryDto dto) {
        System.out.println(dto.getName());
        Category category = categoryMapper.toEntity(dto);
        System.out.println(category.getName());
        Category createdCategory = categoryService.createCategory(category);

        return categoryMapper.toDto(createdCategory);
    }

    @GetMapping
    @Operation(summary = "get all categories")
    List<CategoryDto> getAllCategories() {
        return categoryMapper.toDto(categoryService.getAllCategories());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @PostMapping("/{productId}/{categoryId}")
    @Operation(summary = "add product to category")
    void addProductToCategory(@PathVariable Long productId, @PathVariable Long categoryId) {
        categoryService.addProductToCategory(productId, categoryId);
    }
}
