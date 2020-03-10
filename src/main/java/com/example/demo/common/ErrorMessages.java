package com.example.demo.common;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    ALREADY_PAID("already paid"),
    NOT_READY("not preparing state"),
    NOT_SHIPPED("not shipped state"),
    NOT_DELIVERING("not delivering state"),
    ALREADY_SHIPPED("already shipped"),
    NO_ORDER_LINE("no OrderLine"),
    NO_SHIPPING_INFO("no Shipping Information");

    String msg;

    ErrorMessages(String msg) {
        this.msg = msg;
    }
}
