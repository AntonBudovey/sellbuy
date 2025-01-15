package ee.taltech.iti03022024backend.web.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSearchCriteria {
    private Integer pageNum;
    private Integer pageSize;
    private Double minPrice;
    private Double maxPrice;
    private String sortDirection;
}
