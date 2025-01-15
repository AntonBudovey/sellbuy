package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ReviewRepository;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import ee.taltech.iti03022024backend.web.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final ReviewMapper reviewMapper;


    @Transactional
    public ReviewDto createReview(ReviewDto review, Long productId, Long userId) {
        log.info("Attempting to create review for productId: {} and userId: {}", productId, userId);
        try {
            Review reviewEntity = reviewMapper.toEntity(review);
            Review createdReview = reviewRepository.save(reviewEntity);
            reviewRepository.assignReviewToProduct(createdReview.getId(), productId);
            reviewRepository.assignReviewToUser(createdReview.getId(), userId);
            log.info("Successfully created review with id: {} for productId: {} and userId: {}", createdReview.getId(), productId, userId);
            return reviewMapper.toDto(createdReview);
        } catch (Exception e) {
            log.error("Error occurred while creating review for productId: {} and userId: {}", productId, userId, e);
            throw e;
        }
    }

    @Transactional
    public ReviewDto updateReview(ReviewDto review) {
        log.info("Attempting to update review with id: {}", review.getId());
        try {
            Review reviewEntity = reviewMapper.toEntity(review);
            Review oldReview = reviewRepository.findById(review.getId())
                    .orElseThrow(() -> {
                        log.warn("Review with id {} not found", review.getId());
                        return new ResourceNotFoundException("Review with id " + review.getId() + " not found");
                    });
            reviewEntity.setUser(oldReview.getUser());
            reviewEntity.setProduct(oldReview.getProduct());
            Review updatedReview = reviewRepository.save(reviewEntity);
            log.info("Successfully updated review with id: {}", updatedReview.getId());
            return reviewMapper.toDto(updatedReview);
        } catch (Exception e) {
            log.error("Error occurred while updating review with id: {}", review.getId(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReviewsByProductId(Long productId) {
        log.info("Fetching all reviews for productId: {}", productId);
        if (!productService.existsById(productId)) {
            log.warn("Product with id {} not found", productId);
            throw new ResourceNotFoundException("Product with id " + productId + " not found");
        }

        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        log.info("Successfully fetched {} reviews for productId: {}", reviews.size(), productId);
        return reviewMapper.toDto(reviews);
    }

    @Transactional
    public void deleteReview(Long id) {
        log.info("Attempting to delete review with id: {}", id);
        if (!reviewRepository.existsById(id)) {
            log.warn("Review with id {} not found", id);
            throw new ResourceNotFoundException("Review with id " + id + " not found");
        }
        reviewRepository.deleteById(id);
        log.info("Successfully deleted review with id: {}", id);
    }
}

