package ee.taltech.iti03022024backend.repository;

import ee.taltech.iti03022024backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    boolean existsById(Long id);
}
