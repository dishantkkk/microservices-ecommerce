package com.ecommerce.orderservice.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        requestTemplate.header("Authorization", "Bearer " +token.getToken().getTokenValue());
    }
}
