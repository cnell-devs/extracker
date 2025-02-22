package com.extracker.backend.db.transactions;

import com.extracker.backend.db.accounts.AccountEntity;
import com.extracker.backend.db.users.UserEntity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private AccountEntity account;

    private String primaryCategory;  // ✅ Main category
    private String detailedCategory; // ✅ Subcategory

    private LocalDate date;
    private LocalDate authorizedDate;
    private String name;
    private String paymentChannel;
    private String merchant;

    @Column(nullable = false)
    private BigDecimal amount;
    private String currencyCode;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer isRemoved = 0;

    public TransactionEntity() {
    }

    public TransactionEntity(String id, UserEntity user, String paymentChannel, String merchant, AccountEntity account, String primaryCategory, String detailedCategory, LocalDate date, LocalDate authorizedDate, String name, BigDecimal amount, String currencyCode, Integer isRemoved) {
        this.id = id;
        this.user = user;
        this.paymentChannel = paymentChannel;
        this.merchant = merchant;
        this.account = account;
        this.primaryCategory = primaryCategory;
        this.detailedCategory = detailedCategory;
        this.date = date;
        this.authorizedDate = authorizedDate;
        this.name = name;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.isRemoved = isRemoved != null ? isRemoved : 0;
    }

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

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getDetailedCategory() {
        return detailedCategory;
    }

    public void setDetailedCategory(String detailedCategory) {
        this.detailedCategory = detailedCategory;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getAuthorizedDate() {
        return authorizedDate;
    }

    public void setAuthorizedDate(LocalDate authorizedDate) {
        this.authorizedDate = authorizedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(Integer isRemoved) {
        this.isRemoved = isRemoved;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }
}