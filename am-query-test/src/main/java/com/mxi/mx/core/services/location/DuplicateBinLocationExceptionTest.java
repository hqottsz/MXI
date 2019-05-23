package com.mxi.mx.core.services.location;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;


/**
 * Tests the DuplicateBinLocationException class
 *
 * @author sufelk
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class DuplicateBinLocationExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private HSSFWorkbook iWorkbook;

   private final String PARENT_LOC_CODE = "ParentLocCode";
   private final String BIN_LOC_CODE_1 = "BinLoc1";
   private final String BIN_LOC_CODE_2 = "BinLoc2";
   private final int ROUTE_ORDER = 2;


   @Before
   public void setUp() {
      iWorkbook = RouteOrderTestUtils.buildExportRouteOrderExcel();
   }


   /**
    *
    * GIVEN an imported excel workbook, WHEN the excel contains duplicate bin locations, THEN a
    * DuplicateBinLocationException is raised.
    *
    * @throws DuplicateBinLocationException
    *            because the excel contains duplicate bin location codes
    */
   @Test( expected = DuplicateBinLocationException.class )
   public void testDuplicateBinLocationException_WhenExcelHasDuplicates()
         throws DuplicateBinLocationException {
      // create an excel with two entries for bin_loc_1
      HSSFWorkbook lWorkbook =
            RouteOrderTestUtils.addRouteOrderDataToExcel(
                  RouteOrderTestUtils.addRouteOrderDataToExcel( iWorkbook, PARENT_LOC_CODE,
                        BIN_LOC_CODE_1, ROUTE_ORDER ),
                  PARENT_LOC_CODE, BIN_LOC_CODE_1, ROUTE_ORDER );

      DuplicateBinLocationException.validate( lWorkbook );

      Assert.fail(
            "Exception should be raised because the workbook contains duplicate bin locations." );

   }


   /**
    *
    * GIVEN an imported excel workbook, WHEN the excel does not contain duplicates, THEN a
    * DuplicateBinLocationException should not be raised.
    *
    * @throws DuplicateBinLocationException
    *            if the excel contains duplicate bin location codes
    */
   @Test( expected = Test.None.class )
   public void testDuplicateBinLocationException_WhenExcelDoesNotContainDuplicates()
         throws DuplicateBinLocationException {
      // create an excel without duplicates
      HSSFWorkbook lWorkbook =
            RouteOrderTestUtils.addRouteOrderDataToExcel(
                  RouteOrderTestUtils.addRouteOrderDataToExcel( iWorkbook, PARENT_LOC_CODE,
                        BIN_LOC_CODE_1, ROUTE_ORDER ),
                  PARENT_LOC_CODE, BIN_LOC_CODE_2, ROUTE_ORDER );

      DuplicateBinLocationException.validate( lWorkbook );

   }

}
