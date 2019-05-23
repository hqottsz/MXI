package com.mxi.mx.core.services.location;

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
 * Tests the RouteOrderTableFormatException class
 *
 * @author sufelk
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class RouteOrderTableFormatExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private LocationKey iSrvStore;
   private InvLocTable iBin_A;
   private InvLocTable iBin_B;
   private HSSFWorkbook iWorkbook;

   private final String FILENAME = "ParentSrvstore.xls";
   private final String INVALID_HEADER = "Wrong string";
   private final int ROUTE_ORDER = 2;


   @Before
   public void setUp() throws Exception {

      // create a serviceable store location
      iSrvStore = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore" );
         aLocation.setName( "ParentSrvstore" );
         aLocation.setType( RefLocTypeKey.SRVSTORE );
      } );

      // create 2 bin locations under the serviceable store
      iBin_A = InvLocTable.findByPrimaryKey( Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore/Store/BinA" );
         aLocation.setType( RefLocTypeKey.BIN );
         aLocation.setParent( iSrvStore );
      } ) );

      iBin_B = InvLocTable.findByPrimaryKey( Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore/Store/BinB" );
         aLocation.setType( RefLocTypeKey.BIN );
         aLocation.setParent( iSrvStore );
      } ) );

      String lParentLocCode = InvLocTable.findByPrimaryKey( iSrvStore ).getLocCd();

      // generate the exported excel with route order information for the two bins
      HSSFWorkbook lWorkbook = RouteOrderTestUtils.addRouteOrderDataToExcel(
            RouteOrderTestUtils.buildExportRouteOrderExcel(), lParentLocCode, iBin_A.getLocCd(),
            ROUTE_ORDER );
      iWorkbook = RouteOrderTestUtils.addRouteOrderDataToExcel( lWorkbook, lParentLocCode,
            iBin_B.getLocCd(), ROUTE_ORDER );

   }


   /**
    *
    * GIVEN an excel workbook in the format accepted for importing route order information, WHEN the
    * file is uploaded, THEN an exception should not be raised.
    *
    * @throws RouteOrderTableFormatException
    *            if the table format is invalid
    */
   @Test( expected = Test.None.class )
   public void testRouteOrderTableFormatException_ValidFormat()
         throws RouteOrderTableFormatException {
      RouteOrderTableFormatException.validate( iWorkbook, FILENAME );
   }


   /**
    *
    * GIVEN an excel workbook with an empty sheet, WHEN the file is uploaded, THEN a
    * RouteOrderTableFormatException should be raised.
    *
    * @throws RouteOrderTableFormatException
    *            because the sheet is empty
    */
   @Test( expected = RouteOrderTableFormatException.class )
   public void testRouteOrderTableFormatException_EmptySheet()
         throws RouteOrderTableFormatException {
      HSSFWorkbook lWb = new HSSFWorkbook();
      lWb.createSheet();

      RouteOrderTableFormatException.validate( lWb, FILENAME );

      Assert.fail( "Exception should be raised because the workbook has an empty worksheet." );
   }


   /**
    *
    * GIVEN an excel workbook with valid data, WHEN the file is uploaded with a different header
    * value, THEN a RouteOrderTableFormatException should be raised.
    *
    * @throws RouteOrderTableFormatException
    *            because the header has been modified
    *
    */
   @Test( expected = RouteOrderTableFormatException.class )
   public void testRouteOrderTableFormatException_InvalidColumnHeaders()
         throws RouteOrderTableFormatException {
      Cell lHeaderCell = iWorkbook.getSheetAt( 0 ).getRow( 0 ).getCell( 0 );
      lHeaderCell.setCellValue( INVALID_HEADER );

      RouteOrderTableFormatException.validate( iWorkbook, FILENAME );

      Assert.fail( "Exception should be raised because the header has been changed" );

   }


   /**
    *
    * GIVEN an excel workbook with valid data, WHEN the file is uploaded with an additional column
    * added, THEN a RouteOrderTableFormatException should be raised.
    *
    * @throws RouteOrderTableFormatException
    *            because the header has an additional column
    */
   @Test( expected = RouteOrderTableFormatException.class )
   public void testRouteOrderTableFormatException_ExtraColumns()
         throws RouteOrderTableFormatException {

      Cell lExtraCell = iWorkbook.getSheetAt( 0 ).getRow( 0 ).createCell( 3 );
      lExtraCell.setCellValue( INVALID_HEADER );

      RouteOrderTableFormatException.validate( iWorkbook, FILENAME );

      Assert.fail( "Exception should be raised because the header has an additional column" );

   }


   /**
    *
    * GIVEN an excel workbook with valid data, WHEN the file is uploaded after a bin location code
    * is removed, THEN a RouteOrderTableFormatException should be raised.
    *
    * @throws RouteOrderTableFormatException
    *            because the bin location code column contains empty cells
    */
   @Test( expected = RouteOrderTableFormatException.class )
   public void testRouteOrderTableFormatException_EmptyBinLocCode()
         throws RouteOrderTableFormatException {

      iWorkbook.getSheetAt( 0 ).getRow( 1 ).getCell( 1 ).setCellType( Cell.CELL_TYPE_BLANK );

      RouteOrderTableFormatException.validate( iWorkbook, FILENAME );

      Assert.fail( "Exception should be raised because the bin location code for bin_a is empty" );

   }

}
