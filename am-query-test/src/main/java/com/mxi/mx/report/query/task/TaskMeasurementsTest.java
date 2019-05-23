
package com.mxi.mx.report.query.task;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.TaskKey;


/**
 * This class tests the query TaskMeasurements.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskMeasurementsTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), TaskMeasurementsTest.class );
   }


   /**
    * Tests the retrieval of the Task Measurements.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTaskMeasurements() throws Exception {
      QuerySet lQuerySet = this.execute( new TaskKey( 4650, 139551 ) );

      Assert.assertEquals( "Number of retrieved rows", 5, lQuerySet.getRowCount() );

      // ME
      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "BEL (BEL)", "Airbus A319/A320 - 1806PLANE", "3.00 EA" );

      // TEXT
      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "LBS (Free Form Text)", "Airbus A319/A320 - 1806PLANE",
            "Test Text Measurement" );

      // CHK
      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "QTS (Check Box)", "Airbus A319/A320 - 1806PLANE", "1" );

      // CME
      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "calCode ()", "Airbus A319/A320 - 1806PLANE", "05-JAN-11 " );

      // CH
      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "testCH ()", "Airbus A319/A320 - 1806PLANE", "BUSY " );

      Assert.assertFalse( lQuerySet.next() );
   }


   /**
    * This method executes the query in TaskMeasurements.qrx
    *
    * @param aTaskKey
    *           The task key.
    *
    * @return The QuerySet after execution.
    */
   private QuerySet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aTaskKey, "aTaskDbId", "aTaskId" );

      return QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }


   /**
    * Assert the Row Coloumns.
    *
    * @param aQuerySet
    *           the QuerySet
    * @param aMeasurementParameter
    *           the measurement parameter
    * @param aInvNoSdesc
    *           the inventory sdesc
    * @param aMeasurementsValue
    *           the measurement value
    */
   private void testRow( QuerySet aQuerySet, String aMeasurementParameter, String aInvNoSdesc,
         String aMeasurementsValue ) {
      Assert.assertEquals( "measurement_parameter", aMeasurementParameter,
            aQuerySet.getString( "measurement_parameter" ) );
      Assert.assertEquals( "inv_no_sdesc", aInvNoSdesc, aQuerySet.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "measurement_value", aMeasurementsValue,
            aQuerySet.getString( "measurement_value" ) );
   }
}
