package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends Mappable<Product, ProductDto> {
}
