
package com.mxi.mx.core.query.order;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the com.mxi.mx.web.query.org.GetReceiptOrgOwner query.
 *
 * @author nsubotic
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetReceiptOrgOwnerTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetReceiptOrgOwnerTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Tests the case where receipt organization is of type ADMIN.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testGetOwnerForAdmin() throws Exception {
      OrgKey lOrgKey = new OrgKey( 0, 1 );
      execute( lOrgKey );

      // make sure that only one row is returned
      assertEquals( 1, iDataSet.getRowCount() );

      // test the returned data
      testRow( new OwnerKey( 4650, 20001 ) );
   }


   /**
    * Tests the case where receipt organization is of type MRO.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testGetOwnerForMRO() throws Exception {
      OrgKey lOrgKey = new OrgKey( 4650, 10002 );
      execute( lOrgKey );

      // make sure that only one row is returned
      assertEquals( 1, iDataSet.getRowCount() );

      // test the returned data
      testRow( new OwnerKey( 4650, 20002 ) );
   }


   /**
    * Executes the query.
    *
    * @param aOrgKey
    *           organization key.
    */
   private void execute( OrgKey aOrgKey ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aOrgKey, "aOrgDbId", "aOrgId" );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row of the dataset.
    *
    * @param aOwnerKey
    *           owner key expected to be returned from the query
    */
   private void testRow( OwnerKey aOwnerKey ) {
      iDataSet.first();
      MxAssert.assertEquals( "owner_key", aOwnerKey.toString(), iDataSet.getString( "owner_key" ) );
   }
}
