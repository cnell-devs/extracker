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
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountsRepository;
    private final ItemRepository itemRepository;


    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              AccountRepository accountsRepository,
                              Client client, ItemRepository itemRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.accountsRepository = accountsRepository;
        this.plaidClient = client.getPlaidClient();
        this.itemRepository = itemRepository;
    }

    public void fetchAndStoreTransactions(UUID userId, String cursor) throws IOException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ItemEntity> userItems = itemRepository.findByUser(user);
        if (userItems.isEmpty()) {
            throw new RuntimeException("No items found for user: " + userId);
        }

        for (ItemEntity item : userItems) {
            String accessToken = item.getAccessToken();

            Response<TransactionsSyncResponse> response = plaidClient
                    .transactionsSync(new TransactionsSyncRequest().accessToken(accessToken).cursor(cursor))
                    .execute();

            if (!response.isSuccessful() || response.body() == null) {
                throw new RuntimeException("Failed to fetch transactions from Plaid");
            }

            TransactionsSyncResponse transactionData = response.body();

            for (Transaction transaction : transactionData.getAdded()) {
                storeTransaction(transaction, user);
            }
        }
    }

    private void storeTransaction(Transaction transaction, UserEntity user) {
        AccountEntity account = accountsRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + transaction.getAccountId()));

        List<String> categories = transaction.getCategory();
        String primaryCategory = categories != null && !categories.isEmpty() ? categories.get(0) : "Unknown";
        String detailedCategory = categories != null && categories.size() > 1 ? categories.get(1) : "Unknown";

        // ✅ Extract Merchant Name & Payment Channel
        String merchantName = transaction.getMerchantName() != null ? transaction.getMerchantName() : "Unknown";
        String paymentChannel = transaction.getPaymentChannel() != null ? transaction.getPaymentChannel().toString() : "Unknown";

        TransactionEntity transactionEntity = new TransactionEntity(
                transaction.getTransactionId(),
                user,
                paymentChannel,
                merchantName,
                account,
                primaryCategory,
                detailedCategory,
                transaction.getDate(),
                transaction.getAuthorizedDate(),
                transaction.getName(),
                BigDecimal.valueOf(transaction.getAmount()),
                transaction.getIsoCurrencyCode(),
                0
        );

        transactionRepository.save(transactionEntity);
    }

    public List<TransactionEntity> getUserTransactions(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findByUser(user);
    }
}