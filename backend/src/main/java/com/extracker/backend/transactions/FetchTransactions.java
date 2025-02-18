package com.extracker.backend.transactions;

import com.extracker.backend.MyRunner;
import com.extracker.backend.link.Client;
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

    private PlaidApi plaidClient;

    public FetchTransactions(Client client) {
        this.plaidClient = client.getPlaidClient();
    }

    private static final Logger log = LoggerFactory.getLogger(MyRunner.class);

    void getTransactions(String accessToken) {

        try {
            // Provide a cursor from your database if you've previously
            // received one for the item leave null if this is your
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
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.registerModule(new JavaTimeModule());
//            System.out.println(added.stream().map(item -> {
//                try {
//                    return mapper.writeValueAsString(item);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            }).collect(Collectors.joining(", ", "{\"transactions\" : [", "]}")));

// Persist cursor and updated data
//        database.applyUpdates(itemId, added, modified, removed, cursor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
