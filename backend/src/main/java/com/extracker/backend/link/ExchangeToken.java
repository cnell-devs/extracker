package com.extracker.backend.link;

import com.plaid.client.ApiClient;
import com.plaid.client.model.ItemPublicTokenExchangeRequest;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;

@Component
public class ExchangeToken {

    private PlaidApi plaidClient;

    public ExchangeToken(Client plaidClientProvider) {
        this.plaidClient = plaidClientProvider.getPlaidClient();
    }

    String exchange(String publicToken) {

        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);

        System.out.println(request);
        Response<ItemPublicTokenExchangeResponse> response = null;
        try {
            response = plaidClient
                    .itemPublicTokenExchange(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String accessToken = response.body().getAccessToken();
        System.out.println("accessToken: " + accessToken);
        return accessToken;
    }
}
