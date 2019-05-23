
package com.mxi.mx.core.query.purchase.po;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.ship.ShipShipmentTable;


/**
 * This class tests the IsInventoryInActiveExchgOrderShipment.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IsInventoryInActiveExchgOrderShipmentTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String INV_DESCRIPTION = "INV_DESC";
   private static final String VENDOR_DESCRIPTION = "VENDOR_DESC";
   private static final String LOC_DESCRIPTION = "LOC_DESC";
   private static final String PART_DESCRIPTION = "PART_DESC";
   private OwnerKey iOwnerKey;
   private LocationKey iVendorLocation;
   private VendorKey iVendorKey;
   private PartNoKey iPartNo;
   private InventoryKey iInventory;
   private PurchaseOrderKey iExchangeOrder;
   private PurchaseOrderLineKey iExchangeOrderLine;
   private LocationKey iSupplyLocation;
   private LocationKey iDockLocation;
   private ShipmentKey iShipmentKey;


   /**
    * Build mock data for the query test
    *
    * @throws Exception
    */
   protected void buildTestData() throws Exception {
      // Override to avoid "unable to locate data set" warning.

      // build an owner
      iOwnerKey = new OwnerDomainBuilder().isDefault().isNonLocal().build();

      // build a vendor location
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR )
            .withCode( LOC_DESCRIPTION ).build();

      // build vendor
      iVendorKey = new VendorBuilder().withCode( VENDOR_DESCRIPTION ).atLocation( iVendorLocation )
            .build();

      // build part
      iPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withShortDescription( PART_DESCRIPTION )
            .withAbcClass( RefAbcClassKey.A ).withStatus( RefPartStatusKey.ACTV )
            .withTotalQuantity( new BigDecimal( 0 ) )
            .withAverageUnitPrice( new BigDecimal( 10.50 ) ).withRepairBool( false ).build();

      // build inventory
      iInventory = new InventoryBuilder().withPartNo( iPartNo ).withOwner( iOwnerKey )
            .withDescription( INV_DESCRIPTION ).build();

      // build exchange order
      iExchangeOrder = new OrderBuilder().withOrderType( RefPoTypeKey.EXCHANGE )
            .withVendor( iVendorKey ).build();

      // build an exchange order line
      iExchangeOrderLine = new OrderLineBuilder( iExchangeOrder ).withOwner( iOwnerKey )
            .withLineType( RefPoLineTypeKey.EXCHANGE ).withOrderQuantity( BigDecimal.ONE )
            .forPart( iPartNo ).build();

      // create a supply location
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // build a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // build shipment
      iShipmentKey =
            new ShipmentDomainBuilder().fromLocation( iVendorLocation ).toLocation( iDockLocation )
                  .withType( RefShipmentTypeKey.SENDXCHG ).withOrder( iExchangeOrder ).build();

      // build shipment line
      new ShipmentLineBuilder( iShipmentKey ).forPart( iPartNo ).forInventory( iInventory )
            .forOrderLine( iExchangeOrderLine ).build();

   }


   /**
    * Executes the query and returns one row since the given inventory is in an non-historic
    * shipment generated by an exchange order
    *
    * @throws Exception
    *
    */
   @Test
   public void testOneRowReturned() throws Exception {

      buildTestData();

      DataSet lDs = execute( iInventory );

      assertEquals( 1, lDs.getRowCount() );
      assertTrue( lDs.next() );

   }


   /**
    * Executes the query
    *
    * @param aHr
    *
    * @return DataSet returned from the query
    */
   private DataSet execute( InventoryKey aInventory ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInventoryDbId", "aInventoryId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Executes the query and returns no rows since the given inventory is in an non-historic
    * shipment generated by an purchase order
    *
    * @throws Exception
    *
    */
   @Test
   public void testNoRowsReturned() throws Exception {

      buildTestData();

      ShipShipmentTable lShipShipmentTable = ShipShipmentTable.findByPrimaryKey( iShipmentKey );
      lShipShipmentTable.setShipmentType( RefShipmentTypeKey.PURCHASE );
      lShipShipmentTable.update();

      DataSet lDs = execute( iInventory );

      assertTrue( lDs.isEmpty() );

   }
}
