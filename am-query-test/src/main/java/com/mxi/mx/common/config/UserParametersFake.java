
package com.mxi.mx.common.config;

import java.util.Iterator;
import java.util.Map;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.UtlUserParmKey;
import com.mxi.mx.core.table.utl.UtlUserParm;


/**
 * The Fake User Parameters
 *
 * This fake will access the utl_user_parm table when setting/getting values for the user
 * parameters. It caches those values, so be aware that if the utl_user_parm table is modified
 * external to this class, those changes will not be recognized by an instance of this class.
 */
public final class UserParametersFake extends UserParameters {

   // These queries are actually in mxcommon.
   private static final String LOAD_USER_PARAMETERS =
         "com.mxi.mx.common.query.config.LoadUserParameters";
   private static final String LOAD_USER_ACTION_RESOURCE_CODES =
         "com.mxi.mx.common.query.config.LoadUserActionResourceCodes";
   private static final String LOAD_USER_RESOURCE_CODES =
         "com.mxi.mx.common.query.config.LoadUserResourceCodes";

   private static final long serialVersionUID = 1L;

   private final int iUserId;


   public UserParametersFake(int aUserId, String aParmType) {
      super();

      iUserId = aUserId;
      iParameterType = aParmType;
   }


   /**
    * @see com.mxi.mx.common.misc.PropertiesHelper#getProperty(String)
    */
   @Override
   public String getProperty( String aParmName ) {

      String lValue = null;

      if ( !iPropertiesCache.containsKey( aParmName ) ) {
         // Property is not cached, so retrieve and add it to the cache.

         if ( ParmTypeEnum.SECURED_RESOURCE.equals( ParmTypeEnum.valueOf( iParameterType ) ) ) {
            lValue = getParmValue( LOAD_USER_RESOURCE_CODES, aParmName );
            if ( lValue == null ) {
               lValue = getParmValue( LOAD_USER_ACTION_RESOURCE_CODES, aParmName );
            }
         } else {
            lValue = getParmValue( LOAD_USER_PARAMETERS, aParmName );
         }

         iPropertiesCache.put( aParmName, lValue );
      }

      return iPropertiesCache.get( aParmName );
   }


   private String getParmValue( String aQuery, String aParmName ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", iUserId );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( aQuery, lArgs );
      while ( lQs.next() ) {
         if ( aParmName.equals( lQs.getString( "parm_name" ) ) ) {
            return lQs.getString( "parm_value" );
         }
      }

      return null;
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
      UtlUserParm lParm = UtlUserParm.create(
            new UtlUserParmKey( iUserId, aParmName, ParmTypeEnum.valueOf( iParameterType ) ) );
      lParm.setParmValue( aParmValue );
      lParm.insert();
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
