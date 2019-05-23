
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
 * com.mxi.mx.core.query.adapter.logbook.api.finder.BomPartFinderByInventory.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class BomPartFinderByInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            BomPartFinderByInventoryTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where bom part is retrieved in inventory by part number (part_no_oem) and serial
    * number.
    *
    * <ol>
    * <li>Query for bom part is retrieved in inventory by part number (part_no_oem) and serial
    * number)</li>
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
      execute( "822-0374-134", "037414" );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4318:1834", "4318:F-2000:243:1" );
   }


   /**
    * Test the case where part number does not exists.
    *
    * <ol>
    * <li>Query for bom part is retrieved in inventory by part number (part_no_oem) and serial
    * number.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testPartNumberNotExists() throws Exception {
      execute( "BadPartNo", "037414" );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * his method executes the query in BomPartFinderByInventory.qrx
    *
    * @param aPartNumber
    *           the part number.
    * @param aSerialNumber
    *           the serial number.
    */
   private void execute( String aPartNumber, String aSerialNumber ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aPartNumber", aPartNumber );
      lArgs.add( "aSerialNumber", aSerialNumber );

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
