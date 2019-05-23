
package com.mxi.mx.core.query.user;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsUserDefaultOrgUnderAdminCompanyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsUserDefaultOrgUnderAdminCompanyTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * The query should return a row when the user organization is under ADMIN company.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIsUserDefaultOrgUnderAdmin() throws Exception {

      // user that works for department under ADMIN company
      HumanResourceKey lHrKey = new HumanResourceKey( 4650, 10000 );

      // ACTION: Execute the Query
      execute( lHrKey );

      // Confirm that the query returns result
      // which means the user is under ADMIN company
      assertFalse( iDataSet.isEmpty() );
   }


   /**
    * The query should not return any rows when the user organization is under company other than
    * ADMIN( MRO, OPERATOR)
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testIsUserDefaultOrgUnderMRO() throws Exception {

      // User that works for department under MRO company
      HumanResourceKey lHrKey = new HumanResourceKey( 4650, 10001 );

      // ACTION: Execute the Query
      execute( lHrKey );

      // Confirm that the query does not return any rows
      // which means the user in not under ADMIN company
      assertTrue( iDataSet.isEmpty() );
   }


   /**
    * Execute the query.
    *
    * @param aHuman
    *           {@link InventoryKey} The inventory whose organization is to be tested
    */
   private void execute( HumanResourceKey aHuman ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aHuman, "aHrDbId", "aHrId" );

      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
