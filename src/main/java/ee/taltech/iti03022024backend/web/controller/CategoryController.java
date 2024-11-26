package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.service.CategoryService;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping()
    @Operation(summary = "create new category of product")
    CategoryDto createCategory(@Validated(OnCreate.class) @RequestBody CategoryDto dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    @Operation(summary = "get all categories")
    List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
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
