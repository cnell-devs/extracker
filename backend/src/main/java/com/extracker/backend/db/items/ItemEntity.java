package com.extracker.backend.db.items;

import com.extracker.backend.db.users.UserEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "items")
public class ItemEntity {

    @Id
    private String id; // Primary Key (TEXT in SQL)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign Key referencing users(id)
    private UserEntity user;

    @Column(nullable = false)
    private String accessToken;

    private String transactionCursor; // Nullable

    private String bankName; // Nullable

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer isActive = 1; // Default is active (1)

    // Constructors
    public ItemEntity() {
    }

    public ItemEntity(String id, UserEntity user, String accessToken, String transactionCursor, String bankName, Integer isActive) {
        this.id = id;
        this.user = user;
        this.accessToken = accessToken;
        this.transactionCursor = transactionCursor;
        this.bankName = bankName;
        this.isActive = isActive != null ? isActive : 1;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTransactionCursor() {
        return transactionCursor;
    }

    public void setTransactionCursor(String transactionCursor) {
        this.transactionCursor = transactionCursor;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}