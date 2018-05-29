package com.experian.saas.client.dataimport.batch.validator;

import com.experian.saas.client.dataimport.batch.components.ConfigParameters;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

@Component
public class JobParameterValidator extends DefaultJobParametersValidator {

    @Autowired
    private ConfigParameters configParameters;

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (StringUtils.isEmpty(configParameters.getStsLandingDirPath())) {
            throw new JobParametersInvalidException("Bank prod dir path required .. please configure landing path in properties file");
        }

        File bankDirPath = new File(configParameters.getStsLandingDirPath());
        if (!bankDirPath.exists()) {
            throw new JobParametersInvalidException("Bank prod dir path required .. please create dir : " + configParameters.getStsLandingDirPath());
        }
    }
}
