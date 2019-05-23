
package com.mxi.mx.core.query.order;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.order.OrderService;


/**
 *
 * This class tests the query GetPartAndLocationDetailsByOrder.qrx
 *
 * @author IndunilW
 *
 */
public class GetPartAndLocationDetailsByOrderTest {

   private LocationKey iSupplyLocation = null;
   private LocationKey iInventoryLocation = null;
   private PurchaseOrderKey iRepairOrderKey = null;
   private InventoryKey iInventoryKey = null;
   private PartNoKey iPartNoKey = null;
   private TaskKey iTaskKey = null;
   private String iExpectedLocationCode = "BIA";
   private String iLocationCode = "BIA/DOCK";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iSupplyLocation = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( iExpectedLocationCode );
         aLocation.setType( RefLocTypeKey.AIRPORT );
      } );

      iInventoryLocation = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( false );
         aLocation.setCode( iLocationCode );
         aLocation.setType( RefLocTypeKey.DOCK );
         aLocation.setSupplyLocation( iSupplyLocation );
      } );

      iPartNoKey = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
      } );

      iInventoryKey = Domain.createSerializedInventory( aSerInv -> {
         aSerInv.setPartNumber( iPartNoKey );
         aSerInv.setLocation( iInventoryLocation );
      } );

      iTaskKey = Domain.createComponentWorkPackage( aWorkpackage -> {
         aWorkpackage.setInventory( iInventoryKey );
         aWorkpackage.atLocation( iInventoryLocation );
      } );

      iRepairOrderKey = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
      } );

      Domain.createPurchaseOrderLine( aLine -> {
         aLine.orderKey( iRepairOrderKey );
         aLine.lineType( RefPoLineTypeKey.REPAIR );
         aLine.task( iTaskKey );
         aLine.part( iPartNoKey );
      } );
   }


   /**
    *
    * GIVEN a repair order with a line and a work package, WHEN executing the query to fetch supply
    * location and part, THEN should return the supply location of the work package and the line
    * part
    *
    */
   @Test
   public void testGetPartAndLocationDetailsByOrder() {

      String lActualSupplyLocationCode = null;
      PartNoKey lActualPartNoKey = null;
      QuerySet lQs = new OrderService().getPartAndSupplyLocationByOrder( iRepairOrderKey );
      while ( lQs.next() ) {
         lActualSupplyLocationCode = lQs.getString( "loc_cd" );
         lActualPartNoKey = lQs.getKey( PartNoKey.class, "part_no_db_id", "part_no_id" );
      }

      assertEquals( iExpectedLocationCode, lActualSupplyLocationCode );
      assertEquals( iPartNoKey, lActualPartNoKey );
   }

}
