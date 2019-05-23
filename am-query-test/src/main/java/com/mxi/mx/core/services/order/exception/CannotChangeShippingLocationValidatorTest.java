package com.mxi.mx.core.services.order.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefShipSegmentStatusKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.table.ship.ShipSegmentTable;


public class CannotChangeShippingLocationValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private CannotChangeShippingLocationValidator iCannotChangeShippingLocationValidator =
         new CannotChangeShippingLocationValidator();

   private LocationKey iVendorLocation;
   private PurchaseOrderKey iOrder;


   @Test( expected = CannotChangeShippingLocationException.class )
   public void validate_orderWithACompletedInboundShipment()
         throws CannotChangeShippingLocationException {
      new ShipmentDomainBuilder().withStatus( RefEventStatusKey.IXCMPLT ).withOrder( iOrder )
            .fromLocation( iVendorLocation ).build();
      iCannotChangeShippingLocationValidator.validate( iOrder );
   }


   @Test( expected = CannotChangeShippingLocationException.class )
   public void validate_orderWithAPendInboundShipmentWhichHasACompletedSegment()
         throws CannotChangeShippingLocationException {
      createPendInboundShipmentWithSegment( RefShipSegmentStatusKey.COMPLETE );
      iCannotChangeShippingLocationValidator.validate( iOrder );
   }


   @Test( expected = CannotChangeShippingLocationException.class )
   public void validate_orderWithAPendInboundShipmentWhichHasAnInTransitSegment()
         throws CannotChangeShippingLocationException {
      createPendInboundShipmentWithSegment( RefShipSegmentStatusKey.INTRANSIT );
      iCannotChangeShippingLocationValidator.validate( iOrder );
   }


   @Test( expected = CannotChangeShippingLocationException.class )
   public void validate_orderWithAPendInboundShipmentWhichHasACancelledSegment()
         throws CannotChangeShippingLocationException {
      createPendInboundShipmentWithSegment( RefShipSegmentStatusKey.CANCEL );
      iCannotChangeShippingLocationValidator.validate( iOrder );
   }


   @Test
   public void validate_orderWithAPendInboundShipmentWhichHasAPendingSegment()
         throws CannotChangeShippingLocationException {
      createPendInboundShipmentWithSegment( RefShipSegmentStatusKey.PENDING );
      iCannotChangeShippingLocationValidator.validate( iOrder );
   }


   @Test
   public void validate_orderWithAPendInboundShipmentWhichHasAPlanSegment()
         throws CannotChangeShippingLocationException {
      createPendInboundShipmentWithSegment( RefShipSegmentStatusKey.PLAN );
      iCannotChangeShippingLocationValidator.validate( iOrder );
   }


   /**
    * Create a PEND inbound shipment with different status of segment
    *
    * @param aSegmentStatusKey
    *           the segment status key
    */
   private void createPendInboundShipmentWithSegment( RefShipSegmentStatusKey aSegmentStatusKey ) {
      ShipmentKey lPendInboundShipment =
            new ShipmentDomainBuilder().withStatus( RefEventStatusKey.IXPEND ).withOrder( iOrder )
                  .fromLocation( iVendorLocation ).build();
      QuerySet lShipSegmentDs = getShipSegments( lPendInboundShipment );
      assertEquals( 1, lShipSegmentDs.getRowCount() );
      lShipSegmentDs.next();
      ShipSegmentKey lSegmentKey =
            lShipSegmentDs.getKey( ShipSegmentKey.class, "segment_db_id", "segment_id" );
      ShipSegmentTable lSegment = ShipSegmentTable.findByPrimaryKey( lSegmentKey );
      lSegment.setStatus( aSegmentStatusKey );
      lSegment.update();
   }


   /**
    * Retrieve all ship segments of shipment given.
    *
    * @param aShipmentKey
    *           the shipment key
    *
    * @return the QuerySet object for all ship segments of shipment given.
    */
   private QuerySet getShipSegments( ShipmentKey aShipmentKey ) {
      DataSetArgument lArg = new DataSetArgument();
      lArg.add( aShipmentKey, "aShipmentDbId", "aShipmentId" );

      QuerySet lShipSegmentsDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.shipment.routing.GetSegments", lArg );

      return lShipSegmentsDs;
   }


   @Before
   public void loadData() {
      iVendorLocation = Domain
            .createLocation( aVendorLocation -> aVendorLocation.setType( RefLocTypeKey.VENDOR ) );
      iOrder = new OrderBuilder().build();
   }
}
