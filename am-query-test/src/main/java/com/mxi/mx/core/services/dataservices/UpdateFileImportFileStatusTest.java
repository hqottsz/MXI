package com.mxi.mx.core.services.dataservices;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.UtlFileImportKey;
import com.mxi.mx.core.table.DataServices.JdbcUtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportTableRow;


/**
 * Parameterized unit test. GIVEN a file in UTL_FILE_IMPORT, WHEN the file status is updated using
 * the BulkLoadDataService.updateFileImportStatusMethod, THEN the file status should be updated in
 * the UTL_FILE_IMPORT table.
 *
 * @author sufelk
 *
 */
@RunWith( Parameterized.class )
public class UpdateFileImportFileStatusTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   BulkLoadDataService iBulkLoadDataService;
   UtlFileImportKey iFileImportKey;
   UtlFileImportDao iFileImportDao;

   private RefBulkLoadStatusKey iBulkLoadStatus;


   @Before
   public void setup() {
      // Initialize objects
      iBulkLoadDataService = new BulkLoadDataService();
      iFileImportDao = new JdbcUtlFileImportDao();

      // Insert the file import row into the UTL_FILE_IMPORT table
      iFileImportKey = new UtlFileImportKey( 1234, 15 );
      UtlFileImportTableRow lRow = iFileImportDao.create( iFileImportKey );
      iFileImportDao.insert( lRow );
   }


   public UpdateFileImportFileStatusTest(RefBulkLoadStatusKey aBulkLoadStatus) {
      iBulkLoadStatus = aBulkLoadStatus;
   }


   @Parameterized.Parameters
   public static Collection fileStatuses() {
      return Arrays.asList(
            new Object[][] { { RefBulkLoadStatusKey.QUEUED }, { RefBulkLoadStatusKey.PROCESSING },
                  { RefBulkLoadStatusKey.ERROR }, { RefBulkLoadStatusKey.FINISHED }

            } );
   }


   @Test
   public void testFileImportFileStatus() {

      // set the file import status using the bulk load data service
      iBulkLoadDataService.updateFileImportFileStatus( iFileImportKey, iBulkLoadStatus, null );
      RefBulkLoadStatusKey lStatus = iFileImportDao.findByPrimaryKey( iFileImportKey ).getStatus();

      // assert that the status has been updated
      assertEquals( "Bulk load status", iBulkLoadStatus, lStatus );
   }

}
