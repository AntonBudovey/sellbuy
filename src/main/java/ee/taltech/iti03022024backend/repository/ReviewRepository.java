package ee.taltech.iti03022024backend.repository;

import ee.taltech.iti03022024backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Modifying
    @Query(value = """
    UPDATE reviews
    SET product_id = :productId
    WHERE id = :reviewId
""", nativeQuery = true)
    void assignReviewToProduct(@Param("reviewId") Long reviewId, @Param("productId") Long productId);

    @Query(value = """
    SELECT p.reviews
    FROM Product p
    WHERE p.id = :productId""")
    List<Review> findAllByProductId(@Param("productId") Long productId);
}
