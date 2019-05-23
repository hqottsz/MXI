package com.mxi.mx.core.services.stask.complete.completeservice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the behaviours of
 * {@linkplain CompleteService#allowSerializedInventoryConditionChange(InvInvTable, RefInvCondKey)}
 *
 * @author Libin Cai
 * @created December 5, 2016
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AllowSerializedInventoryConditionChangeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given: Inventory after internal repair<br>
    * _When: Complete Work package<br>
    * _Then: Allow condition change. E.g. INREP-->RFI,
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAllowChangeWhenNewCondIsServiceableAndInvHasNoOrder() throws Exception {

      runWithoutOrderAndAssertTrue( RefInvCondKey.RFI );
   }


   /**
    * Given: Inventory after internal repair<br>
    * _When: Complete Work package as Unserviceable<br>
    * _Then: Allow condition change. E.g. INREP-->REPREQ,
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAllowChangeWhenNewCondIsUSAndInvHasNoOrder() throws Exception {

      runWithoutOrderAndAssertTrue( RefInvCondKey.REPREQ );
   }


   /**
    * Given: Inventory not in repair<br>
    * _When: Complete Work package<br>
    * _Then: Allow condition change. E.g. INSPREQ-->RFI,
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAllowChangeWhenNewCondIsServiceableAndOrderIsNonRepair() throws Exception {

      runWithNonRepairOrderAndAssertTrue( RefInvCondKey.RFI );
   }


   /**
    * Given: Inventory not in repair<br>
    * _When: Complete Work package as Unserviceable<br>
    * _Then: Allow condition change. E.g. INSPREQ-->REPREQ,
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAllowChangeWhenNewCondIsUSAndOrderIsNonRepair() throws Exception {

      runWithNonRepairOrderAndAssertTrue( RefInvCondKey.REPREQ );
   }


   /**
    * Given: Inventory After External Repair but it has not been physically received<br>
    * _When: Complete Work package<br>
    * _Then: Not allow condition change. E.g. INREP (no change)
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNotAllowChangeWhenNewCondIsServiceableAndOrderIsRepairAndInvIsINREP()
         throws Exception {

      runWithRepairOrderAndINREPInvAndAssertFalse( RefInvCondKey.RFI );
   }


   /**
    * Given: Inventory After External Repair but it has not been physically received<br>
    * _When: Complete Work package as Unserviceable<br>
    * _Then: Not allow condition change. E.g. INREP (no change)
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNotAllowChangeWhenNewCondIsUSAndOrderIsRepairAndInvIsINREP() throws Exception {

      runWithRepairOrderAndINREPInvAndAssertFalse( RefInvCondKey.REPREQ );
   }


   /**
    * Given: Inventory After External Repair and it was physically received but not inspected<br>
    * _When: Complete Work package<br>
    * _Then: Not allow condition change. E.g. INSPREQ (no change)
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNotAllowChangeWhenNewCondIsServiceableAndOrderIsRepairAndInvIsINSPREQ()
         throws Exception {

      runWithRepairOrderAndINSPREQInvAndAssertFalse( RefInvCondKey.RFI );
   }


   /**
    * Given: Inventory After External Repair and it was physically received but not inspected<br>
    * _When: Complete Work package as Unserviceable<br>
    * _Then: Not allow condition change. E.g. INSPREQ (no change)
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNotAllowChangeWhenNewCondIsUSAndOrderIsRepairAndInvIsINSPREQ() throws Exception {

      runWithRepairOrderAndINSPREQInvAndAssertFalse( RefInvCondKey.REPREQ );
   }


   /**
    * Given: Inventory was externally repaired and Repair Order is closed<br>
    * _When: Complete Work package<br>
    * _Then: Allow condition change. E.g. INREP-->RFI
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAllowChangeWhenNewCondIsServiceableAndRepairOrderIsClosed() throws Exception {

      runWithClosedRepairOrderAndAssertTrue( RefInvCondKey.RFI );
   }


   /**
    * Given: Inventory was externally repaired and Repair Order is closed<br>
    * _When: Complete Work package as Unserviceable<br>
    * _Then: Allow condition change. E.g. INREP-->REPREQ
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAllowChangeWhenNewCondIsUSAndRepairOrderIsClosed() throws Exception {

      runWithClosedRepairOrderAndAssertTrue( RefInvCondKey.REPREQ );
   }


   /**
    * Run the service on the Inventory After External Repair but it has not been physically
    * received, and assert the allow condition change boolean is false.
    *
    * @param aNewCondition
    *           The new condition
    *
    * @throws Exception
    *            if an error occurs
    */
   private void runWithRepairOrderAndINREPInvAndAssertFalse( RefInvCondKey aNewCondition )
         throws Exception {

      runWithOrderAndAssert( RefInvCondKey.INREP, null,
            new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build(), RefPoTypeKey.REPAIR,
            aNewCondition, false, false );
   }


   /**
    * Run the service on the Inventory under a closed repair order, and assert the allow condition
    * change boolean is true.
    *
    * @param aNewCondition
    *           The new condition
    *
    * @throws Exception
    *            if an error occurs
    */
   private void runWithClosedRepairOrderAndAssertTrue( RefInvCondKey aNewCondition )
         throws Exception {

      runWithOrderAndAssert( RefInvCondKey.INREP, RefPoTypeKey.REPAIR, aNewCondition, true, true );
   }


   /**
    * Run the service on the Inventory After External Repair and it was physically received but not
    * inspected, and assert the allow condition change boolean is false.
    *
    * @param aNewCondition
    *           The new condition
    *
    * @throws Exception
    *            if an error occurs
    */
   private void runWithRepairOrderAndINSPREQInvAndAssertFalse( RefInvCondKey aNewCondition )
         throws Exception {

      runWithOrderAndAssert( RefInvCondKey.INSPREQ, InvInvTable.FinanceStatusCd.RCVD, null,
            RefPoTypeKey.REPAIR, aNewCondition, false, false );
   }


   /**
    * Run the service on the Inventory not associated with an order, and assert the allow condition
    * change boolean is false.
    *
    * @param aNewCondition
    *           The new condition
    *
    * @throws Exception
    *            if an error occurs
    */
   private void runWithoutOrderAndAssertTrue( RefInvCondKey aNewCondition ) throws Exception {

      runAndAssert( getInventoryBuilder( RefInvCondKey.INREP ).build(), aNewCondition, true );
   }


   /**
    * Run the service on the Inventory under non-repair order, and assert the allow condition change
    * boolean is true.
    *
    * @param aNewCondition
    *           The new condition
    *
    * @throws Exception
    *            if an error occurs
    */
   private void runWithNonRepairOrderAndAssertTrue( RefInvCondKey aNewCondition ) throws Exception {

      runWithOrderAndAssert( RefInvCondKey.INSPREQ, RefPoTypeKey.PURCHASE, aNewCondition, false,
            true );
   }


   /**
    * Run the service on the Inventory under an order, and assert the allow condition change
    * boolean.
    *
    * @param aCurrentCondition
    *           The current condition
    * @param aOrderType
    *           The order type
    * @param aNewCondition
    *           The new condition
    * @param aIsClosedOrder
    *           Indicates whether the order is closed
    * @param aExpectedAllowBool
    *           The expected allow condition change boolean
    *
    * @throws Exception
    *            if an error occurs
    */
   private void runWithOrderAndAssert( RefInvCondKey aCurrentCondition, RefPoTypeKey aOrderType,
         RefInvCondKey aNewCondition, boolean aIsClosedOrder, boolean aExpectedAllowBool )
         throws Exception {

      runWithOrderAndAssert( aCurrentCondition, null, null, aOrderType, aNewCondition,
            aIsClosedOrder, aExpectedAllowBool );
   }


   /**
    * Run the service on the Inventory under an order, and assert the allow condition change
    * boolean.
    *
    * @param aCurrentCondition
    *           The current condition
    * @param aFinanceStatusCd
    *           The finance status code
    * @param aLocation
    *           The location
    * @param aOrderType
    *           The order type
    * @param aNewCondition
    *           The new condition
    * @param aIsClosedOrder
    *           Indicates whether the order is closed
    * @param aExpectedAllowBool
    *           The expected allow condition change boolean
    *
    * @throws Exception
    *            if an error occurs
    */
   private void runWithOrderAndAssert( RefInvCondKey aCurrentCondition,
         InvInvTable.FinanceStatusCd aFinanceStatusCd, LocationKey aLocation,
         RefPoTypeKey aOrderType, RefInvCondKey aNewCondition, boolean aIsClosedOrder,
         boolean aExpectedAllowBool ) throws Exception {

      OrderBuilder lOrderBuilder = new OrderBuilder().withOrderType( aOrderType );

      if ( aIsClosedOrder ) {
         lOrderBuilder.withStatus( RefEventStatusKey.POCLOSED );
      }

      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrderBuilder.build() ).build();

      // create an inventory and link to order line
      InventoryKey lInventory =
            getInventoryBuilder( aCurrentCondition ).withFinanceStatusCd( aFinanceStatusCd )
                  .atLocation( aLocation ).withOrderLine( lOrderLine ).build();

      runAndAssert( lInventory, aNewCondition, aExpectedAllowBool );
   }


   /**
    * Run the service on the Inventory, and assert the allow condition change boolean.
    *
    * @param aInventory
    *           The inventory
    * @param aNewCondition
    *           The new condition
    * @param aExpectedAllowBool
    *           TODO
    * @param aExpectedCondition
    *           The expected condition
    * @throws Exception
    *            if an error occurs
    */
   @SuppressWarnings( "deprecation" )
   private void runAndAssert( InventoryKey aInventory, RefInvCondKey aNewCondition,
         boolean aExpectedAllowBool ) throws Exception {

      boolean lAllowChange = new CompleteService( new TaskBuilder().onInventory( aInventory )
            .withTaskClass( RefTaskClassKey.CHECK ).build() )
                  .allowSerializedInventoryConditionChange(
                        InvInvTable.findByPrimaryKey( aInventory ), aNewCondition );

      MxAssert.assertEquals( aExpectedAllowBool, lAllowChange );;
   }


   /**
    * Get inventory builder.
    *
    * @param aCondition
    *           The inventory condition
    *
    * @return the inventory builder
    */
   private InventoryBuilder getInventoryBuilder( RefInvCondKey aCondition ) {

      PartNoKey lSerializedPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withStatus( RefPartStatusKey.ACTV ).build();

      return new InventoryBuilder().withPartNo( lSerializedPart ).withCondition( aCondition )
            .atLocation( new LocationDomainBuilder().build() );
   }

}
