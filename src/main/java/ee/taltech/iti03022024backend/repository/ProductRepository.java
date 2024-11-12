package ee.taltech.iti03022024backend.repository;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {



    @EntityGraph(attributePaths = {"reviews"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findWithReviewsById(Long id);

    @Modifying
    @Query(value = """
    UPDATE products
    SET user_id = :userId
    WHERE id = :productId
""", nativeQuery = true)
    void assignProductToUser(@Param("productId")  Long productId, @Param("userId") Long userId);
}
