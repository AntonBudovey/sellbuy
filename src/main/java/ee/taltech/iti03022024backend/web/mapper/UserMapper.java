package ee.taltech.iti03022024backend.web.mapper;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto dto);
}
