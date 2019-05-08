package com.poseidon.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the OrderItem entity.
 */
public class OrderItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer quantity;


    private Long productId;

    private String productPartNo;

    private Long orderId;

    private String orderCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductPartNo() {
        return productPartNo;
    }

    public void setProductPartNo(String productPartNo) {
        this.productPartNo = productPartNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long productOrderId) {
        this.orderId = productOrderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String productOrderCode) {
        this.orderCode = productOrderCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (orderItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", product=" + getProductId() +
            ", product='" + getProductPartNo() + "'" +
            ", order=" + getOrderId() +
            ", order='" + getOrderCode() + "'" +
            "}";
    }
}
