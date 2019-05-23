package com.mxi.mx.core.query.shipment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.services.shipment.ShipmentLineService;


/**
 * This test ensures that getShipmentLineUsingROLineAndShipment method in ShipmentLineService
 * retrieves shipment lines as expected. Expectation: Retrieve only shipment lines matched with RO
 * line and shipment.
 *
 * @author IndunilW
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetShipmentLineUsingROLineAndShipmentTest {

   private ShipmentKey iShipmentKey = null;
   private ShipmentLineKey iShipmentLineKey = null;
   private PurchaseOrderLineKey iOrderLineKey = null;
   private PurchaseOrderKey iOrder = null;
   private PartNoKey iPart = null;

   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUpData() {
      iShipmentKey = Domain.createShipment();

      iPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      iOrder = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
      } );

      iOrderLineKey = Domain.createPurchaseOrderLine( aOrderLine -> {
         aOrderLine.lineType( RefPoLineTypeKey.REPAIR );
         aOrderLine.orderKey( iOrder );
         aOrderLine.part( iPart );
      } );

      iShipmentLineKey = Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineKey );
         aShipmentLine.shipmentKey( iShipmentKey );
         aShipmentLine.part( iPart );
      } );
   }


   /**
    *
    * GIVEN a shipment line with a mapped RO line WHEN execute getShipmentLineUsingROLineAndShipment
    * method in ShipmentLineService THEN should return shipment key of the mapped shipment line with
    * the RO line.
    */
   @Test
   public void testGetShipmentLinesByROLines() {
      ShipmentLineKey lActualShipmentLineKey = new ShipmentLineService()
            .getShipmentLineUsingROLineAndShipment( iShipmentKey, iOrderLineKey );
      assertEquals( iShipmentLineKey, lActualShipmentLineKey );
   }

}
