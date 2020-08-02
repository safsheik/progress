package com.progrexion.bcm.common.jobs.enums;

public enum JobNameEnum {


    RENTTRACK_REPORTED_ORDERS(true),
	
	;

    private boolean asyncDefault;

    private JobNameEnum(boolean asyncDefault) {
        this.asyncDefault = asyncDefault;
    }

    public boolean getAsyncDefault() {
        return this.asyncDefault;
    }
}