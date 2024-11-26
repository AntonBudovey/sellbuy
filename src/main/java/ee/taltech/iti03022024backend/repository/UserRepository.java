package ee.taltech.iti03022024backend.repository;

import ee.taltech.iti03022024backend.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
                SELECT AVG(r.rating)
                FROM Product p
                JOIN Review r ON p.id = r.product.id
                WHERE p.user.id = :userId
            """)
    Double getUserRatingByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.products WHERE u.id = :id")
    @EntityGraph(attributePaths = {"products"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findWithProductsById(@Param("id") Long id);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.reviews WHERE u.id = :id")
    @EntityGraph(attributePaths = {"reviews"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findWithReviewsById(Long id);
}
