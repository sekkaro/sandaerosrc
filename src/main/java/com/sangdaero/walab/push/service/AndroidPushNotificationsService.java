package com.sangdaero.walab.push.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// firebase_server_key = firebase project > cloud messaging > server key

@Service
public class AndroidPushNotificationsService {
    private static final String firebase_server_key="AAAApnwG2V4:APA91bHhkCFHrdTutWbZAt2mBfTtFVIS2ZtuYsMayL0xh9CYdv-9WOK7rA6MeBZW9nYnqMW6BDzyLvK8_lEmkxE_NUW2df0nAm2OUXrD-tLAMmN95zsKKgNleyiB8_d59FZgY3Kzykv9";
    private static final String firebase_api_url="https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization",  "key=" + firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
