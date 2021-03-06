package org.hisp.dhis.scheduling;

/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import org.hisp.dhis.scheduling.parameters.AnalyticsJobParameters;
import org.hisp.dhis.scheduling.parameters.EventProgramsDataSynchronizationJobParameters;
import org.hisp.dhis.scheduling.parameters.MetadataSyncJobParameters;
import org.hisp.dhis.scheduling.parameters.MockJobParameters;
import org.hisp.dhis.scheduling.parameters.MonitoringJobParameters;
import org.hisp.dhis.scheduling.parameters.PredictorJobParameters;
import org.hisp.dhis.scheduling.parameters.PushAnalysisJobParameters;
import org.hisp.dhis.scheduling.parameters.SmsJobParameters;
import org.hisp.dhis.scheduling.parameters.TrackerProgramsDataSynchronizationJobParameters;

/**
 * Enum describing the different jobs in the system.
 * Each job has a key, class, configurable status and possibly a map containing relative endpoints for possible parameters.
 * <p>
 * The key must match the jobs bean name so that the {@link SchedulingManager} can fetch the correct job
 *
 * @author Henning Håkonsen
 */
public enum JobType
{
    DATA_STATISTICS( "dataStatisticsJob", false ),
    DATA_INTEGRITY( "dataIntegrityJob", true ),
    RESOURCE_TABLE( "resourceTableJob", true ),
    ANALYTICS_TABLE( "analyticsTableJob", true, AnalyticsJobParameters.class, ImmutableMap.of(
        "skipTableTypes", "/api/analytics/tableTypes"  ) ),
    DATA_SYNC( "dataSyncJob", true ),
    TRACKER_PROGRAMS_DATA_SYNC( "trackerProgramsDataSyncJob", true, TrackerProgramsDataSynchronizationJobParameters.class, null ),
    EVENT_PROGRAMS_DATA_SYNC( "eventProgramsDataSyncJob", true, EventProgramsDataSynchronizationJobParameters.class, null ),
    FILE_RESOURCE_CLEANUP( "fileResourceCleanUpJob", false ),
    IMAGE_PROCESSING( "imageProcessingJob", false ),
    META_DATA_SYNC( "metadataSyncJob", true, MetadataSyncJobParameters.class, null ),
    SMS_SEND( "sendSmsJob", false, SmsJobParameters.class, null ),
    SEND_SCHEDULED_MESSAGE( "sendScheduledMessageJob", true ),
    PROGRAM_NOTIFICATIONS( "programNotificationsJob", true ),
    VALIDATION_RESULTS_NOTIFICATION( "validationResultNotificationJob", false ),
    CREDENTIALS_EXPIRY_ALERT( "credentialsExpiryAlertJob", false ),
    MONITORING( "monitoringJob", true, MonitoringJobParameters.class, ImmutableMap.of(
        "relativePeriods", "/api/periodTypes/relativePeriodTypes", "validationRuleGroups", "/api/validationRuleGroups" ) ),
    PUSH_ANALYSIS( "pushAnalysisJob", true, PushAnalysisJobParameters.class, ImmutableMap.of(
        "pushAnalysis", "/api/pushAnalysis"  ) ),
    PREDICTOR( "predictorJob", true, PredictorJobParameters.class, ImmutableMap.of(
        "predictors", "/api/predictors", "predictorGroups", "/api/predictorGroups" ) ),
    DATA_SET_NOTIFICATION( "dataSetNotificationJob", false ),
    REMOVE_EXPIRED_RESERVED_VALUES( "removeExpiredReservedValuesJob", false ),

    // Testing purposes
    MOCK( "mockJob", false, MockJobParameters.class, null ),

    // Deprecated, present to satisfy code using the old enumeration TaskCategory
    DATAVALUE_IMPORT( null, false, null, null ),
    ANALYTICSTABLE_UPDATE( null, false, null, null ),
    METADATA_IMPORT( null, false, null, null ),
    GML_IMPORT( null, false, null, null ),
    DATAVALUE_IMPORT_INTERNAL( null, false, null, null ),
    EVENT_IMPORT( null, false, null, null ),
    ENROLLMENT_IMPORT( null, false, null, null ),
    TEI_IMPORT( null, false, null, null ),
    LEADER_ELECTION( "leaderElectionJob", false, null, null ),
    LEADER_RENEWAL( "leaderRenewalJob", false, null, null ),
    COMPLETE_DATA_SET_REGISTRATION_IMPORT( null, false, null, null );

    private final String key;

    private final boolean configurable;

    private final Class<? extends JobParameters> jobParameters;

    private final Map<String, String> relativeApiElements;

    JobType( String key, boolean configurable )
    {
        this( key, configurable, null, null );
    }

    JobType( String key, boolean configurable, Class<? extends JobParameters> jobParameters,
        Map<String, String> relativeApiElements )
    {
        this.key = key;
        this.configurable = configurable;
        this.jobParameters = jobParameters;
        this.relativeApiElements = relativeApiElements;
    }

    public String getKey()
    {
        return key;
    }

    public Class<? extends JobParameters> getJobParameters()
    {
        return jobParameters;
    }

    public boolean isConfigurable()
    {
        return configurable;
    }

    public Map<String, String> getRelativeApiElements()
    {
        return relativeApiElements;
    }
}
