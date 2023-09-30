package com.ecommerce.notificationservice;

import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    public void sendEmail(VO obj) {
        System.out.println("Order Placed Successfully - Order number is "+ obj.toString());
        System.out.println("Email sent!");
    }
}
