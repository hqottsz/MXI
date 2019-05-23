
package com.mxi.mx.core.services.message;

/**
 * This provides a light-weight {@link MxCoreParameterLabelFactory} for unit testing purposes
 */
public class MxCoreParameterLabelFactoryStub extends MxCoreParameterLabelFactory {

   @Override
   public <T> String getLabel( T aKey ) throws LabelCreationException {
      return aKey.toString();
   }
}
