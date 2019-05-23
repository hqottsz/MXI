
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
 * This class tests the query com.mxi.mx.core.query.adapter.logbook.api.finder.Measurements.qrx
 *
 * @author hliu
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class MeasurementsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), MeasurementsTest.class );
   }


   /** The query execution data set */
   private DataSet iDataSet = null;


   /**
    * Test the case where measurements do not exist by invalid event.
    *
    * <ol>
    * <li>Query for inventory measurement parameter attributes by event key.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testEventNotExists() throws Exception {
      execute( 4650, 134117 );
      assertEquals( 0, iDataSet.getRowCount() );
   }


   /**
    * Test the case to retrieve attributes of measurement parameters for the inventory of the event
    * by event key.
    *
    * <ol>
    * <li>Query for inventory measurement parameter attributes by event key.</li>
    * <li>Verify that the results are as expected.</li>
    * <li>Check for the values of row.</li>
    * <li>Verify that the results are as expected.</li>
    * </ol>
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testMeasurements() throws Exception {
      execute( 4650, 134116 );
      assertEquals( 2, iDataSet.getRowCount() );

      iDataSet.next();
      testRow( "4650:134116:1:4650:3014:4650:393348", "4650:3014", "0:ME",
            "APUOIL (APU Oil Uptake)", 2.0, 2.0, "LITRES", 1 );

      iDataSet.next();
      testRow( "4650:134116:1:4650:3013:4650:392792", "4650:3013", "0:ME",
            "ENGOIL (Engine Oil Uptake)", 5.0, 5.0, "LITRES", 2 );
   }


   /**
    * This method executes the query in Measurements.qrx
    *
    * @param aEventDbId
    *           the event db id
    * @param aEventId
    *           the event id.
    */
   private void execute( int aEventDbId, int aEventId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aEventDbId", aEventDbId );
      lArgs.add( "aEventId", aEventId );

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
    *           the measurement parameter.
    * @param aParmQt
    *           the parm qt.
    * @param aRecParmQt
    *           the record parm qt.
    * @param aEngUnitCd
    *           the eng unit cd.
    * @param aDataOrd
    *           the data ord.
    *
    * @throws Exception
    *            if an error occurs.
    */
   private void testRow( String aInvParmDataKey, String aMeasurementKey, String aDomainTypeKey,
         String aMeasurementParm, double aParmQt, double aRecParmQt, String aEngUnitCd,
         int aDataOrd ) throws Exception {
      MxAssert.assertEquals( aInvParmDataKey, iDataSet.getString( "inv_parm_data_key" ) );
      MxAssert.assertEquals( aMeasurementKey, iDataSet.getString( "measurement_key" ) );
      MxAssert.assertEquals( aDomainTypeKey, iDataSet.getString( "domain_type_key" ) );
      MxAssert.assertEquals( aMeasurementParm, iDataSet.getString( "measurement_parameter" ) );
      MxAssert.assertEquals( aParmQt, iDataSet.getDouble( "parm_qt" ) );
      MxAssert.assertEquals( aRecParmQt, iDataSet.getDouble( "rec_parm_qt" ) );
      MxAssert.assertEquals( aEngUnitCd, iDataSet.getString( "rec_eng_unit_cd" ) );
      MxAssert.assertEquals( aDataOrd, iDataSet.getInt( "data_ord" ) );
   }
}
