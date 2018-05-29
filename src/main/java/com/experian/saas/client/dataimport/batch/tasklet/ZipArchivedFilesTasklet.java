package com.experian.saas.client.dataimport.batch.tasklet;

import com.experian.saas.client.dataimport.batch.components.ConfigParameters;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class ZipArchivedFilesTasklet implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipArchivedFilesTasklet.class);

    @Autowired
    private ConfigParameters configParameters;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        File archiveDir = new File(configParameters.getArchivePath());

        if (archiveDir.isDirectory()) {
            File[] allDirs = archiveDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return dir.isDirectory();
                }
            });

            for (File dir : allDirs) {
                boolean delDir = false;
                File[] allFiles = dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".csv");
                    }
                });

                if (allFiles != null && allFiles.length > 0) {
                    ZipFile zipFile = new ZipFile(configParameters.getZipArchivePath() + File.separator + dir.getName() + ".zip");
                    try {
                        ArrayList<File> fileToAdd = new ArrayList<>();
                        fileToAdd.addAll(Arrays.asList(allFiles));
                        zipFile.addFiles(fileToAdd, getZipParameters());

                        delDir = true;
                    } catch (ZipException e) {
                        LOGGER.error("Error preparing zip file", e);
                    }
                } else {
                    LOGGER.warn("no files to zip in '{}'", dir.getAbsolutePath());
                }

                if (delDir) {
                    for (File file : allFiles) {
                        file.delete();
                    }
                    dir.delete();
                }
            }
        }

        return null;
    }

    private ZipParameters getZipParameters() {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to store compression

        // Set the compression level
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Set the encryption flag to true
        // If this is set to false, then the rest of encryption properties are ignored
        zipParameters.setEncryptFiles(true);

        // Set the encryption method to Standard Zip Encryption
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

        // Set password
        zipParameters.setPassword(configParameters.getZipArchivePassword());

        return zipParameters;
    }
}
