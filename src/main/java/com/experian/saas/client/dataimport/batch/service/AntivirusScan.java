package com.experian.saas.client.dataimport.batch.service;

import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public interface AntivirusScan {

    public final static int FAILED_EXIT_CODE = 242;

    boolean isFileAntivirusScanned(Resource resource);

    /**
     * Passes file to external service for antivirus check.
     * <br/>
     * <u>Method return code status:</u>
     * <ul>
     * <li>0 - success (doesn't contain virus)</li>
     * <li>1 - fail (contains viruses)</li>
     * </ul>
     * <br/>
     * @param file to scan for viruses
     * @return {@link Integer} file virus scan status
     */
    int scanFileWithAntivirus(File file) throws InterruptedException, IOException;
}
