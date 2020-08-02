package com.progrexion.bcm.common.jobs.enums;

import java.util.EnumSet;

public enum OrderProcessStatusEnum {

	PENDING(0),
    PROCESSING(1),
    FAILED(2),
    ERROR(3),
    COMPLETED(4),
    ;
    private int processStatus;

    private OrderProcessStatusEnum(int processStatus) {
        this.processStatus = processStatus;
    }

    public int getProcessStatus() {
        return this.processStatus;
    }
    
    public static EnumSet<OrderProcessStatusEnum> getOrderProcessStates() {
        return EnumSet.of(PENDING, PROCESSING);
    }
}
