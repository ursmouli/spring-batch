package com.file.create;

public class CreateDumpData {
    public static void main(String[] args) throws Exception {

        String accountFile = "C:\\workspace\\files\\test_data\\moredata\\account-data.csv";
        String customerFile = "C:\\workspace\\files\\test_data\\moredata\\customer-data.csv";

        CreateAccountCSV.addAccountData(accountFile);
        CreateCustomerCSV.addCustomerData(customerFile);
    }
}
