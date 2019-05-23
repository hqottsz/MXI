
package com.mxi.mx.core.job.assembly;

import static com.mxi.mx.core.job.assembly.OilConsumptionCoreService.DEFAULT_EXTENSION_IN_DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;


/**
 * Tests the {@link OilConsumptionCoreService} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OilConsumptionCoreServiceTest {

   // Refer to 0dim_time.sql for the following constants.
   private static final int LAST_DIM_TIME_ID = 20131231;
   private static final int ORIGINAL_NUM_OF_DIM_TIME_ROWS = 1826;

   private static final DateFormat DIM_TIME_ID_FORMAT = new SimpleDateFormat( "yyyyMMdd" );
   private static final DataSetArgument EMPTY_ARGS = new DataSetArgument();
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Verify that populateOilConsumptionDataWarehouse() appends to the dim_time table.
    *
    * @throws Exception
    */
   @Test
   public void testUpdateTimeDimensionTableWhenAppendingToDimTime() throws Exception {

      // Ensure the dim_time table is initialized as expected.
      assertInitialDimTimeTable();

      // Test with an end date that is 10 days after the last date in dim_time.
      Date lEndDate = DIM_TIME_ID_FORMAT.parse( Integer.toString( LAST_DIM_TIME_ID ) );
      lEndDate = DateUtils.addDays( lEndDate, 10 );

      // Execute test.
      new OilConsumptionCoreService().updateTimeDimensionTable( lEndDate );

      // Verify that the correct number of rows were appended to dim_time.
      int lExpectedNumOfRows = ORIGINAL_NUM_OF_DIM_TIME_ROWS + DEFAULT_EXTENSION_IN_DAYS + 10;

      assertEquals( lExpectedNumOfRows, getDimTimeRowCount() );
   }


   /**
    * Verify that populateOilConsumptionDataWarehouse() updates the dim_time table when that table
    * is empty.
    *
    * @throws Exception
    */
   @Test
   public void testUpdateTimeDimensionTableWhenDimTimeEmpty() throws Exception {

      // Empty the dim_time table.
      MxDataAccess.getInstance().executeDelete( "dim_time", EMPTY_ARGS );
      assertEquals( 0, getDimTimeRowCount() );

      // Since the default start date for the dim_time table is Jan 1, 2009 00:00:00 we will test
      // with a date that is 10 days after (thus, ending on Jan 10, 2009 00:00:00).
      //
      // (refer to OIL_CONSUMPTION_SERVER_JOB_PKG.populateDimTimeTable and 0dim_time.sql)
      Calendar lCal = Calendar.getInstance();
      lCal.set( 2009, 00, 10, 00, 00, 00 );

      Date lEndDate = lCal.getTime();

      // Execute test.
      new OilConsumptionCoreService().updateTimeDimensionTable( lEndDate );

      // Verify that the correct number of rows were added to dim_time.
      int lExpectedNumOfRows = OilConsumptionCoreService.DEFAULT_EXTENSION_IN_DAYS + 10;
      assertEquals( lExpectedNumOfRows, getDimTimeRowCount() );

      // Additionally, verify that the last expected date was added.
      Date lLastDate =
            DateUtils.addDays( lEndDate, OilConsumptionCoreService.DEFAULT_EXTENSION_IN_DAYS );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "dim_time_id", Integer.parseInt( DIM_TIME_ID_FORMAT.format( lLastDate ) ) );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "dim_time", lArgs );
      assertEquals( 1, lQs.getRowCount() );
   }


   /**
    * Verify that populateOilConsumptionDataWarehouse() does nothing to the dim_time table when the
    * end date already exists.
    *
    * @throws Exception
    */
   @Test
   public void testUpdateTimeDimensionTableWhenEndDateInDimTime() throws Exception {

      // Ensure the dim_time table is initialized as expected.
      assertInitialDimTimeTable();

      // Test with an end date that exists in the dim_time table.
      Date lEndDate = DIM_TIME_ID_FORMAT.parse( Integer.toString( LAST_DIM_TIME_ID ) );

      // Execute test.
      new OilConsumptionCoreService().updateTimeDimensionTable( lEndDate );

      // Verify that NO days worth of data were created in dim_time.
      assertEquals( ORIGINAL_NUM_OF_DIM_TIME_ROWS, getDimTimeRowCount() );
   }


   /**
    * Ensures the dim_time table is initialized as expected. Refer to 0dim_time.sql for the values
    * used.
    */
   private void assertInitialDimTimeTable() {

      // Ensure the last entry in the dim_time table is as expected.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "dim_time_id", LAST_DIM_TIME_ID );
      assertFalse( QuerySetFactory.getInstance().executeQueryTable( "dim_time", lArgs ).isEmpty() );

      // Ensure the number of entries in the dim_time table is as expected.
      assertEquals( ORIGINAL_NUM_OF_DIM_TIME_ROWS, getDimTimeRowCount() );
   }


   /**
    * Convenience method to get the current row count of dim_time.
    *
    * @return number of rows in dim_time
    */
   private int getDimTimeRowCount() {
      return QuerySetFactory.getInstance().executeQueryTable( "dim_time", EMPTY_ARGS )
            .getRowCount();
   }

}
