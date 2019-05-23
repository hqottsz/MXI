
package com.mxi.mx.core.query.plsql;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This test class asserts that the function GetPossibleKitsForLocation within the KIT_PKG operates
 * correctly
 *
 * @author cdaley
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPossibleKitsForLocationTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetPossibleKitsForLocationTest.class );
   }


   /**
    * This test asserts that the batch inventory quantity is properly rounded down.<br />
    * <br />
    * There is enough serialized inventory at the location for 3 kits, but only enough batch
    * inventory for 2.6 kits.
    *
    * @throws Exception
    */
   @Test
   public void testBatchInventoryRounding() throws Exception {
      PartNoKey lKitNoKey = new PartNoKey( 1, 1 );
      LocationKey lLocationKey = new LocationKey( 2, 5 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 2, lResult );
   }


   /**
    * This test asserts that if data corruption occurs, and a kit_qt of 0 is set, then 0 possible
    * kits will be returned
    *
    * @throws Exception
    */
   @Test
   public void testDivideByZero() throws Exception {
      PartNoKey lKitNoKey = new PartNoKey( 2, 1 );
      LocationKey lLocationKey = new LocationKey( 2, 6 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 0, lResult );
   }


   /**
    * This test asserts that installed inventory will not be counted as possible kit inventory<br />
    * <br />
    * There is enough inventory located at the location for 2 possible kits, but 1 of the required
    * inventory will be installed (next highest inventory is not null)
    *
    * @throws Exception
    */
   @Test
   public void testInstalledInventory() throws Exception {
      PartNoKey lKitNoKey = new PartNoKey( 1, 1 );
      LocationKey lLocationKey = new LocationKey( 2, 4 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * This test asserts that inventory that is in a kit will not be counted as possible for a kit.
    * <br />
    * There is enough inventory located at the location for 2 possible kits, but 1 of the required
    * inventory is already kitted
    *
    * @throws Exception
    */
   @Test
   public void testInventoryInKit() throws Exception {
      PartNoKey lKitNoKey = new PartNoKey( 1, 1 );
      LocationKey lLocationKey = new LocationKey( 2, 3 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * This test asserts if data is correctly returned if multiple kits are setup at a single
    * location.<br />
    * <br />
    * 3 kits exist:<br />
    * <br />
    * Kit 1: 1 SER PART A<br />
    * Kit 2: 1 SER PART A, 5 BATCH PART B<br />
    * Kit 3: 15 BATCH PART B<br />
    * <br />
    * At the inventory location, 4 PART A, and 15 PART B exist.<br />
    *
    * @throws Exception
    */
   @Test
   public void testMultipleKits() throws Exception {

      PartNoKey lKitNoKey = new PartNoKey( 3, 1 );
      LocationKey lLocationKey = new LocationKey( 2, 7 );

      // ACTION: Execute the function for Kit 1
      int lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 4, lResult );

      lKitNoKey = new PartNoKey( 3, 2 );
      lLocationKey = new LocationKey( 2, 7 );

      // ACTION: Execute the function for Kit 2
      lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 3, lResult );

      lKitNoKey = new PartNoKey( 3, 3 );
      lLocationKey = new LocationKey( 2, 7 );

      // ACTION: Execute the function for Kit 3
      lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * This test asserts that only serviceable inventory at a location are counted for a possible
    * kit. <br />
    * <br />
    * The inventory supply location (2:2) contains enough inventory for 2 possible kits, but one of
    * the serialized inventory has an inventory condition of INSPREQ.
    *
    * @throws Exception
    */
   @Test
   public void testServiceable() throws Exception {
      PartNoKey lKitNoKey = new PartNoKey( 1, 1 );
      LocationKey lLocationKey = new LocationKey( 2, 2 );

      // ACTION: Execute the function
      int lResult = execute( lKitNoKey, lLocationKey );

      MxAssert.assertEquals( 1, lResult );
   }


   /**
    * Execute the function.
    *
    * @param aPartNoKey
    *           The root of a Task Tree.
    * @param aLocationKey
    *           The Aircraft to which these tasks belong
    *
    * @return the calculated Planning Yield Date.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private int execute( PartNoKey aPartNoKey, LocationKey aLocationKey ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartNoKey, new String[] { "aKitDbId", "aKitId" } );
      lArgs.add( aLocationKey, new String[] { "aLocDbId", "aLocId" } );

      String[] lParmOrder = { "aKitDbId", "aKitId", "aLocDbId", "aLocId" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  "KIT_PKG.GETPOSSIBLEKITSFORLOCATION", lParmOrder, lArgs ) );
   }
}
