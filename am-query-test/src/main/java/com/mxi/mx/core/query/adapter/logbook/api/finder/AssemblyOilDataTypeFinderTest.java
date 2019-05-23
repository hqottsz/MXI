
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
 * com.mxi.mx.core.query.adapter.logbook.api.finder.AssemblyOildataTypeFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblyOilDataTypeFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            AssemblyOilDataTypeFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where Assembly Oil Data Type is retrieved by inventory key.
    *
    * <ol>
    * <li>Query for Assembly Oil Data Type by inventory key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testAssemblyOilDataTypeFinder() throws Exception {
      execute( 4650, 224509 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:3014" );
   }


   /**
    * Test the case where Assembly Oil Data Type does not exists by invalid inventory.
    *
    * <ol>
    * <li>Query for Assembly Oil Data Type by inventory key.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testInventoryNotExists() throws Exception {
      execute( 4650, 224510 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * his method executes the query in AssemblyOildataTypeFinder.qrx
    *
    * @param aInvNoDbId
    *           the inventory no db id.
    * @param aInvNoId
    *           the inventory no id.
    */
   private void execute( int aInvNoDbId, int aInvNoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aInvNoDbId", aInvNoDbId );
      lArgs.add( "aInvNoId", aInvNoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aDataTypePk
    *           the Data Type Key.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aDataTypePk ) throws Exception {
      MxAssert.assertEquals( aDataTypePk, iDataSet.getString( "data_type_pk" ) );
   }
}
