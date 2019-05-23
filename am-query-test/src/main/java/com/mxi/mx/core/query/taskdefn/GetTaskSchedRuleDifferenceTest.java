
package com.mxi.mx.core.query.taskdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

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
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Test GetTaskSchedRuleDifference.qrx returns the appropriate data
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskSchedRuleDifferenceTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetTaskSchedRuleDifferenceTest.class );
   }


   private DataTypeKey iMimDataType_1 = new DataTypeKey( 0, 1 );

   private DataTypeKey iMimDataType_2 = new DataTypeKey( 0, 10 );

   private TaskTaskKey iTaskKey_Added = new TaskTaskKey( 4650, 2 );

   private TaskTaskKey iTaskKey_Base = new TaskTaskKey( 4650, 1 );

   private TaskTaskKey iTaskKey_Modified = new TaskTaskKey( 4650, 3 );

   private TaskTaskKey iTaskKey_NoChange = new TaskTaskKey( 4650, 4 );


   /**
    * Ensure different if rule was added
    */
   @Test
   public void testAddedRule() {
      Set<DataTypeKey> lDataTypes = getDifference( iTaskKey_Base, iTaskKey_Added );

      assertEquals( 1, lDataTypes.size() );
      assertTrue( lDataTypes.contains( iMimDataType_2 ) );
   }


   /**
    * Ensure different if a row was modified
    */
   @Test
   public void testModifiedRule() {
      Set<DataTypeKey> lDataTypes = getDifference( iTaskKey_Base, iTaskKey_Modified );

      assertEquals( 1, lDataTypes.size() );
      assertTrue( lDataTypes.contains( iMimDataType_1 ) );
   }


   /**
    * Ensure not different if the values remained the same
    */
   @Test
   public void testNoChange() {
      Set<DataTypeKey> lDataTypes = getDifference( iTaskKey_Base, iTaskKey_NoChange );

      assertEquals( 0, lDataTypes.size() );
   }


   /**
    * Ensure different if rule was removed
    */
   @Test
   public void testRemovedRule() {
      Set<DataTypeKey> lDataTypes = getDifference( iTaskKey_Added, iTaskKey_Base );

      assertEquals( 1, lDataTypes.size() );
      assertTrue( lDataTypes.contains( iMimDataType_2 ) );
   }


   /**
    * Returns TRUE if there is a difference in the scheduling rules
    *
    * @param aTaskKey
    *           the task
    * @param aOldTaskKey
    *           the old task
    *
    * @return TRUE when there is a difference; FALSE otherwise
    */
   protected Set<DataTypeKey> getDifference( TaskTaskKey aTaskKey, TaskTaskKey aOldTaskKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskKey, "aTaskDbId", "aTaskId" );
      lArgs.add( aOldTaskKey, "aOldTaskDbId", "aOldTaskId" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      Set<DataTypeKey> lChangedDataTypes = new HashSet<DataTypeKey>();
      while ( lDataSet.next() ) {
         lChangedDataTypes
               .add( lDataSet.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
      }

      return lChangedDataTypes;
   }
}
