package com.extracker.backend.db.accounts;

import com.extracker.backend.db.items.ItemEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    private String id; // TEXT PRIMARY KEY

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item; // Foreign Key to Items

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