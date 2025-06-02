package com.example.studentmanagement.config;

import com.example.studentmanagement.designpattern.command.PaymentInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentInvokerConfig {

    @Bean
    public PaymentInvoker paymentInvoker() {
        return new PaymentInvoker();
    }
}