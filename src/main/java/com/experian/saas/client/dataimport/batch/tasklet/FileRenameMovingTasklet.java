package com.experian.saas.client.dataimport.batch.tasklet;

import com.experian.saas.client.dataimport.batch.components.ConfigParameters;
import com.experian.saas.client.dataimport.batch.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Component
public class FileRenameMovingTasklet implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileRenameMovingTasklet.class);

    @Autowired
    private ConfigParameters configParameters;

    @Override
    public synchronized RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        File prodDir = new File(configParameters.getStsLandingDirPath());

        File avScanProcessDir = new File(configParameters.getAvScanProcessDirPath());

        if (prodDir.isDirectory()) {

            List<File> filesList = Arrays.asList(prodDir.listFiles());

            filesList.forEach(file -> {
                File renamedFile = new File(CommonUtil.renameFileToDateTimestamp(file.getAbsolutePath()));
                boolean isFileRenamed = file.renameTo(renamedFile);

                if (!isFileRenamed) {
                    LOGGER.debug("Failed to rename file '{}'", file.getAbsolutePath());
                }

                CommonUtil.moveFileToDestDir(renamedFile, avScanProcessDir);
            });
        }


        return null;
    }
}
