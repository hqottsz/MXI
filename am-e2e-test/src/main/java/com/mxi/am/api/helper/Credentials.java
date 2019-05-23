package com.mxi.am.api.helper;

/**
 * A collection of username and password pairs used to authenticate during e2e testing
 *
 */

public enum Credentials {

   AUTHENTICATED( "mxi", "password" ), AUTHORIZED( "mxi", "password" ),
   UNAUTHENTICATED( "invalidUser", "password" ), UNAUTHORIZED( "notavailable", "password" );

   private final String iUserName;
   private final String iPassword;


   Credentials(String aUserName, String aPassword) {
      iUserName = aUserName;
      iPassword = aPassword;
   }


   public String getUserName() {
      return iUserName;
   }


   public String getPassword() {
      return iPassword;
   }
}
