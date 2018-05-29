package com.experian.saas.client.dataimport.batch.tasklet;


import com.experian.saas.client.dataimport.batch.components.ConfigParameters;
import com.experian.saas.client.dataimport.batch.service.AntivirusScan;
import com.experian.saas.client.dataimport.batch.util.CommonUtil;
import com.experian.saas.client.dataimport.batch.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileAntiVirusScanTasklet implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileAntiVirusScanTasklet.class);

    @Autowired
    private ConfigParameters configParameters;

    @Autowired
    @Qualifier("clamAVScan")
    private AntivirusScan antivirusScan;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        File prodDir = new File(configParameters.getStsLandingDirPath());

        File processDir = new File(configParameters.getFileProcessDirPath());
        File virusAffectedDir = new File(configParameters.getVirusAffectedDirPath());

        if (prodDir.isDirectory()) {

            List<File> filesList = Arrays.asList(prodDir.listFiles());

            filesList.forEach(file -> {
                if (ResourceUtil.isFileInAVScanMap(file.getAbsolutePath())) {
                    int virusStatus = 0;

                    try {
                        virusStatus = antivirusScan.scanFileWithAntivirus(file);
                    } catch (InterruptedException e) {
                        LOGGER.error("antivirus scan interrupted {}", e);
                        virusStatus = AntivirusScan.FAILED_EXIT_CODE;
                    } catch (IOException e) {
                        LOGGER.error("antivirus scan IO exception {}", e);
                        virusStatus = AntivirusScan.FAILED_EXIT_CODE;
                    }

                    if (virusStatus == 0) {
                        moveFileToDestDir(file, processDir);
                        LOGGER.info("Moved file '{}' to process folder '{}'",
                                file.getAbsolutePath(),
                                processDir.getAbsolutePath());
                    } else {
                        moveFileToDestDir(file, virusAffectedDir);
                        LOGGER.warn("Moved file '{}' to virus affected dir '{}'",
                                file.getAbsolutePath(),
                                virusAffectedDir.getAbsolutePath());
                    }
                }
            });
        }

        return null;
    }

    private void moveFileToDestDir(File file, File destDir) {
        try {
            CommonUtil.moveFileToDir(file, destDir);
        } catch (IOException e) {
            LOGGER.error("Failed to move success virus scanned file '{}' to destDir '{}'",
                    file.getAbsolutePath(),
                    destDir.getAbsolutePath());
        }
    }
}
