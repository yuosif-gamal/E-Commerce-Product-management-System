package com.example.Ecommerce.util;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NotificationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationClient.class);
    private final RestTemplate restTemplate;

    @Value("${Notification.url}")
    private String notificationUrl;

    public void sendItemNotReservedEmail(String to, String userName, String itemName) {
        String url = String.format("%s/email?to=%s&userName=%s&itemName=%s",
                notificationUrl, to, userName, itemName);

        LOGGER.info("Calling Notification Service URL: {}", url);

        try {
            restTemplate.postForObject(url, null, Void.class);
            LOGGER.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            LOGGER.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
