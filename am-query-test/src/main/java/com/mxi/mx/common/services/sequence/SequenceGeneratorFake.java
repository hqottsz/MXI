
package com.mxi.mx.common.services.sequence;

import com.mxi.mx.common.key.MxDbKey;


/**
 * This is an in-memory sequence generator used for stubbing out {@link MxSequenceGenerator}.
 */
public class SequenceGeneratorFake implements SequenceGenerator {

   private int iValue = 10000000;


   /**
    * {@inheritDoc}
    */
   @Override
   public int nextValue( String aSequenceCode ) {
      return iValue++;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int[] nextValues( String aSequenceCode, int aCount ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int nextValueWithContext( MxContextSequence aContextSequence, MxDbKey aContextKey ) {
      throw new UnsupportedOperationException();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void removeContextSequence( MxContextSequence aContextSequence, MxDbKey aContextKey ) {
      throw new UnsupportedOperationException();
   }
}
