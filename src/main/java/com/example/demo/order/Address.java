package com.example.demo.order;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Address {
    private String address1;
    private String address2;
    private String zipCode;

    public boolean isBlank() {
        return StringUtils.isEmpty(address1) || StringUtils.isEmpty(address2) || StringUtils.isEmpty(zipCode);
    }
}