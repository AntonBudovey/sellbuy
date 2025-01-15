package ee.taltech.iti03022024backend.web.dto;

import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Review")
@AllArgsConstructor
public class ReviewDto {
    @NotNull(message = "id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "rating cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Min(value = 0, message = "Rating cannot be negative", groups = {OnUpdate.class, OnCreate.class})
    @Max(value = 5, message = "Rating cannot be greater than 5", groups = {OnUpdate.class, OnCreate.class})
    @Schema(description = "Review rating", example = "4.5")
    private Double rating;

    @Length(max = 255, message = "Text cannot be longer than 255 characters", groups = {OnUpdate.class, OnCreate.class})
    @Schema(description = "Review text", example = "I love this T-shirt! but I don't like red color")
    private String text;
}
