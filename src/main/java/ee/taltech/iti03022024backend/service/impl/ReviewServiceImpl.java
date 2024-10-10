package ee.taltech.iti03022024backend.service.impl;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.repository.ReviewRepository;
import ee.taltech.iti03022024backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review createReview(Review review, Long productId) {
        Review createdReview = reviewRepository.save(review);
        reviewRepository.assignReviewToProduct(createdReview.getId(), productId);
        return createdReview;
    }

    @Override
    @Transactional
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAllReviewsByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}

