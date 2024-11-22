package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ReviewRepository;
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
    private ProductService productService;


    @Transactional
    public Review createReview(Review review, Long productId, Long userId) {
        log.info("Attempting to create review for productId: {} and userId: {}", productId, userId);
        try {
            Review createdReview = reviewRepository.save(review);
            reviewRepository.assignReviewToProduct(createdReview.getId(), productId);
            reviewRepository.assignReviewToUser(createdReview.getId(), userId);
            log.info("Successfully created review with id: {} for productId: {} and userId: {}", createdReview.getId(), productId, userId);
            return createdReview;
        } catch (Exception e) {
            log.error("Error occurred while creating review for productId: {} and userId: {}", productId, userId, e);
            throw e;
        }
    }

    @Transactional
    public Review updateReview(Review review) {
        log.info("Attempting to update review with id: {}", review.getId());
        try {
            Review updatedReview = reviewRepository.save(review);
            log.info("Successfully updated review with id: {}", updatedReview.getId());
            return updatedReview;
        } catch (Exception e) {
            log.error("Error occurred while updating review with id: {}", review.getId(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Review> getAllReviewsByProductId(Long productId) {
        log.info("Fetching all reviews for productId: {}", productId);
        if (!productService.existsById(productId)) {
            log.warn("Product with id {} not found", productId);
            throw new ResourceNotFoundException("Product with id " + productId + " not found");
        }
        try {
            List<Review> reviews = reviewRepository.findAllByProductId(productId);
            log.info("Successfully fetched {} reviews for productId: {}", reviews.size(), productId);
            return reviews;
        } catch (Exception e) {
            log.error("Error occurred while fetching reviews for productId: {}", productId, e);
            throw e;
        }
    }

    @Transactional
    public void deleteReview(Long id) {
        log.info("Attempting to delete review with id: {}", id);
        try {
            reviewRepository.deleteById(id);
            log.info("Successfully deleted review with id: {}", id);
        } catch (Exception e) {
            log.error("Error occurred while deleting review with id: {}", id, e);
            throw e;
        }
    }
}

