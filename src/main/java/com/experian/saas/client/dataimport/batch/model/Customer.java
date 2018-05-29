package com.experian.saas.client.dataimport.batch.model;

public class Customer extends BaseResourceAware {
    private String id;
    private String recordType;
    private String customerName;
    private String customerNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nameSuffix;
    private boolean officerDirectorShareholder;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String primaryPhone;
    private String socialNumber;
    private String birthDate;
    private String preApprovalCode;
    private boolean negativeList;

    public Customer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public boolean isOfficerDirectorShareholder() {
        return officerDirectorShareholder;
    }

    public void setOfficerDirectorShareholder(boolean officerDirectorShareholder) {
        this.officerDirectorShareholder = officerDirectorShareholder;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(String socialNumber) {
        this.socialNumber = socialNumber;
    }

    public String getBirthDate() {
        if (birthDate != null && birthDate.length() > 0) {
            birthDate = birthDate.trim();
        }
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPreApprovalCode() {
        return preApprovalCode;
    }

    public void setPreapprovalCode(String preapprovalCode) {
        this.preApprovalCode = preapprovalCode;
    }

    public boolean isNegativeList() {
        return negativeList;
    }

    public void setNegativeList(boolean negativeList) {
        this.negativeList = negativeList;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append("id: " + id);
        info.append(", recordType: " + recordType);
        info.append(", customerName: " + customerName);
        return info.toString();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }
}
