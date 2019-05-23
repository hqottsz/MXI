package com.mxi.mx.web.query.dataservices;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.UtlFileImportKey;
import com.mxi.mx.core.services.dataservices.BulkLoadDataService;
import com.mxi.mx.core.table.DataServices.JdbcUtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportTableRow;


/**
 *
 * Test for com.mxi.mx.web.query.dataservices.BulkExportAndImportTab query.
 *
 */

public class BulkExportAndImportTabTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   public static final String FILENAME = "test.csv";

   UtlFileImportDao iUtlFileImportDao = null;
   UtlFileImportKey iUtlFileImportKey = null;
   UtlFileImportTableRow iUtlFileImportTableRow = null;


   @Before
   public void setUp() {
      iUtlFileImportDao = new JdbcUtlFileImportDao();
   }


   /**
    *
    * GIVEN a csv file with file action key, file name, file status, WHEN the query is executed for
    * files imported with supply location and warehouse stock level data, THEN corresponding data
    * will be retrieved.
    *
    */
   @Test
   public void testGetStockLevelFileImportData() {

      insertUtlFileImportTableRow( 1345, 100, RefBulkLoadFileActionKey.SUPPLY_LOC_STOCK_LEVEL );

      QuerySet lQuerySet = new BulkLoadDataService().getFileImportDataSet(
            new String[] { RefBulkLoadFileActionKey.SUPPLY_LOC_STOCK_LEVEL.getCd(),
                  RefBulkLoadFileActionKey.WAREHOUSE_STOCK_LEVEL.getCd() } );

      while ( lQuerySet.next() ) {
         assertEquals( FILENAME, lQuerySet.getString( "file_name" ) );
         assertEquals( "Supply locations stock levels", lQuerySet.getString( "action_sdesc" ) );
         assertEquals( RefBulkLoadStatusKey.QUEUED.getCd(), lQuerySet.getString( "status_cd" ) );
      }

   }


   /**
    * GIVEN a CSV file with file action key,file name, and file status, WHEN the query is executed
    * for files imported with bin route order data, THEN only the corresponding data will be
    * retrieved.
    *
    */

   @Test
   public void testGetBinRouteOrderFileImportData() {

      insertUtlFileImportTableRow( 1445, 200, RefBulkLoadFileActionKey.BIN_ROUTE_ORDER );

      QuerySet lQuerySet = new BulkLoadDataService().getFileImportDataSet(
            new String[] { RefBulkLoadFileActionKey.BIN_ROUTE_ORDER.getCd() } );
      while ( lQuerySet.next() ) {
         assertEquals( FILENAME, lQuerySet.getString( "file_name" ) );
         assertEquals( RefBulkLoadFileActionKey.BIN_ROUTE_ORDER.getCd(),
               lQuerySet.getString( "file_action_type_cd" ) );
         assertEquals( "Bin route order", lQuerySet.getString( "action_sdesc" ) );
         assertEquals( RefBulkLoadStatusKey.QUEUED.getCd(), lQuerySet.getString( "status_cd" ) );
      }

   }


   /**
    * Insert a row into the utl_file_import table
    *
    * @param aFileImportId
    *           the file import id
    * @param aFileImportDbId
    *           the file import db id
    * @param aFileActionKey
    *           the file action key
    */
   private void insertUtlFileImportTableRow( int aFileImportId, int aFileImportDbId,
         RefBulkLoadFileActionKey aFileActionKey ) {
      iUtlFileImportKey = new UtlFileImportKey( aFileImportId, aFileImportDbId );
      iUtlFileImportTableRow = iUtlFileImportDao.create( iUtlFileImportKey );
      iUtlFileImportTableRow.setFileActionType( aFileActionKey );
      iUtlFileImportTableRow.setFileName( FILENAME );
      iUtlFileImportTableRow.setStatus( RefBulkLoadStatusKey.QUEUED );

      iUtlFileImportDao.insert( iUtlFileImportTableRow );
   }
}
