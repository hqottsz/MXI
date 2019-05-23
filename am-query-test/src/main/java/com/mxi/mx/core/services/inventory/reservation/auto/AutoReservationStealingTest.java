package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.key.RefReqPriorityKey.AOG;
import static com.mxi.mx.core.key.RefReqPriorityKey.NORMAL;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertInventoryReservedForPartRequest;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestHasStatus;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestIsOpen;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.createInventory;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartRequestUtils.createPartRequest;
import static org.junit.Assume.assumeTrue;

import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.ReservedStatus;


/**
 * Auto reservation tests on stealing.
 *
 */
@RunWith( Theories.class )
public class AutoReservationStealingTest extends AbstractAutoReservationTest {

   /**
    * Test that the part request with the earliest needed by date steals the part request.<br>
    * <br>
    * Data Setup: <br>
    * One part request with a reserved inventory and two part requests with different earlier needed
    * by dates than it. <br>
    * <br>
    * Expected Results:<br>
    * Autoreservation should steal the inventory for the part request with the earliest needed by
    * date.
    *
    * @throws Exception
    */
   @Theory
   public void testCanStealForEarliestNeededByDateWhenPriorityIsSameOrHigher(
         AutoReservationRequestMode aContext, RefInvClassKey aInventoryClass,
         RefReqPriorityKey aPriority ) throws Exception {

      setUpTheoryParts( aInventoryClass );

      InventoryKey lInventoryA =
            createInventory( iPartA, ReservedStatus.RESERVED, iAirportLocation, iLocalOwner );

      // create a part request with reserved inventory (and normal priority)
      PartRequestKey lPartRequestNeededIn5 = createPartRequest( iPartA, iAirportLocation, 1,
            getDate( 5 ), NORMAL, PRAVAIL, lInventoryA );

      // create another part request with an earlier needed by date that is open
      PartRequestKey lPartRequestNeededIn2 =
            createPartRequest( iPartA, iAirportLocation, 1, getDate( 2 ), aPriority, PROPEN, null );

      // create another part request with an even earlier needed by date that is open
      PartRequestKey lPartRequestNeededIn1 =
            createPartRequest( iPartA, iAirportLocation, 1, getDate( 1 ), aPriority, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, aContext );

      // Request with the earliest needed by date should have stolen the inventory
      assertPartRequestHasStatus( lPartRequestNeededIn1, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequestNeededIn1, lInventoryA );

      assertPartRequestIsOpen( lPartRequestNeededIn2 );
      assertPartRequestIsOpen( lPartRequestNeededIn5 );

   }


   /**
    * Test that an open part request with any priority will not steal from an AOG part request even
    * if the needed by date of the open request is before the needed by date of the AOG request<br>
    * <br>
    * Data Setup: <br>
    * One AOG part request with a reserved inventory and another open part request with an earlier
    * needed by date than the AOG part request. <br>
    * <br>
    * Expected Results:<br>
    * No stealing should occur.
    *
    * @throws Exception
    */
   @Theory
   public void testBatchCannotStealFromAOG( AutoReservationRequestMode aContext,
         RefInvClassKey aInventoryClass, RefReqPriorityKey aPriority ) throws Exception {

      assumeTrue( RefInvClassKey.BATCH.equals( aInventoryClass ) );
      setUpTheoryParts( aInventoryClass );
      InventoryKey lInventoryA =
            createInventory( iPartA, ReservedStatus.RESERVED, iAirportLocation, iLocalOwner );

      // create a part request with reserved inventory
      PartRequestKey lPartRequestAOG = createPartRequest( iPartA, iAirportLocation, 1, getDate( 2 ),
            AOG, PRAVAIL, lInventoryA );

      // create a part request (any priority) which is open and has an earlier needed by date than
      // the AOG part request
      PartRequestKey lPartRequestAnyPriority =
            createPartRequest( iPartA, iAirportLocation, 1, getDate( 1 ), aPriority, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      // no stealing
      assertPartRequestHasStatus( lPartRequestAOG, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequestAOG, lInventoryA );

      assertPartRequestIsOpen( lPartRequestAnyPriority );
   }


   /**
    * Test that an open AOG part request will steal from another AOG part request if the needed by
    * date of the open request is before the needed by date of the reserved request<br>
    * <br>
    * Data Setup: <br>
    * One AOG part request with a reserved inventory and another open AOG part request with an
    * earlier needed by date than the first request. <br>
    * <br>
    * Expected Results:<br>
    * Stealing should occur.
    *
    * @throws Exception
    */
   @Theory
   public void testSerializedCanStealForAOGWithEarlierNeededByDateFromAOG(
         RefInvClassKey aInventoryClass, AutoReservationRequestMode aContext ) throws Exception {

      assumeTrue( aInventoryClass.isSerialized() );

      setUpTheoryParts( aInventoryClass );

      InventoryKey lInventoryKey =
            createInventory( iPartA, ReservedStatus.RESERVED, iAirportLocation, iLocalOwner );
      // create a part request with reserved inventory
      PartRequestKey lPartRequestAOG = createPartRequest( iPartA, iAirportLocation, 1, getDate( 2 ),
            AOG, PRAVAIL, lInventoryKey );

      // create another AOG part request which is open and has an earlier needed by date than
      // the first AOG part request
      PartRequestKey lPartRequestAOGWithEarlierNeededByDate =
            createPartRequest( iPartA, iAirportLocation, 1, getDate( 1 ), AOG, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, aContext );

      // allows stealing
      assertPartRequestHasStatus( lPartRequestAOGWithEarlierNeededByDate, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequestAOGWithEarlierNeededByDate,
            lInventoryKey );

      assertPartRequestIsOpen( lPartRequestAOG );
   }


   /**
    * Test that an open non-AOG part request will not steal from an AOG part request even if the
    * needed by date of the open request is before the needed by date of the AOG request<br>
    * <br>
    * Data Setup: <br>
    * One AOG part request with a reserved inventory and another non-AOG open part request with an
    * earlier needed by date than the AOG part request. <br>
    * <br>
    * Expected Results:<br>
    * No stealing should occur.
    *
    * @throws Exception
    */
   @Theory
   public void testSerializedCannotStealFromAOGIfNonAOG( RefInvClassKey aInventoryClass,
         AutoReservationRequestMode aContext ) throws Exception {

      assumeTrue( aInventoryClass.isSerialized() );

      setUpTheoryParts( aInventoryClass );

      InventoryKey lInventory =
            createInventory( iPartA, ReservedStatus.RESERVED, iAirportLocation, iLocalOwner );

      // create a part request with reserved inventory
      PartRequestKey lPartRequestAOG =
            createPartRequest( iPartA, iAirportLocation, 1, getDate( 3 ), AOG, PRAVAIL, lInventory );

      // create a normal priority part request which is open and has an earlier needed by date than
      // the AOG part request
      PartRequestKey lPartRequestNormal =
            createPartRequest( iPartA, iAirportLocation, 1, getDate( 1 ), NORMAL, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, aContext );

      // no stealing
      assertPartRequestHasStatus( lPartRequestAOG, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequestAOG, lInventory );

      assertPartRequestIsOpen( lPartRequestNormal );
   }

}
