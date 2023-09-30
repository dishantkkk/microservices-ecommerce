package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.config.interceptor.FeignClientInterceptor;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service", configuration = FeignClientInterceptor.class)
public interface InventoryClient {

    //@Headers("Content-Type: application/json")
    @GetMapping(value = "/api/inventory/{skuCode}", consumes = "application/json")
    Boolean checkStock(@PathVariable String skuCode);

}
