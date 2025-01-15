package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.service.ReviewService;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
@Validated
@Tag(name = "Review controller", description = "Review API")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{productId}")
    @Operation(summary = "create new review and as owner login user")
    public ReviewDto createReview(@Validated(OnCreate.class) @RequestBody ReviewDto dto
            , @PathVariable Long productId, Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return reviewService.createReview(dto, productId, userId);
    }

    @PutMapping
    @Operation(summary = "update review(can review owner and admin)")
    public ReviewDto updateReview(@Validated(OnUpdate.class) @RequestBody ReviewDto dto) {
        return reviewService.updateReview(dto);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get all reviews by product id")
    public List<ReviewDto> getAllReviewsById(@PathVariable Long productId) {
        return reviewService.getAllReviewsByProductId(productId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete review by id(can review owner and admin)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
