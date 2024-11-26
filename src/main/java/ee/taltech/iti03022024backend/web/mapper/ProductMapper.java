package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper extends Mappable<Product, ProductDto> {
}
