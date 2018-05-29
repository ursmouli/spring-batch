package com.experian.saas.client.dataimport.batch.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    private static String ARCHIVE_FOLDER_DATE_FORMAT = "ddMMyyyy";
    private static String IMPORT_FILE_DATE_FORMAT = "MM/dd/yyyy";

    private CommonUtil() {}

    public static String convertCurrentDateToString() {
        DateFormat dateFormat = new SimpleDateFormat(ARCHIVE_FOLDER_DATE_FORMAT);
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public static void moveFileToDir(File file, File destDir) throws IOException {
        FileUtils.moveFileToDirectory(file, destDir, true);
    }

    public static boolean isValidDateString(final String strDate, final String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        boolean result = false;
        try {
            dateFormat.parse(strDate);
            result = true;
        } catch (ParseException e) {
            result = false;
        }
        return result;
    }

    public static String getCurrentDateTimestamp() {
        return new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
    }

    public static void moveFileToDestDir(File file, File destDir) {
        try {
            moveFileToDir(file, destDir);
        } catch (IOException e) {
            LOGGER.error("Failed to move success virus scanned file '{}' to destDir '{}'",
                    file.getAbsolutePath(),
                    destDir.getAbsolutePath(), e);
        }
    }

    public static String renameFileToDateTimestamp(String currFileName) {
        String ext = ".csv";
        if (!StringUtils.isEmpty(currFileName)) {
            int extIndex = currFileName.indexOf(ext);
            String tmpNameWithoutExt = currFileName.substring(0, extIndex);
            System.out.println(tmpNameWithoutExt);
            tmpNameWithoutExt = tmpNameWithoutExt + getCurrentDateTimestamp() + ext;
            return tmpNameWithoutExt;
        } else {
            return currFileName;
        }
    }

    public static void main(String[] args) {
        System.out.println(renameFileToDateTimestamp("C:/path/filename.csv"));
        System.out.println(AppStatusEnum.ADD_FILE_TO_AV_SCAN_MAP.getStatus());
    }
}
