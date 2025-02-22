package com.extracker.backend.db.accounts;

import com.extracker.backend.db.items.ItemEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Saves an account if it does not already exist, otherwise updates it.
     */
    public AccountEntity save(String accountId, ItemEntity item, String accountName) {
        Optional<AccountEntity> existingAccount = accountRepository.findById(accountId);

        if (existingAccount.isPresent()) {
            AccountEntity account = existingAccount.get();
            account.setName(accountName); // Update account name if changed
            return accountRepository.save(account);
        } else {
            AccountEntity newAccount = new AccountEntity(accountId, item, accountName);
            return accountRepository.save(newAccount);
        }
    }
}