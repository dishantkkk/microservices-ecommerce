package com.ecommerce.notificationservice;

import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    public void sendEmail(String message) {
//        System.out.println("Order Placed Successfully - Order number is "+ obj.toString());
        System.out.println("Order Placed Successfully - Order number is "+ message);
        System.out.println("Email sent!");
    }
}
