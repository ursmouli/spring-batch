package com.experian.saas.client.dataimport.batch.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
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
}
