package com.example.demo.order;

/**
 * 주문 상태
 * 결제 대기 -> 준비중 -> 배송준비 -> 배송중 -> 배송완료 // 취소됨
 */
public enum OrderState {
    PAYMENT_WAITING {
        public boolean isShippingChangeable() {
            return true;
        }
    },

    PREPARING {
        public boolean isShippingChangeable() {
            return true;
        }
    },

    SHIPPED,
    DELIVERING,
    DELIVERING_COMPLETED,
    CANCELED;

    public boolean isShippingChangeable() {
        return false;
    }
}
