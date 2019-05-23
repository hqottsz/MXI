
package com.mxi.mx.testing;

import javax.naming.Context;
import javax.naming.NamingException;

import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.server.AppServerFactory;


/**
 * Represents an application server for the purpose of unit testing.
 *
 * @author $Author: mlennox $
 * @version $Revision: 1.1 $
 */
public class MxAppServer {

   /**
    * Returns the JNDI initial context of the application server.
    *
    * @return The JNDI initial context
    */
   public static Context getContext() {
      try {
         return AppServerFactory.getInstance().createAppServer().getInitialContext();
      } catch ( NamingException ex ) {
         throw new MxRuntimeException( "Error retrieving initial context", ex );
      }
   }
}
