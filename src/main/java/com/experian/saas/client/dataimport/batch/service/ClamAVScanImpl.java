package com.experian.saas.client.dataimport.batch.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.experian.saas.client.dataimport.batch.config.AVConfig;
import com.experian.saas.client.dataimport.batch.util.ResourceUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


/**
 * Created by chrisng on 5/22/2018.
 */
@Service("clamAVScan")
public class ClamAVScanImpl implements AntivirusScan {

    private static Logger LOGGER = LoggerFactory.getLogger(ClamAVScanImpl.class);

    @Autowired
    AVConfig avConfig;

    @Override
    public boolean isFileAntivirusScanned(Resource resource) {

        return false;
    }

    @Override
    public int scanFileWithAntivirus(File file) throws InterruptedException, IOException{

        File tempFile = File.createTempFile("file-to-scan-", null);
        FileUtils.writeByteArrayToFile(tempFile, Files.readAllBytes(file.toPath()));

        int scanStatus = performScan(tempFile);
        if (scanStatus != 0) {
            LOGGER.debug("Scanned failed for file {} with status {}", file.getAbsolutePath(), scanStatus);
        }
        ResourceUtil.addFileToCurrAVScanMap(file.getAbsolutePath(), false);
        return scanStatus;
    }

    private int performScan(final File file) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(bindCommand(file));
        Process process = processBuilder.start();

        if (process.waitFor(avConfig.getTimeToWait(), TimeUnit.MINUTES)) {

            int exitValue = process.exitValue();
            LOGGER.debug("performScan : process output {} ", exitValue);

            return exitValue;
        }

        return FAILED_EXIT_CODE;
    }

    private List<String> bindCommand(final File file) {
        List<String> command = new ArrayList<>();
        command.addAll(avConfig.getPredefinedArguments());
        command.add(file.getAbsolutePath());
        return command;
    }

    public AVConfig getAvConfig() {
        return avConfig;
    }

    public void setAvConfig(AVConfig avConfig) {
        this.avConfig = avConfig;
    }
}
