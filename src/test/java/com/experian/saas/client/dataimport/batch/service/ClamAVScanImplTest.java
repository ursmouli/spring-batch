package com.experian.saas.client.dataimport.batch.service;

import com.experian.saas.client.dataimport.batch.config.AVConfig;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by chrisng on 5/24/2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClamAVScanImpl.class)
public class ClamAVScanImplTest {

    private File sampleFile1;
    private boolean clamAVInstalled = false;

    @Mock
    AVConfig avConfig;

    @Before
    public void setUp() throws Exception {

        File clamAV = new File("c:/ClamAV/clamscan.exe");
        clamAVInstalled =  clamAV.exists();

        if (clamAVInstalled) {
            String currentClassPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "samplefile1.txt";

            try (OutputStream os = new FileOutputStream(currentClassPath);) {
                os.write("This is a antivirus to scan file sample...".getBytes("UTF-8"));
                os.close();
            }

            sampleFile1 = new File(currentClassPath);

            when(avConfig.getPredefinedArguments()).thenReturn(Lists.newArrayList("c:/ClamAV/clamscan.exe", "--infected"));
            when(avConfig.getTimeToWait()).thenReturn(3l);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void scanFileWithAntivirusTest() {

        if (clamAVInstalled) {
            try {
                ClamAVScanImpl clamAVScan = Mockito.spy(ClamAVScanImpl.class);
                clamAVScan.setAvConfig(avConfig);

                int exitCode = clamAVScan.scanFileWithAntivirus(sampleFile1);

                assertEquals(0, exitCode);

            } catch (InterruptedException ie) {
                fail("interrupted exception!");
            } catch (IOException ioe) {
                fail("io exception!");
            }
        }
    }
}