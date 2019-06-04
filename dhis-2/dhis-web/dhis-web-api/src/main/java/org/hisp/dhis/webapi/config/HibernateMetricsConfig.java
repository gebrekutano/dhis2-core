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

package org.hisp.dhis.webapi.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jpa.HibernateMetrics;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.Map;

/**
 * @author Luciano Fiandesio
 */
@Configuration
public class HibernateMetricsConfig
{
    private static final String ENTITY_MANAGER_FACTORY_SUFFIX = "entityManagerFactory";

    private final MeterRegistry registry;

    public HibernateMetricsConfig( MeterRegistry registry )
    {
        this.registry = registry;
    }

    @Autowired
    public void bindEntityManagerFactoriesToRegistry( Map<String, EntityManagerFactory> entityManagerFactories )
    {
        entityManagerFactories.forEach( this::bindEntityManagerFactoryToRegistry );
    }

    private void bindEntityManagerFactoryToRegistry( String beanName, EntityManagerFactory entityManagerFactory )
    {
        String entityManagerFactoryName = getEntityManagerFactoryName( beanName );
        try
        {
            SessionFactory sessionFactory = entityManagerFactory.unwrap( SessionFactory.class );
            new HibernateMetrics( sessionFactory, entityManagerFactoryName, Collections.emptyList() )
                .bindTo( this.registry );
        }
        catch ( PersistenceException ex )
        {
            ex.printStackTrace();
        }
    }

    /**
     * Get the name of an {@link EntityManagerFactory} based on its
     * {@code beanName}.
     *
     * @param beanName the name of the {@link EntityManagerFactory} bean
     * @return a name for the given entity manager factory
     */
    private String getEntityManagerFactoryName( String beanName )
    {
        if ( beanName.length() > ENTITY_MANAGER_FACTORY_SUFFIX.length()
            && StringUtils.endsWithIgnoreCase( beanName, ENTITY_MANAGER_FACTORY_SUFFIX ) )
        {
            return beanName.substring( 0, beanName.length() - ENTITY_MANAGER_FACTORY_SUFFIX.length() );
        }
        return beanName;
    }

}
