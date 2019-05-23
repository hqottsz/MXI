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
import com.mxi.mx.core.services.shipment.ShipmentService;


/**
 * This class tests the functionality of CheckIfShipmentContainsOneTypeOfPoLines query.
 *
 * @author IndunilW
 */

@RunWith( Parameterized.class )
public class CheckIfShipmentContainsOneTypeOfPoLinesTest {

   private ShipmentKey iShipmentKey = null;
   private PurchaseOrderLineKey iOrderLineOneKey = null;
   private PurchaseOrderLineKey iOrderLineTwoKey = null;
   private RefPoLineTypeKey iRefPoLineTypeKeyOne = null;
   private RefPoLineTypeKey iRefPoLineTypeKeyTwo = null;
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


   public CheckIfShipmentContainsOneTypeOfPoLinesTest(RefPoLineTypeKey aRefPoLineTypeKeyOne,
         RefPoLineTypeKey aRefPoLineTypeKeyTwo, Boolean aExpectedResult) {
      iRefPoLineTypeKeyOne = aRefPoLineTypeKeyOne;
      iRefPoLineTypeKeyTwo = aRefPoLineTypeKeyTwo;
      iExpectedResult = aExpectedResult;
   }


   @Parameterized.Parameters
   public static Collection<Object[]> testScenarios() {
      return Arrays
            .asList( new Object[][] { { RefPoLineTypeKey.REPAIR, RefPoLineTypeKey.REPAIR, true },
                  { RefPoLineTypeKey.REPAIR, RefPoLineTypeKey.EXCHANGE, false }, } );
   }


   /**
    *
    * GIVEN a active RO with two repair lines which contains mapped shipment lines WHEN executing
    * CheckIfShipmentContainsOneTypeOfPoLines query THEN should return true.
    *
    * GIVEN a active RO with two different types of lines which contains mapped shipment lines WHEN
    * executing CheckIfShipmentContainsOneTypeOfPoLines query THEN should return false.
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

      iOrderLineOneKey = Domain.createPurchaseOrderLine( aOrderLine -> {
         aOrderLine.lineType( iRefPoLineTypeKeyOne );
         aOrderLine.orderKey( iOrder );
         aOrderLine.part( iPart );
      } );

      iOrderLineTwoKey = Domain.createPurchaseOrderLine( aOrderLine -> {
         aOrderLine.lineType( iRefPoLineTypeKeyTwo );
         aOrderLine.orderKey( iOrder );
         aOrderLine.part( iPart );
      } );

      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineOneKey );
         aShipmentLine.shipmentKey( iShipmentKey );
         aShipmentLine.part( iPart );
      } );
      Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.orderLine( iOrderLineTwoKey );
         aShipmentLine.shipmentKey( iShipmentKey );
         aShipmentLine.part( iPart );
      } );
      iActualResult = ShipmentService.checkIfShipmentContainsOneTypeOfPoLines( iShipmentKey,
            RefPoTypeKey.REPAIR );

      assertTrue( "CheckIfShipmentContainsOneTypeOfPoLines returns which contained "
            .concat( iRefPoLineTypeKeyOne.getCd() ).concat( " line type and " )
            .concat( iRefPoLineTypeKeyTwo.getCd() ), iActualResult == iExpectedResult );

   }

}
