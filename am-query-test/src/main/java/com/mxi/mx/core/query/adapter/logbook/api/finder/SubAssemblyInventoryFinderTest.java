
package com.mxi.mx.core.query.adapter.logbook.api.finder;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query
 * com.mxi.mx.core.query.adapter.logbook.api.finder.SubAssemblyInventoryFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SubAssemblyInventoryFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            SubAssemblyInventoryFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where assembly inventory does not exist by invalid position code.
    *
    * <ol>
    * <li>Query for Query for assembly inventory by assembly class code and position code, and
    * parent (aircaft) inventory.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAssemblyPositionNotExists() throws Exception {

      // invalid position code
      execute( "ENG", "1", 4650, 98784 );

      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case where assembly inventory is retrieved by assembly class code (APU or ENG) and
    * position code, and parent (aircaft) inventory.
    *
    * <ol>
    * <li>Query for assembly inventory by assembly class code and position code, and parent
    * (aircaft) inventory.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testSubAssemblyInventoryFinder() throws Exception {
      execute( "ENG", "1.LT", 4650, 98784 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:206517", "CFM56" );
   }


   /**
    * his method executes the query in SubAssemblyInventoryFinder.qrx
    *
    * @param aAssmblClassCd
    *           the assembly class code.
    * @param aAssmblPosCd
    *           the assembly position code.
    * @param aHInvNoDbId
    *           the highest inv no db id.
    * @param aHInvNoId
    *           the highest inv no id.
    */
   private void execute( String aAssmblClassCd, String aAssmblPosCd, int aHInvNoDbId,
         int aHInvNoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aAssmblClassCd", aAssmblClassCd );
      lArgs.add( "aAssmblPosCd", aAssmblPosCd );

      lArgs.add( "aHInvNoDbId", aHInvNoDbId );
      lArgs.add( "aHInvNoId", aHInvNoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInventoryKey
    *           the inventory key.
    * @param aAssemblCd
    *           the assembly code.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInventoryKey, String aAssemblCd ) throws Exception {
      MxAssert.assertEquals( aInventoryKey, iDataSet.getString( "inv_pk" ) );
      MxAssert.assertEquals( aAssemblCd, iDataSet.getString( "orig_assmbl_cd" ) );
   }
}
