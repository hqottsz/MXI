package com.mxi.mx.web.jsp.controller.shipment;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.table.inv.InvInvTable;


public class CreateEditShipmentControllerHelperTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Test
   public void shipFromLocationIsNullWhenInventoryHasNoLocation() {
      Assert.assertNull( CreateEditShipmentControllerHelper.getDefaultShipFromLocation( null ) );
   }


   @Test
   public void shipFromLocationIsInventoryDockWhenInventoryIsAtADock() {
      LocationKey lSupplyLocation = new LocationDomainBuilder().isSupplyLocation().build();
      LocationKey lDefaultDock = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .isDefaultDock( true ).withSupplyLocation( lSupplyLocation ).build();
      LocationKey lDockWithInventory = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .isDefaultDock( false ).withSupplyLocation( lSupplyLocation ).build();

      InventoryKey lInventoryItem = new InventoryBuilder().atLocation( lDockWithInventory ).build();

      LocationKey lLocationKey = InvInvTable.findByPrimaryKey( lInventoryItem ).getLocation();
      LocationKey lDefaultShipFromLocation =
            CreateEditShipmentControllerHelper.getDefaultShipFromLocation( lLocationKey );

      Assert.assertNotEquals( lDefaultDock, lDefaultShipFromLocation );
      Assert.assertEquals( lDockWithInventory, lDefaultShipFromLocation );
   }


   @Test
   public void shipFromLocationIsDefaultDockWhenInventoryIsNotAtADock() {
      LocationKey lSupplyLocation = new LocationDomainBuilder().isSupplyLocation().build();
      LocationKey lDefaultDock = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .isDefaultDock( true ).withSupplyLocation( lSupplyLocation ).build();
      LocationKey lInventoryLocation = new LocationDomainBuilder().withType( RefLocTypeKey.BIN )
            .withSupplyLocation( lSupplyLocation ).build();

      InventoryKey lInventoryItem = new InventoryBuilder().atLocation( lInventoryLocation ).build();

      LocationKey lLocationKey = InvInvTable.findByPrimaryKey( lInventoryItem ).getLocation();
      LocationKey lDefaultShipFromLocation =
            CreateEditShipmentControllerHelper.getDefaultShipFromLocation( lLocationKey );

      Assert.assertNotEquals( lInventoryLocation, lDefaultShipFromLocation );
      Assert.assertEquals( lDefaultDock, lDefaultShipFromLocation );
   }
}
