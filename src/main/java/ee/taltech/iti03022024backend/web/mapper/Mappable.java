package ee.taltech.iti03022024backend.web.mapper;

import org.springframework.data.domain.Page;

import java.util.List;

public interface Mappable<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDto(List<E> entities);

    List<E> toEntity(List<D> dtos);

}
