package com.extracker.backend.link;

import com.plaid.client.ApiClient;
import com.plaid.client.model.PlaidCheckScore;
import com.plaid.client.request.PlaidApi;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component  // Makes it a Spring-managed Bean
public class Client {

    private PlaidApi plaidClient;

    @Value("${env.clientId}")
    private String CLIENT_ID;

    @Value("${env.secretId}")
    private String SECRET;

    @PostConstruct  // Ensures initialization after Spring injects values
    public void init() {

        HashMap<String, String> apiKeys = new HashMap<>();
        apiKeys.put("clientId", CLIENT_ID);
        apiKeys.put("secret", SECRET);

        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox); // Set environment

        this.plaidClient = apiClient.createService(PlaidApi.class);
    }

    public PlaidApi getPlaidClient() {
        return this.plaidClient;
    }
}
