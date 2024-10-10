package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Review;

import java.util.List;

public interface ReviewService {
    Review createReview(Review review, Long productId);

    Review updateReview(Review review);

    List<Review> getAllReviewsByProductId(Long productId);

    void deleteReview(Long id);


}
