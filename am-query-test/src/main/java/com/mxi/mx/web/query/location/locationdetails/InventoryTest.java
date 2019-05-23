
package com.mxi.mx.web.query.location.locationdetails;

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
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Ensures that <code>Inventory</code> query works
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class InventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), InventoryTest.class );
   }


   private static final LocationKey LOC_INSIDE_DEPT_OF_HR = new LocationKey( 4650, 1 );
   private static final LocationKey LOC_OUTSIDE_DEPT_OF_HR = new LocationKey( 4650, 2 );
   private static final LocationKey LOC_PARENT_LOC_INSIDE_DEPT_OF_HR = new LocationKey( 4650, 3 );
   private static final HumanResourceKey HR_HAVING_DEPT_WITH_THE_LOC =
         new HumanResourceKey( 4650, 1 );
   private static final HumanResourceKey HR_HAVING_DEPT_WITHOUT_THE_LOC =
         new HumanResourceKey( 4650, 2 );
   private static final boolean NOT_SHOW_INVENTORY_IN_SUBLOCATIONS = false;
   private static final InventoryKey EXPECTED_INV_ASSIGNED_LOC = new InventoryKey( 4650, 1 );
   private static final InventoryKey EXPECTED_INV_PARENT_ASSIGNED_LOC = new InventoryKey( 4650, 3 );


   /**
    * TEST CASE 1: There is an inventory shown at the location which is one of the locations current
    * login user's departments have.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInventoryShownAtLocationInsideDepartmentsOfUser() throws Exception {

      // Passed in location, human resource, true/false
      DataSet lDataSet = execute( LOC_INSIDE_DEPT_OF_HR, HR_HAVING_DEPT_WITH_THE_LOC,
            NOT_SHOW_INVENTORY_IN_SUBLOCATIONS );

      MxAssert.assertEquals( 1, lDataSet.getRowCount() );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_INV_ASSIGNED_LOC );
   }


   /**
    * TEST CASE 3: There is an inventory shown at the location which parent location assigned to the
    * login user's departments
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testInventoryShownAtLocationParentLocInsideDepartmentsOfUser() throws Exception {

      // Passed in location, human resource, true/false
      DataSet lDataSet = execute( LOC_PARENT_LOC_INSIDE_DEPT_OF_HR, HR_HAVING_DEPT_WITH_THE_LOC,
            NOT_SHOW_INVENTORY_IN_SUBLOCATIONS );

      MxAssert.assertEquals( 1, lDataSet.getRowCount() );

      lDataSet.next();

      assertRow( lDataSet, EXPECTED_INV_PARENT_ASSIGNED_LOC );
   }


   /**
    * TEST CASE 2: There is no inventory shown at the location which is not one of the locations
    * current login user's departments have.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoInventoryShownAtLocationNotInsideDepartmentsOfUser() throws Exception {

      // Passed in location, human resource, true/false
      DataSet lDataSet = execute( LOC_OUTSIDE_DEPT_OF_HR, HR_HAVING_DEPT_WITHOUT_THE_LOC,
            NOT_SHOW_INVENTORY_IN_SUBLOCATIONS );

      MxAssert.assertEquals( 0, lDataSet.getRowCount() );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset
    * @param aInventory
    *           the inventory key
    */
   private void assertRow( DataSet aDs, InventoryKey aInventory ) {

      MxAssert.assertEquals( "inventory_key", aInventory,
            aDs.getKey( InventoryKey.class, "inv_no_key" ) );
   }


   /**
    * Execute the query
    *
    * @param aLocationKey
    *           the inventory key
    * @param aHrKey
    *           the hr key
    * @param aShowInventoryInSublocations
    *           the value true/false to determine whether or not inventories in sublocations are
    *           shown
    *
    * @return the result
    */

   private DataSet execute( LocationKey aLocationKey, HumanResourceKey aHrKey,
         boolean aShowInventoryInSublocations ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aLocationKey, "aLocDbId", "aLocId" );
      lArgs.add( aHrKey, "aHrDbId", "aHrId" );
      lArgs.add( "aShowInventoryInSublocations", aShowInventoryInSublocations );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
