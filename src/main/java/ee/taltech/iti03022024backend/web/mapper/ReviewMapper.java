package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper extends Mappable<Review, ReviewDto> {
}
