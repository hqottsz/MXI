package com.mxi.mx.core.services.location;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * This class tests methods in the ZoneService.
 *
 * @author sufelk
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ZoneServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private LocationKey iSrvStore;
   private LocationKey iStore;
   private LocationKey iBin_A;
   private LocationKey iBin_B;
   private LocationKey iBin_C;
   private final int ROUTE_ORDER = 2;


   @Before
   public void setUp() throws Exception {

      // create a serviceable store location
      iSrvStore = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore" );
         aLocation.setName( "ParentSrvstore" );
         aLocation.setType( RefLocTypeKey.SRVSTORE );
      } );

      // create a store location
      iStore = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore/Store" );
         aLocation.setType( RefLocTypeKey.STORE );
         aLocation.setParent( iSrvStore );
      } );

      // create 3 bin locations - two with the STORE as parent, and one with the SRVSTORE as parent
      iBin_A = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore/Store/BinA" );
         aLocation.setType( RefLocTypeKey.BIN );
         aLocation.setParent( iStore );
      } );

      iBin_B = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore/Store/BinB" );
         aLocation.setType( RefLocTypeKey.BIN );
         aLocation.setParent( iStore );
      } );

      iBin_C = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore/BinC" );
         aLocation.setType( RefLocTypeKey.BIN );
         aLocation.setParent( iSrvStore );
      } );

   }


   /**
    *
    * GIVEN a serviceable store with a store and bin hierarchy below it, WHEN the excel is built to
    * export the route order information, THEN the excel sheet should contain a sheet with 4 rows
    * and 3 columns.
    *
    * Note - data in the generated excel is already validated in the
    * GetBinInformationForSrvstoreTest query test.
    *
    */
   @Test
   public void testBuildExcelForRouteOrder() {
      // build an excel for the serviceable store
      HSSFWorkbook lWorkBook = ZoneService.buildExcelForRouteOrder( iSrvStore );
      HSSFSheet lSheet = lWorkBook.getSheetAt( 0 );

      // ensure there are 4 rows (header + the bin locations we created), and 3 columns (parent
      // location, bin location and route order)
      assertEquals( "Number of rows in the generated excel sheet", 4,
            lSheet.getPhysicalNumberOfRows(), 0 );
      assertEquals( "Number of columns in the generated excel sheet", 3,
            lSheet.getRow( 0 ).getPhysicalNumberOfCells(), 0 );
   }


   /**
    *
    * GIVEN an excel sheet with route order information of three bin locations in the hierarchy of a
    * serviceable store:
    *
    * Bin_A--> already has a route order, belongs to the store; Bin_B--> does not have a route
    * order, belongs to the store; Bin_C--> does not have a route order, belongs to the serviceable
    * store
    *
    * WHEN the route order information is modified in the excel and imported into Maintenix, THEN
    * the new route order information should be added to the database.
    *
    * @throws IOException
    *            if an IO error occurs
    * @throws InvalidRouteOrderException
    *            if the route order value is not empty or a positive integer
    * @throws BinsNotFoundException
    *            if the bin does not exist in Maintenix
    */
   @Test
   public void testReadRouteOrderDataFromExcel_dataFromExcelWrittenToDb()
         throws IOException, BinsNotFoundException, InvalidRouteOrderException {
      // add a route order for one of the bins
      RouteOrderTestUtils.addRouteOrder( iBin_A, 5 );

      // build an excel sheet for the sub locations under iSrvstore and get the first sheet
      HSSFWorkbook lWorkbook = ZoneService.buildExcelForRouteOrder( iSrvStore );
      HSSFSheet lSheet = lWorkbook.getSheetAt( 0 );

      // define a route order value for the three bins
      for ( int i = 1; i < 4; i++ ) {
         Cell lRouteOrderCell = lSheet.getRow( i ).getCell( 2 );
         lRouteOrderCell.setCellValue( ROUTE_ORDER );
      }

      // read data from the excel sheet and write the data into the database
      ZoneService.readRouteOrderDataFromExcel( lWorkbook, iSrvStore );

      // ensure route order has been set for all three bins - the value in binA should have been
      // updated and
      // route orders for bins B and C added to the db
      RouteOrderTestUtils.assertRouteOrder( iBin_A, ROUTE_ORDER );
      RouteOrderTestUtils.assertRouteOrder( iBin_B, ROUTE_ORDER );
      RouteOrderTestUtils.assertRouteOrder( iBin_C, ROUTE_ORDER );
   }


   /**
    *
    * GIVEN an exported excel sheet with route order information of three bin locations with route
    * order information, WHEN the excel sheet is imported with route order information cleared, THEN
    * the data should be cleared from the inv_loc_zone table in the database as well.
    *
    * @throws IOException
    *            if an IO error occurs
    * @throws InvalidRouteOrderException
    *            if the route order is not empty or a positive integer
    * @throws BinsNotFoundException
    *            if the bin does not exist in Maintenix
    */
   @Test
   public void testReadRouteOrderDataFromExcel_dbDataClearedWhenExcelCleared()
         throws IOException, BinsNotFoundException, InvalidRouteOrderException {
      // add route orders for bin_a and bin_c
      RouteOrderTestUtils.addRouteOrder( iBin_A, ROUTE_ORDER );
      RouteOrderTestUtils.addRouteOrder( iBin_B, ROUTE_ORDER );
      RouteOrderTestUtils.addRouteOrder( iBin_C, ROUTE_ORDER );

      // build an excel sheet for the sub locations under iSrvstore and get the first sheet
      HSSFWorkbook lWorkbook = ZoneService.buildExcelForRouteOrder( iSrvStore );
      HSSFSheet lSheet = lWorkbook.getSheetAt( 0 );

      // clear the route order from the generated excel
      for ( int i = 1; i < 4; i++ ) {
         Cell lRouteOrderCell = lSheet.getRow( i ).getCell( 2 );
         lRouteOrderCell.setCellValue( "" );
      }

      // read data from the excel sheet and write the data into the database
      ZoneService.readRouteOrderDataFromExcel( lWorkbook, iSrvStore );

      // ensure route order has been cleared (no data exists in inv_loc_zone for the three bin
      // locations)
      RouteOrderTestUtils.assertRouteOrderCleared( iBin_A );
      RouteOrderTestUtils.assertRouteOrderCleared( iBin_B );
      RouteOrderTestUtils.assertRouteOrderCleared( iBin_C );
   }


   /**
    *
    * GIVEN an excel sheet with bin locations, WHEN the file is uploaded through the Import Sub
    * Locations page for a srvstore that does not have bin sub locations, THEN a
    * BinsNotFoundException should be raised
    *
    * @throws BinsNotFoundException
    *            because the excel sheet contains bins that are not defined in the Maintenix db for
    *            the serviceable store
    * @throws InvalidRouteOrderException
    *            if the route order value is invalid
    * @throws IOException
    *            if an IO error occurs
    */
   @Test( expected = BinsNotFoundException.class )
   public void testBinsNotFoundException_DbHasNoBinLocationsForSrvstore()
         throws BinsNotFoundException, InvalidRouteOrderException, IOException {
      // create a new serviceable store location (with no bins)
      LocationKey lBinlessSrvstore = Domain.createLocation( aLocation -> {
         aLocation.setCode( "SrvstoreWOBins" );
         aLocation.setName( "SrvstoreWOBins" );
         aLocation.setType( RefLocTypeKey.SRVSTORE );
      } );

      // generate an excel with route order information for a bin that belongs to iSrvStore
      HSSFWorkbook lWorkbook = RouteOrderTestUtils.addRouteOrderDataToExcel(
            RouteOrderTestUtils.buildExportRouteOrderExcel(),
            InvLocTable.findByPrimaryKey( iSrvStore ).getLocCd(),
            InvLocTable.findByPrimaryKey( iBin_A ).getLocCd(), 2 );

      // read route order data from the workbook with the binless serviceable store as parent
      ZoneService.readRouteOrderDataFromExcel( lWorkbook, lBinlessSrvstore );

      Assert.fail( "The excel sheet contains bins that do not exist in in the db" );
   }


   /**
    *
    * GIVEN an excel sheet with a bin location code that does not belong to a bin in Maintenix, WHEN
    * the file is uploaded through the Import Sub Locations page, THEN a BinsNotFoundException
    * should be raised.
    *
    * @throws BinsNotFoundException
    *            because the excel contains a bin that does not exist in Maintenix
    * @throws InvalidRouteOrderException
    *            if the route order value is invalid
    * @throws IOException
    *            if an IO error occurs
    */
   @Test( expected = BinsNotFoundException.class )
   public void testBinsNotFoundException_BinLocationDoesNotExistInMaintenix()
         throws BinsNotFoundException, InvalidRouteOrderException, IOException {
      // generate an excel with route order information of the bins that belongs to iSrvStore
      HSSFWorkbook lWorkbook = RouteOrderTestUtils.addRouteOrderDataToExcel(
            RouteOrderTestUtils.addRouteOrderDataToExcel(
                  RouteOrderTestUtils.addRouteOrderDataToExcel(
                        RouteOrderTestUtils.buildExportRouteOrderExcel(),
                        InvLocTable.findByPrimaryKey( iStore ).getLocCd(),
                        InvLocTable.findByPrimaryKey( iBin_A ).getLocCd(), 2 ),
                  InvLocTable.findByPrimaryKey( iStore ).getLocCd(),
                  InvLocTable.findByPrimaryKey( iBin_B ).getLocCd(), 2 ),
            InvLocTable.findByPrimaryKey( iSrvStore ).getLocCd(),
            InvLocTable.findByPrimaryKey( iBin_C ).getLocCd(), 2 );

      // change the bin location code of Bin_B to Bin_D (which does not exist in Maintenix)
      Cell lBinBLocCode = lWorkbook.getSheetAt( 0 ).getRow( 2 ).getCell( 1 );
      lBinBLocCode.setCellValue( "Bin_D" );

      ZoneService.readRouteOrderDataFromExcel( lWorkbook, iSrvStore );

      Assert.fail( "Bin_D does not exist in the Maintenix db" );
   }
}
