
package com.mxi.mx.web.query.task;

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
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the com.mxi.mx.web.query.task.AddMeasurements.qrx query.
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AddMeasurementsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AddMeasurementsTest.class );
   }


   private static final TaskKey TASK = new TaskKey( 4650, 1 );
   private static final DataTypeKey EXPECTED_DATA_TYPE = new DataTypeKey( 4650, 3013 );
   private static final InventoryKey EXPECTED_INV_IN_POSITION1 = new InventoryKey( 4650, 3 );
   private static final InventoryKey EXPECTED_INV_IN_POSITION2 = new InventoryKey( 4650, 4 );


   /**
    * Test Case 1: Test that the query returns same assembly measurement with inventories
    * corresponding to multiple positions under a config slot the assembly measurement is applicable
    * to.
    */
   @Test
   public void testAssmblyMeasurementWithInventories() {
      DataSet lDataSet = execute( TASK );

      // Assert number of rows returned
      assertEquals( 2, lDataSet.getRowCount() );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_DATA_TYPE, EXPECTED_INV_IN_POSITION1 );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_DATA_TYPE, EXPECTED_INV_IN_POSITION2 );
   }


   /**
    * Test row contents returned
    *
    * @param aDataSet
    *           the dataset
    * @param aExpectedDataType
    *           the expected data type
    * @param aExpectedInventory
    *           the expected inventory
    */
   private void assertRow( DataSet aDataSet, DataTypeKey aExpectedDataType,
         InventoryKey aExpectedInventory ) {

      assertEquals( "data_type_db_id", aExpectedDataType.getDbId(),
            aDataSet.getKey( DataTypeKey.class, "data_type_key" ).getDbId() );
      assertEquals( "data_type_id", aExpectedDataType.getId(),
            aDataSet.getKey( DataTypeKey.class, "data_type_key" ).getId() );
      assertEquals( "inv_no_db_id", aExpectedInventory.getDbId(),
            aDataSet.getKey( DataTypeKey.class, "inventory_key" ).getDbId() );
      assertEquals( "inv_no_id", aExpectedInventory.getId(),
            aDataSet.getKey( DataTypeKey.class, "inventory_key" ).getId() );
   }


   /**
    * Executes the query.
    *
    * @param aKey
    *           the task key.
    *
    * @return {@link DataSet}
    */
   private DataSet execute( TaskKey aKey ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "aEventDbId", "aEventId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
