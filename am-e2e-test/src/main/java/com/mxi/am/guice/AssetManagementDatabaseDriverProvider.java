package com.mxi.am.guice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Provider;

import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.DatabaseProperties;
import com.mxi.am.driver.common.database.DatabasePropertiesReader;

import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class AssetManagementDatabaseDriverProvider implements Provider<DatabaseDriver> {

   private DatabaseDriver iDatabaseDriver;


   public AssetManagementDatabaseDriverProvider() throws SQLException, ClassNotFoundException {
      DatabaseProperties lProperties = DatabasePropertiesReader.get();

      // Load the JDBC driver class
      Class.forName( "oracle.jdbc.OracleDriver" );

      // Get a JDBC connection
      Connection lConnection = DriverManager.getConnection( lProperties.getJdbcConnectString(),
            lProperties.getUsername(), lProperties.getPassword() );

      // Enable auto commit
      lConnection.setAutoCommit( true );

      iDatabaseDriver = new DatabaseDriver( lConnection );
   }


   @Override
   public DatabaseDriver get() {
      return iDatabaseDriver;
   }
}
