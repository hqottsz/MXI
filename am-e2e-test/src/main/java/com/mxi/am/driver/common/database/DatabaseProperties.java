
package com.mxi.am.driver.common.database;

/**
 * The database properties
 */
public final class DatabaseProperties {

   private static String iUsername;
   private static String iPassword;
   private static String iServer;
   private static Integer iPort;
   private static String iSid;


   public DatabaseProperties(String aUsername, String aPassword, String aServer, Integer aPort,
         String aSid) {
      iUsername = aUsername;
      iPassword = aPassword;
      iServer = aServer;
      iPort = aPort;
      iSid = aSid;
   }


   public String getUsername() {
      return iUsername;
   }


   public String getPassword() {
      return iPassword;
   }


   public String getConnectString() {
      return String.format( "//%s:%d/%s", iServer, iPort, iSid );
   }


   public String getJdbcConnectString() {
      return String.format( "jdbc:oracle:thin:@%s", getConnectString() );
   }


   @Override
   public String toString() {
      return String.format( "%s:%s@%s", getUsername(), getPassword(), getConnectString() );
   }
}
