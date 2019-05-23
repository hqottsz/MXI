package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertInventoryReservedForPartRequest;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestHasStatus;
import static com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationAssertionUtils.assertPartRequestIsOpen;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartRequestUtils.createPartRequest;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.mx.core.dao.location.BasicAirport;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.services.inventory.reservation.auto.CreateInventoryUtils.ReservedStatus;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.table.req.ReqPart;


/**
 * Testing auto-reservation of SER inventory, especially SerAutoReservationService, with preferred
 * and ignore locations
 *
 */
public class AutoReservationSerTest extends AbstractAutoReservationTest {

   /*
    * Maintenance is done at Line Maintenance, North Hanger, South Hanger.
    *
    * Line Store, NorthWarehouse, South Warehouse are preferred locations for Line Maintenance,
    * North Hanger, and South Hanger respectively.
    *
    * Line Store is an ignored location. Main Warehouse is a non-preferred location.
    */
   private BasicAirport iBasicAirport;

   protected InventoryKey iInventory1;
   protected InventoryKey iInventory2;


   /*
    * Test Line part request reserves INSPREQ inventory from Line Store rather than RFI inventory at
    * Main Warehouse
    */
   @Test
   public void testPartReqReserveFromPreferredLocation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // two inventories at Line Store, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtLineStore =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.UNRESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );
      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // one part request to be fulfilled
      Date lWhenNeeded = getDate( 5 );
      RefEventStatusKey lInitialStatus = PROPEN;
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, iPriority, lInitialStatus, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that inventory at Line Store is reserved and the PR status is INSPREQ
      assertInventoryReservedForPartRequest( lPartRequestAtLine, lInvINSPREQAtLineStore );
      assertPartRequestHasStatus( lPartRequestAtLine, RefEventStatusKey.PRINSPREQ );
   }


   /*
    * Test AOG part request will steal reserved inventory from the NORMAL part request
    */
   @Test
   public void testAOGPartReqStealReservedInventory() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // one inventory at North Warehouse
      InventoryKey lInventoryAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      // one fulfilled PR at Line
      Date lWhenNeeded = getDate( 5 );
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventoryAtNorthWarehouse );

      // one open AOG PR at Line
      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that AOG PR will steal inventory from normal PR
      assertInventoryReservedForPartRequest( lAOGPartRequestAtLine, lInventoryAtNorthWarehouse );
      assertPartRequestIsOpen( lPartRequestAtLine );
   }


   /*
    * Test AOG part request will not steal reserved inventory from the locked part request
    */
   @Test
   public void testAOGPartReqNotStealLockedReservation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // one inventory at North Warehouse
      InventoryKey lInventoryAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      // one fulfilled PR at Line
      Date lWhenNeeded = getDate( 5 );
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventoryAtNorthWarehouse );
      // set the lock boolean to make it a locked reservation
      ReqPartTable lReqPartTable = ReqPartTable.findByPrimaryKey( lPartRequestAtLine );
      lReqPartTable.setLockReserveBool( true );
      lReqPartTable.update();

      // one open AOG PR at Line
      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that AOG PR will not steal inventory from locked reservation
      assertPartRequestIsOpen( lAOGPartRequestAtLine );
      assertInventoryReservedForPartRequest( lPartRequestAtLine, lInventoryAtNorthWarehouse );
   }


   /*
    * Test that North Hanger AOG part request will not steal reserved inventory in Line Store
    */
   @Test
   public void testAOGPartReqNotStealReservedInvFromIgnoredLoc() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // one inventory at North Warehouse
      InventoryKey lInventoryAtLineStore =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );

      // one fulfilled PR at Line
      Date lWhenNeeded = getDate( 5 );
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventoryAtLineStore );

      // one open AOG part request at North Warehouse
      PartRequestKey lAOGPartRequestAtNorthHanger = createPartRequest( iPartA,
            iBasicAirport.iNorthHangar, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that North Hanger AOG part request will not steal reserved inventory from Line
      // Store
      assertPartRequestIsOpen( lAOGPartRequestAtNorthHanger );
      assertInventoryReservedForPartRequest( lPartRequestAtLine, lInventoryAtLineStore );
   }


   /*
    * Test that inventory at preferred location is reserved
    *
    * The PR is based on Part Group, and the two inventories are of Part No and Alternate Part No
    * respectively
    */
   @Test
   public void testPartReqReserveFromPrefThanFromNonPrefLoc() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );
      InventoryKey lInvINSPREQAtLineStore =
            CreateInventoryUtils.createInventory( iPartAlternate, RefInvCondKey.INSPREQ,
                  ReservedStatus.UNRESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );

      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lPartRequestAtLine =
            createPartRequest( iPartGroupA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded,
                  iPriority, PRAVAIL, lInvRFIAtMainWarehouse );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that inventory at preferred location is reserved
      assertInventoryReservedForPartRequest( lPartRequestAtLine, lInvINSPREQAtLineStore );

   }


   /*
    * Test that higher ranking RFI inventory at Line Store Bin is reserved
    *
    * The PR is based on Part Group, and the two inventories are of Part No and Alternate Part No
    * respectively
    */
   @Test
   public void testPartReqReserveFromPrefLocRFIOverINSPREQ() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // three inventories at Line Store Bin, Line Store Bin, and Main Warehouse respectively
      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvINSPREQAtLineStoreBin =
            CreateInventoryUtils.createInventory( iPartAlternate, RefInvCondKey.INSPREQ,
                  ReservedStatus.UNRESERVED, iBasicAirport.iLineStoreBin, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtLineStoreBin =
            CreateInventoryUtils.createInventory( iPartAlternate, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iLineStoreBin, iLocalOwner, 1.0 );

      // one open part request
      Date lWhenNeeded = getDate( 5 );
      RefEventStatusKey lInitialStatus = PROPEN;
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartGroupA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority, lInitialStatus, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that higher ranking RFI inventory at Line Store Bin is reserved
      assertInventoryReservedForPartRequest( lPartRequestAtLine, lInvRFIAtLineStoreBin );

   }


   /*
    * Test Line AOG part request steals inventory from North Warehouse rather than reserve available
    * inventory from Main Warehouse
    */
   @Test
   public void testAOGPartReqStealFromSecondPrefLocation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // two inventories at Line Store, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );
      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // one reserved part request and one open AOG PR at Line Maintenance
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtNorthWarehouse );

      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that PR at Line reserves North Warehouse INSPREQ inventory rather than Main
      // Warehouse RFI one
      assertInventoryReservedForPartRequest( lAOGPartRequestAtLine, lInvINSPREQAtNorthWarehouse );
   }


   /*
    * Test Line South Hanger AOG part request doesn't steals inventory from North Warehouse when
    * inventory available at Main Warehouse
    */
   @Test
   public void testAOGPartReqNotStealFromSecondPrefLocation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // two inventories at North Warehouse, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );
      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // one reserved part request at Line Maintenance and one open AOG PR at South Hanger
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtNorthWarehouse );

      PartRequestKey lAOGPartRequestAtSouthHanger = createPartRequest( iPartA,
            iBasicAirport.iSouthHangar, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN
      assertInventoryReservedForPartRequest( lNormalPartRequestAtLine,
            lInvINSPREQAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lAOGPartRequestAtSouthHanger, lInvRFIAtMainWarehouse );
   }


   /*
    * Test part request reserves inventory from the preferred location rather than inventory on the
    * top of available inventory list.
    */
   @Test
   public void testPartReqReserveFromFirstPreferredLocationThanFromSecondOne() throws Exception {

      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // two inventories at Line Store, and North Warehouse respectively
      InventoryKey lInvINSPREQ =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.UNRESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );
      InventoryKey lInvRFI = CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
            ReservedStatus.UNRESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      // one PR at Line Maintenance
      Date lWhenNeeded = getDate( 5 );
      RefEventStatusKey lInitialStatus = PROPEN;
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lPartRequest = createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0,
            lWhenNeeded, iPriority, lInitialStatus, null );

      // do auto-reservation
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // assert that inventory at preferred location is reserved and the PR status is INSPREQ
      assertInventoryReservedForPartRequest( lPartRequest, lInvINSPREQ );
      assertPartRequestHasStatus( lPartRequest, RefEventStatusKey.PRINSPREQ );
   }


   /*
    * Test part request reserves best match out of available inventory list when no preferred
    * location setup for airport
    */
   @Test
   public void testPartReqReservesBestMatchWhenNoPreferredLocationSetup() throws Exception {

      setUpTheoryParts( RefInvClassKey.SER );

      LocationKey lAirportLocation = new LocationDomainBuilder().withCode( "TEST" )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      LocationKey lStore =
            new LocationDomainBuilder().withCode( "STORE1" ).withType( RefLocTypeKey.SRVSTORE )
                  .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

      LocationKey lLine = new LocationDomainBuilder().withCode( "LINE1" ).withType( RefLocTypeKey.LINE )
            .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

      // two inventories one RFI and other inspection required
      InventoryKey lInvINSPREQ = CreateInventoryUtils.createInventory( iPartA,
            RefInvCondKey.INSPREQ, ReservedStatus.UNRESERVED, lStore, iLocalOwner, 1.0 );
      InventoryKey lInvRFI = CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
            ReservedStatus.UNRESERVED, lLine, iLocalOwner, 1.0 );

      // one part request at line with due date as current time
      PartRequestKey lPartRequest = createPartRequest( iPartA, lLine, 1.0, new Date(),
            RefReqPriorityKey.NORMAL, PROPEN, null );

      // call auto-reservation
      callAutoReservationLogic( lAirportLocation, AutoReservationRequestMode.PART_NO );

      // assert that RFI inventory got reserved and reservation was locked since due date is within
      // 24 hour (print_ticket_interval config param default value)
      assertInventoryReservedForPartRequest( lPartRequest, lInvRFI );
      assertPartRequestHasStatus( lPartRequest, RefEventStatusKey.PRAVAIL );
      assertTrue( new ReqPart( lPartRequest ).getLockReserveBool() );
   }


   /*
    * Test that PR will not steal reserved inventory from fulfilled PR of the same ranking
    */
   @Test
   public void testNewPartReqNotStealFromPartReqOfHigherRanking() throws Exception {

      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // one inventory at Main Warehouse
      InventoryKey lInvRFIAtMain = CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
            ReservedStatus.RESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // one fulfilled PR and one open PR of the same-ranking at Line Maintenance
      Date lWhenNeeded = getDate( 5 );
      RefEventStatusKey lInitialStatus = PROPEN;
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, iPriority, PRAVAIL, lInvRFIAtMain );

      PartRequestKey lSameRankingPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority, lInitialStatus, null );

      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // assert that same ranking PR will be still OPEN
      assertInventoryReservedForPartRequest( lPartRequestAtLine, lInvRFIAtMain );
      assertPartRequestIsOpen( lSameRankingPartRequestAtLine );

   }


   /*
    * Test that AOG part request will steal reserved inventory in preferred location even if the
    * location is ignored for the supply location
    *
    */
   @Test
   public void testAOGPartReqStealReservedInvFromPrefLocIgnored() throws Exception {

      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // one inventory at North Warehouse
      InventoryKey lInventoryAtNorth =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      // one fulfilled PR at North Hanger
      Date lWhenNeeded = getDate( 5 );
      PartRequestKey lPartRequestAtLine = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, RefReqPriorityKey.NORMAL, PRAVAIL, lInventoryAtNorth );

      // one open AOG PR at North Hanger
      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // assert that AOG part request will steal reserved inventory from lower-ranking PR
      assertPartRequestIsOpen( lPartRequestAtLine );
      assertInventoryReservedForPartRequest( lAOGPartRequestAtLine, lInventoryAtNorth );
   }


   /*
    * Test higher-ranking AOG part request will steal reserved inventory from another AOG part
    * request
    */
   @Test
   public void testAOGPartReqStealFromAOGPartReqNeededLater() throws Exception {

      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // one inventory at South Warehouse
      InventoryKey lInventoryAtSouth =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iSouthWarehouse, iLocalOwner, 1.0 );

      // one fulfilled AOG PR at South Warehouse
      Date lWhenNeeded = getDate( 5 );
      PartRequestKey lAOGPartRequest = createPartRequest( iPartA, iBasicAirport.iSouthHangar, 1.0,
            lWhenNeeded, RefReqPriorityKey.AOG, PRAVAIL, lInventoryAtSouth );

      // one open AOG PR with higher-ranking at North Hanger
      lWhenNeeded = getDate( 3 );
      PartRequestKey lNewAOGPartRequest = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // assert that higher-ranking AOG PR will steal inventory from previous AOG PR
      assertInventoryReservedForPartRequest( lNewAOGPartRequest, lInventoryAtSouth );
      assertPartRequestIsOpen( lAOGPartRequest );
   }


   /*
    * Test higher-ranking AOG PR will steal inventory from lower-ranking AOG PR
    */
   @Test
   public void testAOGPartReqStealFromAOGPartReqWhenInvINSPREQ() throws Exception {

      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // two inventory RFI at preferred location
      InventoryKey lInventoryRFIAtLine =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );

      // one inventory INSPREQ at non-preferred location
      InventoryKey lInventoryINSPREQAtMain =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // one fulfilled AOG PR at Line Maintenance
      Date lWhenNeeded = getDate( 5 );
      PartRequestKey lAOGPartRequest = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, RefReqPriorityKey.AOG, PRAVAIL, lInventoryRFIAtLine );

      // one open AOF PR of higher-ranking at Line
      lWhenNeeded = getDate( 3 );
      PartRequestKey lNewAOGPartRequest = createPartRequest( iPartA, iBasicAirport.iLineMaintenance,
            1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // assert that higher-ranking AOG PR will steal inventory from lower-ranking AOG PR
      assertInventoryReservedForPartRequest( lNewAOGPartRequest, lInventoryRFIAtLine );
      assertInventoryReservedForPartRequest( lAOGPartRequest, lInventoryINSPREQAtMain );
   }


   /*
    * Test Line AOG part request steals inventory from Line Store rather than from North Warehouse
    */
   @Test
   public void testAOGPartReqStealFromFirstPrefLocation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // three inventories at Line Store, North Warehouse, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtLineStore =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );

      InventoryKey lInvINSPREQAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // two reserved part requests at Line Maintenance, and North Hanger respectively
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtLineStore );

      lWhenNeeded = getDate( 3 );
      PartRequestKey lHigherRankingPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtNorthWarehouse );

      // one AOG part request to be fulfilled
      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that part requests reserve from most eligible inventories starting from Line
      // Store
      assertInventoryReservedForPartRequest( lAOGPartRequestAtLine, lInvINSPREQAtLineStore );
      assertInventoryReservedForPartRequest( lHigherRankingPartRequestAtLine,
            lInvINSPREQAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lNormalPartRequestAtLine, lInvRFIAtMainWarehouse );
   }


   /*
    * Test North Hanger AOG part request steal inventory from North Warehouse
    */
   @Test
   public void testNorthAOGPartReqStealFromNorthWarehouse() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // three inventories at Line Store, North Warehouse, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtLineStore =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );

      InventoryKey lInvINSPREQAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // two reserved part requests at Line Maintenance, and North Hanger respectively
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtLineStore );

      lWhenNeeded = getDate( 3 );
      PartRequestKey lHigherRankingPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtNorthWarehouse );

      // one AOG part request at North Hanger to be fulfilled
      PartRequestKey lAOGPartRequestAtNorthHanger = createPartRequest( iPartA,
            iBasicAirport.iNorthHangar, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that North Hanger AOG PR reserves North Warehouse inventory rather than from
      // Line Store, the ignored location for North Hanger
      assertInventoryReservedForPartRequest( lAOGPartRequestAtNorthHanger,
            lInvINSPREQAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lHigherRankingPartRequestAtLine,
            lInvINSPREQAtLineStore );
      assertInventoryReservedForPartRequest( lNormalPartRequestAtLine, lInvRFIAtMainWarehouse );
   }


   /*
    * Test Line AOG part request steals inventory from Line Store rather than from North Warehouse
    */
   @Test
   public void testAOGPartReqStealFromFirstPrefLocationEvenWhenInventoryAvailalbeAtOtherLocations()
         throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // three inventories at Line Store, North Warehouse, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtLineStore =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );
      InventoryKey lInvINSPREQAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.UNRESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );
      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // one reserved part request and one unfulfilled PR at Line Maintenance
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtLineStore );

      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN
      assertInventoryReservedForPartRequest( lAOGPartRequestAtLine, lInvINSPREQAtLineStore );
      assertInventoryReservedForPartRequest( lNormalPartRequestAtLine,
            lInvINSPREQAtNorthWarehouse );
   }


   /*
    * Test North Hanger AOG part request reserves inventory from North Warehouse
    */
   @Test
   public void testAOGPartReqReserveFromPreferredLocation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // three inventories at Line Store, North Warehouse, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtLineStore =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );
      InventoryKey lInvINSPREQAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.UNRESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );
      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // one reserved part request at Line Maintenance, and one unfulfilled PR at North Warehouse
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtLineStore );

      PartRequestKey lAOGPartRequestAtNorthHanger = createPartRequest( iPartA,
            iBasicAirport.iNorthHangar, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN
      assertInventoryReservedForPartRequest( lAOGPartRequestAtNorthHanger,
            lInvINSPREQAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lNormalPartRequestAtLine, lInvINSPREQAtLineStore );
   }


   /*
    * Test Line AOG part request steals inventory from Line Store rather than from North Warehouse
    */
   @Test
   public void testAOGPartReqStealFromSecondPrefLocation2() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // three inventories at Line Store, North Warehouse, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtNorthHanger =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.UNRESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // two reserved part requests at Line
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtNorthHanger );

      lWhenNeeded = getDate( 3 );
      PartRequestKey lHigherRankingPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvRFIAtNorthWarehouse );

      // one Line AOG part request to be fulfilled
      PartRequestKey lAOGPartRequestAtLine = createPartRequest( iPartA,
            iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, RefReqPriorityKey.AOG, PROPEN, null );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that part requests reserve from more eligible inventories
      assertInventoryReservedForPartRequest( lAOGPartRequestAtLine, lInvRFIAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lHigherRankingPartRequestAtLine,
            lInvINSPREQAtNorthHanger );
      assertInventoryReservedForPartRequest( lNormalPartRequestAtLine, lInvRFIAtMainWarehouse );
   }


   /*
    * Test invalid reservation happens at the first preferred location.
    *
    */
   @Test
   public void testInvalidReservationAtFirstPrefLocation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // four inventories at Line Store, North Warehouse, North Warehouse, and Main Warehouse
      // respectively
      InventoryKey lInvINSPREQAtLineStore =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iLineStore, iLocalOwner, 1.0 );

      InventoryKey lInvINSPREQAtNorthHanger =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // four reserved part requests at Line Maintenance
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lNormalPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtLineStore );

      lWhenNeeded = getDate( 4 );
      PartRequestKey lHigherOrderRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvINSPREQAtNorthHanger );

      lWhenNeeded = getDate( 3 );
      PartRequestKey lSecondHighestPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvRFIAtNorthWarehouse );

      lWhenNeeded = getDate( 2 );
      PartRequestKey lHighestPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded,
                  RefReqPriorityKey.AOG, PRAVAIL, lInvRFIAtMainWarehouse );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that all PR will be re-fulfilled as AOG PR has an invalid reservation as there
      // is inventory available at Line Store, the first preferred location
      assertInventoryReservedForPartRequest( lHighestPartRequestAtLine, lInvINSPREQAtLineStore );
      assertInventoryReservedForPartRequest( lSecondHighestPartRequestAtLine,
            lInvRFIAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lHigherOrderRequestAtLine, lInvINSPREQAtNorthHanger );
      assertInventoryReservedForPartRequest( lNormalPartRequestAtLine, lInvRFIAtMainWarehouse );
   }


   /*
    * Test invalid reservation happens at the second preferred location.
    *
    */
   @Test
   public void testInvalidReservationAtSecondPrefLocation() throws Exception {

      // GIVEN
      setUpLocations();

      setUpTheoryParts( RefInvClassKey.SER );

      // three inventories at North Warehouse, North Warehouse, and Main Warehouse respectively
      InventoryKey lInvINSPREQAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.INSPREQ,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtNorthWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iNorthWarehouse, iLocalOwner, 1.0 );

      InventoryKey lInvRFIAtMainWarehouse =
            CreateInventoryUtils.createInventory( iPartA, RefInvCondKey.RFI,
                  ReservedStatus.RESERVED, iBasicAirport.iMainWarehouse, iLocalOwner, 1.0 );

      // three part requests in descending order at Line Maintenance with reservation at North
      // Warehouse, Main Warehouse, and North Warehouse respectively.
      // The second par request has invalid reservation as there is inventory available at second
      // preferred location. Unreserving and then reserving will happen from the second PR.
      Date lWhenNeeded = getDate( 5 );
      RefReqPriorityKey iPriority = RefReqPriorityKey.NORMAL;
      PartRequestKey lHigherOrderRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvRFIAtNorthWarehouse );

      lWhenNeeded = getDate( 3 );
      PartRequestKey lSecondHighestPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded, iPriority,
                  PRAVAIL, lInvRFIAtMainWarehouse );

      lWhenNeeded = getDate( 2 );
      PartRequestKey lHighestPartRequestAtLine =
            createPartRequest( iPartA, iBasicAirport.iLineMaintenance, 1.0, lWhenNeeded,
                  RefReqPriorityKey.AOG, PRINSPREQ, lInvINSPREQAtNorthWarehouse );

      // WHEN
      callAutoReservationLogic( iBasicAirport.iAirportLocation,
            AutoReservationRequestMode.PART_NO );

      // THEN: assert that PR1 keeps reservation while PR2 and PR3 re-reserve eligible inventories
      assertInventoryReservedForPartRequest( lHighestPartRequestAtLine,
            lInvINSPREQAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lSecondHighestPartRequestAtLine,
            lInvRFIAtNorthWarehouse );
      assertInventoryReservedForPartRequest( lHigherOrderRequestAtLine, lInvRFIAtMainWarehouse );
   }


   public void setUpLocations() throws Exception {

      iBasicAirport = new BasicAirport.Builder().build();

   }

}
