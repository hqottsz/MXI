package com.mxi.mx.core.query.shipment;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;


/**
 * This test ensures that the query com.mxi.mx.core.query.shipment.IsOutboundShipmentSent.qrx
 * returns result if the outbound shipment of repair order is not sent
 *
 * @author Libin Cai
 * @created February 17, 2017
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IsOutboundShipmentSentTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** The inbound shipment key */
   private ShipmentKey iInboundShipment;


   /**
    * Tests the query to make sure it returns result if the outbound shipment of repair order is
    * PENDING.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHasResultForPendingOutboundShipmentOfRepairOrder() throws Exception {

      createData( RefEventStatusKey.IXPEND );

      QuerySet lQs = executeQuery();

      Assert.assertEquals( 1, lQs.getRowCount() );

   }


   /**
    * Tests the query to make sure it returns result if the outbound shipment of repair order is
    * In-transit.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHasResultForIntrasitOutboundShipmentOfRepairOrder() throws Exception {

      createData( RefEventStatusKey.IXINTR );

      QuerySet lQs = executeQuery();

      Assert.assertEquals( 1, lQs.getRowCount() );

   }


   /**
    * Tests the query to make sure it returns no result if the outbound shipment of repair order is
    * Completed.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoResultForCompletedOutboundShipmentOfRepairOrder() throws Exception {

      createData( RefEventStatusKey.IXCMPLT );

      QuerySet lQs = executeQuery();

      Assert.assertEquals( 0, lQs.getRowCount() );

   }


   /**
    * Create the testing data with the specific outbound shipment status.
    *
    * @param aEventStatus
    *           the outbound shipment status
    */
   private void createData( RefEventStatusKey aEventStatus ) {

      PurchaseOrderKey lRepairOrder =
            new OrderBuilder().withOrderType( RefPoTypeKey.REPAIR ).build();

      iInboundShipment = new ShipmentDomainBuilder().withOrder( lRepairOrder )
            .withType( RefShipmentTypeKey.REPAIR ).build();

      // create outbound shipment
      new ShipmentDomainBuilder().withOrder( lRepairOrder ).withType( RefShipmentTypeKey.SENDREP )
            .withStatus( aEventStatus ).build();

   }


   /**
    * Executes the query being tested and returns the results.
    *
    * @return The results of the query
    */
   private QuerySet executeQuery() {

      DataSetArgument lArg = new DataSetArgument();
      lArg.add( iInboundShipment, "aShipmentDbId", "aShipmentId" );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.shipment.IsOutboundShipmentSent", lArg );

      return lQs;
   }
}
