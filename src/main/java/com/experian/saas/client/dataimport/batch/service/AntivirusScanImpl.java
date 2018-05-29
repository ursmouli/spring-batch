package com.experian.saas.client.dataimport.batch.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("antivirusScan")
public class AntivirusScanImpl implements AntivirusScan {
    @Override
    public boolean isFileAntivirusScanned(Resource resource) {
        return true;
    }

    @Override
    public int scanFileWithAntivirus(File file) {
        return 0;
    }
}
