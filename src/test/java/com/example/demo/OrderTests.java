package com.example.demo;

import com.example.demo.order.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderTests {

    private ProductType[] productTypes = ProductType.values();

    @Test(expected = IllegalStateException.class)
    public void noShippingInfo() {

        List<OrderLine> orderLines = createOrderLines(3);
        ShippingInfo shippingInfo = new ShippingInfo();

        Order order = new Order(orderLines, shippingInfo);
    }

    @Test(expected = IllegalStateException.class)
    public void noOrderLine() {

        List<OrderLine> orderLines = createOrderLines(0);
        ShippingInfo shippingInfo = createShippingInfo();

        Order order = new Order(orderLines, shippingInfo);
    }

    @Test
    public void order() {
        // 주문
        Order order = createOrder();
        assertThat(order.getState()).isEqualTo(OrderState.PAYMENT_WAITING);

        // 금액 지불
        order.payment();
        assertThat(order.getState()).isEqualTo(OrderState.PREPARING);

        // 출고
        order.shipped();
        assertThat(order.getState()).isEqualTo(OrderState.SHIPPED);

        // 배송 시작
        order.startDelivery();
        assertThat(order.getState()).isEqualTo(OrderState.DELIVERING);

        // 배송 완료
        order.completeDelivery();
        assertThat(order.getState()).isEqualTo(OrderState.DELIVERING_COMPLETED);
    }

    @Test
    public void orderCancelBeforePayment() {
        Order order = createOrder();
        // 주문 후 취소
        order.cancel();
        assertThat(order.getState()).isEqualTo(OrderState.CANCELED);
    }

    @Test
    public void orderCancelBeforeShipping() {
        Order order = createOrder();
        order.payment();
        // 결제 후 취소
        order.cancel();
        assertThat(order.getState()).isEqualTo(OrderState.CANCELED);
    }

    @Test(expected = IllegalStateException.class)
    public void failOrderCancelInShipped() {
        Order order = createOrder();
        order.payment();
        order.shipped();
        order.cancel();
        assertThat(order.getState()).isEqualTo(OrderState.CANCELED);
    }

    @Test(expected = IllegalStateException.class)
    public void failOrderCancelInDelivering() {
        Order order = createOrder();
        order.payment();
        order.shipped();
        order.startDelivery();
        order.cancel();
        assertThat(order.getState()).isEqualTo(OrderState.CANCELED);
    }

    @Test(expected = IllegalStateException.class)
    public void failOrderCancelInDeliveringComplete() {
        Order order = createOrder();
        order.payment();
        order.shipped();
        order.startDelivery();
        order.completeDelivery();
        order.cancel();
        assertThat(order.getState()).isEqualTo(OrderState.CANCELED);
    }

    @Test
    public void changeShippingInfo() {
        Order order = createOrder();

        ShippingInfo info = order.getShippingInfo();
        ShippingInfo created = createShippingInfo();

        assertThat(info.getReceiver().equals(created.getReceiver())).isEqualTo(true);
        assertThat(info.getAddress().equals(created.getAddress())).isEqualTo(true);
        
        order.changeShippingInfo(ShippingInfo.builder()
                .receiver(new Receiver("KIM", "010-1234-5678"))
                .address(new Address("Seoul", "Korea", "12345"))
                .build());

        info = order.getShippingInfo();
        assertThat(info.getReceiver().equals(created.getReceiver())).isEqualTo(false);
        assertThat(info.getAddress().equals(created.getAddress())).isEqualTo(false);
    }

    @Test(expected = IllegalStateException.class)
    public void failChangeShippingInfo() {
        Order order = createOrder();
        order.payment();
        order.shipped();

        order.changeShippingInfo(ShippingInfo.builder()
                .receiver(new Receiver("KIM", "010-1234-5678"))
                .address(new Address("Seoul", "Korea", "12345"))
                .build());
    }
    
    public Order createOrder() {
        List<OrderLine> orderLines = createOrderLines(3);
        ShippingInfo shippingInfo = createShippingInfo();
        return new Order(orderLines, shippingInfo);
    }

    public ShippingInfo createShippingInfo() {
        return ShippingInfo.builder()
                .receiver(new Receiver("MIN", "010-1234-5678"))
                .address(new Address("Incheon", "Korea", "12345"))
                .build();
    }

    public List<OrderLine> createOrderLines(int orderLineCount) {

        List<OrderLine> orderLines = new ArrayList<>();

        for (int i = 0; i < orderLineCount; i++) {
            Product product = new Product(UUID.randomUUID().toString(), productTypes[i % productTypes.length]);
            Money price = new Money(10000 * i);

            orderLines.add(new OrderLine(product, price, i));
        }

        return orderLines;
    }
}