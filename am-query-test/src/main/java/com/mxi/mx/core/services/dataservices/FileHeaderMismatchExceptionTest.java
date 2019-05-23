package com.mxi.mx.core.services.dataservices;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.services.dataservices.exceptions.FileHeaderMismatchException;


/**
 * Tests the FileHeaderMismatchException class
 *
 * @author spatlk
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class FileHeaderMismatchExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final String STOCK_LEVEL_LOCATION = "STOCKLOCATION";

   private static final String[] iExpectedHeaderCols =
         { "STOCK_CODE", "STOCK_LEVEL_LOCATION", "STOCK_LOW_ACTION" };

   private static final String[] iImportedHeaderColsCorrect =
         { "STOCK_CODE", "STOCK_LEVEL_LOCATION", "STOCK_LOW_ACTION" };

   // this String Array contains a string which is not expected to include within the header //
   private static final String[] iImportedHeaderColsIncorrect =
         { "STOCK_CODE", STOCK_LEVEL_LOCATION, "STOCK_LOW_ACTION" };


   /**
    *
    * GIVEN a bulk load data feature with a file that should contain certain defined field name ,
    * WHEN a file with correct field name elements is uploaded, THEN no exceptions should be raised
    *
    * @throws FileHeaderMismatchException
    *            This exception validates and ensures that the uploaded file has correct file
    *            headers
    */
   @Test( expected = Test.None.class )
   public void testFileHeaderMismatchException_FieldMatchesExpected()
         throws FileHeaderMismatchException {
      FileHeaderMismatchException.validate( iImportedHeaderColsCorrect, iExpectedHeaderCols );

   }


   /**
    *
    * GIVEN a bulk load data feature with a file that should contain certain defined field name ,
    * WHEN a file with incorrect field name elements is uploaded, THEN exceptions should be raised
    *
    * @throws FileHeaderMismatchException
    *            This exception validates and ensures that the uploaded file has correct file
    *            headers
    */
   @Test( expected = FileHeaderMismatchException.class )
   public void testFileHeaderMismatchException_FieldNotMatchesExpected()
         throws FileHeaderMismatchException {
      FileHeaderMismatchException.validate( iImportedHeaderColsIncorrect, iExpectedHeaderCols );

      Assert.fail( "FileHeaderMismatchException" );
   }
}
