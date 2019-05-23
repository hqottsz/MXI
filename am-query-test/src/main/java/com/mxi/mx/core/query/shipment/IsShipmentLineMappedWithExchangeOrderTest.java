package com.mxi.mx.core.query.shipment;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
 * This class tests the functionality of IsShipmentLineMappedWithExchangeOrder query.
 *
 * @author IndunilW
 */
@RunWith( Parameterized.class )
public class IsShipmentLineMappedWithExchangeOrderTest {

   private ShipmentKey iShipmentKey = null;
   private ShipmentLineKey iShipmentLineKey = null;
   private PurchaseOrderLineKey iOrderLineKey = null;
   private RefPoLineTypeKey iRefPoLineTypeKey = null;
   private Boolean iExpectedResult = false;
   private PurchaseOrderKey iOrder = null;
   private PartNoKey iPart = null;
   private Boolean iActualResult = false;

   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   public IsShipmentLineMappedWithExchangeOrderTest(RefPoLineTypeKey aRefPoLineTypeKey,
         Boolean aExpectedResult) {
      iRefPoLineTypeKey = aRefPoLineTypeKey;
      iExpectedResult = aExpectedResult;
   }


   @Parameterized.Parameters
   public static Collection<Object[]> testScenarios() {
      return Arrays.asList( new Object[][] { { RefPoLineTypeKey.EXCHANGE, true },
            { RefPoLineTypeKey.REPAIR, false }, } );
   }


   /**
    *
    * GIVEN an active RO with one exchange type line in shipment WHEN executing
    * IsShipmentLineMappedWithExchangeOrder query THEN should return true.
    *
    * GIVEN an active RO with no mapped exchange lines in shipment WHEN executing
    * IsShipmentLineMappedWithExchangeOrder query THEN should return false.
    *
    */
   @Test
   public void testCheckIfShipmentContainsOneTypeOfPoLines() {
      iShipmentKey = Domain.createShipment();

      iPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      iOrder = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
      } );

      iOrderLineKey = Domain.createPurchaseOrderLine( aOrderLine -> {
         aOrderLine.lineType( iRefPoLineTypeKey );
         aOrderLine.orderKey( iOrder );
         aOrderLine.part( iPart );
      } );

      iShipmentLineKey = Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineKey );
         aShipmentLine.shipmentKey( iShipmentKey );
         aShipmentLine.part( iPart );
      } );
      iActualResult =
            new ShipmentLineService().isShipmentLineMappedWithExchangeLine( iShipmentLineKey );

      assertTrue(
            "IsShipmentLineMappedWithExchangeOrder returns which contained "
                  .concat( iRefPoLineTypeKey.getCd() ).concat( " line type " ),
            iActualResult == iExpectedResult );

   }
}
