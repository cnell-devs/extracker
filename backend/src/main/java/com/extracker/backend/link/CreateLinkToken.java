package com.extracker.backend.link;

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
public class CreateLinkToken {
    private static PlaidApi plaidClient;
    @Value("${env.clientId}")
    String CLIENT_ID;
    @Value("${env.secretId}")
    String SECRET;

    public record LinkTokenResponse(String linkToken) {
    }

    public String getToken() {
        HashMap<String, String> apiKeys = new HashMap<String, String>();
        apiKeys.put("clientId", CLIENT_ID);
        apiKeys.put("secret", SECRET);

        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox); // or equivalent, depending on which environment you're calling into
        plaidClient = apiClient.createService(PlaidApi.class);


        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
                .clientUserId("user-id7867657687");

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(user)
                .clientName("Personal Finance App")
                .products(Arrays.asList(Products.TRANSACTIONS))
                .countryCodes(Arrays.asList(CountryCode.US))
                .language("en");

        Response<LinkTokenCreateResponse> response = null;
        try {
            response = plaidClient
                    .linkTokenCreate(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String linkToken = response.body().getLinkToken();

        return linkToken;
    }


}

