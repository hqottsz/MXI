
package com.mxi.mx.common.config;

import java.util.HashMap;
import java.util.Map;


/**
 * Stub implementation of the GlobalParameters class. It returns anything the way it was put in.
 */
public class GlobalParametersStub extends GlobalParameters {

   /** Use serialVersionUID for interoperability. */
   private static final long serialVersionUID = 6882966759069008135L;

   private final GlobalParameters iFallbackParameters;

   /** Mapping of Parameters and Values */
   private Map<String, Object> iParameters;


   /**
    * Creates a new GlobalParameters object.
    *
    * @param aParmType
    *           the parameters type
    */
   public GlobalParametersStub(String aParmType) {
      this( aParmType, null );
   }


   /**
    * Creates a new {@linkplain GlobalParametersStub} object.
    *
    * @param aParmType
    *           the parameters type
    * @param aFallbackParameters
    *           the global parameters to use if no parameters are overridden
    */
   public GlobalParametersStub(String aParmType, GlobalParameters aFallbackParameters) {
      super();

      iParameterType = aParmType;
      iParmCache = null;
      iParameters = new HashMap<String, Object>();
      iFallbackParameters = aFallbackParameters;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean exists( String aParmName ) {
      if ( iParameters.containsKey( aParmName ) ) {
         return true;
      } else {

         return ( iFallbackParameters != null ) && iFallbackParameters.exists( aParmName );
      }
   }


   /**
    * @see com.mxi.mx.common.misc.PropertiesHelper#getProperty(String)
    */
   @Override
   public String getProperty( String aParmName ) {
      if ( !iParameters.containsKey( aParmName ) && ( iFallbackParameters != null ) ) {
         return iFallbackParameters.getString( aParmName );
      } else {
         return ( String ) iParameters.get( aParmName );
      }
   }


   /**
    * @see com.mxi.mx.common.config.GlobalParameters#getString(String)
    */
   @Override
   public String getString( String aParmName ) {
      return getProperty( aParmName );
   }


   /**
    * @see com.mxi.mx.common.config.GlobalParameters#storeParameter(String, String)
    */
   @Override
   public void storeParameter( String aParmName, String aParmValue ) {
      iParameters.put( aParmName, aParmValue );
   }
}
