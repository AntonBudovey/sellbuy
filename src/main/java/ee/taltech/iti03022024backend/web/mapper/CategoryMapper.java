package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends Mappable<Category, CategoryDto>{
}
