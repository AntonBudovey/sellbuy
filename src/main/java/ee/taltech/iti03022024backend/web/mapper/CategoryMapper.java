package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper extends Mappable<Category, CategoryDto> {
}
