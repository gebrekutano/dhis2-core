package org.hisp.dhis.artemis.audit.listener;

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

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.hisp.dhis.artemis.audit.Audit;
import org.hisp.dhis.artemis.audit.AuditManager;
import org.hisp.dhis.artemis.audit.legacy.AuditLegacyObjectFactory;
import org.hisp.dhis.artemis.config.UserNameSupplier;
import org.hisp.dhis.audit.AuditType;
import org.hisp.dhis.common.IdentifiableObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Luciano Fiandesio
 */
@Component
public class PostDeleteAuditListener
    extends AbstractHibernateListener implements PostDeleteEventListener
{
    public PostDeleteAuditListener(
        AuditManager auditManager,
        AuditLegacyObjectFactory auditLegacyObjectFactory,
        UserNameSupplier userNameSupplier )
    {
        super( auditManager, auditLegacyObjectFactory, userNameSupplier );
    }

    @Override
    public boolean requiresPostCommitHanding( EntityPersister entityPersister )
    {
        return false;
    }

    @Override
    public void onPostDelete( PostDeleteEvent postDeleteEvent )
    {
        Object entity = postDeleteEvent.getEntity();

        getAuditingScope( entity ).ifPresent( scope -> {
            IdentifiableObject io = (IdentifiableObject) entity;

            auditManager.send( Audit.builder().withAuditType( AuditType.DELETE )
                .withAuditScope( scope )
                .withCreatedAt( new Date() )
                .withCreatedBy( getCreatedBy() )
                .withObject( entity )
                .withData( this.legacyObjectFactory.create( scope, AuditType.DELETE, io, getCreatedBy() ) )
                .build() );
        } );
    }
}
