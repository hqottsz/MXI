
package com.mxi.mx.core.dao.dataservices;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.UtlFileImportKey;
import com.mxi.mx.core.table.DataServices.JdbcUtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportTableRow;


/*
 * @author indunilW
 *
 * @created January 24, 2019
 */
public class JdbcUtlFileImportDaoTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   // Dao initialization
   private UtlFileImportDao iUtlFileImportDao = new JdbcUtlFileImportDao();

   private UtlFileImportKey iUtlFileImportKey = new UtlFileImportKey( 1234, 100 );
   private RefBulkLoadFileActionKey iRefBulkLoadFileActionKey =
         new RefBulkLoadFileActionKey( 0, "SUPPLY_LOC_STOCK_LEVEL" );


   /**
    * GIVEN a row of data, WHEN the data is inserted into the utl_file_import table using the dao,
    * THEN a corresponding record should be created in the table
    */
   @Test
   public void testCreateUtlFileImportRecord() {
      UtlFileImportTableRow lUtlFileImportTableRow = iUtlFileImportDao.create( iUtlFileImportKey );
      lUtlFileImportTableRow.setFileActionType( iRefBulkLoadFileActionKey );
      lUtlFileImportTableRow.setFileName( "File01.csv" );
      lUtlFileImportTableRow.setStatus( RefBulkLoadStatusKey.QUEUED );

      UtlFileImportKey lCreatedUtlFileImportKey =
            iUtlFileImportDao.insert( lUtlFileImportTableRow );

      UtlFileImportTableRow lFetchedRTableRow =
            iUtlFileImportDao.findByPrimaryKey( lCreatedUtlFileImportKey );
      assertEquals( iUtlFileImportKey, lFetchedRTableRow.getFileImport() );
   }
}
