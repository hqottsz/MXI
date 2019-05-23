package com.mxi.mx.core.services.dataservices;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.services.dataservices.exceptions.UploadFileTypeException;


/**
 * Tests the UploadFileTypeException class
 *
 * @author sufelk
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class UploadFileTypeExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private final String JPEG_FILE = "Somefile.jpeg";
   private final String XLS_FILE = "Somefile.xls";
   private final String CSV_FILE = "Somefile.csv";


   /**
    *
    * GIVEN a file that is not in .xls format, WHEN the file is uploaded to Maintenix through the
    * Import Sub Locations functionality, THEN an UploadFileTypeException should be raised.
    *
    * @throws UploadFileTypeException
    *            if the file is not in .xls format
    */
   @Test( expected = UploadFileTypeException.class )
   public void testUploadFileTypeExceptionWhenFileIsNotExcel() throws UploadFileTypeException {
      UploadFileTypeException.validate( JPEG_FILE, ".xls" );

      Assert.fail( "Only files in .xls or .csv format can be uploaded" );
   }


   /**
    *
    * GIVEN a file that is in .xls format, WHEN the file is uploaded to Maintenix through the Import
    * Sub Locations functionality, THEN an UploadFileTypeException should not be raised.
    *
    * @throws UploadFileTypeException
    *            if the file format is invalid
    *
    */
   @Test( expected = Test.None.class )
   public void testUploadFileTypeExceptionWhenFileIsExcel() throws UploadFileTypeException {
      UploadFileTypeException.validate( XLS_FILE, ".xls" );
   }


   /**
    *
    * GIVEN a file that is in .csv format, WHEN the file is uploaded to Maintenix, THEN an
    * UploadFileTypeException should not be raised
    *
    * @throws UploadFileTypeException
    *            if the file format is invalid
    */
   @Test( expected = Test.None.class )
   public void testUploadFileTypeExceptionWhenFileIsCSV() throws UploadFileTypeException {
      UploadFileTypeException.validate( CSV_FILE, ".csv" );
   }

}
