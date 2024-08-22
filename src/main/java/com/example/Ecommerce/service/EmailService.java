package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.CartItem;
import com.example.Ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender emailSender;

    public void sendItemNotReservedEmail(User user, CartItem cartItem) {
        String subject = "Item Not Reserved ";
        String message = String.format("Dear %s, We Aboseada E-commerce your item %s has been marked as NOT_RESERVED. " +
                        "please go to re reserve it",
                user.getEmail(), cartItem.getProduct().getName());
        LOGGER.info(message);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            emailSender.send(mailMessage);
            LOGGER.info("Sending NOT_RESERVED email to user: {}", user.getEmail());
        } catch (Exception e) {
            LOGGER.error("Failed to send NOT_RESERVED email to user: {}", user.getEmail(), e);
        }
    }
}

