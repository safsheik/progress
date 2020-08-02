package com.progrexion.bcm.common.jobs.enums;

import java.util.EnumSet;

public enum JobStatusEnum {

    /** No such job exists */
    DOESNT_EXIST("Doesn't Exist"),

    /** The job has been created but not started */
    NOT_RUNNING("Not Running"),

    /** The job has been started and is currently processing */
    PROCESSING("Processing"),

    /** The job has completed normally */
    COMPLETE("Complete"),

    /** The job has been requested to stop */
    STOPPING("Stopping"),

    /** The job is not running and was interrupted before normal completion */
    INTERRUPTED("Interrupted"),

    /** The job is not running and encountered a fatal error while running */
    ERROR("Error"),

    /** The job is already running*/
    ALREADY_RUNNING("Already Running"),
	
    /** Order Process Submitted for all the customers */
	PROCESS_SUBMITTED("PROCESS SUBMITTED");

    /** human-readable string */
    private String description;

    private JobStatusEnum(String hrString) {
        this.description = hrString;
    }

    public String getDescription() {
        return this.description;
    }

    public static EnumSet<JobStatusEnum> getTerminalStates() {
        return EnumSet.of(COMPLETE, INTERRUPTED, ERROR);
    }
    
    public static EnumSet<JobStatusEnum> getProcessingStates() {
        return EnumSet.of(PROCESSING, PROCESS_SUBMITTED);
    }
}
