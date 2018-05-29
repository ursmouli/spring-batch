package com.experian.saas.client.dataimport.batch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);

    private static ConcurrentHashMap<String, Boolean> CURR_AV_SCAN_FILES_MAP = new ConcurrentHashMap<>();

    private static final int MAP_MAX_SIZE = 100_000;

    private ResourceUtil() {}

    public static Resource[] readResources(String stagingDirectory, String pattern) {
        ResourceArrayPropertyEditor resourceLoader = new ResourceArrayPropertyEditor();
        resourceLoader.setAsText("file://" + stagingDirectory + "/" + pattern + "*.csv");
        Resource[] resources = (Resource[]) resourceLoader.getValue();
        return resources;
    }

    /**
     *
     * @param fileAbsolutePath is fi
     * @return
     */
    public static void addOrUpdateFileToCurrAVScanMap(final String fileAbsolutePath, final boolean status) {
        if (!StringUtils.isEmpty(fileAbsolutePath) && CURR_AV_SCAN_FILES_MAP.size() <= MAP_MAX_SIZE) {
            if (CURR_AV_SCAN_FILES_MAP.containsKey(fileAbsolutePath)) {
                LOGGER.info("File '{}' is already in AV scanned map .. updating with status {}", fileAbsolutePath, status);
                CURR_AV_SCAN_FILES_MAP.put(fileAbsolutePath, status);
            } else {
                CURR_AV_SCAN_FILES_MAP.put(fileAbsolutePath, status);
            }
        }
    }

    public static boolean isFileInAVScanProcessing(final String fileAbsolutePath) {
        boolean result = false;
        if (CURR_AV_SCAN_FILES_MAP.containsKey(fileAbsolutePath)) {
            result = CURR_AV_SCAN_FILES_MAP.get(fileAbsolutePath);
        } else {
            LOGGER.warn("File '{}' not in AV scan map", fileAbsolutePath);
        }
        return result;
    }

    public static boolean isFileInAVScanMap(final String fileAbsolutePath) {
        return CURR_AV_SCAN_FILES_MAP.containsKey(fileAbsolutePath);
    }

    public static void deleteFileInAVScanMap(final String fileAbsolutePath) {
        if (CURR_AV_SCAN_FILES_MAP.remove(fileAbsolutePath)) {
            LOGGER.debug("Removed file '{}' from AV scan list", fileAbsolutePath);
        } else {
            LOGGER.warn("File '{}' doesn't exist in AV scan list", fileAbsolutePath);
        }
    }

    public static ConcurrentHashMap<String, Boolean> getCurrentAVScanMap() {
        return CURR_AV_SCAN_FILES_MAP;
    }

}
