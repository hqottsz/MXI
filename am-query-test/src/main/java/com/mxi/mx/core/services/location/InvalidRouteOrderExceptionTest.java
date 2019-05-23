
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
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;


/**
 *
 * Tests the InvalidRouteOrderException class
 *
 * @author spatlk
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class InvalidRouteOrderExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private Cell iRouteOrderCell;

   private final String BIN_LOC_CODE = "BinLocationCode";


   @Before
   public void setUp() throws Exception {
      // create a new cell to pass the route order value to the validation method
      iRouteOrderCell = new HSSFWorkbook().createSheet().createRow( 0 ).createCell( 0 );
   }


   /**
    *
    * GIVEN an empty cell, WHEN the cell is validated, THEN an InvalidRouteOrderException should not
    * be raised
    *
    * @throws InvalidRouteOrderException
    *            if the cell contains an invalid value
    */
   @Test( expected = Test.None.class )
   public void testInvalidRouteOrderException_testCellContainsNullValue()
         throws InvalidRouteOrderException {
      InvalidRouteOrderException.validate( null, BIN_LOC_CODE );
   }


   /**
    *
    * GIVEN a cell that contains a value for the route order, WHEN the value is negative, THEN a
    * InvalidRouteOrderException should be raised
    *
    * @throws InvalidRouteOrderException
    *            because the route order cannot be negative
    */
   @Test( expected = InvalidRouteOrderException.class )
   public void testInvalidRouteOrderException_testCellContainsNegativeNumber()
         throws InvalidRouteOrderException {

      iRouteOrderCell.setCellValue( -3 );

      InvalidRouteOrderException.validate( iRouteOrderCell, BIN_LOC_CODE );

      Assert.fail( "Route order cannot be negative" );

   }


   /**
    *
    * GIVEN a cell that contains a value for the route order, WHEN the value is a decimal, THEN a
    * InvalidRouteOrderException should be raised
    *
    * @throws InvalidRouteOrderException
    *            because the route order cannot be a decimal
    */
   @Test( expected = InvalidRouteOrderException.class )
   public void testInvalidRouteOrderException_testCellContainsDecimals()
         throws InvalidRouteOrderException {

      iRouteOrderCell.setCellValue( 2.5 );

      InvalidRouteOrderException.validate( iRouteOrderCell, BIN_LOC_CODE );

      Assert.fail( "Route order cannot be a decimal" );

   }
}
