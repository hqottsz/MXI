
package com.mxi.mx.core.query.task;

import org.junit.Before;
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
import com.mxi.mx.core.key.TaskTaskFlagsKey;
import com.mxi.mx.core.table.task.TaskTaskFlagsTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.core.query.task.getRequirementsMissingJobCards.qrx
 *
 * @author srengasamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetRequirementsMissingJobCardsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetRequirementsMissingJobCardsTest.class );
   }


   @Before
   public void setUp() throws Exception {
      TaskTaskFlagsTable lTaskTaskFlagsTable =
            TaskTaskFlagsTable.findByPrimaryKey( new TaskTaskFlagsKey( 4650, 238677 ) );
      lTaskTaskFlagsTable.setPreventExeBool( true );
      lTaskTaskFlagsTable.update();
   }


   /**
    * Tests the retrieval of All Prevent Execusion And or Missing JIC Requirement Of WorkPackage.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetRequirementsMissingJobCards() throws Exception {

      int lCheckDbId = 4650;
      int lCheckId = 138186;

      DataSet lDataSet = this.execute( lCheckDbId, lCheckId );

      // Assert for No of Rows
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDataSet.getRowCount() );
      lDataSet.next();

      // JIC Missing Requirement
      testRow( lDataSet, "REQ10 (requ)" );
      lDataSet.next();

      // Prevent Execution Requirement
      testRow( lDataSet, "REQ11 (reqv)" );
   }


   /**
    * This method executes the query in getRequirementsMissingJobCards.qrx
    *
    * @param aCheckDbId
    *           The work package db id.
    * @param aCheckID
    *           The work package id.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( int aCheckDbId, int aCheckID ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aCheckDbId", aCheckDbId );
      lDataSetArgument.add( "aCheckID", aCheckID );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.task.getRequirementsMissingJobCards", lDataSetArgument );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset.
    * @param aReqLabel
    *           the requirement label
    */
   private void testRow( DataSet aDs, String aReqLabel ) {
      MxAssert.assertEquals( "req_list", aReqLabel, aDs.getString( "req_list" ) );
   }
}
