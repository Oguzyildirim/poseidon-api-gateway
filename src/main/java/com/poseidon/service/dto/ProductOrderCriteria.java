package com.poseidon.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.poseidon.domain.enumeration.OrderStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the ProductOrder entity. This class is used in ProductOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /product-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductOrderCriteria implements Serializable {
    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter placedDate;

    private OrderStatusFilter status;

    private StringFilter code;

    private LongFilter orderItemId;

    private LongFilter shipId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(InstantFilter placedDate) {
        this.placedDate = placedDate;
    }

    public OrderStatusFilter getStatus() {
        return status;
    }

    public void setStatus(OrderStatusFilter status) {
        this.status = status;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public LongFilter getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(LongFilter orderItemId) {
        this.orderItemId = orderItemId;
    }

    public LongFilter getShipId() {
        return shipId;
    }

    public void setShipId(LongFilter shipId) {
        this.shipId = shipId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductOrderCriteria that = (ProductOrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(placedDate, that.placedDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(code, that.code) &&
            Objects.equals(orderItemId, that.orderItemId) &&
            Objects.equals(shipId, that.shipId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        placedDate,
        status,
        code,
        orderItemId,
        shipId
        );
    }

    @Override
    public String toString() {
        return "ProductOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (placedDate != null ? "placedDate=" + placedDate + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (orderItemId != null ? "orderItemId=" + orderItemId + ", " : "") +
                (shipId != null ? "shipId=" + shipId + ", " : "") +
            "}";
    }

}
