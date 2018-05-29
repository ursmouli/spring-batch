package com.experian.saas.client.dataimport.batch.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@ConfigurationProperties("experian.saas.av.service")
public class AVConfig {

    private List<String> predefinedArguments;

    private long timeToWait;

    public List<String> getPredefinedArguments() {
        return predefinedArguments;
    }

    public void setPredefinedArguments(List<String> predefinedArguments) {

        this.predefinedArguments = predefinedArguments;
    }

    public long getTimeToWait() {
        return timeToWait;
    }

    public void setTimeToWait(long timeToWait) {
        this.timeToWait = timeToWait;
    }
}
