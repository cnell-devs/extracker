package com.extracker.backend.db.transactions;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
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
    public ResponseEntity<List<TransactionEntity>> getUserTransactions(
            @PathVariable UUID userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate != null || endDate != null) {
            return ResponseEntity.ok(transactionService.getUserTransactions(userId, startDate, endDate));
        }
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }

    @PostMapping("/{userId}/sync")
    public ResponseEntity<String> syncTransactions(@PathVariable UUID userId,
                                                   @RequestParam(required = false) String cursor) throws IOException {
        System.out.println("USERID: " + userId);
        cursor = null;

        boolean hasItems = transactionService.hasLinkedItems(userId);
        if (!hasItems) {
            return ResponseEntity.status(400).body("No linked bank accounts found. Please connect a bank account first.");
        }

        transactionService.fetchAndStoreTransactions(userId, cursor);
        return ResponseEntity.ok("Transactions synced successfully!");
    }


}
