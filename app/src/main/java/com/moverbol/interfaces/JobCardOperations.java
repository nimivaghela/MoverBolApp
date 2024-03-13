package com.moverbol.interfaces;

/**
 * Created by AkashM on 3/1/18.
 */

public interface JobCardOperations {

    void acceptJob(int adapterPosition, String jobId, String opportunityId);
    void rejectJob(int adapterPosition, String jobId, String opportunityId);
    void showSummary( int adapterPosition, String jobId, String opportunityId);
    void startJob(int adapterPosition, String jobId, String opportunityId, boolean isStorage, boolean isMoveInternational, String moveTypeId, String jobStatus);

    /**
     *
     * @param adapterPosition The adapter position
     * @param addressType delivery or pickup
     */
    void showExtraStops(int adapterPosition, String jobId, String opportunityId, String addressType);

    void showAditionalDates(int adapterPosition, String jobId, String opportunityId, String message);

}
