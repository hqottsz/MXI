
package com.mxi.mx.core.query.taskdefn;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This class tests the query com.mxi.mx.core.query.taskdefn.getCreateOnAnyInstTaskDefns.qrx
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetCreateOnAnyInstTaskDefnsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetCreateOnAnyInstTaskDefnsTest.class );
   }


   /**
    * TEST CASE: Retrieve the 'Create on Any Install' tasks given the parent inventory.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetCreateOnAnyInstallTasksViaParentInv() throws Exception {

      // ARRANGE
      int lParentInvDbId = 4650;
      int lParentInvId = 398935;

      // ACT
      QuerySet lQuerySet = this.getCreateOnAnyInstallTaskDefinitions(
            new InventoryKey( lParentInvDbId, lParentInvId ) );

      // ASSERT
      Assert.assertEquals( 0, lQuerySet.getRowCount() );
   }


   /**
    * TEST CASE: Retrieve the 'Create on Any Install' tasks given the inventory.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetCreateOnAnyInstallTasksViaInv() throws Exception {

      // ARRANGE
      int lInvDbId = 4650;
      int lInvId = 398936;

      // ACT
      QuerySet lQuerySet =
            this.getCreateOnAnyInstallTaskDefinitions( new InventoryKey( lInvDbId, lInvId ) );

      // ASSERT
      Assert.assertEquals( 1, lQuerySet.getRowCount() );

      while ( lQuerySet.next() ) {

         Assert.assertEquals( "inventory_key", "4650:398936",
               lQuerySet.getString( "inventory_key" ) );
         Assert.assertEquals( "task_task_key", "4650:1", lQuerySet.getString( "task_task_key" ) );

      }
   }


   /**
    * Get 'Create On Any Install' task definitions
    *
    * @param aTaskKey
    *           the TaskKey object
    *
    * @return The queryset after execution.
    */
   private QuerySet getCreateOnAnyInstallTaskDefinitions( InventoryKey aInvKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aInvNoDbId", aInvKey.getDbId() );
      lArgs.add( "aInvNoId", aInvKey.getId() );

      return QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.taskdefn.getCreateOnAnyInstTaskDefns", lArgs );
   }
}
