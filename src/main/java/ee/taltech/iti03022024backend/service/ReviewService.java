package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private ProductService productService;


    @Transactional
    public Review createReview(Review review, Long productId, Long userId) {
        Review createdReview = reviewRepository.save(review);
        reviewRepository.assignReviewToProduct(createdReview.getId(), productId);
        reviewRepository.assignReviewToUser(createdReview.getId(), userId);
        return createdReview;
    }


    @Transactional
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }


    @Transactional(readOnly = true)
    public List<Review> getAllReviewsByProductId(Long productId) {
        if (!productService.existsById(productId)) {
            throw new ResourceNotFoundException("Product with id " + productId + " not found");
        }
            return reviewRepository.findAllByProductId(productId);
        }


    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}

