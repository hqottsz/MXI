
package com.mxi.mx.core.query.task;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.qrx.QuerySetKeyColumnPredicate;
import com.mxi.am.db.qrx.QuerySetRowSelector;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Tests the retrieval of all component do at next install tasks for given inventory item.
 *
 * @author gliang
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetComponentDoAtNextTaskByInvTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetComponentDoAtNextTaskByInvTest.class );
   }


   /**
    * Tests the retrieval of all component do at next install tasks for given inventory item.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetComponentDoAtNextTaskByInv() throws Exception {

      DataSet lDataSet = this.execute( new InventoryKey( 4650, 200001 ) );

      // There are two 'Do at Next Install' tasks for the given inventory item in the testing
      // dataset.
      Assert.assertEquals( "count", 2, lDataSet.getRowCount() );

      EventKey lFirstRow = new EventKey( 4650, 100001 );
      QuerySetRowSelector.select( lDataSet,
            new QuerySetKeyColumnPredicate( lFirstRow, "event_db_id", "event_id" ) );

      EventKey lSecondRow = new EventKey( 4650, 100002 );
      QuerySetRowSelector.select( lDataSet,
            new QuerySetKeyColumnPredicate( lSecondRow, "event_db_id", "event_id" ) );
   }


   /**
    * This method executes the query in GetComponentDoAtNextTaskByInv.qrx
    *
    * @param aInvKey
    *           the InventoryKey object
    *
    * @return The dataset after execution.
    */
   private DataSet execute( InventoryKey aInvKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aInvNoDbId", aInvKey.getDbId() );
      lDataSetArgument.add( "aInvNoId", aInvKey.getId() );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
