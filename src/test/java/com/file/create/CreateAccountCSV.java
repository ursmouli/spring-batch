package com.file.create;

import com.experian.saas.client.dataimport.batch.model.Account;
import com.experian.saas.client.dataimport.batch.util.CommonUtil;

import java.io.FileWriter;

public class CreateAccountCSV {
    public static void addAccountData(String file) throws Exception {

        String newLine = "\n";
        String firstLine = "id,customer_id,account_number,account_type,open_date,current_balance,account_status";

        //System.out.println(generateRandomString());
        int records = 10000;

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.append(firstLine + newLine);
            for (int i = 0; i < records; i++) {
                Account account = new Account();
                account.setId(CommonUtil.getUniqueId());
                account.setCustomerId(CommonUtil.getIds().get(0));
                account.setAccountType("Credit");
                account.setAccountNumber("acc" + i);
                account.setOpenDate("11-19-2018");
                account.setCurrentBalance(Double.valueOf(i));
                account.setAccountStatus("Open");
                fileWriter.append(account.getCsvDelimitedString() + newLine);
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
