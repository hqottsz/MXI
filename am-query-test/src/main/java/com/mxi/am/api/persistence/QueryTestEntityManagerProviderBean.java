package com.mxi.am.api.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_DRIVER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_URL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mxi.mx.db.connection.DatabaseProperties;
import com.mxi.mx.db.connection.DatabasePropertiesReader;
import com.mxi.mx.db.jpa.TestEntityManagerProvider;


/**
 * A provider of the entity manager used by this component.
 */
public class QueryTestEntityManagerProviderBean implements TestEntityManagerProvider {

   private EntityManager iEntityManager;

   static EntityManagerFactory iEntityManagerFactory;


   /**
    * {@inheritDoc}
    */
   @Override
   public EntityManager get() {
      if ( iEntityManager == null ) {
         Map<String, String> lProperties = new HashMap<String, String>();

         // Get the connection properties
         DatabaseProperties lDatabaseProperties = DatabasePropertiesReader.get();

         // Configure the internal EclipseLink connection pool
         lProperties.put( JDBC_DRIVER, "oracle.jdbc.OracleDriver" );
         lProperties.put( JDBC_URL, lDatabaseProperties.getJdbcConnectString() );
         lProperties.put( JDBC_USER, lDatabaseProperties.getUsername() );
         lProperties.put( JDBC_PASSWORD, lDatabaseProperties.getPassword() );

         iEntityManagerFactory =
               Persistence.createEntityManagerFactory( "am-query-test", lProperties );
         iEntityManager = iEntityManagerFactory.createEntityManager();
      }
      return iEntityManager;
   }

}
