
package com.mxi.mx.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Stub implementation of the UserParameters class. It returns anything the way it was put in.
 *
 * Note: it does NOT access the utl_user_parm table. That functionality is available in
 * {@link UserParametersFake}
 */
public class UserParametersStub extends UserParameters {

   /** Use serialVersionUID for interoperability. */
   private static final long serialVersionUID = 5437013924583335152L;

   /** Mapping of Parameters and Values */
   private Map<String, Object> iParameters;


   /**
    * Creates a new UserParameters object.
    *
    * @param aUserId
    *           user associated with these parameters.
    * @param aParmType
    *           Parameter type for this object.
    */
   public UserParametersStub(int aUserId, String aParmType) {
      super();

      iParameterType = aParmType;
      iParmCache = null;
      iParameters = new HashMap<String, Object>();
   }


   /**
    * @see com.mxi.mx.common.misc.PropertiesHelper#getProperty(String)
    */
   @Override
   public String getProperty( String aParmName ) {
      return ( String ) iParameters.get( aParmName );
   }


   /**
    * @see com.mxi.mx.common.misc.PropertiesHelper#setProperty(String,String)
    */
   @Override
   public void setProperty( String aParmName, String aParmValue ) {
      storeParameter( aParmName, aParmValue );
   }


   /**
    * @see com.mxi.mx.common.config.UserParameters#getString(String)
    */
   @Override
   public String getString( String aParmName ) {
      return getProperty( aParmName );
   }


   /**
    * @see com.mxi.mx.common.config.UserParameters#storeParameter(String, String)
    */
   @Override
   public void storeParameter( String aParmName, String aParmValue ) {
      iParameters.put( aParmName, aParmValue );
   }


   @Override
   public Iterator<?> propertyNames() {
      throw new UnsupportedOperationException( "not supported" );
   }


   @Override
   public Map<String, String> getProperties() {
      throw new UnsupportedOperationException( "not supported" );
   }


   @Override
   public boolean exists( String aParmName, String aParmType ) {
      throw new UnsupportedOperationException( "not supported" );
   }


   @Override
   public Object remove( String aParmName ) {
      throw new UnsupportedOperationException( "not supported" );
   }

}
