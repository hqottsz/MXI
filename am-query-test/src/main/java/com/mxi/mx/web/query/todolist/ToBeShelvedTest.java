
package com.mxi.mx.web.query.todolist;

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
import com.mxi.mx.core.key.HumanResourceKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author amar
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ToBeShelvedTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ToBeShelvedTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Tests the query for a specific user.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testQuery() throws Exception {
      execute( new HumanResourceKey( "4650:100" ) );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:2000", "InvDesc1", "SerialNo1", "4650:3000", "OEM1", "PartDesc1", "EA", "0",
            "5", "4650:104" );
   }


   /**
    * Execute the query.
    *
    * @param aUserHrKey
    *           the user for whom the to-be-shelved data needs to be tested.
    */
   private void execute( HumanResourceKey aUserHrKey ) {

      // Build the arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", aUserHrKey.getDbId() );
      lArgs.add( "aHrId", aUserHrKey.getId() );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInventoryKey
    *           the inventory key.
    * @param aInvDesc
    *           the inv desc.
    * @param aSerialNo
    *           the inv serial no.
    * @param aPartKey
    *           the part key.
    * @param aPartOEM
    *           the part oem.
    * @param aQtyUnitCd
    *           the qty cd.
    * @param aDecimalPlaceQt
    *           the decimal qty.
    * @param aInvQt
    *           the inv qty.
    * @param aLocationKey
    *           the location key.
    * @param aLocationCode
    *           the location code.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInventoryKey, String aInvDesc, String aSerialNo, String aPartKey,
         String aPartOEM, String aQtyUnitCd, String aDecimalPlaceQt, String aInvQt,
         String aLocationKey, String aLocationCode ) throws Exception {
   }
}
