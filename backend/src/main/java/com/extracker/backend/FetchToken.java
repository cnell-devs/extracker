package com.extracker.backend;

import com.plaid.client.ApiClient;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@Component
public class FetchToken {

    private PlaidApi client;
    @Value("${env.clientId}")
    String plaidClientId;
    @Value("${env.secretId}")
    String plaidSecret;


    public String getToken() {
        HashMap<String, String> apiKeys = new HashMap<String, String>();
        apiKeys.put("clientId", plaidClientId);
        apiKeys.put("secret", plaidSecret);
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox); // or equivalent, depending on which environment you're calling into
        client = apiClient.createService(PlaidApi.class);

        try {
            SandboxPublicTokenCreateRequest createRequest = new SandboxPublicTokenCreateRequest()
                    .institutionId("ins_1")
                    .initialProducts(Arrays.asList(Products.AUTH));

            Response<SandboxPublicTokenCreateResponse> createResponse = client
                    .sandboxPublicTokenCreate(createRequest)
                    .execute();

            // The generated public_token can now be
// exchanged for an access_token
            ItemPublicTokenExchangeRequest exchangeRequest = new ItemPublicTokenExchangeRequest()
                    .publicToken(createResponse.body().getPublicToken());

            Response<ItemPublicTokenExchangeResponse> response = client
                    .itemPublicTokenExchange(exchangeRequest)
                    .execute();

            return response.body().getAccessToken();
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
}
