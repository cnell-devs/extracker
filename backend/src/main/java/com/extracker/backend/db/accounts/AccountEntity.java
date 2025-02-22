package com.extracker.backend.db.accounts;

import com.extracker.backend.db.items.ItemEntity;
import com.extracker.backend.db.transactions.TransactionEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    private String id; // TEXT PRIMARY KEY

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item; // Foreign Key to Items

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // ✅ Allows transactions to include account details
    private List<TransactionEntity> transactions;

    private String name; // TEXT

    public AccountEntity() {
    }

    public AccountEntity(String id, ItemEntity item, String name) {
        this.id = id;
        this.item = item;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}