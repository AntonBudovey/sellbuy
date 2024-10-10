package ee.taltech.iti03022024backend.web.dto;

import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProductDto {
    @NotNull(message = "id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "title cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max=255, message = "Title cannot be longer than 255 characters",
            groups = {OnUpdate.class, OnCreate.class})
    private String title;

    @Length(max=255, message = "Description cannot be longer than 255 characters")
    private String description;

    @Min(value = 0, message = "Price cannot be negative", groups = {OnCreate.class, OnUpdate.class})
    private Double price;

    private Boolean soldOut;


}
