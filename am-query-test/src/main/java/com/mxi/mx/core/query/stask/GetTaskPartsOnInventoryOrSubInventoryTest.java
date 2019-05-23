
package com.mxi.mx.core.query.stask;

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
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Ensures that <code>GetTaskPartsOnInventoryOrSubInventory</code> query works
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskPartsOnInventoryOrSubInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetTaskPartsOnInventoryOrSubInventoryTest.class );
   }


   /**
    * TEST CASE 3: There is one COMPLETE part requirement on open tasks against that inventory or
    * its sub-inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoActvPartRequirementOnInventoryOrSubInventory() throws Exception {
      DataSet lDataSet;

      // Passed in TRK inventory
      lDataSet = execute( new InventoryKey( "4650:5" ) );

      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * TEST CASE 1: There is no part requirement on open tasks against that inventory or its
    * sub-inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoPartRequirementOnInventoryOrSubInventory() throws Exception {
      DataSet lDataSet;

      // Passed in TRK inventory
      lDataSet = execute( new InventoryKey( "4650:1" ) );
      Assert.assertFalse( lDataSet.next() );
   }


   /**
    * TEST CASE 2: There is one ACTV part requirement on open tasks against that inventory or its
    * sub-inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testPartRequirementOnInventoryOrSubInventory() throws Exception {
      DataSet lDataSet;

      // Passed in TRK inventory
      lDataSet = execute( new InventoryKey( "4650:3" ) );
      Assert.assertEquals( 1, lDataSet.getTotalRowCount() );
      lDataSet.next();
      assertRow( lDataSet, new ConfigSlotPositionKey( "5000000:A320:2696:1" ),
            new TaskPartKey( "4650:1:1" ) );
   }


   /**
    * TEST CASE 5:There are ACTV part requirements with different positions on open tasks against
    * that inventory or its sub-inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testPartRequirementsWithDifferentPositionsActvOnInventoryOrSubInventory()
         throws Exception {
      DataSet lDataSet;

      // Passed in TRK inventory
      lDataSet = execute( new InventoryKey( "4650:9" ) );
      Assert.assertEquals( 2, lDataSet.getTotalRowCount() );

      TaskPartKey lFirstTaskPartKey = new TaskPartKey( 4650, 9, 1 );
      QuerySetRowSelector.select( lDataSet, withSchedTaskPartKey( lFirstTaskPartKey ) );
      assertRow( lDataSet, new ConfigSlotPositionKey( "5000000:A320:2696:1" ), lFirstTaskPartKey );

      TaskPartKey lSecondTaskPartKey = new TaskPartKey( 4650, 11, 1 );
      QuerySetRowSelector.select( lDataSet, withSchedTaskPartKey( lSecondTaskPartKey ) );
      assertRow( lDataSet, new ConfigSlotPositionKey( "5000000:A320:2697:1" ), lSecondTaskPartKey );
   }


   /**
    * TEST CASE 4:There are multiple ACTV part requirements on open tasks against that inventory or
    * its sub-inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testPartRequirementsWithSamePositionActvOnInventoryOrSubInventory()
         throws Exception {
      DataSet lDataSet;

      // Passed in TRK inventory
      lDataSet = execute( new InventoryKey( "4650:7" ) );
      Assert.assertEquals( 2, lDataSet.getTotalRowCount() );

      TaskPartKey lFirstTaskPartKey = new TaskPartKey( 4650, 5, 1 );
      QuerySetRowSelector.select( lDataSet, withSchedTaskPartKey( lFirstTaskPartKey ) );
      assertRow( lDataSet, new ConfigSlotPositionKey( "5000000:A320:2696:1" ), lFirstTaskPartKey );

      TaskPartKey lSecondTaskPartKey = new TaskPartKey( 4650, 7, 1 );
      QuerySetRowSelector.select( lDataSet, withSchedTaskPartKey( lSecondTaskPartKey ) );
      assertRow( lDataSet, new ConfigSlotPositionKey( "5000000:A320:2696:1" ), lSecondTaskPartKey );
   }


   private static QuerySetKeyColumnPredicate
         withSchedTaskPartKey( final TaskPartKey aTaskPartKey ) {
      return new QuerySetKeyColumnPredicate( aTaskPartKey, "sched_part_key" );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           The dataset!
    * @param aConfigSlotPositionKey
    *           The config slot position
    * @param aTaskPartKey
    *           The key of scheduled part
    */
   private void assertRow( DataSet aDs, ConfigSlotPositionKey aConfigSlotPositionKey,
         TaskPartKey aTaskPartKey ) {
      MxAssert.assertEquals( "bom_item_position_key", aConfigSlotPositionKey,
            new ConfigSlotPositionKey( aDs.getString( "bom_item_position_key" ) ) );

      MxAssert.assertEquals( "sched_part_key", aTaskPartKey,
            new TaskPartKey( aDs.getString( "sched_part_key" ) ) );
   }


   /**
    * Execute the query
    *
    * @param aInvNoKey
    *           The Inventory Key
    *
    * @return the result
    */

   private DataSet execute( InventoryKey aInvNoKey ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aInvNoKey, new String[] { "aInvNoDbId", "aInvNoId" } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
