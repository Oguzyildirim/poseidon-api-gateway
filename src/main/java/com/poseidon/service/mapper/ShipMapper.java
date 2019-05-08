package com.poseidon.service.mapper;

import com.poseidon.domain.*;
import com.poseidon.service.dto.ShipDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ship and its DTO ShipDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CompanyMapper.class})
public interface ShipMapper extends EntityMapper<ShipDTO, Ship> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.email", target = "companyEmail")
    ShipDTO toDto(Ship ship);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "orders", ignore = true)
    @Mapping(source = "companyId", target = "company")
    Ship toEntity(ShipDTO shipDTO);

    default Ship fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ship ship = new Ship();
        ship.setId(id);
        return ship;
    }
}
