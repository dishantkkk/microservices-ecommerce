package com.ecommerce.orderservice;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableFeignClients
@SpringBootApplication
@RequiredArgsConstructor
public class OrderServiceApplication {
	private final BeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	public ExecutorService tracableExecutorService() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		return new TraceableExecutorService(beanFactory, executorService);
	}
}
