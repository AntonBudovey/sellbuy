package ee.taltech.iti03022024backend.web.dto;

import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryDto {
    @NotNull(message = "id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "category name cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max=255, message = "Category name cannot be longer than 255 characters",
            groups = {OnUpdate.class, OnCreate.class})
    @Schema(description = "Category name", example = "home")
    private String name;
}
