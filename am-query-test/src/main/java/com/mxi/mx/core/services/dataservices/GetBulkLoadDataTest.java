package com.mxi.mx.core.services.dataservices;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.BulkLoadElementKey;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.UtlFileImportKey;
import com.mxi.mx.core.table.DataServices.BulkLoadElementDao;
import com.mxi.mx.core.table.DataServices.BulkLoadElementTableRow;
import com.mxi.mx.core.table.DataServices.JdbcBulkLoadElementDao;
import com.mxi.mx.core.table.DataServices.JdbcUtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportTableRow;


/**
 * This class tests the GetBulkLoadData query.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetBulkLoadDataTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private RefBulkLoadFileActionKey iRefBulkLoadFileActionKey =
         RefBulkLoadFileActionKey.SUPPLY_LOC_STOCK_LEVEL;
   private UtlFileImportKey iFileImportKey = new UtlFileImportKey( 1234, 1 );
   private UtlFileImportDao iFileImportDao = new JdbcUtlFileImportDao();
   private BulkLoadElementDao iBulkLoadElementDao = new JdbcBulkLoadElementDao();
   private int iNumberOfRows = 5;
   private int iActualRows = 3;
   private int iStartIndex = 0;
   private int iEndIndex = 10;


   @Before
   public void setupData() {
      // create a file import key entry in the UtlFileImport table
      UtlFileImportTableRow lUtlFileImportTableRow = iFileImportDao.create( iFileImportKey );
      iFileImportDao.insert( lUtlFileImportTableRow );

      for ( int i = 0; i < iNumberOfRows; i++ ) {
         BulkLoadElementTableRow lBulkLoadElementTableRow = iBulkLoadElementDao.create(
               new BulkLoadElementKey( iFileImportKey.getDbId(), iFileImportKey.getId(), i ) );

         if ( i % 2 == 0 ) {
            lBulkLoadElementTableRow.setStatus( RefBulkLoadStatusKey.QUEUED );
         } else {
            lBulkLoadElementTableRow.setStatus( RefBulkLoadStatusKey.ERROR );
         }
         lBulkLoadElementTableRow.setFileActionType( iRefBulkLoadFileActionKey );
         iBulkLoadElementDao.insert( lBulkLoadElementTableRow );
      }
   }


   /**
    * GIVEN data set in bulk_load_element table WHEN execute query THEN number of rows in table must
    * be retrieved.
    *
    */
   @Test
   public void testBulkLoadDataQuery() {
      QuerySet lQs = new BulkLoadDataService().getBulkLoadElements( iStartIndex, iEndIndex,
            iFileImportKey, iRefBulkLoadFileActionKey, RefBulkLoadStatusKey.QUEUED );
      assertEquals( iActualRows, lQs.getRowCount(), 0 );
   }
}
