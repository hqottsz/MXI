package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertInventoryReservedForPartRequest;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestHasStatus;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestIsOpen;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestWasNotProcessedByAutoReservation;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestWasProcessedByAutoReservation;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.createInventory;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartRequestUtils.createOpenCnsStockRequest;

import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.ReservedStatus;


/**
 * Auto reservation tests for consignment stock requests.
 *
 */
@RunWith( Theories.class )
public final class AutoReservationCsnStockTest extends AbstractAutoReservationTest {

   /**
    *
    * Test that consignment stock part requests go through serialized and batch auto-reservation
    * logic.
    *
    */
   @Theory
   public void testCsnStockRequestWithRemoteLocationIsProcessedAndReserved(
         RefInvClassKey aInvClassKey ) throws TriggerException {

      setUpTheoryParts( aInvClassKey );
      InventoryKey lInventory =
            createInventory( iPartA, ReservedStatus.UNRESERVED, iHubLocation, iLocalOwner );

      PartRequestKey lPartRequestKey =
            createOpenCnsStockRequest( iPartA, iAirportLocation, iHubLocation );
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
   public void testCsnStockRequestReqWithoutRemoteLocationIsNotProcessed(
         RefInvClassKey aInvClassKey ) throws TriggerException {

      setUpTheoryParts( aInvClassKey );

      // create two part requests, one for the part and one for the alternate part
      PartRequestKey lPartRequestA = createOpenCnsStockRequest( iPartA, iAirportLocation, null );
      PartRequestKey lPartRequestB =
            createOpenCnsStockRequest( iPartAlternate, iAirportLocation, null );

      // run auto-reservation for the part
      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      // assert that both requests are open with auto-reservation dates
      assertPartRequestIsOpen( lPartRequestA );
      assertPartRequestWasNotProcessedByAutoReservation( lPartRequestA );
      assertPartRequestIsOpen( lPartRequestB );
      assertPartRequestWasNotProcessedByAutoReservation( lPartRequestB );
   }

}
