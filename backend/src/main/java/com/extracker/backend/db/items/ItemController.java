package com.extracker.backend.db.items;

import com.extracker.backend.db.accounts.AccountEntity;
import com.extracker.backend.db.accounts.AccountRepository;
import com.extracker.backend.db.users.UserEntity;
import com.extracker.backend.db.users.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public ItemController(ItemRepository itemRepository,
                          UserRepository userRepository,
                          AccountRepository accountRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserItems(@PathVariable UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ItemEntity> items = itemRepository.findByUser(user);

        List<Map<String, Object>> result = new ArrayList<>();

        for (ItemEntity item : items) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("id", item.getId());
            itemData.put("bankName", item.getBankName());
            itemData.put("isActive", item.getIsActive() == 1);

            // Get accounts for this item
            List<AccountEntity> accounts = accountRepository.findAll().stream()
                    .filter(account -> account.getItem().getId().equals(item.getId()))
                    .collect(Collectors.toList());

            List<Map<String, String>> accountsData = accounts.stream()
                    .map(account -> {
                        Map<String, String> accountMap = new HashMap<>();
                        accountMap.put("id", account.getId());
                        accountMap.put("name", account.getName());
                        return accountMap;
                    })
                    .collect(Collectors.toList());

            itemData.put("accounts", accountsData);
            result.add(itemData);
        }

        return ResponseEntity.ok(result);
    }
}
