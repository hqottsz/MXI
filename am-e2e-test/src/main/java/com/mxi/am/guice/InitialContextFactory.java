
package com.mxi.am.guice;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Creates an {@linkplain InitialContext} for the e2e enviornment under test.
 */
@Singleton
public class InitialContextFactory {

   /**
    * Creates a new {@linkplain com.mxi.am.guice.InitialContextFactory} object.
    */
   public InitialContext getIntance() {

      InitialContext lInitialContext;
      try {
         Properties lProperties = new Properties();
         lProperties.put( Context.INITIAL_CONTEXT_FACTORY,
               "weblogic.jndi.WLInitialContextFactory" );
         try {
            lProperties.put( Context.PROVIDER_URL,
                  "t3://".concat( InetAddress.getLocalHost().getHostName() ).concat( ":7001" ) );
         } catch ( UnknownHostException e ) {
            throw new RuntimeException( "Cannot find local host name." );
         }

         lProperties.put( Context.SECURITY_PRINCIPAL, "system" );
         lProperties.put( Context.SECURITY_CREDENTIALS, "password1" );
         lInitialContext = new InitialContext( lProperties );
      } catch ( NamingException e ) {
         throw new RuntimeException( "Failed to initalize JNDI naming context.", e );
      }
      return lInitialContext;

   }
}
