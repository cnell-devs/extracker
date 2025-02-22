package com.extracker.backend.db.transactions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionEntity>> getUserTransactions(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }

    public ResponseEntity<String> syncTransactions(@PathVariable UUID userId,
                                                   @RequestParam(required = false) String cursor) throws IOException {
        System.out.println("USERID: " + userId);
        cursor = null;
        transactionService.fetchAndStoreTransactions(userId, cursor);
        return ResponseEntity.ok("Transactions synced successfully!");
    }


}