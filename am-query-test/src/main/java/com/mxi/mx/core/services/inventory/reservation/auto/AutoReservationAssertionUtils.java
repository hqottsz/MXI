package com.mxi.mx.core.services.inventory.reservation.auto;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.req.ReqPart;


/**
 * Assertions for the auto-reservation tests.
 *
 */
public class AutoReservationAssertionUtils {

   /**
    *
    * Assert that the part request has the given inventory reserved against it.
    *
    * @param aPartRequestKey
    *           the part request
    * @param aInventoryKey
    *           the inventory - null if no inventory
    */
   public static void assertInventoryReservedForPartRequest( PartRequestKey aPartRequest,
         InventoryKey aInventory ) {
      new ReqPart( aPartRequest ).assertInventoryKey( aInventory );
   }


   /**
    *
    * Assert that the inventory has the expected reserved quantity. Useful for batch - serialized
    * expects 1.0 or 0.0
    *
    * @param aInventoryKey
    *           the reserved inventory
    * @param aQuantity
    *           the quantity that is reserved
    */
   public static void assertQuantityReserved( InventoryKey aInventory, Double quantity ) {
      new InvInv( aInventory ).assertReservedQuantity( quantity );
   }


   /**
    *
    * Asserts that the given part request was processed by auto-reservation, i.e. has a last
    * auto-reservation date.
    *
    * @param aPartRequestKey
    *           the part request
    */
   public static void
         assertPartRequestWasProcessedByAutoReservation( PartRequestKey aPartRequestKey ) {
      assertNotNull( new ReqPart( aPartRequestKey ).getLastAutoRsrvDt() );
   }


   /**
    *
    * Asserts that the given part request was processed by auto-reservation, i.e. has a last
    * auto-reservation date.
    *
    * @param aPartRequestKey
    *           the part request
    */
   public static void
         assertPartRequestWasNotProcessedByAutoReservation( PartRequestKey aPartRequestKey ) {
      assertNull( new ReqPart( aPartRequestKey ).getLastAutoRsrvDt() );
   }


   /**
    *
    * Assert that the part request has the given status.
    *
    * @param aPartRequest
    *           the part request
    * @param aStatus
    *           the part request's status
    */
   public static void assertPartRequestHasStatus( PartRequestKey aPartRequest,
         RefEventStatusKey aStatus ) {
      new EvtEventUtil( aPartRequest ).assertEventStatus( aStatus );
   }


   /**
    *
    * Assert that the part request is open - has status PROPEN and has no reserved inventory.
    *
    * @param aPartRequest
    *           the part request
    */
   public static void assertPartRequestIsOpen( PartRequestKey aPartRequest ) {
      new EvtEventUtil( aPartRequest ).assertEventStatus( RefEventStatusKey.PROPEN );
      new ReqPart( aPartRequest ).assertInventoryKey( null );
   }

}
