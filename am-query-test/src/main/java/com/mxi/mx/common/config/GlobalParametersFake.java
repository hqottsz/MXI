
package com.mxi.mx.common.config;

import com.mxi.mx.core.key.UtlConfigParmKey;
import com.mxi.mx.core.table.utl.UtlConfigParm;


/**
 * The fake global parameters
 */
public final class GlobalParametersFake extends GlobalParameters {

   private static final long serialVersionUID = 1L;


   /**
    * Creates a new {@linkplain GlobalParametersFake} object.
    *
    * @param aString
    */
   public GlobalParametersFake(String aParmType) {
      super();

      iParameterType = aParmType;
   }


   /**
    * @see com.mxi.mx.common.misc.PropertiesHelper#getProperty(String)
    */
   @Override
   public String getProperty( String aParmName ) {
      UtlConfigParm lParmValue = UtlConfigParm.findByPrimaryKey(
            new UtlConfigParmKey( aParmName, ParmTypeEnum.valueOf( iParameterType ) ) );
      return lParmValue.getParmValueString();
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
      UtlConfigParm lParmValue = UtlConfigParm.findByPrimaryKey(
            new UtlConfigParmKey( aParmName, ParmTypeEnum.valueOf( iParameterType ) ) );
      lParmValue.setParmValue( aParmValue );
      lParmValue.update();
   }
}
