package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertInventoryReservedForPartRequest;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestHasStatus;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.createInventory;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartRequestUtils.createPartRequest;

import java.util.Date;

import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.ReservedStatus;
import com.mxi.mx.core.services.location.LocationService;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * A Theory test for auto-reservation. Each test method is called with each combination of the data
 * points in the parent class.
 *
 */
@RunWith( Theories.class )
public final class AutoReservationHubTest extends AbstractAutoReservationTest {

   /**
    * Test that an inventory will reserve from the hub location if not available locally
    */
   @Theory
   public void testAutoReservationFromHub( AutoReservationRequestMode aRequestMode,
         RefInvClassKey aInventoryClass, SupplyLocationMode aInventoryLocationMode,
         DemandLocationMode aWhereNeededLocationMode, boolean noAutoReserveFromHubFlag )
         throws Exception {

      // Modify the Supply location. If the supply location has this flag, it will not auto-reserve
      // from the hub.
      if ( noAutoReserveFromHubFlag ) {
         InvLocTable lInvLoc = InvLocTable.findByPrimaryKey( iAirportLocation );
         lInvLoc.setNoHubAutoRsrvBool( true );
         lInvLoc.update();
      }

      // Set up 2 parts and a part group
      setUpTheoryParts( aInventoryClass );

      // Create an inventory at a location
      InventoryKey lInventoryA = createInventory( iPartA, ReservedStatus.UNRESERVED,
            LocationService.getLocationKey( aInventoryLocationMode.name() ), iLocalOwner );

      // Create a part request for either the part or part group at a possibly different location
      PartRequestKey lPartRequestNeededIn5;
      LocationKey lWhereNeeded = LocationService.getLocationKey( aWhereNeededLocationMode.name() );
      Date lWhenNeeded = getDate( 5 );
      double lRequestQuantity = 1.0;
      RefEventStatusKey lInitialStatus = PROPEN;
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      if ( AutoReservationRequestMode.PART_NO.equals( aRequestMode ) ) {
         lPartRequestNeededIn5 = createPartRequest( iPartA, lWhereNeeded, lRequestQuantity,
               lWhenNeeded, iPriority, lInitialStatus, null );
      } else {
         lPartRequestNeededIn5 = createPartRequest( iPartGroupA, lWhereNeeded, lRequestQuantity,
               lWhenNeeded, iPriority, lInitialStatus, null );
      }

      // Call autoreservation on the supply location
      LocationKey lWhereNeededSupplyLocation = LocationService
            .getSupplyLocation( LocationService.getLocationKey( aWhereNeededLocationMode.name() ) );

      callAutoReservationLogic( iAirportLocation, aRequestMode );
      callAutoReservationLogic( iHubLocation, aRequestMode );

      // Assert that a Hub (supplier) won't reserve from a non-hub location

      LocationKey lInventorySupplyLocation = LocationService
            .getSupplyLocation( LocationService.getLocationKey( aInventoryLocationMode.name() ) );

      // Assert that a location will reserve from its hub location
      if ( lInventorySupplyLocation.equals( iHubLocation ) ) {

         // .. unless NO_HUB_AUTO_RSRV_BOOL is true
         if ( noAutoReserveFromHubFlag ) {
            assertPartRequestHasStatus( lPartRequestNeededIn5, PROPEN );
            assertInventoryReservedForPartRequest( lPartRequestNeededIn5, null );
         } else {
            assertPartRequestHasStatus( lPartRequestNeededIn5, PRREMOTE );
            assertInventoryReservedForPartRequest( lPartRequestNeededIn5, lInventoryA );
         }
      }
      // Assert that the inventory will be reserved
      else {
         assertPartRequestHasStatus( lPartRequestNeededIn5, PRAVAIL );
         assertInventoryReservedForPartRequest( lPartRequestNeededIn5, lInventoryA );
      }

   }
}
