package com.extracker.backend.db.transactions;

import com.extracker.backend.db.accounts.AccountEntity;
import com.extracker.backend.db.accounts.AccountRepository;
import com.extracker.backend.db.items.ItemEntity;
import com.extracker.backend.db.items.ItemRepository;
import com.extracker.backend.db.users.UserEntity;
import com.extracker.backend.db.users.UserRepository;
import com.extracker.backend.link.Client;
import com.plaid.client.request.PlaidApi;
import com.plaid.client.model.*;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final PlaidApi plaidClient;
    private final TransactionRepository transactionsRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountsRepository;
    private final ItemRepository itemRepository;


    public TransactionService(TransactionRepository transactionsRepository,
                              UserRepository userRepository,
                              AccountRepository accountsRepository,
                              Client client, ItemRepository itemRepository) {
        this.transactionsRepository = transactionsRepository;
        this.userRepository = userRepository;
        this.accountsRepository = accountsRepository;
        this.plaidClient = client.getPlaidClient();
        this.itemRepository = itemRepository;
    }

    public void fetchAndStoreTransactions(UUID userId, String cursor) throws IOException {
        // Step 1: Fetch User
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2: Retrieve the Item associated with the User
        List<ItemEntity> userItems = itemRepository.findByUser(user);
        if (userItems.isEmpty()) {
            throw new RuntimeException("No items found for user: " + userId);
        }

        for (ItemEntity item : userItems) {
            String accessToken = item.getAccessToken(); // ✅ Get accessToken dynamically

            // Step 3: Call Plaid API with the correct accessToken
            Response<TransactionsSyncResponse> response = plaidClient
                    .transactionsSync(new TransactionsSyncRequest().accessToken(accessToken).cursor(cursor))
                    .execute();

            System.out.println(response.body());

            if (!response.isSuccessful() || response.body() == null) {
                throw new RuntimeException("Failed to fetch transactions from Plaid");
            }

            TransactionsSyncResponse transactionData = response.body();

            // Step 4: Store Transactions
            for (Transaction transaction : transactionData.getAdded()) {
                storeTransaction(transaction, user);
            }
        }
    }

    private void storeTransaction(Transaction transaction, UserEntity user) {
        // Find account in DB
        AccountEntity account = accountsRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + transaction.getAccountId()));

        TransactionEntity transactionEntity = new TransactionEntity(
                transaction.getTransactionId(),
                user,
                account,
                transaction.getCategory() != null ? String.join(", ", transaction.getCategory()) : "Unknown",
                transaction.getDate(),
                transaction.getAuthorizedDate(),
                transaction.getName(),
                BigDecimal.valueOf(transaction.getAmount()),
                transaction.getIsoCurrencyCode(),
                0 // Default to not removed
        );

        transactionsRepository.save(transactionEntity);
    }
}