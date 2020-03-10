package com.example.demo.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송지 정보
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInfo {
    private Receiver receiver;
    private Address address;

    public boolean isBlank() {
        return receiver == null || receiver.isBlank() || address == null || address.isBlank();
    }
}