package com.experian.saas.client.dataimport.batch.logger;


import com.experian.saas.client.dataimport.batch.model.BaseResourceAware;
import com.experian.saas.client.dataimport.batch.util.CommonUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

public class ResourceLoggingItemWriter<T extends BaseResourceAware> implements ItemWriter<T>, ItemStream, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoggingItemWriter.class);

    private ItemWriter < T > delegate;

    private Resource currentInputResource;
    private String archivePath;

    public ResourceLoggingItemWriter(ItemWriter<T> delegate, String archivePath) {
        this.delegate = delegate;
        this.archivePath = archivePath;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        delegate.write(items);

        for (T item : items) {
            Resource resource = item.getResource();

            if (currentInputResource != null && resource != currentInputResource) {
                finishedProcessingInput(currentInputResource);
            }

            currentInputResource = resource;
        }

    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream)delegate).open(executionContext);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream)delegate).update(executionContext);
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (delegate instanceof ItemStream) {
            ((ItemStream)delegate).close();
        }

        if (currentInputResource != null) {
            finishedProcessingInput(currentInputResource);
            currentInputResource = null;
        }
    }

    private void finishedProcessingInput(Resource resource) {
        LOGGER.info("Finished writing all items from {}", resource);
        LOGGER.info("Archive current resource and setting current resource to null after archiving");
        try {
            String destPath = archivePath + File.separator + CommonUtil.convertCurrentDateToString();
            File destDir = new File(destPath);
            FileUtils.moveFileToDirectory(resource.getFile(), destDir, true);
        } catch (Exception e) {
            LOGGER.error("failed to move/create dir : '{}' for file '{}'", archivePath, resource.getFilename(), e);
        } finally {
            resource = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (delegate instanceof InitializingBean) {
            ((InitializingBean)delegate).afterPropertiesSet();
        }
    }
}
