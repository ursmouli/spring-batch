package com.experian.saas.client.dataimport.batch.service;

import com.experian.saas.client.dataimport.batch.config.AVConfig;
import com.experian.saas.client.dataimport.batch.util.AppStatusEnum;
import com.experian.saas.client.dataimport.batch.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Service("antivirusScan")
public class AntivirusScanImpl implements AntivirusScan {

    private static final Logger LOGGER = LoggerFactory.getLogger(AntivirusScanImpl.class);

    @Autowired
    AVConfig avConfig;

    @Override
    public boolean isFileAntivirusScanned(Resource resource) {
        return true;
    }

    @Override
    public int scanFileWithAntivirus(File file) {
        ResourceUtil.addOrUpdateFileToCurrAVScanMap(file.getAbsolutePath(), AppStatusEnum.FILE_IN_AV_SCAN_PROCESSING.getStatus());
        try {
            //runCmd();
            LOGGER.info("Waking after wait .. '{}'", file.getName());
        } catch (Exception e) {
            LOGGER.error("Interrupt exception:", e);
        } finally {
            ResourceUtil.deleteFileInAVScanMap(file.getAbsolutePath());
        }
        return 0;
    }

    private static void runCmd() throws Exception {
        File script = new File("C:\\powercurve\\clientdataimport\\scan.bat");
        ProcessBuilder processBuilder = new ProcessBuilder(script.getAbsolutePath(), "");
        processBuilder.directory(new File("C:\\powercurve\\clientdataimport"));

        File log = new File("C:/workspace/log.txt");
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        Process process = processBuilder.start();

        process.waitFor();
        if (process.waitFor(3, TimeUnit.MINUTES)) {

        }
    }

    public static void main(String[] args) throws Exception {
        runCmd();
    }
}
