package com.extracker.backend.db.items;

import com.extracker.backend.db.items.ItemEntity;
import com.extracker.backend.db.items.ItemRepository;
import com.extracker.backend.db.users.UserEntity;
import com.extracker.backend.db.users.UserRepository;
import com.extracker.backend.link.Client;
import com.plaid.client.request.PlaidApi;
import com.plaid.client.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    private final PlaidApi plaidClient;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    public ItemService(ItemRepository itemRepository, UserRepository userRepository, Client client) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;

        // Initialize Plaid API Client
        this.plaidClient = client.getPlaidClient();
    }

    public ItemEntity saveItemFromPlaidResponse(UUID userId, String accessToken) throws IOException {
        // Step 1: Call Plaid API to get Item details
        Response<ItemGetResponse> itemResponse = plaidClient
                .itemGet(new ItemGetRequest().accessToken(accessToken))
                .execute();

        if (!itemResponse.isSuccessful() || itemResponse.body() == null) {
            throw new RuntimeException("Failed to fetch item from Plaid");
        }

        // Extract item details
        ItemGetResponse itemData = itemResponse.body();
        String itemId = itemData.getItem().getItemId();
        String institutionId = itemData.getItem().getInstitutionId();

        // Step 2: Get Institution Name
        String bankName = Optional.ofNullable(institutionId)
                .map(this::getInstitutionName)
                .orElse(null);

        // Step 3: Fetch User
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 4: Save ItemEntity
        ItemEntity itemEntity = new ItemEntity(itemId, user, accessToken, null, bankName, 1);
        return itemRepository.save(itemEntity);
    }

    private String getInstitutionName(String institutionId) {
        try {
            Response<InstitutionsGetByIdResponse> response = plaidClient
                    .institutionsGetById(new InstitutionsGetByIdRequest()
                            .institutionId(institutionId)
                            .countryCodes(java.util.List.of(CountryCode.US)))
                    .execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getInstitution().getName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}