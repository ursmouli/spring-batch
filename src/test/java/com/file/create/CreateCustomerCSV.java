package com.file.create;

import com.experian.saas.client.dataimport.batch.model.Account;
import com.experian.saas.client.dataimport.batch.model.Customer;
import com.experian.saas.client.dataimport.batch.util.CommonUtil;

import java.io.FileWriter;

public class CreateCustomerCSV {
    public static void addCustomerData(String file) throws Exception {

        String newLine = "\n";
        String firstLine = "id,record_type,customer_number,first_name,middle_name,last_name,name_suffix,officer_director_shareholder,addressline1,addressline2,city,state,zip_code,primary_phone,social_number,birthdate,preapproval_code,negative_list";
        //System.out.println(generateRandomString());
        int records = 10000;

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.append(firstLine + newLine);
            for (int i = 0; i < records; i++) {
                Customer customer = new Customer();
                if (i == 0) {
                    customer.setId(CommonUtil.getIds().get(0));
                } else {
                    customer.setId(CommonUtil.getUniqueId());
                }
                customer.setRecordType("NEW");
                customer.setCustomerNumber("142-41256-123");
                customer.setFirstName("fname" + i);
                customer.setMiddleName("mname" + i);
                customer.setLastName("lname" + i);
                customer.setNameSuffix("Jr");
                customer.setOfficerDirectorShareholder(true);
                customer.setAddressLine1("address1-" + i);
                customer.setAddressLine2("address2-" + i);
                customer.setCity("city" + i);
                customer.setState("st");
                customer.setZipCode("zip" + i);
                customer.setPrimaryPhone("123456");
                customer.setSocialNumber("123456");
                customer.setBirthDate("11-19-2018");
                customer.setPreapprovalCode("code" + i);
                customer.setNegativeList(true);
                fileWriter.append(customer.getCsvDelimitedString() + newLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                fileWriter.flush();
                fileWriter.close();
            }
        }
    }
}
