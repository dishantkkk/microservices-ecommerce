package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.client.InventoryClient;
import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.Payload;
import com.ecommerce.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.observation.ObservedCircuitBreaker;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final StreamBridge streamBridge;
    private final ExecutorService tracableExecutorService;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto) {
        circuitBreakerFactory.configureExecutorService(tracableExecutorService);
        ObservedCircuitBreaker circuitBreaker = (ObservedCircuitBreaker) circuitBreakerFactory.create("inventory");
        Supplier<Boolean> booleanSupplier = () -> orderDto.getOrderLineItemsList().stream()
                .allMatch(orderLineItems -> inventoryClient.checkStock(orderLineItems.getSkuCode()));
        boolean allProductsInStock  = circuitBreaker.run(booleanSupplier,throwable -> handleErrorCase());
        if(allProductsInStock) {
            Order order=new Order();
            order.setOrderLineItems(orderDto.getOrderLineItemsList());
            order.setOrderNumber(UUID.randomUUID().toString());

            orderRepository.save(order);
            log.info("Sending order details to Notification Service with order is "+ order.getId());
            //Payload obj = new Payload("Dishant", "Kushwaha", "dishantkushwahah71@gmail.com");
            streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(order.getId()).build());
            return "Order Placed Successfully";
        } else {
            return "Order Failed, One of the products in the order is not is stock";
        }
    }
    private Boolean handleErrorCase() {
        return false;
    }
}
