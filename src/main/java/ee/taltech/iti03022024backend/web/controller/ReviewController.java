package ee.taltech.iti03022024backend.web.controller;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.service.ReviewService;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import ee.taltech.iti03022024backend.web.dto.validation.OnCreate;
import ee.taltech.iti03022024backend.web.dto.validation.OnUpdate;
import ee.taltech.iti03022024backend.web.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class ReviewController {
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    @PostMapping("/{productId}")
    public ReviewDto createReview(@Validated(OnCreate.class) @RequestBody ReviewDto dto, @PathVariable Long productId) {
        Review createdReview = reviewService.createReview(reviewMapper.toEntity(dto), productId);
        return reviewMapper.toDto(createdReview);
    }

    @PutMapping
    public ReviewDto updateReview(@Validated(OnUpdate.class) @RequestBody ReviewDto dto) {
        Review updatedReview = reviewService.updateReview(reviewMapper.toEntity(dto));
        return reviewMapper.toDto(updatedReview);
    }

    @GetMapping("/{productId}")
    public List<ReviewDto> getAllReviewsById(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getAllReviewsByProductId(productId);
        return reviewMapper.toDto(reviews);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
