/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 * 
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
 * 
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */
package com.mxi.mx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


/**
 * Database Manager
 *
 */
public class DBConnectionManager {

   private static DBConnectionManager iInstance;
   private Connection iConnection;
   protected String iPassword;
   protected String iPort;
   protected String iServer;
   protected String iSid;
   protected String iUsername;
   private Properties iProperties;


   private DBConnectionManager() {

   }


   static DBConnectionManager getInstance() {

      if ( iInstance == null ) {
         iInstance = new DBConnectionManager();
      }

      return iInstance;
   }


   public void openConnection() throws Exception {

      loadProperties();

      String iConnectionString = "jdbc:oracle:thin:@" + iServer + ":" + iPort + ":" + iSid;

      iConnection = DriverManager.getConnection( iConnectionString, iUsername, iPassword );
      iConnection.setAutoCommit( false );

   }


   private void loadProperties() throws Exception {

      if ( iProperties == null ) {
         iProperties = PropertyLoader.getActualsLoaderProperties();
      }

      iServer = iProperties.getProperty( "db.server" );
      iPort = iProperties.getProperty( "db.port" );
      iSid = iProperties.getProperty( "db.sid" );
      iUsername = iProperties.getProperty( "db.user" );
      iPassword = iProperties.getProperty( "db.password" );
   }


   public void closeConnection() throws Exception {
      if ( getConnection() != null ) {
         getConnection().rollback();
         getConnection().close();
      }
   }


   public Connection getConnection() {
      return iConnection;
   }

}
