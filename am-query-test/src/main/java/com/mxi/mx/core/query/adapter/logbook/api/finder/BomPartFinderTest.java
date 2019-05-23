
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
 * This class tests the query com.mxi.mx.core.query.adapter.logbook.api.finder.BomPartFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class BomPartFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), BomPartFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where bom part is retrieved by part number (part_no_oem).
    *
    * <ol>
    * <li>Query for bom part by part number (part_no_oem)</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testBomPartFinder() throws Exception {
      execute( "AJ11-07" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4318:2087", "4318:F-2000:354:1" );
   }


   /**
    * Test the case where part number does not exists.
    *
    * <ol>
    * <li>Query for bom part by part number (part_no_oem).</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testPartNumberNotExists() throws Exception {
      execute( "BADPartNO" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * his method executes the query in BomPartFinder.qrx
    *
    * @param aPartNoOem
    *           the part no oem.
    */
   private void execute( String aPartNoOem ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aPartNumber", aPartNoOem );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aBomPartKey
    *           the bom part key.
    * @param aBomPosKey
    *           the bom part position key
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aBomPartKey, String aBomPosKey ) throws Exception {
      MxAssert.assertEquals( aBomPartKey, iDataSet.getString( "bom_part_key" ) );
      MxAssert.assertEquals( aBomPosKey, iDataSet.getString( "bom_item_position_key" ) );
   }
}
