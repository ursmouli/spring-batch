package com.experian.saas.client.dataimport.batch.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigParameters {

    @Value("${csv.sts.landing.path}")
    private String stsLandingDirPath;

    @Value("${csv.file.process.path}")
    private String fileProcessDirPath;

    @Value("${csv.virus.affected.path}")
    private String virusAffectedDirPath;

    @Value("${csv.archive.path}")
    private String archivePath;

    @Value("${zip.archive.password}")
    private String zipArchivePassword;

    @Value("${csv.zip.archive.path}")
    private String zipArchivePath;

    public String getStsLandingDirPath() {
        return stsLandingDirPath;
    }

    public String getFileProcessDirPath() {
        return fileProcessDirPath;
    }

    public String getVirusAffectedDirPath() {
        return virusAffectedDirPath;
    }

    public String getArchivePath() {
        return archivePath;
    }

    public String getZipArchivePassword() {
        return zipArchivePassword;
    }

    public String getZipArchivePath() {
        return zipArchivePath;
    }
}
