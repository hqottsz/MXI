
package com.mxi.mx.common.services.sequence;

import javax.sql.DataSource;


/**
 * Stub sequence generator
 */
public class SequenceGeneratorFactoryFake extends SequenceGeneratorFactory {

   private static final SequenceGenerator iSequenceGenerator = new SequenceGeneratorFake();


   /**
    * {@inheritDoc}
    */
   @Override
   public SequenceGenerator getSequenceGenerator() {
      return iSequenceGenerator;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public SequenceGenerator getSequenceGenerator( DataSource aDataSource ) {
      return iSequenceGenerator;
   }
}
