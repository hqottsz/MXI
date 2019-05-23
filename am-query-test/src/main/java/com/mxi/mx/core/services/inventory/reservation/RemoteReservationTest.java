package com.mxi.mx.core.services.inventory.reservation;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.services.req.PartRequestService;
import com.mxi.mx.core.table.ref.RefQtyUnit;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.req.ReqPart;


/**
 * Testing the
 * {@link InventoryReservationService#createRemoteReservation(PartRequestKey, PartNoKey, LocationKey, HumanResourceKey, boolean)}
 * method.
 *
 */
public class RemoteReservationTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final RefInvClassKey BATCH = RefInvClassKey.BATCH;
   private InventoryReservationService iInventoryReservationService;
   private LocationKey iSupplyLocation;
   private LocationKey iRemoteLocation;
   private HumanResourceKey iHr;
   private RefQtyUnitKey iLiterUnit;


   /**
    *
    * Test reserving a batch part request which has requested quantity of less than one (0 <
    * quantity < 1) and there is exactly the correct quantity of inventory to fulfill the request.
    *
    * @throws Exception
    */
   @Test
   public void testBatchReservingQuantityLessThanOne() throws Exception {

      // create a part
      PartNoKey lPart = new PartNoBuilder().withOemPartNo( "PART1" ).withInventoryClass( BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withUnitType( iLiterUnit ).withDefaultPartGroup()
            .build();

      // create a part request with 0 < requested quantity < 1
      double lRequestedQuantity = 0.11;
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( lRequestedQuantity ).requiredBy( new Date() )
            .isNeededAt( iSupplyLocation ).forSpecifiedPart( lPart ).build();

      // create an inventory item with enough quantity to fulfill the request
      InventoryKey lInventory = new InventoryBuilder().withClass( BATCH ).withPartNo( lPart )
            .withCondition( RefInvCondKey.RFI ).atLocation( iRemoteLocation )
            .withBinQt( lRequestedQuantity ).withReserveQt( 0.0 ).build();

      iInventoryReservationService.createRemoteReservation( lPartRequest, lPart, iRemoteLocation,
            iHr, false );

      // assert inventory was reserved
      new ReqPart( lPartRequest ).assertInventoryKey( lInventory );

      // assert part request status is PRREMOTE
      new EvtEventUtil( lPartRequest.getEventKey() )
            .assertEventStatus( RefEventStatusKey.PRREMOTE );

   }


   /**
    *
    * Test reserving a batch part request when there is not enough inventory to fulfill the request.
    * A new request should be created for the remaining quantity.
    *
    * @throws Exception
    */
   @Test
   public void testBatchReservingQuantityNotEnough() throws Exception {

      // create a part
      PartNoKey lPart = new PartNoBuilder().withOemPartNo( "PART1" ).withInventoryClass( BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withUnitType( iLiterUnit ).withDefaultPartGroup()
            .build();

      // create a part request
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 12.34 ).requiredBy( new Date() ).isNeededAt( iSupplyLocation )
            .forSpecifiedPart( lPart ).build();

      // create an inventory item with not enough quantity to fulfill the request
      InventoryKey lInventory = new InventoryBuilder().withClass( BATCH ).withPartNo( lPart )
            .withCondition( RefInvCondKey.RFI ).atLocation( iRemoteLocation ).withBinQt( 12.20 )
            .withReserveQt( 0.0 ).build();

      PartRequestKey lSplitRequest = iInventoryReservationService
            .createRemoteReservation( lPartRequest, lPart, iRemoteLocation, iHr, false );

      // assert inventory was reserved for the request
      new ReqPart( lPartRequest ).assertInventoryKey( lInventory );
      new EvtEventUtil( lPartRequest.getEventKey() )
            .assertEventStatus( RefEventStatusKey.PRREMOTE );

      // assert new request created (split) for remaining quantity
      new ReqPart( lSplitRequest ).assertReqQt( 0.14 );
      new EvtEventUtil( lSplitRequest.getEventKey() ).assertEventStatus( RefEventStatusKey.PROPEN );

   }


   /**
    *
    * Test reserving a batch part request when there is enough inventory to fulfill the request
    * between two batches at the same location. The request will be split into two requests and each
    * one should be reserved.
    *
    * @throws Exception
    */
   @Test
   public void testBatchReservingQuantityEnoughWithTwoBatches() throws Exception {

      // create a part
      PartNoKey lPart = new PartNoBuilder().withOemPartNo( "PART1" ).withInventoryClass( BATCH )
            .withStatus( RefPartStatusKey.ACTV ).withUnitType( iLiterUnit ).withDefaultPartGroup()
            .build();

      // create a part request
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 12.34 ).requiredBy( new Date() ).isNeededAt( iSupplyLocation )
            .forSpecifiedPart( lPart ).build();

      // create an inventory item with not enough quantity to fulfill the request
      new InventoryBuilder().withClass( BATCH ).withPartNo( lPart )
            .withCondition( RefInvCondKey.RFI ).atLocation( iRemoteLocation ).withBinQt( 12.20 )
            .withReserveQt( 0.0 ).build();

      // create another inventory item for the remaining quantity
      new InventoryBuilder().withClass( BATCH ).withPartNo( lPart )
            .withCondition( RefInvCondKey.RFI ).atLocation( iRemoteLocation ).withBinQt( 0.14 )
            .withReserveQt( 0.0 ).build();

      iInventoryReservationService.createRemoteReservation( lPartRequest, lPart, iRemoteLocation,
            iHr, false );

      // assert inventory was reserved for both requests
      List<PartRequestKey> lRequests =
            new PartRequestService().getPartRequestsSharingMasterId( lPartRequest );
      assertEquals( "there should be 2 requests", 2, lRequests.size() );
      for ( PartRequestKey lRequest : lRequests ) {
         new EvtEventUtil( lRequest.getEventKey() ).assertEventStatus( RefEventStatusKey.PRREMOTE );
      }

   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {

      iSupplyLocation = new LocationDomainBuilder().withCode( "YYZ" ).withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      iRemoteLocation = new LocationDomainBuilder().withCode( "ATL" ).withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      iHr = new HumanResourceDomainBuilder().build();

      // create a quantity unit with 2 decimal places
      RefQtyUnit lLiters = RefQtyUnit.create( "L" );
      lLiters.setDecimalPlacesQt( 2 );
      iLiterUnit = lLiters.insert();

      iInventoryReservationService = new InventoryReservationService();

   }

}
