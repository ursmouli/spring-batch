package com.experian.saas.client.dataimport.batch.processor;

import com.experian.saas.client.dataimport.batch.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerItemProcessor.class);

    @Override
    public Customer process(Customer customer) throws Exception {
        if (isValid(customer)) {
            LOGGER.debug("processing customer : {}", customer);
            return customer;
        }
        return null;
    }

    private boolean isValid(Customer customer) {
        return true;
    }
}
