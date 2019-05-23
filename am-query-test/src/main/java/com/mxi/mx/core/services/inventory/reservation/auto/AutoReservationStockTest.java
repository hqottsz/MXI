package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertInventoryReservedForPartRequest;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestHasStatus;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestIsOpen;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestWasProcessedByAutoReservation;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.createInventory;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartRequestUtils.createOpenStockRequest;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.ReservedStatus;


/**
 * Auto reservation tests for stock requests.<br>
 * <em>Notes:</em>
 * <ul>
 * <li>Stock requests are only for a part number and never for a part group.</li>
 * <li>Stock requests are only picked up by auto-reservation when their remote location matches the
 * location of the auto-reservation request.</li>
 * </ul>
 *
 *
 */
@RunWith( Theories.class )
public final class AutoReservationStockTest extends AbstractAutoReservationTest {

   /**
    *
    * Test that serialized stock requests are processed by auto-reservation but are not reserved.
    *
    */
   @Theory
   public void testSerializedStockRequestWithRemoteLocationIsProcessedButUnreserved(
         RefInvClassKey aInvClassKey ) throws TriggerException {

      assumeTrue( aInvClassKey.isSerialized() );

      setUpTheoryParts( aInvClassKey );
      createInventory( iPartA, ReservedStatus.UNRESERVED, iHubLocation, iLocalOwner );

      PartRequestKey lPartRequestKey =
            createOpenStockRequest( iPartA, iAirportLocation, iHubLocation );

      callAutoReservationLogic( iHubLocation, AutoReservationRequestMode.PART_NO );

      // check that it is still open but it did get processed by auto-reservation
      assertPartRequestIsOpen( lPartRequestKey );
      assertPartRequestWasProcessedByAutoReservation( lPartRequestKey );
   }


   /**
    *
    * Test that batch stock requests are processed by auto-reservation and will be reserved.
    *
    */
   @Test
   public void testBatchStockRequestWithRemoteLocationIsProcessedAndReserved()
         throws TriggerException {

      setUpTheoryParts( BATCH );
      InventoryKey lInventory =
            createInventory( iPartA, ReservedStatus.UNRESERVED, iHubLocation, iLocalOwner );

      PartRequestKey lPartRequestKey =
            createOpenStockRequest( iPartA, iAirportLocation, iHubLocation );
      callAutoReservationLogic( iHubLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lPartRequestKey, PRREMOTE );
      assertInventoryReservedForPartRequest( lPartRequestKey, lInventory );
      assertPartRequestWasProcessedByAutoReservation( lPartRequestKey );
   }


   /**
    *
    * Test that stock part requests without a remote location are processed by auto-reservation but
    * are not reserved (still open).
    *
    */
   @Theory
   public void testStockRequestWithoutRemoteLocationIsProcessedButUnreserved(
         RefInvClassKey aInventoryClass ) throws TriggerException {

      setUpTheoryParts( aInventoryClass );

      // create two part requests, one for the part and one for the alternate part
      PartRequestKey lPartRequestA = createOpenStockRequest( iPartA, iAirportLocation, null );
      PartRequestKey lPartRequestB =
            createOpenStockRequest( iPartAlternate, iAirportLocation, null );

      // run auto-reservation for the part
      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      // assert that both requests are open with auto-reservation dates
      assertPartRequestIsOpen( lPartRequestA );
      assertPartRequestWasProcessedByAutoReservation( lPartRequestA );
      assertPartRequestIsOpen( lPartRequestB );
      assertPartRequestWasProcessedByAutoReservation( lPartRequestB );
   }

}
