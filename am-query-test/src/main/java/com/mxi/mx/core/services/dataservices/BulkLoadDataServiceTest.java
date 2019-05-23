package com.mxi.mx.core.services.dataservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.key.MxKeyBuilder;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.BulkLoadElementKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefBulkLoadFileActionKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.UtlFileImportKey;
import com.mxi.mx.core.services.dataservices.transferobject.BulkLoadElementTO;
import com.mxi.mx.core.services.dataservices.transferobject.FileImportTO;
import com.mxi.mx.core.table.DataServices.BulkLoadElementDao;
import com.mxi.mx.core.table.DataServices.BulkLoadElementTableRow;
import com.mxi.mx.core.table.DataServices.JdbcBulkLoadElementDao;
import com.mxi.mx.core.table.DataServices.JdbcUtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportDao;
import com.mxi.mx.core.table.DataServices.UtlFileImportTableRow;


/**
 * Test methods for the BulkLoadDataService. Parameterized tests for the
 * BulkLoadDataService.updateFileImportFileStatus() method and BulkLoadDataService.writeToCsv()
 * method can be found in the {@link UpdateFileImportFileStatusTest} and {@link WriteToCsvTest} test
 * classes respectively
 *
 * @author sufelk
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class BulkLoadDataServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   // CSV data
   private static final String FILE_NAME = "somefile.csv";
   private static final String C0 = "field_1";
   private static final String C1 = "field_2";
   private static final String C2 = "field_3";
   private static final String WORK_TYPE = "BULK_LOAD_DATA";

   private static final RefBulkLoadStatusKey QUEUED = RefBulkLoadStatusKey.QUEUED;
   private static final RefBulkLoadStatusKey ERROR = RefBulkLoadStatusKey.ERROR;
   private static final RefBulkLoadFileActionKey WAREHOUSE_STOCK_LEVEL =
         RefBulkLoadFileActionKey.WAREHOUSE_STOCK_LEVEL;
   private static final String PARAMETER_NAME = "BULK_LOAD_BATCH_SIZE";

   UtlFileImportKey iFileImportKey;
   UtlFileImportDao iFileImportDao;
   BulkLoadElementDao iBulkLoadElementDao;
   BulkLoadDataService iBulkLoadDataService;


   @Before
   public void setupData() {
      // initialize service
      iBulkLoadDataService = new BulkLoadDataService();

      // initialize DAOs
      iFileImportDao = new JdbcUtlFileImportDao();
      iBulkLoadElementDao = new JdbcBulkLoadElementDao();

      // initialize the file import key
      iFileImportKey = new UtlFileImportKey( 1234, 12 );

      // create a file import key entry in the UtlFileImport table
      UtlFileImportTableRow lRow = iFileImportDao.create( iFileImportKey );
      iFileImportDao.insert( lRow );
   }


   /**
    *
    * GIVEN a file import transfer object, WHEN the TO is passed to the createFileImport method,
    * THEN the information should be added to a corresponding entry in the UTL_FILE_IMPORT table.
    *
    */
   @Test
   public void testCreateFileImport() {
      FileImportTO lFileImportTO = new FileImportTO( FILE_NAME, QUEUED, WAREHOUSE_STOCK_LEVEL,
            new HumanResourceKey( 1, 1992 ) );

      UtlFileImportKey lFileImportKey = iBulkLoadDataService.createFileImport( lFileImportTO );

      UtlFileImportTableRow lFileImportRow = iFileImportDao.findByPrimaryKey( lFileImportKey );

      // Assert whether the file header information has been set properly
      assertEquals( "File name", FILE_NAME, lFileImportRow.getFileName() );
      assertEquals( "File action type", WAREHOUSE_STOCK_LEVEL, lFileImportRow.getFileActionType() );

   }


   /**
    *
    * GIVEN rows in the bulk_load_element staging table, WHEN the row statuses are checked using the
    * {@link BulkLoadDataService.checkExistByRowStatus} method, THEN the method should return True
    * if rows in the corresponding status exist
    *
    */
   @Test
   public void testCheckExistByRowStatus() {
      // assert the query returns false when there are no rows
      assertFalse( "No rows exist",
            iBulkLoadDataService.checkExistByRowStatus( iFileImportKey, QUEUED ) );

      // add a row with status ERROR to the table
      BulkLoadElementKey lRowKey = new BulkLoadElementKey( iFileImportKey, 100 );
      BulkLoadElementTableRow lRow = iBulkLoadElementDao.create( lRowKey );
      lRow.setStatus( RefBulkLoadStatusKey.ERROR );
      iBulkLoadElementDao.insert( lRow );

      // assert the query returns true when an error row exists
      assertTrue( "Error row exists",
            iBulkLoadDataService.checkExistByRowStatus( iFileImportKey, ERROR ) );
   }


   /**
    *
    * GIVEN a list of file indices, a file action and a file import key, WHEN the indices are passed
    * to the BulkLoadDataService.generateWorkItems method, THEN new BulkLoadData work items should
    * be generated for each batch of data as specified in the file index list
    */
   @Test
   public void testGenerateWorkItems() {

      ArrayList<Integer> lFileIdx = new ArrayList<>( Arrays.asList( 10, 20, 30, 37 ) );
      List<String> lExpectedWorkItemKeys = new ArrayList<String>();

      iBulkLoadDataService.generateWorkItems( lFileIdx, WAREHOUSE_STOCK_LEVEL, iFileImportKey );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WORK_TYPE );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      // assert whether the no of work items created for particular data set is correct.
      assertEquals( "Number of work items", 4, lQs.getRowCount() );

      while ( lQs.next() ) {
         lExpectedWorkItemKeys.add( lQs.getString( "data" ) );
      }

      int lStartingIdx = 1;
      String lActualWorkItemKey = null;

      for ( int j = 0; j < lFileIdx.size(); j++ ) {

         lActualWorkItemKey = new MxKeyBuilder( iFileImportKey, WAREHOUSE_STOCK_LEVEL, lStartingIdx,
               lFileIdx.get( j ) ).build().toValueString();

         // assert whether the created work items are in the correct format to excute.
         assertEquals( lExpectedWorkItemKeys.get( j ), lActualWorkItemKey );
         lStartingIdx = lFileIdx.get( j ) + 1;
      }

   }


   /**
    *
    * GIVEN a list of bulk load staging transfer objects, WHEN the list and a file import key are
    * passed to the loadDataToStagingTable method, THEN the data should be written to the database
    *
    */
   @Test
   public void testLoadToStagingTable() {

      // create a list of 8 transfer objects and set the batch size to 5
      List<BulkLoadElementTO> lTOList = populateBulkLoadElementTO( 8 );
      setBatchSize( 5 );

      // load the data to the staging table. All data should be written with two batch operations
      // (one for 5 rows and one for 3 rows)
      iBulkLoadDataService.loadToStagingTable( lTOList, iFileImportKey );

      // assert whether all 8 rows have been written to the staging table
      BulkLoadElementTableRow lBulkLoadElementRow;
      for ( int i = 0; i < 8; i++ ) {
         lBulkLoadElementRow = iBulkLoadElementDao.findByPrimaryKey(
               new BulkLoadElementKey( iFileImportKey.getDbId(), iFileImportKey.getId(), i ) );

         assertEquals( "Component 1 (C0)", C0, lBulkLoadElementRow.getC0() );

      }
   }


   /**
    *
    * GIVEN data in the UTL_FILE_IMPORT and BULK_LOAD_ELEMENT tables, WHEN the file element key
    * pertaining to the data is passed to the undoFileImport method, THEN the data should be removed
    * from the staging table.
    *
    */
   @Test
   public void testUndoFileImport() {

      // create new BulkLoadElementKeys
      BulkLoadElementKey lKey1 =
            new BulkLoadElementKey( iFileImportKey.getDbId(), iFileImportKey.getId(), 15 );
      BulkLoadElementKey lKey2 =
            new BulkLoadElementKey( iFileImportKey.getDbId(), iFileImportKey.getId(), 16 );

      // add rows corresponding to the keys to the BULK_LOAD_ELEMENT table
      iBulkLoadElementDao.insert( iBulkLoadElementDao.create( lKey1 ) );
      iBulkLoadElementDao.insert( iBulkLoadElementDao.create( lKey2 ) );

      // call the undoFileImport method
      iBulkLoadDataService.undoFileImport( iFileImportKey );

      // assert that the file import key has been removed from the database
      assertEquals( "UTL_FILE_IMPORT", false,
            iFileImportDao.findByPrimaryKey( iFileImportKey ).exists() );

      // assert that both the rows in the BULK_LOAD_ELEMENT table have been deleted
      assertEquals( "BULK_LOAD_ELEMENT", false,
            iBulkLoadElementDao.findByPrimaryKey( lKey1 ).exists() );
      assertEquals( "BULK_LOAD_ELEMENT", false,
            iBulkLoadElementDao.findByPrimaryKey( lKey2 ).exists() );

   }


   /**
    *
    * Creates and returns a list of BulkLoadElementTOs with the number of specified elements and
    * three fields per row
    *
    * @param numElements
    *           the number of transfer objects the list should contain
    * @return a list of BulkLoadElementTO objects
    */
   private List<BulkLoadElementTO> populateBulkLoadElementTO( int numElements ) {
      List<BulkLoadElementTO> lTOList = new ArrayList<BulkLoadElementTO>();

      for ( int i = 0; i < numElements; i++ ) {
         lTOList.add( new BulkLoadElementTO( i, WAREHOUSE_STOCK_LEVEL, QUEUED, "", C0, C1, C2 ) );
      }

      return lTOList;

   }


   /**
    *
    * Sets the value of the BULK_LOAD_BATCH_SIZE config parameter
    *
    * @param aValue
    *           the batch size
    */
   private void setBatchSize( int aValue ) {
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setInteger( PARAMETER_NAME, aValue );
      GlobalParameters.setInstance( lConfigParms );
   }

}
