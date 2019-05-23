
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Tests the GetTaskApplicability PL/SQL function
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskApplicabilityTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetTaskApplicabilityTest.class );
   }


   private static final InventoryKey ON_WING_ENGINE_777 = new InventoryKey( 1, 3 );

   private static final InventoryKey ON_WING_ENGINE_787 = new InventoryKey( 1, 4 );

   private static final InventoryKey OFF_WING_ENGINE = new InventoryKey( 1, 5 );


   /**
    * Test [Aircraft Part No] = '777' applicability rule
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAircraftPartNoApplicable777() throws Exception {

      String lTaskApplSqlLdesc = "rootpart.part_no_oem = 777";

      // verify on wing engine 777 is applicable
      int lResult = execute( ON_WING_ENGINE_777, lTaskApplSqlLdesc );
      assertEquals( 1, lResult );

      // verify on wing engine 787 is not applicable
      lResult = execute( ON_WING_ENGINE_787, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );

      // verify off wing engine is not applicable
      lResult = execute( OFF_WING_ENGINE, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );
   }


   /**
    * Test [Aircraft Part No] = '787' applicability rule
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAircraftPartNoApplicable787() throws Exception {

      String lTaskApplSqlLdesc = "rootpart.part_no_oem = 787";

      // verify on wing engine 777 is not applicable
      int lResult = execute( ON_WING_ENGINE_777, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );

      // verify on wing engine 787 is applicable
      lResult = execute( ON_WING_ENGINE_787, lTaskApplSqlLdesc );
      assertEquals( 1, lResult );

      // verify off wing engine is not applicable
      lResult = execute( OFF_WING_ENGINE, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );
   }


   /**
    * Test [Aircraft Part No] <> '777' applicability rule
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAircraftPartNoNotApplicable777() throws Exception {

      String lTaskApplSqlLdesc = "rootpart.part_no_oem <> 777";

      // verify on wing engine 777 is not applicable
      int lResult = execute( ON_WING_ENGINE_777, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );

      // verify on wing engine 787 is applicable
      lResult = execute( ON_WING_ENGINE_787, lTaskApplSqlLdesc );
      assertEquals( 1, lResult );

      // verify off wing engine is applicable
      lResult = execute( OFF_WING_ENGINE, lTaskApplSqlLdesc );
      assertEquals( 1, lResult );
   }


   /**
    * Test [Aircraft Part No] <> '787' applicability rule
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAircraftPartNoNotApplicable787() throws Exception {

      String lTaskApplSqlLdesc = "rootpart.part_no_oem <> 787";

      // verify on wing engine 777 is applicable
      int lResult = execute( ON_WING_ENGINE_777, lTaskApplSqlLdesc );
      assertEquals( 1, lResult );

      // verify on wing engine 787 is not applicable
      lResult = execute( ON_WING_ENGINE_787, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );

      // verify off wing engine is applicable
      lResult = execute( OFF_WING_ENGINE, lTaskApplSqlLdesc );
      assertEquals( 1, lResult );
   }


   /**
    * Test [Aircraft Part No] <> '787' AND [Aircraft Part No] <> '777' applicability rule
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAircraftPartNoOnlyApplicableOffWing() throws Exception {

      String lTaskApplSqlLdesc = "rootpart.part_no_oem <> 787 AND rootpart.part_no_oem <> 777";

      // verify on wing engine 777 is not applicable
      int lResult = execute( ON_WING_ENGINE_777, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );

      // verify on wing engine 787 is not applicable
      lResult = execute( ON_WING_ENGINE_787, lTaskApplSqlLdesc );
      assertEquals( 0, lResult );

      // verify off wing engine is applicable
      lResult = execute( OFF_WING_ENGINE, lTaskApplSqlLdesc );
      assertEquals( 1, lResult );
   }


   /**
    * Execute the funtion
    *
    * @param aInventory
    *           the inventory key
    * @param aTaskApplSqlLdesc
    *           the applicability filter clause
    *
    * @return true if the inventory is applicable
    *
    * @throws Exception
    *            if an error occurs
    */
   private int execute( InventoryKey aInventory, String aTaskApplSqlLdesc ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );
      lArgs.add( "aTaskApplSqlLdesc", aTaskApplSqlLdesc );

      String[] lParmOrder = { "aInvNoDbId", "aInvNoId", "aTaskApplSqlLdesc" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getFunctionName( getClass() ), lParmOrder, lArgs ) );
   }
}
