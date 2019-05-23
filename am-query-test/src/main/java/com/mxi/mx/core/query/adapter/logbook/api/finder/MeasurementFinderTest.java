
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
 * This class tests the query com.mxi.mx.core.query.adapter.logbook.api.finder.MeasurementFinder.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class MeasurementFinderTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), MeasurementFinderTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case to get inventory measurement parameter key by event key, inventory key and data
    * type key.
    *
    * <ol>
    * <li>Query for inventory measurement parameter key by event key, inventory key and data type
    * key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testMeasurementFinder() throws Exception {
      execute( 4650, 134116, 4650, 3013, 4650, 392792 );
      assertEquals( 1, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:134116:1:4650:3013:4650:392792", "4650:3013", "0:ME",
            "ENGOIL (Engine Oil Uptake)" );
   }


   /**
    * Test the case where measurement does not exist by invalid data type.
    *
    * <ol>
    * <li>Query for inventory measurement parameter key by event key, inventory key and data type
    * key.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testMeasurementNotExists() throws Exception {
      execute( 4650, 134116, 4650, 3015, 4650, 392792 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * This method executes the query in MeasurementFinder.qrx
    *
    * @param aEventDbId
    *           the event db id.
    * @param aEventId
    *           the event id.
    * @param aDataTypeDbId
    *           the data type db id.
    * @param aDataTypeId
    *           the data type id.
    * @param aInvNoDbId
    *           the inventory no db id.
    * @param aInvNoId
    *           the inventory no id.
    */
   private void execute( int aEventDbId, int aEventId, int aDataTypeDbId, int aDataTypeId,
         int aInvNoDbId, int aInvNoId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aEventDbId", aEventDbId );
      lArgs.add( "aEventId", aEventId );
      lArgs.add( "aDataTypeDbId", aDataTypeDbId );
      lArgs.add( "aDataTypeId", aDataTypeId );
      lArgs.add( "aInvNoDbId", aInvNoDbId );
      lArgs.add( "aInvNoId", aInvNoId );

      // Execute the query
      iDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Method for testing a specific row returned by dataset.
    *
    * @param aInvParmDataKey
    *           the inventory parm key.
    * @param aMeasurementKey
    *           the measurement key.
    * @param aDomainTypeKey
    *           the domain type key.
    * @param aMeasurementParm
    *           the measurement parm.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInvParmDataKey, String aMeasurementKey, String aDomainTypeKey,
         String aMeasurementParm ) throws Exception {
      MxAssert.assertEquals( aInvParmDataKey, iDataSet.getString( "inv_parm_data_key" ) );
      MxAssert.assertEquals( aMeasurementKey, iDataSet.getString( "measurement_key" ) );
      MxAssert.assertEquals( aDomainTypeKey, iDataSet.getString( "domain_type_key" ) );
      MxAssert.assertEquals( aMeasurementParm, iDataSet.getString( "measurement_parameter" ) );
   }
}
