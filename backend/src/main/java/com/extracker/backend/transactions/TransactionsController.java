package com.extracker.backend.transactions;

import com.plaid.client.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class TransactionsController {

    private final FetchTransactions fetchTransactions;

    public TransactionsController(FetchTransactions fetchTransactions) {
        this.fetchTransactions = fetchTransactions;
    }

    public record AccessTokenRequest(String accessToken) {
    }

/*    @PostMapping(value = "/transactions-sync", consumes = "application/json")
    public ResponseEntity<Transaction> getTransactions(@RequestBody AccessTokenRequest accessToken) {

        fetchTransactions.getTransactions(accessToken.accessToken);

        if (accessToken == null) {
            return ResponseEntity.status(500).body("Error: Link token is empty");
        }
        return ResponseEntity.ok(new Transaction(accessToken));
    }*/

}
