package com.experian.saas.client.dataimport.batch.appsql;

/**
 * Contains all SQL and DTO data mappings.
 *
 * @author Mouli
 */
public class AppSqlQuery {

    private AppSqlQuery() {}

    public static String insertAccount() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO account (");
        sql.append("id,");
        sql.append("customer_id, account_number, account_type, open_date, current_balance, account_status");
        sql.append(") VALUES (");
        sql.append(":id,");
        sql.append(":customerId, :accountNumber, :accountType, to_date(:openDate,'MM/DD/YYYY'), :currentBalance, :accountStatus");
        sql.append(")");
        sql.append(updateAccount());

        return sql.toString();
    }

    private static String updateAccount() {
        StringBuilder sql = new StringBuilder();

        sql.append(" ON CONFLICT (id) DO UPDATE SET ");
        sql.append("  customer_id = :customerId, account_number = :accountNumber");
        sql.append(", open_date = to_date(:openDate,'MM/DD/YYYY'), current_balance = :currentBalance");
        sql.append(", account_status = :accountStatus ");

        return sql.toString();
    }

    public static String insertCustomer() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO customer (");
        sql.append("id,");
        sql.append("record_type, customer_number, first_name, middle_name, last_name, name_suffix, ");
        sql.append("officer_director_shareholder, addressline1, addressline2, city, state, zip_code, ");
        sql.append("primary_phone, social_number, birthdate, preapproval_code, negative_list");
        sql.append(") VALUES (");
        sql.append(":id,");
        sql.append(":recordType, :customerName, :firstName, :middleName, :lastName, :nameSuffix, ");
        sql.append(":officerDirectorShareholder, :addressLine1, :addressLine2, :city, :state, :zipCode, ");
        sql.append(":primaryPhone, :socialNumber, to_date(:birthDate,'MM/DD/YYYY'), :preApprovalCode, :negativeList");
        sql.append(")");
        sql.append(updateCustomer());

        return sql.toString();
    }

    private static String updateCustomer() {
        StringBuilder sql = new StringBuilder();

        sql.append(" ON CONFLICT (id) DO UPDATE SET ");
        sql.append("  record_type = :recordType, customer_number = :customerName");
        sql.append(", first_name = :firstName, middle_name = :middleName");
        sql.append(", last_name = :lastName, name_suffix = :nameSuffix");
        sql.append(", officer_director_shareholder = :officerDirectorShareholder, addressline1 = :addressLine1");
        sql.append(", addressline2 = :addressLine2, city = :city");
        sql.append(", state = :state, zip_code = :zipCode");
        sql.append(", primary_phone = :primaryPhone, social_number = :socialNumber");
        sql.append(", birthdate = to_date(:birthDate,'MM/DD/YYYY'), preapproval_code = :preApprovalCode");
        sql.append(", negative_list = :negativeList");

        return sql.toString();
    }

    public static String selectUniqueCustomerId() {
        return "select id from customer where id = ?";
    }

    public static String[] accountDtoMappingNames() {
        String[] accountDtoMappings = {
                "id",
                "customerId",
                "accountNumber",
                "accountType",
                "openDate",
                "currentBalance",
                "accountStatus"};
        return accountDtoMappings;
    }

    public static String[] customerDtoMappingNames() {
        String[] customerDtoMappings = {
                "id",
                "recordType",
                "customerName",
                "customerNumber",
                "firstName",
                "middleName",
                "lastName",
                "nameSuffix",
                "officerDirectorShareholder",
                "addressLine1",
                "addressLine2",
                "city",
                "state",
                "zipCode",
                "primaryPhone",
                "socialNumber",
                "birthDate",
                "preApprovalCode",
                "negativeList"};
        return customerDtoMappings;
    }
}
