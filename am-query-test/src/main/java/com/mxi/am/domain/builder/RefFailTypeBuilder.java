
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.RefFailTypeKey;
import com.mxi.mx.core.table.ref.RefFailTypeTable;


/**
 *
 * Builds a <code>ref_fail_type</code> object
 *
 */
public class RefFailTypeBuilder implements DomainBuilder<RefFailTypeKey> {

   private String iCode;


   /**
    * {@inheritDoc}
    */
   @Override
   public RefFailTypeKey build() {
      RefFailTypeTable lRefFailTypeTable = RefFailTypeTable.create();
      lRefFailTypeTable.setCode( iCode );
      return lRefFailTypeTable.insert();
   }


   /**
    * Sets the failure type code.
    *
    * @param aCode
    * @return
    */
   public RefFailTypeBuilder withCode( String aCode ) {
      iCode = aCode;
      return this;
   }

}
