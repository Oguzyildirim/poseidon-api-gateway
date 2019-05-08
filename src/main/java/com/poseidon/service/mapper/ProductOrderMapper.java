package com.poseidon.service.mapper;

import com.poseidon.domain.*;
import com.poseidon.service.dto.ProductOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProductOrder and its DTO ProductOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {ShipMapper.class})
public interface ProductOrderMapper extends EntityMapper<ProductOrderDTO, ProductOrder> {

    @Mapping(source = "ship.id", target = "shipId")
    @Mapping(source = "ship.email", target = "shipEmail")
    ProductOrderDTO toDto(ProductOrder productOrder);

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(source = "shipId", target = "ship")
    ProductOrder toEntity(ProductOrderDTO productOrderDTO);

    default ProductOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(id);
        return productOrder;
    }
}
