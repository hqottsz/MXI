
package com.mxi.mx.core.query.order;

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
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author nsubotic
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetRepairOrderOwnerTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetRepairOrderOwnerTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * The query should return ADMIN organization's company for the repired inventory that does not
    * have owner
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testRepairOrderOwnerForAdmin() throws Exception {

      // Task Key - work order key
      TaskKey lTaskKey = new TaskKey( 4650, 30000 );

      // ACTION: Execute the Query
      execute( lTaskKey );

      // Organization returned is the organization of type ADMIN
      testRow( new OrgKey( "0:1" ) );
   }


   /**
    * The query should return repaired inventory owner organization's company
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testRepairOrderOwnerForMRO() throws Exception {

      // task key
      TaskKey lTaskKey = new TaskKey( 4650, 30001 );

      // ACTION: Execute the Query
      execute( lTaskKey );

      // Organization returned is the repaired inventory owner organization's company
      // In this case it is MRO company
      testRow( new OrgKey( "4650:20004" ) );
   }


   /**
    * Execute the query.
    *
    * @param aTaskKey
    *           - work package for the repaired inventory
    */
   private void execute( TaskKey aTaskKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey, "aTaskDbId", "aTaskId" );

      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Assert that the row contains correct data
    *
    * @param aOrgKey
    *           Organization key
    */
   private void testRow( OrgKey aOrgKey ) {
      iDataSet.first();
      MxAssert.assertEquals( "org_comp_key", aOrgKey,
            iDataSet.getKey( OrgKey.class, "org_comp_key" ) );
   }
}
