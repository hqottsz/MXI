package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.inventory.InventoryService;


/**
 *
 * This class tests the GetNonScheduledActiveWorkPackagesByInventory query.
 *
 * @author IndunilW
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetNonScheduledActiveWorkPackagesByInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private InventoryKey iInventoryKey = null;
   private PartNoKey iPartNokey = null;
   private TaskKey iWorkPackageKey = null;
   private PurchaseOrderKey iRepairOrder = null;


   @Before
   public void setup() {

      iPartNokey = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
      } );
      iInventoryKey = Domain.createSerializedInventory( aInventory -> {
         aInventory.setPartNumber( iPartNokey );
      } );

      iWorkPackageKey = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAircraft( iInventoryKey );
         aWorkPackage.setStatus( RefEventStatusKey.ACTV );
      } );

      iRepairOrder = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
      } );

      Domain.createPurchaseOrderLine( aLine -> {
         aLine.task( iWorkPackageKey );
         aLine.orderKey( iRepairOrder );
         aLine.lineType( RefPoLineTypeKey.REPAIR );
      } );

   }


   /**
    *
    * GIVEN a active non scheduled work package with which contains an inventory WHEN run
    * GetNonScheduledActiveWorkPackagesByInventory query THEN should return work package mapped with
    * the inventory.
    */
   @Test
   public void testGetNonScheduledActiveWorkPackagesByInventory() {
      QuerySet lQs =
            new InventoryService().getNonScheduledActiveWorkPackagesByInventory( iInventoryKey );
      assertEquals( 1, lQs.getRowCount() );
   }
}
