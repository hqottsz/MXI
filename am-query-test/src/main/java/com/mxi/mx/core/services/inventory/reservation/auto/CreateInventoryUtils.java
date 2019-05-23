package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.services.inventory.reservation.auto.AbstractAutoReservationTest.BATCH;
import static com.mxi.mx.core.services.inventory.reservation.auto.AbstractAutoReservationTest.RFI;

import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;


/**
 * Helper class to create inventory items.
 *
 */
public class CreateInventoryUtils {

   public static enum ReservedStatus {
      RESERVED, UNRESERVED
   };


   /**
    *
    * create inventory with quantity 1
    *
    * @param aPartKey
    * @param aReservedStatus
    * @param aLocation
    * @param aOwner
    * @return
    */
   public static InventoryKey createInventory( PartNoKey aPartKey, ReservedStatus aReservedStatus,
         LocationKey aLocation, OwnerKey aOwner ) {
      return createInventory( aPartKey, aReservedStatus, aLocation, aOwner, 1.0 );
   }


   /**
    *
    * create RFI inventory
    *
    * @param aPartKey
    * @param aReservedStatus
    * @param aLocation
    * @param aOwner
    * @param aQty
    * @return
    */
   public static InventoryKey createInventory( PartNoKey aPartKey, ReservedStatus aReservedStatus,
         LocationKey aLocation, OwnerKey aOwner, double aQty ) {

      return createInventory( aPartKey, RefInvCondKey.RFI, aReservedStatus, aLocation, aOwner,
            aQty );
   }


   /**
    *
    * create inventory
    *
    * @param aPartKey
    * @param aInvCond
    * @param aReservedStatus
    * @param aLocation
    * @param aOwner
    * @param aQty
    * @return
    */
   public static InventoryKey createInventory( PartNoKey aPartKey, RefInvCondKey aInvCond,
         ReservedStatus aReservedStatus, LocationKey aLocation, OwnerKey aOwner, double aQty ) {

      RefInvClassKey lInvClassKey = EqpPartNoTable.findByPrimaryKey( aPartKey ).getInvClass();
      InventoryBuilder lInventoryBuilder =
            new InventoryBuilder().withPartNo( aPartKey ).withCondition( RFI )
                  .atLocation( aLocation ).withOwner( aOwner ).withClass( lInvClassKey );

      if ( BATCH.equals( lInvClassKey ) ) {
         lInventoryBuilder.withBinQt( aQty ).withReserveQt( 0.0 );
      }

      if ( ReservedStatus.RESERVED.equals( aReservedStatus ) ) {
         lInventoryBuilder.isReserved();

         if ( BATCH.equals( lInvClassKey ) ) {
            lInventoryBuilder.withReserveQt( aQty );
         }
      }

      lInventoryBuilder.withCondition( aInvCond );

      return lInventoryBuilder.build();
   }
}
