package com.mxi.mx.web.query.part;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ibm.icu.math.BigDecimal;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipSegmentBuilder;
import com.mxi.am.domain.builder.ShipSegmentMapBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipSegmentKey;
import com.mxi.mx.core.key.ShipmentKey;



/**
 * Test case for IsFinanceTypeChangeProhibited.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsFinanceTypeChangeProhibitedTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private PartNoKey iPartNo;
   private static final BigDecimal RECEIVED_QT = new BigDecimal( 1 );
   private DataSet iDataSet = null;


   /**
    * * Verify that query returns if inventory is received with open Purchase Order
    *
    */
   @Test
   public void testQueryWithPOStatusOpen() {
      // prepare the test data
      createTestData( RefEventStatusKey.POOPEN );
      // assert the data
      assertTrue( "Expected query to return a row.", execute( iPartNo ) );

   }


   /**
    * * Verify that query does not returns if inventory is received with closed Purchase Order
    *
    */

   @Test
   public void testQueryWithPOStatusClosed() throws Exception {

      // prepare the test data
      createTestData( RefEventStatusKey.POCLOSED );
      // assert the data
      assertFalse( "Expected query to return no row.", execute( iPartNo ) );

   }


   /**
    * Data setup
    *
    * @param aEventStatus
    *           Event Status
    */
   protected void createTestData( RefEventStatusKey aEventStatus ) {

      // create a part
      iPartNo = new PartNoBuilder().build();

      // create a purchase order
      PurchaseOrderKey lPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE )
            .withOrderType( RefPoTypeKey.EXCHANGE ).withStatus( aEventStatus ).build();

      // create a purchase order line
      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lPO ).forPart( iPartNo ).build();

      // create shipment for order
      ShipmentKey lShipment =
            new ShipmentDomainBuilder().withType( RefShipmentTypeKey.PURCHASE ).withOrder( lPO ).build();

      // create shipment line
      new ShipmentLineBuilder( lShipment ).forOrderLine( lOrderLine )
            .withReceivedQuantity( RECEIVED_QT.doubleValue() ).build();

      // create a vendor location
      LocationKey lLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();

      // create a segment
      ShipSegmentKey lShipSegment = new ShipSegmentBuilder().fromLocation( lLocation ).build();

      // create a map to link segment to shipment
      new ShipSegmentMapBuilder().withShipment( lShipment ).withSegment( lShipSegment )
            .withOrder( 1 ).build();
   }


   /**
    * Execute the query.
    *
    * @param aPartNo
    *           the Part No.
    */

   private boolean execute( PartNoKey aPartNo ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNo, "aPartNoDbId", "aPartNoId" );

      // Return true if the query returns a row, otherwise false.
      return !QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs ).isEmpty();
   }

}
