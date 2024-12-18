package ee.taltech.iti03022024backend.repository.specification;

import ee.taltech.iti03022024backend.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static final String PRICE = "price";

    private ProductSpecification() {
    }

    public static Specification<Product> priceInRange(Double min, Double max) {

        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), min);
            }
            return criteriaBuilder.between(root.get(PRICE), min, max);
        };
    }
}
