package com.experian.saas.client.dataimport.batch.model;

public class Account extends BaseResourceAware {

    private String id;
    private String customerId;
    private String accountNumber;
    private String accountType;
    private String openDate;
    private Double currentBalance;
    private String accountStatus;

    public Account() {
    }

    @Override
    public String toString() {
        String s = "id: " + id
                + ", customerId: " + customerId
                + ", accountNumber: " + accountNumber
                + ", accountType: " + accountType
                + ", openDate: " + openDate
                + ", currentBalance: " + currentBalance
                + ", accountStatus: " + accountStatus;
        if (getResource() != null) {
            s += ", resource: " + getResource().getFilename();
        }
        return s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getOpenDate() {
        if (openDate != null && openDate.length() > 0) {
            openDate = openDate.trim();
        }
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
