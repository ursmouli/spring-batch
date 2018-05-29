package com.experian.saas.client.dataimport.batch.util;

public enum AppStatusEnum {

    ADD_FILE_TO_AV_SCAN_MAP (false),
    FILE_IN_AV_SCAN_PROCESSING (true);

    private boolean status;

    AppStatusEnum(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
    }
}
