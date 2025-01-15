package ee.taltech.iti03022024backend.web.dto;

import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Schema(description = "Product")
@AllArgsConstructor
public class ProductDto {
    @NotNull(message = "id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "title cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Title cannot be longer than 255 characters",
            groups = {OnUpdate.class, OnCreate.class})
    @Schema(description = "Product title", example = "T-Shirt")
    private String title;

    @Length(max = 255, message = "Description cannot be longer than 255 characters")
    @Schema(description = "Product description", example = "T-Shirt with red color")
    private String description;

    @Min(value = 0, message = "Price cannot be negative", groups = {OnCreate.class, OnUpdate.class})
    @Schema(description = "Product price", example = "10.0")
    private Double price;

    private Boolean soldOut;

    private UserDto user;

    private List<CategoryDto> categories;
}
