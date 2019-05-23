
package com.mxi.mx.core.services.message;

import com.mxi.mx.common.key.MxKey;
import com.mxi.mx.common.message.MxParameter;


/**
 * A stub for the {@link MxCoreParameterFactory} that removes the dependencies to the database
 */
public class MxCoreParameterFactoryStub extends MxCoreParameterFactory {

   /**
    * Returns a parameter containing the value of the key as a string (instead of getting the value
    * from {@link MxCoreParameterLabelFactory})
    *
    * @param aKey
    *           the key
    *
    * @return the parameter
    */
   @Override
   public MxParameter get( MxKey aKey ) {
      return new MxParameter( MxParameter.TYPE_STRING, aKey.toString(), aKey.toString() );
   }
}
