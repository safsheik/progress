package com.progrexion.bcm.common.jobs.enums;

/**
 * Events in the job lifecycle.
 *
 * <p>The jobStatus is the status if the this JobEventType is the latest event for the job.</p>
 */
public enum JobEventTypeEnum {

    /** the job has been created, but not started */
    CREATED(JobStatusEnum.NOT_RUNNING),

    /** the job has been started, but is not yet completed */
    STARTED(JobStatusEnum.PROCESSING),

    /** the job has already been running, but is not yet completed */
    ALREADY_RUNNING(JobStatusEnum.ALREADY_RUNNING),

    /** the job completed successfully */
    COMPLETED(JobStatusEnum.COMPLETE),

    /** an interrupt has been registered for the job, but the job has not yet processed the interrupt (the job will soon stop). */
    INTERRUPT_REQUESTED(JobStatusEnum.STOPPING),

    /** the job has exited because of interrupt */
    INTERRUPTED(JobStatusEnum.INTERRUPTED),

    /** the job did not complete because of an error */
    ERRORED(JobStatusEnum.ERROR),
	
	 /** Order Process Submitted for all the customers */
	PROCESS_SUBMITTED(JobStatusEnum.PROCESS_SUBMITTED);

    /** This is the status of the this JobEventType if this is the latest event for the job */
    private JobStatusEnum jobStatus;

    private JobEventTypeEnum(JobStatusEnum jobStatus) {
        this.jobStatus = jobStatus;
    }

    public JobStatusEnum getJobStatus() {
        return this.jobStatus;
    }

}
