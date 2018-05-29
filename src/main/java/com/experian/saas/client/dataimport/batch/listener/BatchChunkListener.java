package com.experian.saas.client.dataimport.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class BatchChunkListener implements ChunkListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchChunkListener.class);

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        LOGGER.info("beforeChunk : {}", chunkContext.getStepContext().getJobName());
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        LOGGER.info("afterChunk : {}, complete : {}", chunkContext.getStepContext().getJobName(), chunkContext.isComplete());
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        LOGGER.info("afterChunkError : {}", chunkContext.toString());
    }
}
