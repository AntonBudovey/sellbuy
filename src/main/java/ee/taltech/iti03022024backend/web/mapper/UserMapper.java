package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends Mappable<User, UserDto> {
}
