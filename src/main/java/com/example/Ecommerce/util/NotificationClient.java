package com.example.Ecommerce.util;

import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.repository.UserRepo;
import com.example.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.example.Ecommerce.entity.UserSubscribeStatus.SUBSCRIBED;
import static com.example.Ecommerce.entity.UserSubscribeStatus.UNSUBSCRIBED;

@Service
@RequiredArgsConstructor
public class NotificationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationClient.class);
    private final RestTemplate restTemplate;

    @Value("${Notification.url}")
    private String notificationUrl;

    @Async("taskExecutor")
    public void sendEmail(Map<String, Object> mailInfo) {
        String url = String.format("%s/email", notificationUrl);

        LOGGER.info("Calling Notification Service URL: {}", url);
        try {
            restTemplate.postForObject(url, mailInfo, Void.class);
            LOGGER.info("Email sent successfully to {}", mailInfo.get("username"));
        } catch (Exception e) {
            LOGGER.error("Failed to send email to {} : ", mailInfo.get("username"), e);
        }
    }

}