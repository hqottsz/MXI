package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.key.RefReqPriorityKey.NORMAL;
import static com.mxi.mx.core.services.inventory.reservation.auto.AbstractAutoReservationTest.PROPEN;
import static com.mxi.mx.core.services.inventory.reservation.auto.AbstractAutoReservationTest.getDate;

import java.util.Date;

import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * Helper class to create part requests.
 *
 */
public class CreatePartRequestUtils {

   /**
    *
    * Creates an open stock request.
    *
    * @param aPart
    * @param aNeededAtLocation
    * @param aRemoteLocation
    * @return the part request key
    */
   public static PartRequestKey createOpenStockRequest( PartNoKey aPart,
         LocationKey aNeededAtLocation, LocationKey aRemoteLocation ) {

      return new PartRequestBuilder().withType( RefReqTypeKey.STOCK ).withRequestedQuantity( 1.0 )
            .requiredBy( getDate( 1 ) ).withPriority( NORMAL ).withStatus( PROPEN )
            .isNeededAt( aNeededAtLocation ).withRemoteLocation( aRemoteLocation )
            .forSpecifiedPart( aPart ).build();
   }


   /**
    *
    * Creates an open consignment stock request.
    *
    * @param aPart
    * @param aNeededAtLocation
    * @param aRemoteLocation
    * @return the part request key
    */
   public static PartRequestKey createOpenCnsStockRequest( PartNoKey aPart,
         LocationKey aNeededAtLocation, LocationKey aRemoteLocation ) {

      return new PartRequestBuilder().withType( RefReqTypeKey.CSNSTOCK )
            .withRequestedQuantity( 1.0 ).requiredBy( getDate( 1 ) ).withPriority( NORMAL )
            .withStatus( PROPEN ).isNeededAt( aNeededAtLocation )
            .withRemoteLocation( aRemoteLocation ).forSpecifiedPart( aPart ).build();
   }


   private static PartRequestBuilder getTaskPartRequestBuilder( LocationKey aLocation,
         double aRequestedQuantity, Date aNeededByDate, RefReqPriorityKey aPriority,
         RefEventStatusKey aStatus, InventoryKey aInventory ) {

      LocationKey lSupplyLocation = InvLocTable.findByPrimaryKey( aLocation ).getSupplyLocation();
      InvLocTable lSupplyLocTable = InvLocTable.findByPrimaryKey( lSupplyLocation );
      LocationKey lRemoteLocation =
            lSupplyLocTable.getNoHubAutoRsrvBool() ? null : lSupplyLocTable.getHubLocation();

      return new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( aRequestedQuantity ).requiredBy( aNeededByDate )
            .withPriority( aPriority ).withStatus( aStatus ).isNeededAt( aLocation )
            .withReservedInventory( aInventory ).withRemoteLocation( lRemoteLocation );
   }


   /**
    *
    * Create a part request for a specific part with the given details.
    *
    * @param aPartNo
    *           The specified part of the part request.
    * @param aLocation
    *           The location where the inventory is needed.
    * @param aRequestedQuantity
    * @param aNeededByDate
    * @param aPriority
    * @param aStatus
    * @param aInventory
    * @return the part request key
    */
   public static PartRequestKey createPartRequest( PartNoKey aPartNo, LocationKey aLocation,
         double aRequestedQuantity, Date aNeededByDate, RefReqPriorityKey aPriority,
         RefEventStatusKey aStatus, InventoryKey aInventory ) {
      return getTaskPartRequestBuilder( aLocation, aRequestedQuantity, aNeededByDate, aPriority,
            aStatus, aInventory ).forSpecifiedPart( aPartNo ).build();
   }


   /**
    *
    * Create a part request for a part group with the given details.
    *
    * @param aPartGroup
    *           The part group of the part request.
    * @param aLocation
    *           The location where the inventory is needed.
    * @param aRequestedQuantity
    * @param aNeededByDate
    * @param aPriority
    * @param aStatus
    * @param aInventory
    * @return the part request key
    */
   public static PartRequestKey createPartRequest( PartGroupKey aPartGroup, LocationKey aLocation,
         double aRequestedQuantity, Date aNeededByDate, RefReqPriorityKey aPriority,
         RefEventStatusKey aStatus, InventoryKey aInventory ) {
      return getTaskPartRequestBuilder( aLocation, aRequestedQuantity, aNeededByDate, aPriority,
            aStatus, aInventory ).forPartGroup( aPartGroup ).build();
   }

}
