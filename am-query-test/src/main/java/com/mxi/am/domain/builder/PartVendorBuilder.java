
package com.mxi.am.domain.builder;

import com.mxi.mx.core.key.EqpPartVendorKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.eqp.EqpPartVendorTable;


/**
 * Builds a <code>eqp_part_vendor</code> object
 */
public class PartVendorBuilder implements DomainBuilder<EqpPartVendorKey> {

   private EqpPartVendorKey iKey;
   private boolean iIsPreferred = false;
   private RefVendorStatusKey iVendorStatus;


   /**
    * Creates a new {@linkplain PartVendorBuilder} object.
    *
    */
   public PartVendorBuilder(VendorKey aVendorKey, PartNoKey aPartNoKey) {
      iKey = new EqpPartVendorKey( aPartNoKey, aVendorKey );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public EqpPartVendorKey build() {

      EqpPartVendorTable lTable = EqpPartVendorTable.create();

      lTable.setPrefBool( iIsPreferred );

      if ( iVendorStatus != null ) {
         lTable.setVendorStatus( iVendorStatus );
      }

      return lTable.insert( iKey );
   }


   /**
    * Sets if the vendor is preferred to true or false
    *
    * @param aValue
    *           the value to set the flag
    *
    * @return The builder
    */
   public PartVendorBuilder isPreferred( boolean aValue ) {
      iIsPreferred = aValue;

      return this;
   }


   /**
    * Sets the vendor status
    *
    * @param aStatus
    *           status of the vendor
    *
    * @return The builder
    */
   public PartVendorBuilder withVendorStatus( RefVendorStatusKey aStatus ) {
      iVendorStatus = aStatus;

      return this;
   }

}
