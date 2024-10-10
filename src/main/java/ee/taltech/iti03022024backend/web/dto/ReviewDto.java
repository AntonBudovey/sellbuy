package ee.taltech.iti03022024backend.web.dto;

import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ReviewDto {
    @NotNull(message = "id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "rating cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Min(value = 0, message = "Rating cannot be negative", groups = {OnUpdate.class, OnCreate.class})
    @Max(value = 5, message = "Rating cannot be greater than 5", groups = {OnUpdate.class, OnCreate.class})
    private Double rating;

    @Length(max=255, message = "Text cannot be longer than 255 characters", groups = {OnUpdate.class, OnCreate.class})
    private String text;
}
