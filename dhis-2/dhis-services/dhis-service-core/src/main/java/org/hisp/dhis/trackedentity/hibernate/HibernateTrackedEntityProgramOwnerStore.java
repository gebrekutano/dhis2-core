package org.hisp.dhis.trackedentity.hibernate;

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

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.trackedentity.TrackedEntityProgramOwner;
import org.hisp.dhis.trackedentity.TrackedEntityProgramOwnerStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Ameen Mohamed
 */
@Repository( "org.hisp.dhis.trackedentity.TrackedEntityProgramOwnerStore" )
public class HibernateTrackedEntityProgramOwnerStore extends HibernateGenericStore<TrackedEntityProgramOwner> implements TrackedEntityProgramOwnerStore
{
    public HibernateTrackedEntityProgramOwnerStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher )
    {
        super( sessionFactory, jdbcTemplate, publisher, TrackedEntityProgramOwner.class, false );
    }

    @Override
    public TrackedEntityProgramOwner getTrackedEntityProgramOwner( long teiId, long programId )
    {
        return getQuery( "from TrackedEntityProgramOwner tepo where tepo.entityInstance.id="
            + teiId + " and tepo.program.id=" + programId ).uniqueResult();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<TrackedEntityProgramOwner> getTrackedEntityProgramOwners( List<Long> teiIds )
    {
        String hql = "from TrackedEntityProgramOwner tepo where tepo.entityInstance.id in (:teiIds)";
        Query q = getQuery( hql );
        q.setParameterList( "teiIds", teiIds );
        return q.list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<TrackedEntityProgramOwner> getTrackedEntityProgramOwners( List<Long> teiIds, long programId )
    {
        String hql = "from TrackedEntityProgramOwner tepo where tepo.entityInstance.id in (:teiIds) and tepo.program.id=(:programId) ";
        Query q = getQuery( hql );
        q.setParameterList( "teiIds", teiIds );
        q.setParameter( "programId", programId );
        return q.list();
    }

}
