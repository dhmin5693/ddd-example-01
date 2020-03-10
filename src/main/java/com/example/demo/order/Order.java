package com.example.demo.order;

import com.example.demo.common.ErrorMessages;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * 주문 정보
 */
@Getter
public class Order {

    private OrderNo id;
    private List<OrderLine> orderLines;
    private OrderState state;
    private ShippingInfo shippingInfo;
    private Money totalAmounts;

    public Order(List<OrderLine> orderLines, ShippingInfo shippingInfo) {
        id = new OrderNo(UUID.randomUUID().toString());
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
        state = OrderState.PAYMENT_WAITING;
    }

    private void setOrderLines(List<OrderLine> orderLines) {
        if (isEmptyLOrderLines(orderLines)) {
            throw new IllegalStateException(ErrorMessages.NO_ORDER_LINE.getMsg());
        }

        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    private boolean isEmptyLOrderLines(List<OrderLine> orderLines) {
        return orderLines == null || orderLines.isEmpty();
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        if (isBlankShippingInfo(shippingInfo)) {
            throw new IllegalStateException(ErrorMessages.NO_SHIPPING_INFO.getMsg());
        }

        this.shippingInfo = shippingInfo;
    }

    private boolean isBlankShippingInfo(ShippingInfo shippingInfo) {
        return shippingInfo == null || shippingInfo.isBlank();
    }

    public void payment() {
        if (state != OrderState.PAYMENT_WAITING) {
            throw new IllegalStateException(ErrorMessages.ALREADY_PAID.getMsg());
        }

        this.state = OrderState.PREPARING;
    }

    public void shipped() {
        if (state != OrderState.PREPARING) {
            throw new IllegalStateException(ErrorMessages.NOT_READY.getMsg());
        }

        this.state = OrderState.SHIPPED;
    }

    public void startDelivery() {
        if (state != OrderState.SHIPPED) {
            throw new IllegalStateException(ErrorMessages.NOT_SHIPPED.getMsg());
        }

        this.state = OrderState.DELIVERING;
    }

    public void completeDelivery() {
        if (state != OrderState.DELIVERING) {
            throw new IllegalStateException(ErrorMessages.NOT_DELIVERING.getMsg());
        }

        this.state = OrderState.DELIVERING_COMPLETED;
    }

    public void changeShippingInfo(ShippingInfo newShippingInfo) {
        verifyBeforeShipping();
        this.shippingInfo = newShippingInfo;
    }

    public void cancel() {
        verifyBeforeShipping();
        this.state = OrderState.CANCELED;
    }

    private void verifyBeforeShipping() {
        if (state != OrderState.PAYMENT_WAITING && state != OrderState.PREPARING) {
            throw new IllegalStateException(ErrorMessages.ALREADY_SHIPPED.getMsg());
        }
    }

    private void calculateTotalAmounts() {
        this.totalAmounts = new Money(orderLines.stream()
                .mapToInt(x -> x.getAmounts().getValue()).sum());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj.getClass() != Order.class) return false;
        if (this.id.getValue().isEmpty()) return false;
        return this.id.equals(((Order) obj).id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        return prime * hash + (id.getValue().isEmpty()? 0 : id.hashCode());
    }
}