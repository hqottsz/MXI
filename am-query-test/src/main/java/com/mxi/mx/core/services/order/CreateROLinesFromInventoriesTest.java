package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.inventory.phys.PhysicalInventoryService;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.table.fnc.FncAccount;
import com.mxi.mx.core.table.sched.JdbcSchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.ship.ShipShipmentTable;
import com.mxi.mx.domain.shipment.Shipment;


/**
 *
 * This class tests the CreateROLinesFromInventories functionality and automatically creation and
 * scheduling of work packages.
 *
 * @author IndunilW
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class CreateROLinesFromInventoriesTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   InventoryKey iSerialInventoryKey = null;
   InventoryKey iBatchInventoryKey = null;
   FncAccountKey iFnCAccount = null;
   InventoryKey[] iInventoryKeys = null;
   PurchaseOrderKey iRepairOrderKey = null;
   HumanResourceKey iHumanResourceKey = null;
   LocationKey iLocationKey = null;
   PartNoKey iSerialPart = null;
   PartNoKey iBatchPart = null;
   LocationKey iRepairLocation = null;
   VendorKey iRepairVendor = null;


   @Before
   public void setup() {

      iSerialPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      iBatchPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.BATCH );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      iLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( "BIA" );
         aLocation.setType( RefLocTypeKey.DOCK );
      } );

      iRepairLocation = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( "ABQ" );
         aLocation.setType( RefLocTypeKey.DOCK );
      } );

      iRepairVendor = Domain.createVendor( aVendor -> {
         aVendor.setLocation( iRepairLocation );
      } );

      iSerialInventoryKey = Domain.createSerializedInventory( aSerInv -> {
         aSerInv.setLocation( iLocationKey );
         aSerInv.setCondition( RefInvCondKey.REPREQ );
         aSerInv.setPartNumber( iSerialPart );
      } );

      iBatchInventoryKey = Domain.createBatchInventory( aBatchInv -> {
         aBatchInv.setLocation( iLocationKey );
         aBatchInv.setCondition( RefInvCondKey.REPREQ );
         aBatchInv.setPartNumber( iBatchPart );
         aBatchInv.setBinQt( 12.00 );
      } );
      iRepairOrderKey = Domain.createPurchaseOrder( aOrder -> {
         aOrder.status( RefEventStatusKey.POOPEN );
         aOrder.vendor( iRepairVendor );
      } );

      iFnCAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "ChargeToAccount" ).build();
      iHumanResourceKey = Domain.createHumanResource();
   }


   /**
    *
    * GIVEN a inventory keys, authorizing Hr, charge to account code and repair order WHEN run
    * CreateROLinesFromInventories method THEN should create repair order lines,create and schedule
    * work packages and create out bound shipment.
    */
   @Test
   public void testCreateROLinesFromInventories() throws MxException, TriggerException {
      iInventoryKeys = new InventoryKey[] { iSerialInventoryKey, iBatchInventoryKey };

      new OrderService().createROLinesFromInventories( iRepairOrderKey, iHumanResourceKey,
            iInventoryKeys, FncAccount.findByPrimaryKey( iFnCAccount ).getAccountCd() );
      SchedStaskTable lSchedStask = null;
      Boolean lROAvailable = false;

      // Assert serial inventory and RO
      QuerySet lSerialQs = PhysicalInventoryService.getScheduleWorkPackages( iSerialInventoryKey );
      assertEquals( 1.0, lSerialQs.getRowCount(), 0 );
      while ( lSerialQs.next() ) {
         lSchedStask = new JdbcSchedStaskDao()
               .findByPrimaryKey( lSerialQs.getKey( TaskKey.class, "work_package_key" ) );
         assertEquals( iSerialInventoryKey, lSchedStask.getMainInventory() );
         lROAvailable = new OrderService().getRepairOrdersByInventory( iSerialInventoryKey );
         assertEquals( true, lROAvailable );
      }

      // Assert batch inventory and RO
      QuerySet lBatchQs = PhysicalInventoryService.getScheduleWorkPackages( iBatchInventoryKey );
      assertEquals( 1.0, lBatchQs.getRowCount(), 0 );
      while ( lBatchQs.next() ) {
         lSchedStask = new JdbcSchedStaskDao()
               .findByPrimaryKey( lBatchQs.getKey( TaskKey.class, "work_package_key" ) );
         assertEquals( iBatchInventoryKey, lSchedStask.getMainInventory() );
         lROAvailable = new OrderService().getRepairOrdersByInventory( iBatchInventoryKey );
         assertEquals( true, lROAvailable );
      }

      // Assert shipment
      List<Shipment> lOutboundshipment = new ShipmentService().getOrderShipments( iRepairOrderKey );
      assertEquals( 1, lOutboundshipment.size() );
      ShipShipmentTable lShipShipmentTable =
            ShipShipmentTable.findByPrimaryKey( lOutboundshipment.get( 0 ).getShipmentKey() );
      assertEquals( RefShipmentTypeKey.SENDREP, lShipShipmentTable.getShipmentType() );

   }
}
