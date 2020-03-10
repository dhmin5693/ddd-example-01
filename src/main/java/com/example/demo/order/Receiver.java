package com.example.demo.order;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Receiver {
    private String name;
    private String phone;

    public boolean isBlank() {
        return StringUtils.isEmpty(name) || StringUtils.isEmpty(phone);
    }
}