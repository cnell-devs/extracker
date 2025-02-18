package com.extracker.backend.db.transactions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/plaid")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{userId}/sync-transactions")
    public ResponseEntity<String> syncTransactions(@PathVariable UUID userId,
                                                   @RequestParam(required = false) String cursor) throws IOException {
        transactionService.fetchAndStoreTransactions(userId, cursor);
        return ResponseEntity.ok("Transactions synced successfully!");
    }
}