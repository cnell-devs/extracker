package com.extracker.backend.item;

import com.extracker.backend.link.Client;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;

@Component
public class Item {

    private PlaidApi plaidClient;

    public Item(Client plaidClientProvider) {
        this.plaidClient = plaidClientProvider.getPlaidClient();
    }

    public Response<ItemGetResponse> getItem(String accessToken) {
        ItemGetRequest request = new ItemGetRequest()
                .accessToken(accessToken);
        Response<ItemGetResponse> response = null;
        try {
            response = plaidClient.itemGet(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ItemWithConsentFields item = response.body().getItem();

        ItemStatusNullable status = response.body().getStatus();

        System.out.println("response: " + response.body());
        return response;

    }
}
