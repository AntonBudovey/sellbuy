package ee.taltech.iti03022024backend.web.dto.pagination;

import java.util.List;

public record PageResponse<T>(List<T> content,
                              int pageNumber,
                              int pageSize,
                              int totalPages,
                              long totalElements
                              ) {
}
