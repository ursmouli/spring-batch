package com.experian.saas.client.dataimport.batch.processor;

import com.experian.saas.client.dataimport.batch.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountItemProcessor implements ItemProcessor<Account, Account> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountItemProcessor.class);

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate postgresJdbcTemplate;

    @Override
    public Account process(Account account) throws Exception {
        if (isValid(account)) {
            LOGGER.debug("processing account : {}", account);
            return account;
        } else {
            return null;
        }
    }

    private boolean isValid(Account account) {
        boolean result = true;
        return result;
    }
}
