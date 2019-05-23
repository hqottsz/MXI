package com.mxi.mx.core.query.ppc.task;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

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
import com.mxi.mx.core.key.TaskKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetOnOrderPartRequestsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetOnOrderPartRequestsTest.class );
   }


   private static final TaskKey WORK_PACKAGE = new TaskKey( 4650, 100 );

   private static final TaskKey TASK_WITH_ON_ORDER_PART_REQ = new TaskKey( 4650, 1 );


   /**
    * Tests that the query returns tasks that have part requests with an Estimated Arrival Time
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( WORK_PACKAGE, "aWpDbId", "aWpId" );

      DataSet lQs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Asserts only the part request with an ETA is returned
      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      Date lEstimatedArrivalDt =
            new GregorianCalendar( 2016, GregorianCalendar.JULY, 7, 9, 0, 0 ).getTime();

      assertEquals( TASK_WITH_ON_ORDER_PART_REQ,
            lQs.getKey( TaskKey.class, "sched_db_id", "sched_id" ) );
      assertEquals( lEstimatedArrivalDt, lQs.getDate( "est_arrival_dt" ) );
      assertEquals( "PART ABC", lQs.getString( "part_no_oem" ) );
      assertEquals( "Part ABC Name", lQs.getString( "part_no_sdesc" ) );
   }

}
