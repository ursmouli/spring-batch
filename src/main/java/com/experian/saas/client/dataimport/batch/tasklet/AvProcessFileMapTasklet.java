package com.experian.saas.client.dataimport.batch.tasklet;

import com.experian.saas.client.dataimport.batch.components.ConfigParameters;
import com.experian.saas.client.dataimport.batch.util.AppStatusEnum;
import com.experian.saas.client.dataimport.batch.util.ResourceUtil;
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
public class AvProcessFileMapTasklet implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvProcessFileMapTasklet.class);

    @Autowired
    private ConfigParameters configParameters;

    @Override
    public synchronized RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        File avScanProcessDir = new File(configParameters.getAvScanProcessDirPath());

        if (avScanProcessDir.isDirectory()) {
            List<File> fileList = Arrays.asList(avScanProcessDir.listFiles());

            fileList.forEach(file -> {
                if (!ResourceUtil.isFileInAVScanMap(file.getAbsolutePath())) {
                    ResourceUtil.addOrUpdateFileToCurrAVScanMap(file.getAbsolutePath(), AppStatusEnum.ADD_FILE_TO_AV_SCAN_MAP.getStatus());
                }
            });
        }

        return null;
    }
}
