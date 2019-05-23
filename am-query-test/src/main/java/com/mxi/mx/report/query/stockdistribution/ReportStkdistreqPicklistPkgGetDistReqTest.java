package com.mxi.mx.report.query.stockdistribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TransferKey;


/**
 * Tests the report_stkdistreq_picklist_pkg PL/SQL function GetDistReq
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ReportStkdistreqPicklistPkgGetDistReqTest {

   /** Function info */
   private static final String PACKAGE_NAME = "report_stkdistreq_picklist_pkg";
   private static final String FUNCTION = "GetDistReq";

   private static final String PREFIX_MAIN_WH_CODE = "MAIN_WH";
   private static final String PREFIX_LINE_CODE = "LINE";

   private static final RefQtyUnitKey QTY_UNIT_EA = RefQtyUnitKey.EA;
   private static StockNoKey STOCK_NO = new StockNoKey( 1, 1 );
   private static final String OWNER_CD = "OWNER_CD";
   private static final double NEEDED_QTY = 20.0;
   private static final double PENDING_XFER_QTY = 1.0;
   private static final double COMPLETED_XFER_QTY = 2.0;
   private static final double CANCELLED_XFER_QTY = 5.0;
   private static final String[] REQ_CODES = { "CD_A", "CD_D", "CD_C", "CD_B" };
   private static final Integer[] ROUTER_ORDER = { null, 5, 5, 8 };


   public enum ColumName {
      DIST_REQ_DB_ID, DIST_REQ_ID, REQUEST_ID, TO_WAREHOUSE, FROM_SUPPLIER, NEEDED_QTY, OWNER,
      QTY_UNIT
   }


   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private PartNoKey iStoreAltPart;
   private QuerySet iQuerySet;
   private OwnerKey iOwner;


   @Before
   public void setup() throws Exception {

      iOwner = Domain.createOwner( owner -> owner.setCode( OWNER_CD ) );

      iStoreAltPart = Domain.createPart( part -> {
         part.setQtyUnitKey( QTY_UNIT_EA );
         part.setStockNoKey( STOCK_NO );
      } );
   }


   /**
    * Scenario Description:
    *
    * - A stock no has an alternative part <br>
    * - A line warehouse's supplier is the main warehouse <br>
    * - A main warehouse has two bins <br>
    * - Inventory is created for both bins <br>
    * - Two bins are created to simulate the duplicate distribution request and make sure the
    * request fetches the unique request <br>
    * - Create a stock distribution request on this line warehouse <br>
    * - Do the same steps to create another three stock distribution requests <br>
    * - Make sure zone router order in the distribution request is different <br>
    * - Make sure the location code is different if two bins have the same zone router order <br>
    *
    * Query Result:
    *
    * - There are four rows in the report (no duplicate rows) <br>
    * - The rows are ordered by zone router order, then by bin location code, with the null value as
    * the last<br>
    * - The other values of the stock distribution request are correct, such as needed_qty, unit,
    * etc.<br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testRequestIsOrderedByBinLocation() throws Exception {

      StockDistReqKey[] lReqs = new StockDistReqKey[REQ_CODES.length];

      String lParmValue = "";

      // Create distribution requests
      for ( int i = 0; i < REQ_CODES.length; i++ ) {

         lReqs[i] = createStockDistRequest( REQ_CODES[i], ROUTER_ORDER[i], true );

         lParmValue += lReqs[i].toString();

         if ( i < REQ_CODES.length - 1 ) {
            lParmValue += ",";
         }
      }

      execute( lParmValue );

      assertEquals( 4, iQuerySet.getRowCount() );

      assertDistRequest( lReqs[2], REQ_CODES[2] );
      assertDistRequest( lReqs[1], REQ_CODES[1] );
      assertDistRequest( lReqs[3], REQ_CODES[3] );
      assertDistRequest( lReqs[0], REQ_CODES[0] );
   }


   /**
    * Scenario Description:
    *
    * - A stock no has an alternative part <br>
    * - A line warehouse's supplier is the main warehouse <br>
    * - A main warehouse has two bins <br>
    * - Two bins are created for simulate the duplicate distribution request and make sure the
    * request fetches the unique request <br>
    * - Create a stock distribution request on this line warehouse <br>
    * - Do the same steps to create another one stock distribution requests <br>
    * - Make sure zone router order in the distribution request is different <br>
    * - Create the inventory at the bin of one distribution request that has lower number of zone
    * route order and don't create inventory at the bin in another distribution request <br>
    *
    * Query Result:
    *
    * - There are two rows in the report (no duplicate rows) <br>
    * - The first row is the distribution request with higher number of zone route order with
    * inventory <br>
    * - The second row is the distribution request with lower number of zone route order without
    * inventory <br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testBinIsNotConsideredForOrderWhenItHasNoInventory() throws Exception {

      StockDistReqKey lFirstReq = createStockDistRequest( REQ_CODES[1], ROUTER_ORDER[1], false );
      StockDistReqKey lSecondReq = createStockDistRequest( REQ_CODES[3], ROUTER_ORDER[3], true );

      String lParmValue = lFirstReq.toString() + "," + lSecondReq.toString();

      execute( lParmValue );

      assertEquals( 2, iQuerySet.getRowCount() );

      assertDistRequest( lSecondReq, REQ_CODES[3] );
      assertDistRequest( lFirstReq, REQ_CODES[1] );
   }


   /**
    * Scenario Description:
    *
    * - A stock no has two alternative parts <br>
    * - A line warehouse's supplier is the main warehouse <br>
    * - Create a stock distribution request on this line warehouse with needed quantity
    * NEEDED_QTY<br>
    * - Create a picked item with transfer quantity PENDING_XFER_QTY <br>
    *
    * Query Result:
    *
    * - The new needed quantity is NEEDED_QTY - PENDING_XFER_QTY <br>
    *
    * Note:
    *
    * This test case is for a bug that the multiple stock alternative parts may cause the picked
    * item wrongly duplicated multiple times in the function
    * report_stkdistreq_picklist_pkg.GetDistReq. E.g. if there are two stock alternate parts, the
    * new needed quantity might be NEEDED_QTY- 2 * PENDING_XFER_QTY. <br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNeededQuantityWithTwoStockAlternateParts() throws Exception {

      // create another stock alternative part
      Domain.createPart( part -> {
         part.setQtyUnitKey( QTY_UNIT_EA );
         part.setStockNoKey( STOCK_NO );
      } );

      StockDistReqKey lDistReq = createDistRequestAndPickedItem( PENDING_XFER_QTY );

      execute( lDistReq.toString() );

      iQuerySet.next();

      assertEquals( NEEDED_QTY - PENDING_XFER_QTY,
            iQuerySet.getDouble( ColumName.NEEDED_QTY.toString() ), 0 );
   }


   /**
    * Scenario Description:
    *
    * - A stock no has one alternative part <br>
    * - A line warehouse's supplier is the main warehouse <br>
    * - Create a stock distribution request on this line warehouse with needed quantity
    * NEEDED_QTY<br>
    * - Create a picked item with transfer quantity NEEDED_QTY <br>
    *
    * Query Result:
    *
    * - This distribution request will not show because it is all picked. <br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDistRequestNotShowWhenAllPicked() throws Exception {

      createDistReqWithPickedQuantityAndAssertResult( NEEDED_QTY );
   }


   /**
    * Scenario Description:
    *
    * - A stock no has one alternative part <br>
    * - A line warehouse's supplier is the main warehouse <br>
    * - Create a stock distribution request on this line warehouse with needed quantity
    * NEEDED_QTY<br>
    * - Create a picked item with transfer quantity NEEDED_QTY + 1 <br>
    *
    * Query Result:
    *
    * - This distribution request will not show because it is over picked. <br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDistRequestNotShowWhenOverPicked() throws Exception {

      createDistReqWithPickedQuantityAndAssertResult( NEEDED_QTY + 1 );
   }


   /**
    * Create the following data:
    *
    * - A main warehouse has two bins <br>
    * - One bin has bin level with the stock alternative part <br>
    * - This bin also has zone router assigned <br>
    * - The other bin has no bin level and zone router order. It is created for simulate the
    * duplicate distribution request and make sure the request fetches the unique request <br>
    * - A line warehouse's supplier is the main warehouse <br>
    * - Create a stock distribution request on this line warehouse <br>
    *
    * @param aLocationCode
    *           the partial of location code
    * @param aRouterOrder
    *           the router order
    */
   private StockDistReqKey createStockDistRequest( String aLocationCode, Integer aRouterOrder,
         boolean aToCreateInventory ) {

      // Create main warehouse and two bins
      LocationKey lMainWarehouse = createMainWarehouse( aLocationCode );
      LocationKey lMainWarehouseBin = Domain.createLocation( location -> {
         location.setParent( lMainWarehouse );
         location.setCode( aLocationCode + "_BIN_CODE" );
      } );
      LocationKey lMainWarehouseBin1 = Domain.createLocation( location -> {
         location.setParent( lMainWarehouse );
         location.setCode( aLocationCode + "_BIN_CODE1" );
      } );

      // create inventory at the bin location
      if ( aToCreateInventory ) {
         createInventory( lMainWarehouseBin );
         createInventory( lMainWarehouseBin1 );
      }

      // assign zone route order to one of the bin if the router order is provided
      if ( aRouterOrder != null ) {
         Domain.createLocationZone( entity -> {
            entity.setLocation( lMainWarehouseBin );
            entity.setRouteOrder( aRouterOrder );
         } );
      }

      // create line warehouse and set its supplier as the main warehouse, then create distribution
      // request on the line warehouse
      StockDistReqKey lDistReq = createLineWarehouseAndDistRequest( aLocationCode, lMainWarehouse );

      // Create PEND, COMPLETED and CANCELLED picked transfer
      createPickedItem( lDistReq, RefEventStatusKey.LXPEND, PENDING_XFER_QTY );
      createPickedItem( lDistReq, RefEventStatusKey.LXCMPLT, COMPLETED_XFER_QTY );
      createPickedItem( lDistReq, RefEventStatusKey.LXCANCEL, CANCELLED_XFER_QTY );

      return lDistReq;
   }


   private StockDistReqKey createLineWarehouseAndDistRequest( String aLocationCode,
         LocationKey aMainWarehouse ) {

      LocationKey lLineWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( PREFIX_LINE_CODE + aLocationCode );
         location.setHubLocation( aMainWarehouse );
      } );

      // create distribution request on the line warehouse
      StockDistReqKey lDistReq = Domain.createStockDistReq( pojo -> {
         pojo.setNeededLocation( lLineWarehouse );
         pojo.setSupplierLocation( aMainWarehouse );
         pojo.setStockNo( STOCK_NO );
         pojo.setOwner( iOwner );
         pojo.setNeededQuantity( new Float( NEEDED_QTY ) );
         pojo.setQtyUnit( QTY_UNIT_EA );
      } );
      return lDistReq;
   }


   private LocationKey createMainWarehouse( String aLocationCode ) {

      LocationKey lMainWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( PREFIX_MAIN_WH_CODE + aLocationCode );
         location.setIsSupplyLocation( true );
      } );

      return lMainWarehouse;
   }


   private InventoryKey createInventory( LocationKey aLocation ) {

      InventoryBuilder lInvBuilder = new InventoryBuilder().withPartNo( iStoreAltPart )
            .withSerialNo( "sn" ).withClass( RefInvClassKey.BATCH ).withBinQt( 2 )
            .atLocation( aLocation ).withCondition( RefInvCondKey.RFI ).withOwner( iOwner );

      return lInvBuilder.build();
   }


   private StockDistReqKey createDistRequestAndPickedItem( double aXferQty ) {

      final String LOCATION_CODE = "aa";
      StockDistReqKey lDistReq = createLineWarehouseAndDistRequest( LOCATION_CODE,
            createMainWarehouse( LOCATION_CODE ) );

      createPickedItem( lDistReq, RefEventStatusKey.LXPEND, aXferQty );

      return lDistReq;
   }


   private void createPickedItem( StockDistReqKey aDistReq, RefEventStatusKey aStatus,
         double aXferQty ) {

      TransferKey lXfer = new InventoryTransferBuilder().withStatus( aStatus )
            .withQuantity( new BigDecimal( aXferQty ) ).build();

      Domain.createStockDistReqPickedItem( pickedItem -> {
         pickedItem.setStockDistReq( aDistReq );
         pickedItem.setTransferKey( lXfer );
      } );
   }


   private void execute( String aParmValue ) throws SQLException {

      // for the function test, we don't care about the argument parameter name, but the order of
      // the parameter
      DataSetArgument lAgs = new DataSetArgument();
      lAgs.add( "1", aParmValue );

      iQuerySet = QueryExecutor.executeTableFunction( null, lAgs, iConnection.getConnection(),
            PACKAGE_NAME, FUNCTION );
   }


   private void assertDistRequest( StockDistReqKey aDistReq, String aCode ) {

      iQuerySet.next();

      assertEquals( aDistReq.getDbId(), iQuerySet.getInt( ColumName.DIST_REQ_DB_ID.toString() ) );
      assertEquals( aDistReq.getId(), iQuerySet.getInt( ColumName.DIST_REQ_ID.toString() ) );

      assertNotNull( iQuerySet.getString( ColumName.REQUEST_ID.toString() ) );

      assertEquals( PREFIX_LINE_CODE + aCode,
            iQuerySet.getString( ColumName.TO_WAREHOUSE.toString() ) );
      assertEquals( PREFIX_MAIN_WH_CODE + aCode,
            iQuerySet.getString( ColumName.FROM_SUPPLIER.toString() ) );

      assertEquals( NEEDED_QTY - PENDING_XFER_QTY - COMPLETED_XFER_QTY,
            iQuerySet.getDouble( ColumName.NEEDED_QTY.toString() ), 0 );
      assertEquals( OWNER_CD, iQuerySet.getString( ColumName.OWNER.toString() ) );
      assertEquals( QTY_UNIT_EA.getCd(), iQuerySet.getString( ColumName.QTY_UNIT.toString() ) );
   }


   /**
    * Scenario Description:
    *
    * - A stock no has one alternative part <br>
    * - A line warehouse's supplier is the main warehouse <br>
    * - Create a stock distribution request on this line warehouse with needed quantity
    * NEEDED_QTY<br>
    * - Create a picked item with transfer quantity as equal to or more than NEEDED_QTY <br>
    *
    * Query Result:
    *
    * - This distribution request will not show because it is all/over picked. <br>
    *
    * @throws Exception
    *            if an error occurs
    */
   private void createDistReqWithPickedQuantityAndAssertResult( double aXferQty )
         throws SQLException {

      StockDistReqKey lDistReq = createDistRequestAndPickedItem( aXferQty );

      execute( lDistReq.toString() );

      assertTrue( iQuerySet.isEmpty() );
   }

}
