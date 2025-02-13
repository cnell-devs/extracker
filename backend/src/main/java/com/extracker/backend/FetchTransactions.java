package com.extracker.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plaid.client.ApiClient;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FetchTransactions {
    FetchToken fetchToken;
    private PlaidApi plaidClient;
    @Value("${env.clientId}")
    String plaidClientId;
    @Value("${env.secretId}")
    String plaidSecret;

    public FetchTransactions(FetchToken fetchToken) {
        this.fetchToken = fetchToken;
    }

    private static final Logger log = LoggerFactory.getLogger(MyRunner.class);

    void getTransactions() {

        HashMap<String, String> apiKeys = new HashMap<String, String>();
        apiKeys.put("clientId", plaidClientId);
        apiKeys.put("secret", plaidSecret);
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox); // or equivalent, depending on which environment you're calling into
        plaidClient = apiClient.createService(PlaidApi.class);

        try {
            // Provide a cursor from your database if you've previously
// recieved one for the item leave null if this is your
// first sync call for this item. The first request will
// return a cursor.
            String cursor = null;// database.getLatestCursorOrNull(itemId);
            // New transaction updates since "cursor"
            List<Transaction> added = new ArrayList<Transaction>();
            List<Transaction> modified = new ArrayList<Transaction>();
            List<RemovedTransaction> removed = new ArrayList<RemovedTransaction>();
            boolean hasMore = true;
            TransactionsSyncRequestOptions options = new TransactionsSyncRequestOptions()
                    .includePersonalFinanceCategory(true);
            // get access token
            String accessToken = fetchToken.getToken();
            log.info(accessToken);
            TransactionsSyncResponse response;

// Iterate through each page of new transaction updates for item
            do {
                TransactionsSyncRequest request = new TransactionsSyncRequest()
                        .accessToken(accessToken)
                        .cursor(cursor)
                        .options(options);

                response = plaidClient.transactionsSync(request).execute().body();

                // Add this page of results
                added.addAll(response.getAdded());
                modified.addAll(response.getModified());
                removed.addAll(response.getRemoved());

                hasMore = response.getHasMore();

                // Update cursor to the next cursor
                cursor = response.getNextCursor();
//                log.info(response.toString());
            } while (hasMore || response.getTransactionsUpdateStatus() != TransactionsUpdateStatus.HISTORICAL_UPDATE_COMPLETE);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            System.out.println(added.stream().map(item -> {
                try {
                    return mapper.writeValueAsString(item);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.joining(", ", "{\"transactions\" : [", "]}")));
// Persist cursor and updated data
//        database.applyUpdates(itemId, added, modified, removed, cursor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
