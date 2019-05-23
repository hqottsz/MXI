
/**
 *
 */
package com.mxi.mx.core.query.part;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Tests the inventory validation part of the IsPartNoBomPartInUse query.
 *
 * @author slevert
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsPartNoBomPartInUseTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), IsPartNoBomPartInUseTest.class );
   }


   private static final PartNoKey PART_NO_KEY = new PartNoKey( 4650, 0 );
   private static final PartGroupKey BOM_PART_KEY_1 = new PartGroupKey( 4650, 0 );
   private static final PartGroupKey BOM_PART_KEY_2 = new PartGroupKey( 4650, 1 );
   private static final PartNoKey PART_NO_KEY_WITH_CUSTOM_CONDITION = new PartNoKey( 4650, 1 );

   private DataSet iDataSet;


   /**
    * Tests that if a remaining inventory on a config slot has a custom condition, a record gets
    * returned.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testCustomConditionPartStillAssigned() throws Exception {
      executeQuery( PART_NO_KEY_WITH_CUSTOM_CONDITION, BOM_PART_KEY_1 );

      assertTrue( iDataSet.next() );
   }


   /**
    * Tests that if the remaining inventory on a config slot is not scrapped, the query will return
    * rows.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testNoScrappedPartsAssigned() throws Exception {
      executeQuery( PART_NO_KEY, BOM_PART_KEY_2 );

      assertTrue( iDataSet.next() );
   }


   /**
    * Tests that if a remaining inventory on a config slot is scrapped, the query will return no
    * rows.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testScrappedPartStillAssigned() throws Exception {
      executeQuery( PART_NO_KEY, BOM_PART_KEY_1 );

      assertFalse( iDataSet.next() );
   }


   /**
    * Executes the query with the given inventory
    *
    * @param aPartNo
    *           {@link PartNoKey}
    * @param aBomPart
    *           {@link PartGroupKey}
    */
   private void executeQuery( PartNoKey aPartNo, PartGroupKey aBomPart ) {

      // Build query arguments
      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aBomPartDbId", aBomPart.getDbId() );
      lArgs.add( "aBomPartId", aBomPart.getId() );
      lArgs.add( "aPartNoDbId", aPartNo.getDbId() );
      lArgs.add( "aPartNoId", aPartNo.getId() );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.core.query.part.isPartNoBomPartInUse", lArgs );
   }
}
