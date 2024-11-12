package ee.taltech.iti03022024backend.web.dto.pagination;

import lombok.Data;

@Data
public class ProductSearchCriteria {
    private Integer pageNum;
    private Integer pageSize;
    private Double minPrice;
    private Double maxPrice;
    private String sortDirection;
}
