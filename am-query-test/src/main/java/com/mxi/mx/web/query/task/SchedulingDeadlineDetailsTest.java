package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;

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
 * The class tests the com.mxi.mx.web.query.task.SchedulingDeadlineDetails query.
 *
 * @author hzheng
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SchedulingDeadlineDetailsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            SchedulingDeadlineDetailsTest.class );
   }


   private static final TaskKey TASK_KEY = new TaskKey( 4650, 10 );
   private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss a" );


   @Test
   public void testQuery() throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( TASK_KEY, "aTaskDbId", "aTaskId" );

      DataSet lResults =
            QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 2, lResults.getRowCount() );
      boolean lCADeadlineValidated = false;
      boolean lUSDeadlineValidated = false;

      while(lResults.next()){
    	  // Oracle may return the rows in any order it likes.
	      if (lResults.getString("domain_type_cd").equals("CA")){

		      // calendar parameter
		      assertEquals( "CDY", lResults.getString( "data_type_cd" ) );
		      assertEquals( "CA", lResults.getString( "domain_type_cd" ) );

		      // due date
		      assertEquals( "2015-08-09 11:59:59 AM", SDF.format( lResults.getDate( "sched_dead_dt" ) ) );

		      // usage remaining for forecast
		      assertEquals( -70.0, lResults.getDouble( "usage_remaining_plus_deviation" ), 0f );

		      // estimated date
		      assertEquals( "2015-08-19 11:59:59 AM",
		            SDF.format( lResults.getDate( "sched_dead_plus_deviation" ) ) );

		      lCADeadlineValidated = true;
	      }else{

		      // usage parameter
		      assertEquals( "HOURS", lResults.getString( "data_type_cd" ) );
		      assertEquals( "US", lResults.getString( "domain_type_cd" ) );

		      // usage remaining for forecast
		      assertEquals( 230.0, lResults.getDouble( "usage_remaining_plus_deviation" ), 0f );

		      // estimated date
		      assertEquals( "2016-06-14 11:59:59 AM",
		            SDF.format( lResults.getDate( "sched_dead_plus_deviation" ) ) );

		      lUSDeadlineValidated = true;
	      }
      }

      // Ensure both CA and US Deadlines have been seen
      assertTrue(lCADeadlineValidated && lUSDeadlineValidated);

   }
}
