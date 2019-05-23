package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.key.RefReqPriorityKey.AOG;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertInventoryReservedForPartRequest;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestHasStatus;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestIsOpen;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertQuantityReserved;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.createInventory;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartRequestUtils.createPartRequest;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.ReservedStatus;
import com.mxi.mx.core.services.location.LocationService;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.table.inv.InvInv;
import com.mxi.mx.core.unittest.table.req.ReqPart;


/**
 * Testing auto-reservation of Batch inventory, especially BatchAutoReservationService
 *
 */
@RunWith( Theories.class )
public class AutoReservationBatchTest extends AbstractAutoReservationTest {

   /** Data: Existing Reservation */
   public static enum ReservationMode {
      UNRESERVED, EXISTING_LOW_PRIORITY, EXISTING_AOG
   };


   @DataPoints
   public static final ReservationMode iExistingReservationEnums[] = ReservationMode.values();


   /*
    * Test two part requests reserving from the same batch. Total needed qty equals batch.
    */
   @Test
   public void testBatchOneInvTwoRequests() throws TriggerException {

      setUpTheoryParts( RefInvClassKey.BATCH );
      InventoryKey lInv = CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
            iAirportLocation, iLocalOwner, 2.0 );

      Date lWhenNeeded = getDate( 5 );
      RefEventStatusKey lInitialStatus = PROPEN;
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lRequestA = createPartRequest( iPartA, iAirportLocation, 1.0, lWhenNeeded,
            iPriority, lInitialStatus, null );

      PartRequestKey lRequestB = createPartRequest( iPartA, iAirportLocation, 1.0, lWhenNeeded,
            iPriority, lInitialStatus, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequestA, PRAVAIL );
      assertPartRequestHasStatus( lRequestB, PRAVAIL );

      assertInventoryReservedForPartRequest( lRequestA, lInv );
      assertInventoryReservedForPartRequest( lRequestB, lInv );

      assertQuantityReserved( lInv, 2.0 );
   }


   /*
    * Test that one part request gets split when not enough inventory. Starting with one part
    * request and one batch, where the qty needed is greater than batch qty, Check that the batch is
    * reserved, the part request is AVAIL, and a new part request for the difference is OPEN
    */
   @Test
   public void testNotEnoughBatchSplitRequest() throws TriggerException {

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // 2L inventory
      InventoryKey lInv = CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
            iAirportLocation, iLocalOwner, 2.0 );

      // A part request at the same location due in 5 days at the same location
      PartRequestKey lOriginalPartRequest = createPartRequest( iPartA, iAirportLocation, 5.0,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lOriginalPartRequest, PRAVAIL );

      QuerySet lQs = getReqPartForPartA();

      Assert.assertEquals( "Expected 2 rows", 2, lQs.getRowCount() );

      // get get the newly created PartRequirement key
      lQs.next();
      PartRequestKey lNewPartRequest = lQs.getKey( PartRequestKey.class,
            new String[] { ReqPartTable.ColumnName.REQ_PART_DB_ID.toString(),
                  ReqPartTable.ColumnName.REQ_PART_ID.toString() } );

      if ( lNewPartRequest.equals( lOriginalPartRequest ) ) {
         lQs.next();
         lNewPartRequest = lQs.getKey( PartRequestKey.class,
               new String[] { ReqPartTable.ColumnName.REQ_PART_DB_ID.toString(),
                     ReqPartTable.ColumnName.REQ_PART_ID.toString() } );
      }

      assertPartRequestHasStatus( lNewPartRequest, PROPEN );

      assertInventoryReservedForPartRequest( lOriginalPartRequest, lInv );
      assertInventoryReservedForPartRequest( lNewPartRequest, null );

      assertQuantityReserved( lInv, 2.0 );
   }


   /*
    * Test that a part request will reserve from a preferred location rather than a more eligible
    * batch at another location
    */
   @Test
   public void testReservesFromPreferredLocationOverMoreEligibleBatchElsewhere()
         throws TriggerException {
      final Double lRequestedAmount = new Double( 1.5 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      InventoryKey lNonPreferredStoreInventory = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      // Preferred location - but higher key (i.e., newer inventory)
      InventoryKey lPreferredStoreInventory = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertExpectedReservation( lRequest, lRequestedAmount,
            new InventoryData( lNonPreferredStoreInventory, lBigEnoughAmount,
                  SupplyLocationMode.SRVSTORE, ReservationMode.UNRESERVED ),
            new InventoryData( lPreferredStoreInventory, lBigEnoughAmount,
                  SupplyLocationMode.PREFSRVSTORE, ReservationMode.UNRESERVED ) );
   }


   /*
    * Test that a part request will reserve from a big enough batch rather than a more eligible, too
    * small batch
    */
   @Test
   public void testReservesBigEnoughBatchOverMoreEligibleTooSmallBatch() throws TriggerException {

      final Double lRequestedAmount = new Double( 1.5 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iAirportLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory -- too small
      InventoryKey lSmallInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iAirportLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory -- big enough - but higher key (i.e., newer inventory)
      InventoryKey lBigInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iAirportLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertExpectedReservation( lRequest, lRequestedAmount,
            new InventoryData( lSmallInv, lTooSmallAmount, SupplyLocationMode.SRVSTORE,
                  ReservationMode.UNRESERVED ),
            new InventoryData( lBigInv, lBigEnoughAmount, SupplyLocationMode.SRVSTORE,
                  ReservationMode.UNRESERVED ) );

   }


   /*
    * Test that a part request will reserve from a big enough batch rather than a more eligible, too
    * small batch at a preferred location. Same as previous test but both inventory are at preferred
    * location
    */
   @Test
   public void testReservesBigEnoughBatchOverMoreEligibleTooSmallBatchAtPreferredLocation()
         throws TriggerException {
      final Double lRequestedAmount = new Double( 3.0 );
      final Double lTooSmallAmount = new Double( 2.0 );
      final Double lBigEnoughAmount = new Double( 4.0 );

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory -- too small
      InventoryKey lSmallInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory -- big enough - but higher key (i.e., newer inventory)
      InventoryKey lBigInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertExpectedReservation( lRequest, lRequestedAmount,
            new InventoryData( lSmallInv, lTooSmallAmount, SupplyLocationMode.PREFSRVSTORE,
                  ReservationMode.UNRESERVED ),
            new InventoryData( lBigInv, lBigEnoughAmount, SupplyLocationMode.PREFSRVSTORE,
                  ReservationMode.UNRESERVED ) );

   }


   /*
    * Test part request reserves best match out of available inventory list when no preferred
    * location setup for airport. Inventory that fully fulfills the part request ( in our case
    * lBigInv ) is reserved rather than the one on top (lSmallInv) of the available inventory list.
    */
   @Test
   public void testPartReqReservesBestMatchWhenNoPreferredLocationSetup() throws Exception {

      final Double lRequestedAmount = new Double( 3.0 );
      final Double lTooSmallAmount = new Double( 2.0 );
      final Double lBigEnoughAmount = new Double( 4.0 );

      setUpTheoryParts( RefInvClassKey.BATCH );

      LocationKey lAirportLocation = new LocationDomainBuilder().withCode( "TEST" )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      LocationKey lStore =
            new LocationDomainBuilder().withCode( "STORE1" ).withType( RefLocTypeKey.SRVSTORE )
                  .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

      LocationKey lLine = new LocationDomainBuilder().withCode( "LINE1" ).withType( RefLocTypeKey.LINE )
            .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

      // two inventories one with less qty and other with enough qty
      InventoryKey lSmallInv = CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
            ReservedStatus.UNRESERVED, lStore, iLocalOwner, lTooSmallAmount );
      InventoryKey lBigInv = CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
            ReservedStatus.UNRESERVED, lLine, iLocalOwner, lBigEnoughAmount );

      // one part request at line with due date as current time
      PartRequestKey lPartRequest = createPartRequest( iPartA, lLine, lRequestedAmount, new Date(),
            RefReqPriorityKey.NORMAL, PROPEN, null );

      // call auto-reservation
      callAutoReservationLogic( lAirportLocation, AutoReservationRequestMode.PART_NO );

      // assert that RFI inventory got reserved and reservation was locked since due date is within
      // 24 hour (print_ticket_interval config param default value)
      assertInventoryReservedForPartRequest( lPartRequest, lBigInv );
      assertPartRequestHasStatus( lPartRequest, RefEventStatusKey.PRAVAIL );
      assertTrue( new ReqPart( lPartRequest ).getLockReserveBool() );
   }


   /**
    * Test that an open AOG part request will steal from another normal part request if the needed
    * by date of the open request is before the needed by date of the reserved request<br>
    * <br>
    * Data Setup: <br>
    * One normal part request with a reserved inventory and another open AOG part request with an
    * earlier needed by date than the first request. partial inventory available for AOG
    * part-request<br>
    * <br>
    * Expected Results:<br>
    * AOG part request should reserve partial available, and for remainder it should steal from
    * other part-request. Also the remainder inventory it will put back in the stolen from
    * part-request and create a split part-request for the stolen quantity
    *
    * @throws Exception
    */
   @Test
   public void testCanStealForAOGWithEarlierNeededByDateFromPR() throws Exception {

      setUpTheoryParts( RefInvClassKey.BATCH );

      InventoryKey lInventoryKey = createInventory( iPartA, ReservedStatus.UNRESERVED,
            iPreferredStoreLocation, iLocalOwner, 10.0 );

      InventoryKey lInventoryKey2 = createInventory( iPartA, ReservedStatus.UNRESERVED,
            iNonPreferredStoreLocation, iLocalOwner, 10.0 );

      // create a part request with reserved inventory
      PartRequestKey lPartRequestNormal = createPartRequest( iPartA, iLineLocation, 6, getDate( 2 ),
            RefReqPriorityKey.NORMAL, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lPartRequestNormal, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequestNormal, lInventoryKey );

      // create another AOG part request which is open and has an earlier needed by date than
      // the first AOG part request
      PartRequestKey lPartRequestAOGWithEarlierNeededByDate =
            createPartRequest( iPartA, iLineLocation, 5, getDate( 1 ), AOG, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      // allows stealing
      assertPartRequestHasStatus( lPartRequestAOGWithEarlierNeededByDate, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequestAOGWithEarlierNeededByDate,
            lInventoryKey );

      assertPartRequestHasStatus( lPartRequestNormal, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequestNormal, lInventoryKey );

      assertQuantityReserved( lInventoryKey, 10.0 );

      // check that part-request was split and is OPEN for the stolen quantity

   }


   /*
    * Test variations around locations, quantities, splitting, remainders and stealing
    *
    * Test that after being split, the new part request is processed by autoreservation
    *
    * Test that after being broken, the broken part request will be reprocessed by autoreservation
    *
    */
   @Theory
   public void testRemaindersAndStealing( SupplyLocationMode aFirstSupplyLocation,
         Double aAmountOutOf10, SupplyLocationMode aSecondSupplyLocation,
         DemandLocationMode aFirstNeededAt, DemandLocationMode aSecondNeededAt,
         boolean aIsFirstRankingHigher ) throws TriggerException {

      setUpTheoryParts( RefInvClassKey.BATCH );

      // Inventory 1
      double lSupplyAmount1 = aAmountOutOf10;
      InventoryKey lInventoryKey1 = createInventory( iPartA, ReservedStatus.UNRESERVED,
            LocationService.getLocationKey( aFirstSupplyLocation.name() ), iVendorOwner,
            lSupplyAmount1 );

      // Inventory 2 of lower eligibility and such that the combined quantity is 10.0
      double lSupplyAmount2 = 10.0 - aAmountOutOf10;
      InventoryKey lInventoryKey2 = createInventory( iPartA, ReservedStatus.UNRESERVED,
            LocationService.getLocationKey( aSecondSupplyLocation.name() ), iLocalOwner,
            lSupplyAmount2 );

      // create a part request for 5 and reserve it
      Date lFirstNeededBy = getDate( 2 + ( aIsFirstRankingHigher ? 0 : 1 ) );
      PartRequestKey lFirstPartRequest =
            createPartRequest( iPartA, LocationService.getLocationKey( aFirstNeededAt.name() ), 5.0,
                  lFirstNeededBy, RefReqPriorityKey.NORMAL, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );
      callAutoReservationLogic( iHubLocation, AutoReservationRequestMode.PART_NO );

      // create another part request
      Date lSecondNeededBy = getDate( 2 + ( aIsFirstRankingHigher ? 1 : 0 ) );
      PartRequestKey lSecondPartRequest =
            createPartRequest( iPartA, LocationService.getLocationKey( aSecondNeededAt.name() ),
                  5.0, lSecondNeededBy, RefReqPriorityKey.NORMAL, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );
      callAutoReservationLogic( iHubLocation, AutoReservationRequestMode.PART_NO );

      // Assert that all the inventory is reserved
      assertQuantityReserved( lInventoryKey1, lSupplyAmount1 );
      assertQuantityReserved( lInventoryKey2, lSupplyAmount2 );

   }


   /*
    * Test AOG part request will not steal reserved inventory from the locked part request
    */
   @Test
   public void testAOGPartReqNotStealLockedReservation() throws Exception {

      // GIVEN
      setUpTheoryParts( RefInvClassKey.BATCH );

      // one inventory at North Warehouse
      InventoryKey lInventory = CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
            ReservedStatus.RESERVED, iNonPreferredStoreLocation, iLocalOwner, 10.0 );

      // one fulfilled PR at Line
      Date lWhenNeeded = getDate( 5 );
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartA, iLineLocation, 10.0,
            lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventory );
      // set the lock boolean to make it a locked reservation
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( lPartRequestAtLine );
      lReqPartTable.setLockReserveBool( true );
      lReqPartTable.update();

      // create open AOG PR at Line
      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA, iLineLocation, 10.0,
            lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      // THEN: assert that AOG PR will not steal inventory from locked reservation
      assertPartRequestIsOpen( lAOGPartRequestAtLine );
      assertInventoryReservedForPartRequest( lPartRequestAtLine, lInventory );
   }


   /*
    * This tests a fairly specific area around OPER-19793. The fix needed to make sure it didn't
    * break the fix for MX-1531. The setup is as follows:
    *
    * 1. Two supply locations, one a remote supply location of the first. 2. A deferred fault task
    * has inventory (qty:1) reserved for it through a part request at the remote supply location.
    * The reservation is locked. 3. Inventory at the main location is set to 1 and is unreserved. 4.
    * Part request is made at the main location, part is reserved through auto reservation. 5.
    * Higher priority part request is made at main location, original reservation is broken during
    * auto reservation. New request reserves inventory at main location. Original reservation goes
    * to Open state and does not break the locked reservation that the deferred fault has.
    */
   @Test
   public void testDeferredFaultPartReqNotStealLockedReservation() throws Exception {

      // GIVEN
      setUpTheoryParts( RefInvClassKey.BATCH );

      // one inventory locally.
      InventoryKey lInventory = CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
            ReservedStatus.UNRESERVED, iAirportLocation, iLocalOwner, 1.0 );

      // one inventory remote which is reserved for the part request attached to the deferred fault
      InventoryKey lInventoryRemote = CreateInventoryUtils.createInventory( iPartA,
            RefInvCondKey.RFI, ReservedStatus.RESERVED, iHubLocation, iLocalOwner, 1.0 );

      // Next few steps do the following:
      // 1. Create the inventory for the task associated to the fault.
      // 2. Create the task.
      // 3. Create the fault with deferred status.
      InventoryKey lAircraft = Domain.createAircraft();
      ConfigSlotKey lConfigSlotKey = new ConfigSlotBuilder( "SYS-1" ).build();
      ConfigSlotPositionKey lConfigSlotPositionKey = new ConfigSlotPositionKey(
            lConfigSlotKey.getDbId(), lConfigSlotKey.getCd(), lConfigSlotKey.getBomId(), 1 );
      // Create main inventory for the task
      InventoryKey lInventoryKey =
            new InventoryBuilder().withConfigSlotPosition( lConfigSlotPositionKey ).build();
      // Set main inventory for the task
      TaskBuilder lTaskBuilder = new TaskBuilder();
      TaskKey lTask =
            lTaskBuilder.withTaskClass( RefTaskClassKey.CORR ).onInventory( lInventoryKey ).build();
      // Create fault
      SdFaultBuilder lFaultBuilder = new SdFaultBuilder();
      FaultKey lFault = lFaultBuilder.withFailureSeverity( RefFailureSeverityKey.MEL )
            .withCorrectiveTask( lTask ).withStatus( RefEventStatusKey.CFDEFER )
            .onInventory( lAircraft ).build();

      // Create the Part requirement and associate to the task.
      TaskPartKey iTaskPartKey = new PartRequirementDomainBuilder( lTask ).withInstallPart( iPartA )
            .forPartGroup( iPartGroupA ).withRequestAction( RefReqActionKey.REQ ).build();

      TaskInstPartKey lTaskInstPart = new TaskInstPartKey( iTaskPartKey, 1 );

      DataSetArgument lArgs = lTaskInstPart.getPKWhereArg();
      lArgs.add( iPartA.getPKWhereArg() );
      MxDataAccess.getInstance().executeInsert( "sched_inst_part", lArgs );

      // setup part request for part requirement with reserved BATCH inventory
      PartRequestKey lPartRequestAtRemoteSupply = new PartRequestBuilder()
            .forPartRequirement( lTaskInstPart ).withReservedInventory( lInventoryRemote )
            .withStatus( PRREMOTE ).isLockedReservation().build();

      // Confirm the part request status is PRREMOTE.
      assertPartRequestHasStatus( lPartRequestAtRemoteSupply, PRREMOTE );

      // Create part request and run auto reservation. Inventory should be reserved locally.
      PartRequestKey lRequest = createPartRequest( iPartA, iAirportLocation, 1.0, getDate( 5 ),
            RefReqPriorityKey.NORMAL, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );
      callAutoReservationLogic( iHubLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequest, PRAVAIL );

      assertInventoryReservedForPartRequest( lRequest, lInventory );

      // Create higher priority part request and run auto reservation. Inventory should be reserved
      // for the new part request, the original part request at this location should be broken but
      // it will not steal the remote because it has been locked against the deferred fault part
      // req.
      PartRequestKey lStealingRequest = createPartRequest( iPartA, iAirportLocation, 1.0,
            getDate( 2 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      callAutoReservationLogic( iHubLocation, AutoReservationRequestMode.PART_NO );
      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lStealingRequest, PRAVAIL );

      assertPartRequestHasStatus( lRequest, PROPEN );

      assertInventoryReservedForPartRequest( lPartRequestAtRemoteSupply, lInventoryRemote );
   }


   /*
    * Test that a part request will reserve from a larger, but still insufficient batch rather than
    * a smaller batch
    */
   @Test
   public void testReservesLargerTooSmallBatch() throws TriggerException {

      // Our part's Bin Decimal Places is configured to zero, so using integral amounts here:
      final Double lRequestedAmount = new Double( 3.0 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lLargerTooSmallAmount = new Double( 2.0 );

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iAirportLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory -- too small
      InventoryKey lSmallInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory -- still too small - but higher key (i.e., newer inventory)
      InventoryKey lBigInv =
            CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
                  iNonPreferredStoreLocation, iLocalOwner, lLargerTooSmallAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertExpectedReservation( lRequest, lRequestedAmount,
            new InventoryData( lSmallInv, lTooSmallAmount, SupplyLocationMode.SRVSTORE,
                  ReservationMode.UNRESERVED ),
            new InventoryData( lBigInv, lLargerTooSmallAmount, SupplyLocationMode.SRVSTORE,
                  ReservationMode.UNRESERVED ) );
   }


   /*
    * Test that a part request will reserve from a larger, but still insufficient batch rather than
    * a smaller batch. Same as previous test but both inventory are at preferred location
    */
   @Test
   public void testReservesLargerTooSmallBatchAtPreferredLocation() throws TriggerException {
      final Double lRequestedAmount = new Double( 3.0 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lLargerTooSmallAmount = new Double( 2.0 );

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory -- too small
      InventoryKey lSmallInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory -- big enough - but higher key (i.e., newer inventory)
      InventoryKey lBigInv =
            CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
                  iPreferredStoreLocation, iLocalOwner, lLargerTooSmallAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertExpectedReservation( lRequest, lRequestedAmount,
            new InventoryData( lSmallInv, lTooSmallAmount, SupplyLocationMode.PREFSRVSTORE,
                  ReservationMode.UNRESERVED ),
            new InventoryData( lBigInv, lLargerTooSmallAmount, SupplyLocationMode.PREFSRVSTORE,
                  ReservationMode.UNRESERVED ) );
   }


   /*
    * Test that a part request will reserve an insufficient batch at a preferred location rather
    * than a big enough batch at the same preferred location
    */
   @Test
   public void testReservesTooSmallAtPreferredLocationOverBigEnoughElsewhere()
         throws TriggerException {
      final Double lRequestedAmount = new Double( 1.5 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory -- too small
      InventoryKey lSmallInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory -- big enough - but higher key (i.e., newer inventory)
      InventoryKey lBigInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequest, PRAVAIL );

      assertInventoryReservedForPartRequest( lRequest, lBigInv );

      assertQuantityReserved( lBigInv, lRequestedAmount );

      assertExpectedReservation( lRequest, lRequestedAmount,
            new InventoryData( lSmallInv, lTooSmallAmount, SupplyLocationMode.PREFSRVSTORE,
                  ReservationMode.UNRESERVED ),
            new InventoryData( lBigInv, lBigEnoughAmount, SupplyLocationMode.PREFSRVSTORE,
                  ReservationMode.UNRESERVED ) );

   }


   /*
    * Test that a part request will reserve at second preferred location from a bin that can fully
    * reserve the requested-amount, if nothing is available at first preferred rather than a big
    * enough batch elsewhere
    */
   @Test
   public void testReservesFullyAtSecondPreferredLocationOverBigEnoughElsewhere()
         throws TriggerException {
      final Double lRequestedAmount = new Double( 1.5 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // add second preferred location
      addSecondPreferredStoreLocation();

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory at preferred location-- too small
      InventoryKey lSmallInvAtSecondPreferred =
            CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
                  iSecondPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory at preferred location -- big enough
      InventoryKey lBigInvAtSecondPreferred =
            CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
                  iSecondPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      // 2L inventory at non-preferred-- big enough
      InventoryKey lBigInvElseWhere = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequest, PRAVAIL );
      // assert that part-request reserves the bin that can fully satisfy the requested amount at
      // 2nd preferred location
      assertInventoryReservedForPartRequest( lRequest, lBigInvAtSecondPreferred );

      // request amount is reserved from big enough bin at 2nd preferred location
      assertQuantityReserved( lBigInvAtSecondPreferred, lRequestedAmount );

      // nothing is reserved from the smaller bin at 2nd preferred location
      assertQuantityReserved( lSmallInvAtSecondPreferred, 0.0 );

      // nothing is reserved from big bin at non-preferred location
      assertQuantityReserved( lBigInvElseWhere, 0.0 );
   }


   /*
    * Test that a part request will reserve an insufficient batch at first preferred location, then
    * for the remainder needed quantity it reserves at second preferred location rather than a big
    * enough batch elsewhere
    */
   @Test
   public void testReservesPartialAtSecondPreferredLocationAfterFirstPreferredLocation()
         throws TriggerException {
      final Double lRequestedAmount = new Double( 1.5 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // add second preferred location
      addSecondPreferredStoreLocation();

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory at first preferred location-- too small
      InventoryKey lSmallInvAtFirstPreferred = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory at second preferred location-- big enough
      InventoryKey lBigInvAtSecondPreferred =
            CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
                  iSecondPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      // 2L inventory at non-preferred location-- big enough
      InventoryKey lBigInvElseWhere = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequest, PRAVAIL );

      // assert the smaller bin at preferred location is all reserved
      assertInventoryReservedForPartRequest( lRequest, lSmallInvAtFirstPreferred );
      assertQuantityReserved( lSmallInvAtFirstPreferred, lTooSmallAmount );

      // assert the bin at second preferred location is reserved for the remainder needed amount
      assertQuantityReserved( lBigInvAtSecondPreferred, new Double( 0.5 ) );

      // get all the part-requests for part
      QuerySet lQs = getReqPartForPartA();

      Assert.assertEquals( "Expected 2 rows", 2, lQs.getRowCount() );
      // get the split/newly created part-request key
      lQs.next();
      PartRequestKey lNewPartRequest = lQs.getKey( PartRequestKey.class,
            new String[] { ReqPartTable.ColumnName.REQ_PART_DB_ID.toString(),
                  ReqPartTable.ColumnName.REQ_PART_ID.toString() } );

      if ( lNewPartRequest.equals( lRequest ) ) {
         lQs.next();
         lNewPartRequest = lQs.getKey( PartRequestKey.class,
               new String[] { ReqPartTable.ColumnName.REQ_PART_DB_ID.toString(),
                     ReqPartTable.ColumnName.REQ_PART_ID.toString() } );
      }

      // verify the split request has correct inventory reservation
      assertPartRequestHasStatus( lNewPartRequest, PRAVAIL );
      assertInventoryReservedForPartRequest( lNewPartRequest, lBigInvAtSecondPreferred );

      // assert the bigger bin at non-preferred location has nothing reserved
      assertQuantityReserved( lBigInvElseWhere, 0.0 );

   }


   /*
    * Test that a part request will reserve an insufficient batch at second preferred location, if
    * nothing is available at first preferred rather than a big enough batch elsewhere. Then for the
    * split part-request for the remainder needed quantity it reserves from non-preferred location.
    */
   @Test
   public void testReservesPartialAtSecondPreferredLocationOverBigEnoughElsewhere()
         throws TriggerException {
      final Double lRequestedAmount = new Double( 1.5 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // add second preferred location
      addSecondPreferredStoreLocation();

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory at second preferred location-- too small
      InventoryKey lSmallInvAtSecondPreferred =
            CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
                  iSecondPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory at non-preferred location-- big enough
      InventoryKey lBigInvAtNonPreferred = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequest, PRAVAIL );
      assertInventoryReservedForPartRequest( lRequest, lSmallInvAtSecondPreferred );

      // assert the smaller bin at preferred location is all reserved
      assertQuantityReserved( lSmallInvAtSecondPreferred, lTooSmallAmount );

      // assert the bigger bin at non-preferred location is reserved for the remainder needed amount
      assertQuantityReserved( lBigInvAtNonPreferred, new Double( 0.5 ) );

      // get all the part-requests for part
      QuerySet lQs = getReqPartForPartA();

      Assert.assertEquals( "Expected 2 rows", 2, lQs.getRowCount() );
      // get the split/newly created part-request key
      lQs.next();
      PartRequestKey lNewPartRequest = lQs.getKey( PartRequestKey.class,
            new String[] { ReqPartTable.ColumnName.REQ_PART_DB_ID.toString(),
                  ReqPartTable.ColumnName.REQ_PART_ID.toString() } );

      if ( lNewPartRequest.equals( lRequest ) ) {
         lQs.next();
         lNewPartRequest = lQs.getKey( PartRequestKey.class,
               new String[] { ReqPartTable.ColumnName.REQ_PART_DB_ID.toString(),
                     ReqPartTable.ColumnName.REQ_PART_ID.toString() } );
      }
      // verify the split request has correct inventory reservation
      assertPartRequestHasStatus( lNewPartRequest, PRAVAIL );
      assertInventoryReservedForPartRequest( lNewPartRequest, lBigInvAtNonPreferred );
   }


   /*
    * Test that a part request will reserve from non-preferred location, if nothing is available at
    * first preferred and second preferred location.
    */
   @Test
   public void testReservesElsewhereWhenNoneAvailAtFirstAndSecondPreferredLocation()
         throws TriggerException {
      final Double lRequestedAmount = new Double( 1.5 );
      final Double lTooSmallAmount = new Double( 1.0 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // add second preferred location
      addSecondPreferredStoreLocation();

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1L inventory at non-preferred location-- too small
      InventoryKey lSmallInvAtNonPreferred = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lTooSmallAmount );

      // 2L inventory at non-preferred location -- big enough
      InventoryKey lBigInvAtNonPreferred = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequest, PRAVAIL );
      // assert that part-request reserves the bin that can fully satisfy the requested amount
      assertInventoryReservedForPartRequest( lRequest, lBigInvAtNonPreferred );

      // request amount is reserved from big enough bin at 2nd preferred location
      assertQuantityReserved( lBigInvAtNonPreferred, lRequestedAmount );

      // nothing is reserved from the smaller bin
      assertQuantityReserved( lSmallInvAtNonPreferred, 0.0 );
   }


   /*
    * Test that a higher ranking part request will steal inventory from another part-request at
    * second preferred location even though there is a bin that can fully reserve the
    * requested-amount elsewhere, the other part-request whose inventory is stolen will then reserve
    * from non-preferred location since there is none available at 1st or second preferred location.
    */
   @Test
   public void testStealsAtSecondPreferredLocationOverBigEnoughElsewhere() throws TriggerException {
      final Double lRequestedAmount = new Double( 1.0 );
      final Double lExactAmount = new Double( 1.0 );
      final Double lBigEnoughAmount = new Double( 2.0 );

      // add second preferred location
      addSecondPreferredStoreLocation();

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 1.5L
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // 1.5L inventory at preferred location
      InventoryKey lInvAtSecondPreferred = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iSecondPreferredStoreLocation, iLocalOwner, lExactAmount );

      // 2L inventory at non-preferred-- big enough
      InventoryKey lBigInvElseWhere = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, iNonPreferredStoreLocation, iLocalOwner, lBigEnoughAmount );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lRequest, PRAVAIL );
      // assert that part-request reserves inventory at second preferred
      assertInventoryReservedForPartRequest( lRequest, lInvAtSecondPreferred );

      // request amount is reserved from bin at 2nd preferred location
      assertQuantityReserved( lInvAtSecondPreferred, lRequestedAmount );

      // nothing is reserved from big bin at non-preferred location
      assertQuantityReserved( lBigInvElseWhere, 0.0 );

      // create another part-request that's needed earlier and 1.5L needed qty
      PartRequestKey lNewRequest = createPartRequest( iPartA, iLineLocation, lRequestedAmount,
            getDate( 3 ), RefReqPriorityKey.AOG, PROPEN, null );

      callAutoReservationLogic( iAirportLocation, AutoReservationRequestMode.PART_NO );

      assertPartRequestHasStatus( lNewRequest, PRAVAIL );

      // assert that new part-request stole inventory at second preferred
      assertInventoryReservedForPartRequest( lNewRequest, lInvAtSecondPreferred );
      assertQuantityReserved( lInvAtSecondPreferred, lRequestedAmount );

      // assert that old part-request has reserved inventory from non-preferred location
      assertPartRequestHasStatus( lRequest, PRAVAIL );
      assertInventoryReservedForPartRequest( lRequest, lBigInvElseWhere );

      // assert the reserved amount for the bin at non-preferred location
      assertQuantityReserved( lBigInvElseWhere, lRequestedAmount );

   }


   /*
    * Test various combinations of location and quantities. Test that:
    *
    * a part request will reserve from a preferred location rather than a more eligible batch at
    * another location
    *
    * a part request will reserve an insufficient batch at a preferred location rather than a big
    * enough batch at another location
    *
    * if locations are the same, a part request will reserve from a big enough batch rather than a
    * more eligible, too small batch
    *
    * if locations are the same, a part request will reserve from a larger, but still insufficient
    * batch rather than a smaller batch
    *
    * Consider disabling this test after active development on autoreservation
    */
   @Theory
   public void testBatchLocationQuantityReservation( SupplyLocationMode aFirstLocation,
         Double aFirstQt, SupplyLocationMode aSecondLocation, Double aSecondQt )
         throws TriggerException {

      AutoReservationRequestMode aRequestMode = AutoReservationRequestMode.PART_NO;

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 3L at Line location
      final double lRequestedQuantity = 3.0;
      PartRequestKey lRequest = createPartRequest( iPartA, iLineLocation, lRequestedQuantity,
            getDate( 5 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // Two batches of greater or lesser quantity
      InventoryKey lFirstInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, LocationService.getLocationKey( aFirstLocation.name() ),
            iLocalOwner, aFirstQt.doubleValue() );

      InventoryKey lSecondInv = CreateInventoryUtils.createInventory( iPartA,
            ReservedStatus.UNRESERVED, LocationService.getLocationKey( aSecondLocation.name() ),
            iLocalOwner, aSecondQt.doubleValue() );

      // Call autoreservation on the parent supply location
      callAutoReservationLogic( iAirportLocation, aRequestMode );
      // Call autoreservation on the remote supply location
      callAutoReservationLogic( iHubLocation, aRequestMode );

      assertExpectedReservation( lRequest, lRequestedQuantity,
            new InventoryData( lFirstInv, aFirstQt.doubleValue(), aFirstLocation,
                  ReservationMode.UNRESERVED ),
            new InventoryData( lSecondInv, aSecondQt.doubleValue(), aSecondLocation,
                  ReservationMode.UNRESERVED ) );
   }


   /*
    * Test various combinations of location and existing reservation. Test that:
    *
    * A part request will break an existing reservation (of lower ranking) at a preferred location
    * before reserving at another location
    *
    * A part request will reserve at a non-preferred location at the same airport before reserving
    * remotely.
    *
    * A part request will break a lower-ranking part request before a higher-ranking one.
    *
    * Consider disabling this test after active development on autoreservation
    */
   @Theory
   public void testBatchLocationExistingReservation(

         SupplyLocationMode aFirstLocation, ReservationMode aFirstExistingReservationMode,
         SupplyLocationMode aSecondLocation, ReservationMode aSecondExistingReservationMode )
         throws TriggerException {

      AutoReservationRequestMode aRequestMode = AutoReservationRequestMode.PART_NO;

      // Create a Batch part
      setUpTheoryParts( RefInvClassKey.BATCH );

      // Need 3L at Line location
      final double lRequestedQuantity = 3.0;

      // Two batches
      ReservedStatus lFirstReservedStatus = getReservedStatus( aFirstExistingReservationMode );
      InventoryKey lFirstInv = CreateInventoryUtils.createInventory( iPartA, lFirstReservedStatus,
            LocationService.getLocationKey( aFirstLocation.name() ), iLocalOwner,
            lRequestedQuantity );
      if ( ReservedStatus.RESERVED.equals( lFirstReservedStatus ) ) {
         RefEventStatusKey lFirstReservedEventStatus =
               getReservedEventStatus( lFirstInv, aFirstExistingReservationMode );

         createPartRequest( iPartA, iLineLocation, lRequestedQuantity, getDate( 5 ),
               getReqPriority( aFirstExistingReservationMode ), lFirstReservedEventStatus,
               lFirstInv );
      }

      ReservedStatus lSecondReservedStatus = getReservedStatus( aSecondExistingReservationMode );
      InventoryKey lSecondInv = CreateInventoryUtils.createInventory( iPartA, lSecondReservedStatus,
            LocationService.getLocationKey( aSecondLocation.name() ), iLocalOwner,
            lRequestedQuantity );
      if ( ReservedStatus.RESERVED.equals( lSecondReservedStatus ) ) {
         RefEventStatusKey lSecondReservedEventStatus =
               getReservedEventStatus( lFirstInv, aSecondExistingReservationMode );

         createPartRequest( iPartA, iLineLocation, lRequestedQuantity, getDate( 5 ),
               getReqPriority( aSecondExistingReservationMode ), lSecondReservedEventStatus,
               lSecondInv );
      }

      PartRequestKey lNeededQuicklyNormalPartRequest = createPartRequest( iPartA, iLineLocation,
            lRequestedQuantity, getDate( 2 ), RefReqPriorityKey.NORMAL, PROPEN, null );

      // Call autoreservation on the parent supply location
      callAutoReservationLogic( iAirportLocation, aRequestMode );
      // Call autoreservation on the remote supply location
      callAutoReservationLogic( iHubLocation, aRequestMode );

      assertExpectedReservation( lNeededQuicklyNormalPartRequest, lRequestedQuantity,
            new InventoryData( lFirstInv, lRequestedQuantity, aFirstLocation,
                  aFirstExistingReservationMode ),
            new InventoryData( lSecondInv, lRequestedQuantity, aSecondLocation,
                  aSecondExistingReservationMode ) );
   }


   /*
    * Test that auto reservation by Part No will reserve BATCH inventory with null reserve qty
    *
    * Reserve qty might be null for BATCH inventories loaded through Actual Loader.
    */
   @Test
   public void testPartNoReqReserveBatchInvWithNullReserveQty() throws TriggerException {

      reserveBatchInvWithNullReserveQty( AutoReservationRequestMode.PART_NO );
   }


   /*
    * Test that auto reservation by Part Group will reserve BATCH inventory with null reserve qty
    *
    * Reserve qty might be null for BATCH inventories loaded through Actual Loader.
    */
   @Test
   public void testPartGroupReqReserveBatchInvWithNullReserveQty() throws TriggerException {

      reserveBatchInvWithNullReserveQty( AutoReservationRequestMode.PART_GROUP );
   }


   /**
    *
    * test auto reservation for BATCH inventory with null reserve_qt
    *
    * @param aAutoResRequestMode
    *           auto reservation by either PART_NO or PART_GROUP
    * @throws TriggerException
    */
   public void reserveBatchInvWithNullReserveQty( AutoReservationRequestMode aAutoResRequestMode )
         throws TriggerException {

      setUpTheoryParts( RefInvClassKey.BATCH );

      // one BATCH inventory with null reserve qty
      InventoryKey lInv = CreateInventoryUtils.createInventory( iPartA, ReservedStatus.UNRESERVED,
            iAirportLocation, iLocalOwner, 2.0 );

      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( lInv );
      lInvTable.setReservedQt( null );
      lInvTable.update();

      // one part request
      Date lWhenNeeded = getDate( 5 );
      RefEventStatusKey lInitialStatus = PROPEN;
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lPartRequest = createPartRequest( iPartA, iAirportLocation, 1.0, lWhenNeeded,
            iPriority, lInitialStatus, null );

      // auto reservation with Auto Reservation Request Mode and Location
      callAutoReservationLogic( iAirportLocation, aAutoResRequestMode );

      // assert that the inventory is reserved for the part request
      assertPartRequestHasStatus( lPartRequest, PRAVAIL );
      assertInventoryReservedForPartRequest( lPartRequest, lInv );
      assertQuantityReserved( lInv, 1.0 );
   }


   /*
    * Assert that aRequestKey has reserved (some quantity of) aInvKey
    */
   private void assertReservation( PartRequestKey aRequestKey, InventoryKey aInvKey ) {

      assertInventoryReservedForPartRequest( aRequestKey, aInvKey );

      if ( aInvKey != null ) {

         LocationKey lInvLocationKey = InvInvTable.findByPrimaryKey( aInvKey ).getLocation();
         LocationKey lSupplyLocationKey =
               InvLocTable.findByPrimaryKey( lInvLocationKey ).getSupplyLocation();

         if ( isLocal( aInvKey ) ) {
            assertPartRequestHasStatus( aRequestKey, PRAVAIL );
         } else if ( lSupplyLocationKey.equals( iHubLocation ) ) {
            assertPartRequestHasStatus( aRequestKey, PRREMOTE );
         }
      } else {
         assertPartRequestHasStatus( aRequestKey, PROPEN );
      }
   }


   /*
    *
    * Assert that a part request has reserved the more eligible of two inventory
    *
    */
   private void assertExpectedReservation( PartRequestKey aRequest, double aRequestedQuantity,
         InventoryData aFirst, InventoryData aSecond ) {

      int lComparison =
            compareInventoryForPartRequest( aRequest, aRequestedQuantity, aFirst, aSecond );

      if ( lComparison == 0 ) {
         assertReservation( aRequest, null );
      } else if ( lComparison < 0 ) {
         assertReservation( aRequest, aFirst.lInvKey );
      } else {
         assertReservation( aRequest, aSecond.lInvKey );
      }
   }


   /**
    *
    * Here are the logical rules for determining inventory eligibility for a part request. Assume
    * the inventory is eligible (same part number, not in a locked reservation, found, etc...).
    * Apply these rules to determine the more eligible of two inventory.
    *
    * <pre>
    *  Order of resolution:
    *  0) Don't reserve an unacceptable inventory (in particular, one with an existing AOG reservation)
    *  1) Reserve from preferred locations before other unpreferred local locations, and unpreferred local locations before before remotely
    *  2) Reserve an unreserved inventory before breaking another (lower-ranking) part request
    *  3) If there are no unreserved inventory, steal from the lowest-ranking unlocked part request, else continue (there is available inventory)
    *  4) Take from a big enough batch before a batch that is too small to fill the part request
    *  5) If there are no big enough batches, take whichever is larger, else continue (there is big enough inventory, or there are batches too small but they are the same size)
    *  6) Vendor owned before local
    *  7) Higher inventory condition before lower
    *  8) Earlier expiry before later
    *  9) Earlier received date before later
    * 10) Lower available quantity before higher
    * 11) Finally, lower inv_no_id before higher
    * </pre>
    *
    * Returns -1 if the part request would reserve the first of two inventory, or +1 if the second.
    * Returns zero only if neither were acceptable.
    */
   private int compareInventoryForPartRequest( PartRequestKey aRequest, double lRequestedQuantity,
         InventoryData aFirst, InventoryData aSecond ) {

      // Step 0) Don't reserve an unacceptable inventory (in particular, one with an existing AOG
      // reservation)
      boolean lIsFirstAcceptable = isAcceptableForReservation( aFirst );
      boolean lIsSecondAcceptable = isAcceptableForReservation( aSecond );
      if ( lIsFirstAcceptable && !lIsSecondAcceptable )
         return -1;
      else if ( lIsSecondAcceptable && !lIsFirstAcceptable )
         return 1;
      else if ( !lIsFirstAcceptable && !lIsSecondAcceptable )
         return 0;

      // At this point, both are acceptable for reservation.
      // Step 1) Reserve from preferred locations before other unpreferred local locations, and
      // unpreferred local locations before before remotely
      if ( isPreferred( aFirst ) && !isPreferred( aSecond ) )
         return -1;
      else if ( isPreferred( aSecond ) && !isPreferred( aFirst ) )
         return 1;
      else if ( ( isLocal( aFirst.lInvKey ) && !isLocal( aSecond.lInvKey ) ) )
         return -1;
      else if ( ( isLocal( aSecond.lInvKey ) && !isLocal( aFirst.lInvKey ) ) )
         return 1;

      // Step 2) Reserve an unreserved inventory before breaking another (lower-ranking) part
      // request
      if ( !isReserved( aFirst ) && isReserved( aSecond ) )
         return -1;
      else if ( !isReserved( aSecond ) && isReserved( aFirst ) )
         return 1;

      // Step 3) If there are no unreserved inventory, steal from the lowest-ranking unlocked part
      // request, else continue (there is available inventory)
      if ( isReserved( aFirst ) && isReserved( aSecond ) ) {

         return 1; /*
                    * aSecondRank is lower than aFirstRank, i.e. aSecond is better for stealing,
                    * which will be TRUE in these theories only because the first part request's key
                    * is lower
                    */
      }

      // At this point, both are unreserved
      // Step 4) Take from a big enough batch before a batch that is too small to fill the part
      // request
      if ( isBigEnough( lRequestedQuantity, aFirst )
            && !isBigEnough( lRequestedQuantity, aSecond ) )
         return -1;
      else if ( isBigEnough( lRequestedQuantity, aSecond )
            && !isBigEnough( lRequestedQuantity, aFirst ) )
         return 1;

      // At this point, both are big enough or neither is big enough.
      // Step 5) If there are no big enough batches, take whichever is larger, else continue
      if ( !isBigEnough( lRequestedQuantity, aFirst )
            && !isBigEnough( lRequestedQuantity, aSecond ) ) {

         // If neither batch is big enough take the larger
         if ( aFirst.lQuantity > aSecond.lQuantity )
            return -1;
         else if ( aSecond.lQuantity > aFirst.lQuantity )
            return 1;
      }

      // At this point both are big enough or (neither is big enough and they have the same
      // quantity)
      // Next criteria are:
      // Step 6) Vendor owned before local
      // Step 7) Higher inventory condition before lower
      // Step 8) Earlier expiry before later
      // Step 9) Earlier received date before later

      // Step 10) Reserve from lower available quantity before higher
      if ( aFirst.lQuantity < aSecond.lQuantity )
         return -1;
      else if ( aSecond.lQuantity < aFirst.lQuantity )
         return 1;

      // Step 11) Reserve a lower inv_no_id before higher
      return aFirst.lInvKey.getId() < aSecond.lInvKey.getId() ? -1 : 1;
   }


   /*
    * Returns whether the argument is eligible for reservation
    */
   private boolean isAcceptableForReservation( InventoryData aInvData ) {
      return !ReservationMode.EXISTING_AOG.equals( aInvData.lExistingReservationMode );

   }


   /*
    * Returns true if the inventory is at the same supply location as the part request
    */
   private boolean isLocal( InventoryKey aInvKey ) {
      LocationKey lSupplyLocationKey =
            InvLocTable.findByPrimaryKey( new InvInv( aInvKey ).getLocation() ).getSupplyLocation();
      return lSupplyLocationKey.equals( iAirportLocation );
   }


   /*
    * Returns whether the first argument is the preferred store and the second is not
    */
   private boolean isPreferred( InventoryData aInvData ) {
      return SupplyLocationMode.PREFSRVSTORE.equals( aInvData.lLocationMode );
   }


   /*
    * Returns whether the argument only is reserved at all
    */
   private boolean isReserved( InventoryData aInvData ) {
      return !ReservationMode.UNRESERVED.equals( aInvData.lExistingReservationMode );
   }


   /*
    * Returns whether the second argument is big enough to satisfy the first and the third is not
    */
   private boolean isBigEnough( double aRequestedAmount, InventoryData aInvData ) {
      return aInvData.lQuantity >= aRequestedAmount;
   }


   /*
    * Returns the RefReqPriorityKey given a ExistingReservationEnum
    */
   private RefReqPriorityKey getReqPriority( ReservationMode aExistingReservationEnum ) {
      if ( ReservationMode.EXISTING_AOG.equals( aExistingReservationEnum ) ) {
         return RefReqPriorityKey.AOG;
      } else if ( ReservationMode.EXISTING_LOW_PRIORITY.equals( aExistingReservationEnum ) ) {
         return RefReqPriorityKey.NORMAL;
      } else {
         return null;
      }
   }


   /*
    * Returns the RefEventStatusKey given a ReservationMode
    */
   private RefEventStatusKey getReservedEventStatus( InventoryKey aInventoryKey,
         ReservationMode aExistingReservationEnum ) {
      if ( ReservationMode.UNRESERVED.equals( aExistingReservationEnum ) ) {
         return RefEventStatusKey.PROPEN;
      } else {
         if ( isLocal( aInventoryKey ) ) {
            return RefEventStatusKey.PRAVAIL;
         } else {
            return RefEventStatusKey.PRREMOTE;
         }
      }
   }


   /*
    * Returns the ReservedStatus given a ExistingReservationEnum
    */
   private ReservedStatus getReservedStatus( ReservationMode aExistingReservationEnum ) {
      if ( ReservationMode.UNRESERVED.equals( aExistingReservationEnum ) ) {
         return ReservedStatus.UNRESERVED;
      } else {
         return ReservedStatus.RESERVED;
      }
   }


   // get a query set req_part_table for PartA
   // i.e. SELECT * FROM req_part WHERE req_spec_part_no_id = iPartA.getId()
   private QuerySet getReqPartForPartA() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "req_spec_part_no_id", iPartA.getId() );
      return QuerySetFactory.getInstance().executeQueryTable( "req_part", lArgs );
   }


   /*
    * A value object to hold some data about an inventory
    */
   final class InventoryData {

      InventoryKey lInvKey;
      double lQuantity;
      SupplyLocationMode lLocationMode;
      ReservationMode lExistingReservationMode;


      public InventoryData(InventoryKey aInvKey, Double aQuantity, SupplyLocationMode aLocationMode,
            ReservationMode aExistingReservationMode) {
         lInvKey = aInvKey;
         lQuantity = aQuantity;
         lLocationMode = aLocationMode;
         lExistingReservationMode = aExistingReservationMode;
      }
   }
}
