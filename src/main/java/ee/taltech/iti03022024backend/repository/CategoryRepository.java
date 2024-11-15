package ee.taltech.iti03022024backend.repository;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    boolean existsById(Long id);
}
