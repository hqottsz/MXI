package com.mxi.am.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;

import com.mxi.am.db.connection.executor.DatabaseExecutable;
import com.mxi.mx.common.license.MxLicenseFeatureValidator;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.common.services.dao.MxBatchDataAccess;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.unittest.MxBatchUnittestDao;
import com.mxi.mx.core.unittest.MxUnittestDao;
import com.mxi.mx.core.unittest.MxUnittestQao;
import com.mxi.mx.db.connection.DatabaseProperties;
import com.mxi.mx.db.connection.DatabasePropertiesReader;
import com.mxi.mx.testing.LicenseFeatureValidatorStub;


/**
 * The connection rule.
 */
public final class DatabaseConnectionRule extends ExternalResource {

   private final DatabaseProperties iDatabaseProperties;
   private static Connection sConnection;
   private boolean iRollbackEnabled = true;
   private DatabaseExecutable iBeforeDatabaseExecutable;
   private DatabaseExecutable iAfterDatabaseExecutable;

   // fetch the database connection details on initialization
   private final static DatabaseProperties sDefaultDatabaseProperties =
         DatabasePropertiesReader.get();


   public DatabaseConnectionRule() {
      this( sDefaultDatabaseProperties );
   }


   /**
    * Constructor to instantiate iRunnable. Creates a new {@linkplain DatabaseConnectionRule}
    * object.
    *
    * @param aRunnable
    *           : Holds the DDL statement of enabling the trigger
    */
   public DatabaseConnectionRule(DatabaseExecutable aBeforeDatabaseExecuteable,
         DatabaseExecutable aAfterDatabaseExecutable) {
      this( sDefaultDatabaseProperties );
      iBeforeDatabaseExecutable = aBeforeDatabaseExecuteable;
      iAfterDatabaseExecutable = aAfterDatabaseExecutable;
   }


   public DatabaseConnectionRule(DatabaseProperties aDatabaseProperties) {
      iDatabaseProperties = aDatabaseProperties;
   }


   /**
    * Override to set up your specific external resource.
    *
    * @throws if
    *            setup fails (which will disable {@code after}
    */
   @Override
   protected void before() throws Throwable {
      // only initialize the connection once to prevent connections being opened over and over for
      // every test case
      if ( sConnection == null ) {
         // Load Driver
         Class.forName( "oracle.jdbc.OracleDriver" );

         // Create connection
         sConnection = DriverManager.getConnection( iDatabaseProperties.getJdbcConnectString(),
               iDatabaseProperties.getUsername(), iDatabaseProperties.getPassword() );
      }

      // Disable auto commit
      sConnection.setAutoCommit( false );

      MxLicenseFeatureValidator.setValidator( new LicenseFeatureValidatorStub() );
      QuerySetFactory.setInstance( new MxUnittestQao( sConnection ) );
      QuerySetFactory.setMutableInstance( new MxUnittestQao( sConnection ) );
      MxDataAccess.setInstance( new MxUnittestDao( sConnection ) );
      MxBatchDataAccess.setInstance( new MxBatchUnittestDao( sConnection ) );

      // Reset this service since it has dependencies that have stale references to the qao
      StoredProcedureCall.setInstance( null );

      if ( iBeforeDatabaseExecutable != null ) {
         iBeforeDatabaseExecutable.execute();
      }
   }


   public Connection getConnection() {
      return sConnection;
   }


   /**
    * If a connection is opened at the beginning of a unit test class (via the {@link ClassRule}
    * annotation), this method can be called at the end of an individual test method to rollback a
    * transaction started by that method. Useful if you're using different data sets between test
    * methods.
    *
    * @throws SQLException
    *            If the transaction can't be rolled back.
    */
   public void rollbackCurrentTransaction() throws SQLException {
      if ( isRollbackEnabled() ) {
         sConnection.rollback();
      }
   }


   /**
    * Override to tear down your specific external resource.
    */
   @Override
   protected void after() {
      try {
         rollbackCurrentTransaction();
         if ( iAfterDatabaseExecutable != null ) {
            MxDataAccess.setInstance( new MxUnittestDao( sConnection ) );
            iAfterDatabaseExecutable.execute();
         }

      } catch ( SQLException e ) {
         throw new RuntimeException( "Could not rollback transaction" );
      } catch ( Exception e ) {
         throw new RuntimeException( "Unexpected error occured. ", e );
      } finally {
         MxDataAccess.setInstance( null );
         QuerySetFactory.setMutableInstance( null );
         QuerySetFactory.setInstance( null );
         MxLicenseFeatureValidator.setValidator( null );
         StoredProcedureCall.setInstance( null );

      }
   }


   /**
    * Returns the value of the rollbackEnabled property.
    *
    * @return the value of the rollbackEnabled property
    */
   public boolean isRollbackEnabled() {
      return iRollbackEnabled;
   }


   /**
    * Sets a new value for the rollbackEnabled property.
    *
    * @param aRollbackEnabled
    *           the new value for the rollbackEnabled property
    */
   public void setRollbackEnabled( boolean aRollbackEnabled ) {
      iRollbackEnabled = aRollbackEnabled;
   }
}
