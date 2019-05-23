package com.mxi.mx.report.query.stockdistribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefHazmatKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPartTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqDao;
import com.mxi.mx.core.materials.stockdistribution.infra.StockDistReqTableRow;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Tests the report_stkdistreq_picklist_pkg PL/SQL function GetPickList
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ReportStkdistreqPicklistPkgGetPickListTest {

   /** Function info */
   private static final String PACKAGE_NAME = "report_stkdistreq_picklist_pkg";
   private static final String FUNCTION = "GetPickList";

   /** Stock Alternative Part info */
   private static final String PART_NO_OEM = "PART_NO_OEM";
   private static final String PART_NAME = "PART_NAME";
   private static final RefPartTypeKey PART_TYPE_COMNHW = RefPartTypeKey.COMNHW;
   private static final RefQtyUnitKey QTY_UNIT_EA = RefQtyUnitKey.EA;
   private static final RefHazmatKey PART_HAZMAT = new RefHazmatKey( 1, "HAZMAT" );

   /** Main Warehouse (supplier) info */
   private static final String MAIN_WH_CODE = "MAIN_WH_CODE";
   private static final String MAIN_WH_BIN_CODE1 = "MAIN_WH_BIN_CODE1";
   private static final double MAIN_WAREHOUSE_INV_QTY = 8;
   private static final double MAIN_WAREHOUSE_BIN_INV_QTY1 = 5;

   /** Line Warehouse (stock level location) info */
   private static final String LINE_WH_CODE = "LINE_WH_CODE";
   private static final String LINE_WH_BIN_CODE1 = "LINE_WH_BIN_CODE1";
   private static final String LINE_WH_BIN_CODE2 = "LINE_WH_BIN_CODE2";

   /** Stock Distribution Request info */
   private static StockNoKey STOCK_NO = new StockNoKey( 1, 1 );
   private static final double NEEDED_QTY = 20.0;
   private static final String OWNER_CD = "OWNER_CD";

   private LocationKey iMainWarehouse;
   private LocationKey iMainWarehouseBin1;
   private LocationKey iLineWarehouse;
   private PartNoKey iStoreAltPart;
   private OwnerKey iOwner;
   private InventoryKey iMainWarehouseInv;
   private StockDistReqKey iStockDistReq;


   public enum ColumName {
      PART_NO_OEM, PART_NAME, PART_TYPE, PART_UNIT, HAZMAT, FROM_LOC, TO_BIN
   }


   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setup() throws Exception {

      iOwner = Domain.createOwner( owner -> owner.setCode( OWNER_CD ) );

      // --------------------------------------------------------------------------------------------------------------
      // Create main warehouse, line warehouse and their bins
      // --------------------------------------------------------------------------------------------------------------

      iMainWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( MAIN_WH_CODE );
         location.setIsSupplyLocation( true );
      } );
      iMainWarehouseBin1 = Domain.createLocation( location -> {
         location.setParent( iMainWarehouse );
         location.setCode( MAIN_WH_BIN_CODE1 );
      } );

      iLineWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( LINE_WH_CODE );
         location.setHubLocation( iMainWarehouse );
      } );
      LocationKey lLineWarehouseBin1 = Domain.createLocation( location -> {
         location.setParent( iLineWarehouse );
         location.setCode( LINE_WH_BIN_CODE1 );
      } );
      LocationKey lLineWarehouseBin2 = Domain.createLocation( location -> {
         location.setParent( iLineWarehouse );
         location.setCode( LINE_WH_BIN_CODE2 );
      } );

      // --------------------------------------------------------------------------------------------------------------
      // Create two inventories in main warehouse and its bin location
      // --------------------------------------------------------------------------------------------------------------

      iStoreAltPart = createPart( STOCK_NO, RefInvClassKey.BATCH, PART_NO_OEM, PART_NAME );
      iMainWarehouseInv = createInventory( iStoreAltPart, iMainWarehouse, RefInvClassKey.BATCH,
            MAIN_WAREHOUSE_INV_QTY );
      createInventory( iStoreAltPart, iMainWarehouseBin1, RefInvClassKey.BATCH,
            MAIN_WAREHOUSE_BIN_INV_QTY1 );

      // --------------------------------------------------------------------------------------------------------------
      // Create bin levels on line warehouse bins
      // --------------------------------------------------------------------------------------------------------------

      createBinLevel( lLineWarehouseBin1, iStoreAltPart );
      createBinLevel( lLineWarehouseBin2, iStoreAltPart );
   }


   /**
    * Scenario Description:
    *
    * - A line warehouse has one bin <br>
    * - The line warehouse's supplier is the main warehouse <br>
    * - The main warehouse has one bin <br>
    * - A stock has one alternative part <br>
    * - This part has bin level at the line warehouse bin for a specific owner<br>
    * - The main warehouse has an inventory on this part for the same owner<br>
    * - The main warehouse bin has an inventory on this part for the same owner <br>
    * - A stock level is created in the stock at line warehouse for the same owner <br>
    * - A stock distribution request is created on the stock level with owner for the same owner<br>
    *
    * Query Result:
    *
    * - There is one row in the report<br>
    * - The part_no_oem shows the stock alternative part oem no<br>
    * - The part_name shows the stock alternative part name<br>
    * - The part_type shows the stock alternative part type<br>
    * - The hazmat shows the stock alternative part hazmat code<br>
    * - The from_bin shows "main warehouse code\<br\>(main warehouse inv
    * quantity[space]unit)\<br\>main warehouse bin code\<br\>(main warehouse bin inv
    * quantity[space]unit)"<br>
    * - The to_bin shows "line warehouse bin code"<br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testOnePartPickListWithFullDetailsForBatch() throws Exception {

      iStockDistReq = createDistRequest();

      QuerySet lQuerySet = executeFunction();

      assertEquals( 1, lQuerySet.getRowCount() );

      lQuerySet.next();

      assertCommonColumns( lQuerySet );

      assertEquals( PART_NO_OEM, lQuerySet.getString( ColumName.PART_NO_OEM.toString() ) );
      assertEquals( PART_NAME, lQuerySet.getString( ColumName.PART_NAME.toString() ) );

      String lFirst = getBinInfoString( MAIN_WH_BIN_CODE1, MAIN_WAREHOUSE_BIN_INV_QTY1 );
      String lSecond = getBinInfoString( MAIN_WH_CODE, MAIN_WAREHOUSE_INV_QTY );
      assertEquals( lFirst + "<br>" + lSecond,
            lQuerySet.getString( ColumName.FROM_LOC.toString() ) );

      assertEquals( LINE_WH_BIN_CODE1 + "<br>" + LINE_WH_BIN_CODE2,
            lQuerySet.getString( ColumName.TO_BIN.toString() ) );
   }


   @Test
   public void testOnePartPickListWithFullDetailsForSerilized() throws Exception {

      StockNoKey lSerializedStockNo = new StockNoKey( 2, 2 );
      PartNoKey lSerializedPart =
            createPart( lSerializedStockNo, RefInvClassKey.SER, "SER" + PART_NO_OEM, PART_NAME );
      createInventory( lSerializedPart, iMainWarehouse, RefInvClassKey.SER, null );

      iStockDistReq = createDistRequest( lSerializedStockNo );

      QuerySet lQuerySet = executeFunction();

      assertEquals( 1, lQuerySet.getRowCount() );

      lQuerySet.next();

      assertCommonColumns( lQuerySet );

      assertEquals( "SER" + PART_NO_OEM, lQuerySet.getString( ColumName.PART_NO_OEM.toString() ) );
      assertEquals( PART_NAME, lQuerySet.getString( ColumName.PART_NAME.toString() ) );

      assertEquals( getBinInfoString( MAIN_WH_CODE, 1 ),
            lQuerySet.getString( ColumName.FROM_LOC.toString() ) );

      // line warehouse bins are not in stock level, so they are not shown
      assertNull( lQuerySet.getString( ColumName.TO_BIN.toString() ) );
   }


   /**
    * Scenario Description:
    *
    * Besides all the scenarios in the test case
    * {@link ReportStkdistreqPicklistPkgGetPickListTest#testOnePartPickListWithFullDetailsForSingleDistRequest()},
    * we created a second alternative part for the stock no.
    *
    * Query Result:
    *
    * Besides the first row asserted in the test case linked above, the second row:<br>
    * - The from_supplier, to_warehouse, hight_light, needed_qty, request_id, part_type, and hazmat
    * are the same as the first row<br>
    * - The part_no_oem shows the second stock alternative part oem no<br>
    * - The part_name shows the second stock alternative part name<br>
    * - The from_bin and the to_bin shows null<br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testTwoPartPickList() throws Exception {

      iStockDistReq = createDistRequest();

      final String SECOND_PART_NO_OEM = "SECOND_PART_NO_OEM";
      final String SECOND_PART_NAME = "SECOND_PART_NAME";

      createPart( STOCK_NO, RefInvClassKey.BATCH, SECOND_PART_NO_OEM, SECOND_PART_NAME );

      QuerySet lQuerySet = executeFunction();

      assertEquals( 2, lQuerySet.getRowCount() );

      // The first part is the part with oem PART_NO_OEM because the result is ordered by part no
      // oem. We don't assert the other columns because they are already asserted in another test
      // method.
      lQuerySet.next();
      assertEquals( PART_NO_OEM, lQuerySet.getString( ColumName.PART_NO_OEM.toString() ) );
      assertEquals( PART_NAME, lQuerySet.getString( ColumName.PART_NAME.toString() ) );

      lQuerySet.next();
      assertCommonColumns( lQuerySet );
      assertEquals( SECOND_PART_NO_OEM, lQuerySet.getString( ColumName.PART_NO_OEM.toString() ) );
      assertEquals( SECOND_PART_NAME, lQuerySet.getString( ColumName.PART_NAME.toString() ) );

      // the from_bin is empty because there is no inventory on the second part, and the second part
      // has no bin level setup
      assertNull( lQuerySet.getString( ColumName.FROM_LOC.toString() ) );

      // the to_bin is empty because the part has no bin level setup
      assertNull( lQuerySet.getString( ColumName.TO_BIN.toString() ) );
   }


   /**
    * Scenario Description:
    *
    * Besides all the scenarios in the test case
    * {@link ReportStkdistreqPicklistPkgGetPickListTest#testOnePartPickListWithFullDetailsForSingleDistRequest()},
    * we created two more bins in the main warehouse.<br>
    * - main warehouse bin3 is assigned to zone route order 1<br>
    * - main warehouse bin2 is assigned to zone route order 1<br>
    * - main warehouse bin1 is assigned to zone route order 2<br>
    *
    * Query Result:
    *
    * Besides the other columns asserted in the test case linked above, the from_bin column:<br>
    * - The first one is main warehouse bin2<br>
    * - The second one is main warehouse bin3. Note: bin2 and bin3 are both route order 1, after
    * ordered by the location code, bin3 is behind bin2<br>
    * - The third one is main warehouse bin1<br>
    * - The fourth one is the main warehouse itself because it has no route order<br>
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testSupplierBinsOrderedByZoneRouteOrderAndBinCode() throws Exception {

      iStockDistReq = createDistRequest();

      final String MAIN_WH_BIN_CODE2 = "MAIN_WH_BIN_CODE2";
      final String MAIN_WH_BIN_CODE3 = "MAIN_WH_BIN_CODE3";
      final double MAIN_WAREHOUSE_BIN_INV_QTY2 = 3;
      final double MAIN_WAREHOUSE_BIN_INV_QTY3 = 23;

      // create second main warehouse bin and its bin level, then create inventory at that bin
      LocationKey lMainWarehouseBin2 = Domain.createLocation( location -> {
         location.setParent( iMainWarehouse );
         location.setCode( MAIN_WH_BIN_CODE2 );
      } );
      createBinLevel( lMainWarehouseBin2, iStoreAltPart );
      createInventory( iStoreAltPart, lMainWarehouseBin2, RefInvClassKey.BATCH,
            MAIN_WAREHOUSE_BIN_INV_QTY2 );

      // create third main warehouse bin and its bin level, then create inventory at that bin
      LocationKey lMainWarehouseBin3 = Domain.createLocation( location -> {
         location.setParent( iMainWarehouse );
         location.setCode( MAIN_WH_BIN_CODE3 );
      } );
      createBinLevel( lMainWarehouseBin3, iStoreAltPart );
      createInventory( iStoreAltPart, lMainWarehouseBin3, RefInvClassKey.BATCH,
            MAIN_WAREHOUSE_BIN_INV_QTY3 );

      // assign route order to these three main warehouse bins
      createLocationZone( lMainWarehouseBin3, 1 );
      createLocationZone( lMainWarehouseBin2, 1 );
      createLocationZone( iMainWarehouseBin1, 2 );

      QuerySet lQuerySet = executeFunction();

      assertEquals( 1, lQuerySet.getRowCount() );

      lQuerySet.next();
      assertCommonColumns( lQuerySet );

      assertEquals( PART_NO_OEM, lQuerySet.getString( ColumName.PART_NO_OEM.toString() ) );
      assertEquals( PART_NAME, lQuerySet.getString( ColumName.PART_NAME.toString() ) );

      // MAIN_WH_BIN_CODE2 and MAIN_WH_BIN_CODE3's route order is 1, so they show firstly. When the
      // route order is the same, the order is by the location code.
      String lFirst = getBinInfoString( MAIN_WH_BIN_CODE2, MAIN_WAREHOUSE_BIN_INV_QTY2 );
      String lSecond = getBinInfoString( MAIN_WH_BIN_CODE3, MAIN_WAREHOUSE_BIN_INV_QTY3 );
      String lThird = getBinInfoString( MAIN_WH_BIN_CODE1, MAIN_WAREHOUSE_BIN_INV_QTY1 );
      String lFourth = getBinInfoString( MAIN_WH_CODE, MAIN_WAREHOUSE_INV_QTY );
      assertEquals( lFirst + "<br>" + lSecond + "<br>" + lThird + "<br>" + lFourth,
            lQuerySet.getString( ColumName.FROM_LOC.toString() ) );

      assertEquals( LINE_WH_BIN_CODE1 + "<br>" + LINE_WH_BIN_CODE2,
            lQuerySet.getString( ColumName.TO_BIN.toString() ) );
   }


   @Test
   public void test_GIVEN_OnePartPickList_WHEN_TheDistRequestOwnerIsRemoved_THEN_OwnerDoesnotShow()
         throws Exception {

      iStockDistReq = createDistRequest();

      StockDistReqDao lDistReqDao = InjectorContainer.get().getInstance( StockDistReqDao.class );
      StockDistReqTableRow lDistReqRow = lDistReqDao.findByPrimaryKey( iStockDistReq );
      lDistReqRow.setOwner( null );
      lDistReqDao.update( lDistReqRow );

      QuerySet lQuerySet = executeFunction();

      assertEquals( 1, lQuerySet.getRowCount() );

      lQuerySet.next();

      // don't assert the other columns because they are tested in
      // testOnePartPickListWithFullDetails()
      String lFirst = getBinInfoString( MAIN_WH_BIN_CODE1, MAIN_WAREHOUSE_BIN_INV_QTY1, null );
      String lSecond = getBinInfoString( MAIN_WH_CODE, MAIN_WAREHOUSE_INV_QTY, null );
      assertEquals( lFirst + "<br>" + lSecond,
            lQuerySet.getString( ColumName.FROM_LOC.toString() ) );
   }


   @Test
   public void test_GIVEN_OnePartPickList_WHEN_TheInventoryOwnerIsDifferent_THEN_ItIsNotConsidered()
         throws Exception {

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iMainWarehouseInv );
      lInvInv.setOwner( new OwnerKey( 2, 2 ) );
      lInvInv.update();

      assertInvalidInventoryIsNotConsidered();
   }


   @Test
   public void test_GIVEN_OnePartPickList_WHEN_TheInventoryIsNotRFI_THEN_ItIsNotConsidered()
         throws Exception {

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iMainWarehouseInv );
      lInvInv.setInvCond( RefInvCondKey.INREP );
      lInvInv.update();

      assertInvalidInventoryIsNotConsidered();
   }


   @Test
   public void test_GIVEN_OnePartPickList_WHEN_TheInventoryIsNotFound_THEN_ItIsNotConsidered()
         throws Exception {

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iMainWarehouseInv );
      lInvInv.setNotFoundBool( true );
      lInvInv.update();

      assertInvalidInventoryIsNotConsidered();
   }


   @Test
   public void test_GIVEN_OnePartPickList_WHEN_TheInventoryIsIssued_THEN_ItIsNotConsidered()
         throws Exception {

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iMainWarehouseInv );
      lInvInv.setIssuedBool( true );
      lInvInv.update();

      assertInvalidInventoryIsNotConsidered();
   }


   @Test
   public void test_GIVEN_OnePartPickList_WHEN_TheInventoryIsNotLoose_THEN_ItIsNotConsidered()
         throws Exception {

      InvInvTable lInvInv = InvInvTable.findByPrimaryKey( iMainWarehouseInv );
      lInvInv.setNhInvNo( new InventoryKey( 1, 1 ) );
      lInvInv.update();

      assertInvalidInventoryIsNotConsidered();
   }


   private StockDistReqKey createDistRequest() {
      return createDistRequest( STOCK_NO );
   }


   private StockDistReqKey createDistRequest( StockNoKey aStockNo ) {

      return Domain.createStockDistReq( pojo -> {
         pojo.setNeededLocation( iLineWarehouse );
         pojo.setSupplierLocation( iMainWarehouse );
         pojo.setStockNo( aStockNo );
         pojo.setOwner( iOwner );
         pojo.setNeededQuantity( new Float( NEEDED_QTY ) );
         pojo.setQtyUnit( QTY_UNIT_EA );
      } );
   }


   private void createBinLevel( LocationKey aLocation, PartNoKey aPart ) {

      DataSetArgument lArgs = aLocation.getPKWhereArg();
      lArgs.add( aPart.getPKWhereArg() );

      MxDataAccess.getInstance().executeInsert( "INV_LOC_BIN", lArgs );
   }


   private void createLocationZone( LocationKey aLocation, Integer aRouteOrder ) {

      Domain.createLocationZone( entity -> {
         entity.setLocation( aLocation );
         entity.setRouteOrder( aRouteOrder );
      } );
   }


   private String getBinInfoString( String aBinCode, double aBinQt ) {

      return getBinInfoString( aBinCode, aBinQt, OWNER_CD );
   }


   private String getBinInfoString( String aBinCode, double aBinQty, String aOwnerCode ) {

      return aBinCode + "<br>(" + ( aOwnerCode == null ? "" : aOwnerCode + ", " )
            + new Double( aBinQty ).intValue() + " " + QTY_UNIT_EA.getCd() + ")";
   }


   private PartNoKey createPart( StockNoKey aStock, RefInvClassKey aInvClass, String aPartNoOem,
         String aPartName ) {

      return Domain.createPart( part -> {
         part.setCode( aPartNoOem );
         part.setShortDescription( aPartName );
         part.setPartType( PART_TYPE_COMNHW );
         part.setInventoryClass( aInvClass );
         part.setPartStatus( RefPartStatusKey.ACTV );
         part.setQtyUnitKey( QTY_UNIT_EA );
         part.setStockNoKey( aStock );
         part.setHazmat( PART_HAZMAT );
      } );
   }


   private InventoryKey createInventory( PartNoKey aPart, LocationKey aLocation,
         RefInvClassKey aInvClass, Double aBinQty ) {

      InventoryBuilder lInvBuilder =
            new InventoryBuilder().withPartNo( aPart ).withSerialNo( "sn" ).withClass( aInvClass )
                  .atLocation( aLocation ).withCondition( RefInvCondKey.RFI ).withOwner( iOwner );

      if ( aBinQty != null ) {
         lInvBuilder = lInvBuilder.withBinQt( aBinQty );
      }

      return lInvBuilder.build();
   }


   private QuerySet executeFunction() throws SQLException {

      // for the function test, we don't care about the argument parameter name, but the order of
      // the parameter
      return QueryExecutor.executeTableFunction( null, iStockDistReq.getPKWhereArg(),
            iConnection.getConnection(), PACKAGE_NAME, FUNCTION );
   }


   /**
    * Make sure the main warehouse is not in the from_bin column because its inventory is set as
    * invalid in the test case.
    *
    * @throws SQLException
    *            if a sql error occurs
    */
   private void assertInvalidInventoryIsNotConsidered() throws SQLException {

      iStockDistReq = createDistRequest();

      QuerySet lQuerySet = executeFunction();

      assertEquals( 1, lQuerySet.getRowCount() );

      lQuerySet.next();
      assertEquals( getBinInfoString( MAIN_WH_BIN_CODE1, MAIN_WAREHOUSE_BIN_INV_QTY1 ),
            lQuerySet.getString( ColumName.FROM_LOC.toString() ) );
   }


   private void assertCommonColumns( QuerySet aQuerySet ) {

      assertEquals( PART_TYPE_COMNHW, aQuerySet.getString( ColumName.PART_TYPE.toString() ) );
      assertEquals( PART_HAZMAT.getCd(), aQuerySet.getString( ColumName.HAZMAT.toString() ) );
      assertEquals( QTY_UNIT_EA.getCd(), aQuerySet.getString( ColumName.PART_UNIT.toString() ) );
   }

}
