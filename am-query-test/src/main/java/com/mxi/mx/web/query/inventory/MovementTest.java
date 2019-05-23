package com.mxi.mx.web.query.inventory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;

import junit.framework.Assert;


/**
 * This tests the Movement.qrx, which retrieves movements of an inventory including shipments and
 * transfers
 *
 * @author DuHeLK
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class MovementTest {

   private static final String SERIAL_NO_OEM = "SERIAL_OEM";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iVendorLocation;
   private LocationKey iDockLocation;
   private PartNoKey iPartNo;
   private InventoryKey iInventory;


   /**
    * GIVEN a shipment with a shipment line with part key and serial no WHEN queried for movement
    * THEN the shipment should be retrieved.
    *
    */
   @Test
   public void testMovementWithPartShipmentLine() {

      // create shipment with part no and serial no
      ShipmentKey lShipment = new ShipmentDomainBuilder().toLocation( iVendorLocation )
            .withType( RefShipmentTypeKey.SENDXCHG ).fromLocation( iDockLocation ).build();

      new ShipmentLineBuilder( lShipment ).forPart( iPartNo ).withSerialNo( SERIAL_NO_OEM )
            .withExpectedQuantity( 1.0 ).build();

      assertMovementExist();
   }


   /**
    * GIVEN an shipment with an inventory key values WHEN queried for open moves THEN that shipment
    * should be retrieved
    *
    */
   @Test
   public void testMovementWithInventoryShipmentLine() {

      // create shipment with inventory key
      ShipmentKey lShipment = new ShipmentDomainBuilder().toLocation( iVendorLocation )
            .withType( RefShipmentTypeKey.PURCHASE ).fromLocation( iDockLocation ).build();

      new ShipmentLineBuilder( lShipment ).forPart( iPartNo ).withSerialNo( SERIAL_NO_OEM )
            .forInventory( iInventory ).withExpectedQuantity( 1.0 ).build();

      assertMovementExist();
   }


   private void assertMovementExist() {

      QuerySet lMovements = executeQuery( iInventory, false, 60 );

      Assert.assertEquals( 1, lMovements.getRowCount() );
   }


   @Before
   public void DataSetup() {
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();

      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).build();

      iPartNo = new PartNoBuilder().isTool().build();

      iInventory =
            new InventoryBuilder().withPartNo( iPartNo ).withSerialNo( SERIAL_NO_OEM ).build();

   }


   private QuerySet executeQuery( InventoryKey aInventoryKey, boolean aHistorical,
         Integer aDayCount ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, new String[] { "aInvNoDbId", "aInvNoId" } );
      lArgs.add( "aHistorical", aHistorical );
      lArgs.add( "aDayCount", aDayCount );

      return QuerySetFactory.getInstance().executeQuery( QueryExecutor.getQueryName( getClass() ),
            lArgs );
   }
}
