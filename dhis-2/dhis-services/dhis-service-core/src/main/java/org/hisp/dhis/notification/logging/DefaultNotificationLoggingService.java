package org.hisp.dhis.notification.logging;

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

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by zubair@dhis2.org on 10.01.18.
 */

@Transactional
@Service( "org.hisp.dhis.notification.logging.NotificationLoggingService" )
public class DefaultNotificationLoggingService
    implements NotificationLoggingService
{
    private final NotificationLoggingStore notificationLoggingStore;

    public DefaultNotificationLoggingService( NotificationLoggingStore notificationLoggingStore )
    {
        checkNotNull( notificationLoggingStore );

        this.notificationLoggingStore = notificationLoggingStore;
    }

    @Override
    public ExternalNotificationLogEntry get(String uid )
    {
        return notificationLoggingStore.getByUid( uid );
    }

    @Override
    public ExternalNotificationLogEntry get( int id )
    {
        return notificationLoggingStore.get( id );
    }

    @Override
    public ExternalNotificationLogEntry getByKey( String key )
    {
        return notificationLoggingStore.getByKey( key );
    }

    @Override
    public List<ExternalNotificationLogEntry> getAllLogEntries()
    {
        return notificationLoggingStore.getAll();
    }

    @Override
    public boolean isValidForSending( String key )
    {
        ExternalNotificationLogEntry logEntry = getByKey( key );

        return logEntry == null || logEntry.isAllowMultiple();
    }

    @Override
    public ExternalNotificationLogEntry getByTemplateUid( String templateUid )
    {
        return notificationLoggingStore.getByTemplateUid( templateUid );
    }

    @Override
    public void save( ExternalNotificationLogEntry entry )
    {
        notificationLoggingStore.save( entry );
    }

    @Override
    public void update( ExternalNotificationLogEntry entry )
    {
        notificationLoggingStore.update( entry );
    }
}
