package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.service.CategoryService;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "http://localhost:3000")
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;


    @PostMapping()
    @Operation(summary = "create new category of product")
    CategoryDto createCategory(@Validated(OnCreate.class) @RequestBody CategoryDto dto) {
        Category category = categoryMapper.toEntity(dto);
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
