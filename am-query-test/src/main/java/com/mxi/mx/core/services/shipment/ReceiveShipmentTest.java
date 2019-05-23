package com.mxi.mx.core.services.shipment;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;


/**
 * This class tests the receiveing shipment logic in shipment service.
 *
 * @author Libin Cai
 * @created February 17, 2017
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ReceiveShipmentTest {

   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;

   private ShipmentKey iInboundShipment;
   private HumanResourceKey iHr;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test ROHasNotBeenSentException is thrown when receiving inbound shipment of repair order while
    * the outbound shipment is Pending.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testROHasNotBeenSentException_ReceiveInboundShipmentWhileOutboundShipmentIsPending()
         throws Exception {

      createDataForTestingROHasNotBeenSentException( RefEventStatusKey.IXPEND );

      receiveShipmentAndAssertException();
   }


   /**
    * Test ROHasNotBeenSentException is thrown when receiving inbound shipment of repair order while
    * the outbound shipment is In-Transit.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void
         testROHasNotBeenSentException_ReceiveInboundShipmentWhileOutboundShipmentIsInTransit()
               throws Exception {

      createDataForTestingROHasNotBeenSentException( RefEventStatusKey.IXINTR );

      receiveShipmentAndAssertException();
   }


   /**
    * Receive the inbound shipment and make sure the right exception is thrown.
    *
    * @throws Exception
    *            if an error occurs
    */
   private void receiveShipmentAndAssertException() throws Exception {

      try {

         ShipmentService.receive( iInboundShipment, new Date(), null, null, iHr );

         Assert.fail( "Expect ROHasNotBeenSentException." );
      } catch ( ROHasNotBeenSentException e ) {
         ;
      }
   }


   @Before
   public void loadData() throws Exception {

      iHr = Domain.createHumanResource();
   }


   /**
    * Create the testing data with the specific outbound shipment status.
    *
    * @param aEventStatus
    *           the outbound shipment status
    */
   private void createDataForTestingROHasNotBeenSentException( RefEventStatusKey aEventStatus ) {

      PartNoKey lPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      PurchaseOrderKey lRepairOrder = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
      } );

      PurchaseOrderLineKey lOrderLineKey = Domain.createPurchaseOrderLine( aOrderLine -> {
         aOrderLine.lineType( RefPoLineTypeKey.REPAIR );
         aOrderLine.orderKey( lRepairOrder );
         aOrderLine.part( lPart );
      } );

      iInboundShipment = new ShipmentDomainBuilder().withOrder( lRepairOrder )
            .withType( RefShipmentTypeKey.REPAIR ).build();

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( lOrderLineKey );
         aShipmentLine.shipmentKey( iInboundShipment );
         aShipmentLine.part( lPart );
      } );

      // create outbound shipment
      new ShipmentDomainBuilder().withOrder( lRepairOrder ).withType( RefShipmentTypeKey.SENDREP )
            .withStatus( aEventStatus ).build();

   }

}
