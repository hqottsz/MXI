package com.mxi.mx.core.services.dataservices;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.common.dataset.DataSet;
import com.mxi.mx.core.common.ejb.dao.QueryDAOBean;
import com.mxi.mx.core.key.BulkLoadElementKey;
import com.mxi.mx.core.key.RefBulkLoadStatusKey;
import com.mxi.mx.core.key.UtlFileImportKey;
import com.mxi.mx.core.table.DataServices.BulkLoadElementDao;
import com.mxi.mx.core.table.DataServices.BulkLoadElementTableRow;
import com.mxi.mx.core.table.DataServices.JdbcBulkLoadElementDao;


/**
 * Tests the GetBulkLoadElementComponents.qrx query.
 *
 * @author sufelk
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetBulkLoadElementComponentsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private UtlFileImportKey iFileKey;
   private BulkLoadElementDao iBulkLoadElementDao;

   private String C0 = "The quick brown fox";
   private String C1 = "jumps over";
   private String C2 = "the lazy dog";
   private String ERROR = "Something wrong somewhere";


   @Before
   public void setup() {
      // initialization
      iFileKey = new UtlFileImportKey( 12, 14 );
      iBulkLoadElementDao = new JdbcBulkLoadElementDao();

      // create four rows - two with the ERROR status and correct file key, one with the wrong file
      // key, and one with the wrong status for retrieval by the query
      insertBulkLoadElementRow( iFileKey, 1, RefBulkLoadStatusKey.ERROR );
      insertBulkLoadElementRow( iFileKey, 2, RefBulkLoadStatusKey.ERROR );
      insertBulkLoadElementRow( new UtlFileImportKey( 12, 15 ), 1, RefBulkLoadStatusKey.QUEUED );
      insertBulkLoadElementRow( new UtlFileImportKey( 12, 16 ), 1, RefBulkLoadStatusKey.ERROR );

   }


   /**
    *
    * GIVEN an error row in the bulk_load_element table, WHEN the table is queried using the
    * GetBulkLoadElementComponents.qrx query with a where clause for rows in the ERROR status, THEN
    * erroneous rows with the given file id should be returned.
    *
    */
   @Test
   public void testGetBulkLoadElementComponentsQrx() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iFileKey, "aFileImportDbId", "aFileImportId" );
      lArgs.addWhereEquals( new String[] { "status_db_id", "status_cd" },
            RefBulkLoadStatusKey.ERROR );

      DataSet lDs = ( ( QueryDAOBean ) QuerySetFactory.getInstance() ).executeQuery(
            "com.mxi.mx.core.query.dataservices.GetBulkLoadElementComponents", lArgs );

      assertEquals( "Expected row count", 2, lDs.getRowCount() );

      lDs.first();
      assertEquals( "C0", C0, lDs.getString( "C0" ) );
      assertEquals( "C1", C1, lDs.getString( "C1" ) );
      assertEquals( "C2", C2, lDs.getString( "C2" ) );
      assertEquals( "Error info", ERROR, lDs.getString( "error_info" ) );
   }


   /**
    *
    * Inserts a row into the bulk_load_element table
    *
    * @param aFileKey
    *           the file import key
    * @param aFileElement
    *           the file element
    * @param aStatus
    *           the bulk load status
    */
   private void insertBulkLoadElementRow( UtlFileImportKey aFileKey, int aFileElement,
         RefBulkLoadStatusKey aStatus ) {
      BulkLoadElementTableRow lRow =
            iBulkLoadElementDao.create( new BulkLoadElementKey( aFileKey, aFileElement ) );
      lRow.setStatus( aStatus );
      lRow.setC0( C0 );
      lRow.setC1( C1 );
      lRow.setC2( C2 );
      lRow.setErrorInfo( RefBulkLoadStatusKey.ERROR.equals( aStatus ) ? ERROR : "" );
      iBulkLoadElementDao.insert( lRow );
   }

}
