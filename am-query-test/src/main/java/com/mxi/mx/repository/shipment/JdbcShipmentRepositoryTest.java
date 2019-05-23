package com.mxi.mx.repository.shipment;

import static com.mxi.mx.core.key.RefShipSegmentStatusKey.PENDING;
import static com.mxi.mx.core.key.RefShipSegmentStatusKey.PLAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.ShipSegmentMapKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.ship.ShipSegmentMapTable;
import com.mxi.mx.domain.shipment.Segment;
import com.mxi.mx.domain.shipment.Shipment;


public class JdbcShipmentRepositoryTest {

   private static final JdbcShipmentRepository JDBC_SHIPMENT_REPOSITORY =
         new JdbcShipmentRepository();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private PurchaseOrderKey iOrder;


   @Test
   public void getPOShipments_withACompletedOutboundShipment() {
      ShipmentKey lShipment = Domain.createShipment( shipment -> {
         shipment.setPurchaseOrder( iOrder );
         shipment.setStatus( RefEventStatusKey.IXCMPLT );
         shipment.addShipmentSegment( segment1 -> {
            segment1.setFromLocation( Domain.createLocation() );
         } );
      } );

      List<Shipment> lShipmentList = JDBC_SHIPMENT_REPOSITORY.getShipments( iOrder );

      assertShipment( lShipmentList, lShipment, RefEventStatusKey.IXCMPLT, false );
   }


   @Test
   public void getPOShipments_withAPendingInboundShipment() {
      LocationKey lVendorLocation = Domain
            .createLocation( aVendorLocation -> aVendorLocation.setType( RefLocTypeKey.VENDOR ) );
      ShipmentKey lShipment = Domain.createShipment( shipment -> {
         shipment.setPurchaseOrder( iOrder );
         shipment.setStatus( RefEventStatusKey.IXPEND );
         shipment.addShipmentSegment( segment1 -> {
            segment1.setFromLocation( lVendorLocation );
         } );
      } );

      List<Shipment> lShipmentList = JDBC_SHIPMENT_REPOSITORY.getShipments( iOrder );

      assertShipment( lShipmentList, lShipment, RefEventStatusKey.IXPEND, true );
   }


   @Test
   public void getShipSegments_withTwoSegments() {
      ShipmentKey lShipment = Domain.createShipment( shipment -> {
         shipment.addShipmentSegment( segment1 -> {
            segment1.setStatus( PENDING );
         } );
         shipment.addShipmentSegment( segment2 -> {
            segment2.setStatus( PLAN );
         } );
      } );

      List<Segment> lShipSegments = JDBC_SHIPMENT_REPOSITORY.getSegments( lShipment );

      assertEquals( 2, lShipSegments.size() );
      assertEquals( RefShipSegmentStatusKey.PENDING, lShipSegments.get( 0 ).getSegmentStatusKey() );
      assertEquals( RefShipSegmentStatusKey.PLAN, lShipSegments.get( 1 ).getSegmentStatusKey() );
   }


   @Test
   public void deleteShipSegment() {
      // this shipment builder will create one segment with a PENDING status
      ShipmentKey lShipment = Domain.createShipment( shipment -> {
         shipment.addShipmentSegment( segment -> {
            segment.setStatus( PENDING );
         } );
      } );
      List<Segment> lShipSegments = JDBC_SHIPMENT_REPOSITORY.getSegments( lShipment );
      ShipSegmentMapTable lShipSegmentMapTable = ShipSegmentMapTable.findByPrimaryKey(
            new ShipSegmentMapKey( lShipment, lShipSegments.get( 0 ).getSegmentKey() ) );
      assertTrue( lShipSegmentMapTable.exists() );
      assertEquals( 1, lShipSegments.size() );

      JDBC_SHIPMENT_REPOSITORY.deleteSegment( Shipment.builder().shipmentKey( lShipment )
            .shipmentStatusKey( RefEventStatusKey.IXPEND ).build(), lShipSegments.get( 0 ) );

      // assert the ship segment and the record in the map table are deleted
      ShipSegmentMapTable lShipSegmentMapTableAfterDelete = ShipSegmentMapTable.findByPrimaryKey(
            new ShipSegmentMapKey( lShipment, lShipSegments.get( 0 ).getSegmentKey() ) );
      assertTrue( !lShipSegmentMapTableAfterDelete.exists() );
      List<Segment> lShipSegmentsAfterDelete = JDBC_SHIPMENT_REPOSITORY.getSegments( lShipment );
      assertEquals( 0, lShipSegmentsAfterDelete.size() );
   }


   /**
    * Assert shipment key, shipment status and whether the shipment is inbound shipment or not.
    *
    * @param aShipmentList
    *           the QuerySet
    * @param aExpectedShipment
    *           the expected shipment key
    * @param aExpectedShipmentStatus
    *           the expected shipment status
    * @param aExpectedIsInboundShipment
    *           the expected whether the shipment is inbound shipment or not
    */
   private void assertShipment( List<Shipment> aShipmentList, ShipmentKey aExpectedShipment,
         RefEventStatusKey aExpectedShipmentStatus, boolean aExpectedIsInboundShipment ) {
      assertEquals( 1, aShipmentList.size() );
      Shipment lShipment = aShipmentList.get( 0 );
      assertEquals( aExpectedShipment, lShipment.getShipmentKey() );
      assertEquals( aExpectedShipmentStatus, lShipment.getShipmentStatusKey() );
      assertEquals( aExpectedIsInboundShipment, lShipment.isInbound() );
   }


   @Before
   public void loadData() {
      iOrder = Domain.createPurchaseOrder();
   }
}
