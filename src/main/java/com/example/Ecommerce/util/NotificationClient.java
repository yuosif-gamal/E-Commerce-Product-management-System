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
import static com.example.Ecommerce.entity.UserSubscribeStatus.SUBSCRIBED;
import static com.example.Ecommerce.entity.UserSubscribeStatus.UNSUBSCRIBED;
@Service
@RequiredArgsConstructor
public class NotificationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationClient.class);
    private final RestTemplate restTemplate;

    @Value("${Notification.url}")
    private String notificationUrl;

    public void sendItemNotReservedEmail(String to, String userName, String itemName,Long id) {

        String url = String.format("%s/email?to=%s&userName=%s&itemName=%s&id=%d",
                notificationUrl, to, userName, itemName, id);


        sendingEmailLogger(url);
        try {
            restTemplate.postForObject(url, null, Void.class);
            sendingEmailSuccessLogger(to);
        } catch (Exception e) {
            sendingEmailFailedLogger(to, e.getMessage());
        }
    }
    @Async("taskExecutor")
    public void registrationCompleteEmail(String to, String userName,Long id) {
        String url = String.format("%s/email/register?to=%s&userName=%s&id=%d",
                notificationUrl, to, userName,id);
        sendingEmailLogger(url);

        try {
            restTemplate.postForObject(url, null, Void.class);
            sendingEmailSuccessLogger(to);
        } catch (Exception e) {
            sendingEmailFailedLogger(to, e.getMessage());

        }
    }


    private void sendingEmailLogger(String url) {
        LOGGER.info("Calling Notification Service URL: {}", url);
    }

    private void sendingEmailFailedLogger(String to, String e) {
        LOGGER.error("Failed to send email to {}: {}", to, e);
    }

    private void sendingEmailSuccessLogger(String to) {
        LOGGER.info("Email sent successfully to {}", to);
    }


}
