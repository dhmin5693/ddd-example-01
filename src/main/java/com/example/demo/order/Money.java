package com.example.demo.order;

import lombok.Getter;

@Getter
public class Money {
    private int value;

    public Money(int value) {
        this.value = value;
    }

    public Money add(Money money) {
        return new Money(this.value + money.getValue());
    }

    public Money multiply(Money money) {
        return new Money(this.value * money.getValue());
    }
}
