package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends Mappable<Review, ReviewDto> {
}
