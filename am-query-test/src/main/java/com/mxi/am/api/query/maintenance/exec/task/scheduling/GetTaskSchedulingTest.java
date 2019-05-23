package com.mxi.am.api.query.maintenance.exec.task.scheduling;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;


/**
 * This class is to test query for Task Scheduling API
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetTaskSchedulingTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      // Given a requirement task
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), GetTaskSchedulingTest.class,
            "GetTaskSchedulingTest.xml" );

   }


   @DataPoint
   public static final double NO_DEVIATION = 0;
   @DataPoint
   public static final double ONE_DT_DEVIATION = 1;
   @DataPoint
   public static final double TWO_DT_DEVIATION = 2;

   @DataPoint
   public static final String DAY = "CDY";

   @DataPoint
   public static final String LANDING = "LANDING";

   @DataPoint
   public static final String DRIVEN_DEADLINE_TYPE = "Calendar";

   @DataPoint
   public static final String NON_DRIVEN_DEADLINE_TYPE = "Usage";

   @DataPoint
   public static final float USAGE_UNTIL_DEADLINE_CASE1 = 0;

   @DataPoint
   public static final float USAGE_UNTIL_DEADLINE_CASE2 = 4000;

   @DataPoint
   public static final String DEADLINE_DATE = "2018-01-02 00:00:00";

   @DataPoint
   public static final String EXTENDED_DEADLINE_DATE_CASE1 = "2018-01-04 00:00:00";

   @DataPoint
   public static final String EXTENDED_DEADLINE_DATE_CASE2 = "2018-01-02 00:00:00";

   @DataPoint
   public static final float EXTENSION_AMOUNT_CASE1 = 2;

   @DataPoint
   public static final float EXTENSION_AMOUNT_CASE2 = 200;


   @Test
   public void getTaskDrivenSchedulingByTaskId() throws ParseException {
      String lTaskId = "E39F34F40AA511E8BA890ED5F89F718B";

      QuerySet lQs = getTaskScheduingByTaskId( lTaskId );
      lQs.next();
      Assert.assertEquals( DAY, lQs.getString( "data_type_cd" ) );
      Assert.assertEquals( true, lQs.getBoolean( "driving_task_bool" ) );
      Assert.assertEquals( USAGE_UNTIL_DEADLINE_CASE1, lQs.getFloat( "usage_until_deadline" ), 0 );
      Assert.assertEquals( DRIVEN_DEADLINE_TYPE, lQs.getString( "deadline_type" ) );
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Assert.assertEquals( lDateFormat.parse( DEADLINE_DATE ), lQs.getDate( "deadline_date" ) );
      Assert.assertEquals( lDateFormat.parse( EXTENDED_DEADLINE_DATE_CASE1 ),
            lQs.getDate( "extended_deadline_date" ) );
      Assert.assertEquals( EXTENSION_AMOUNT_CASE1, lQs.getFloat( "extension_amount" ), 0.1 );
   }


   @Test
   public void getTaskNonDrivenSchedulingByTaskId() throws ParseException {
      String lTaskId = "64630C780AA611E8BA890ED5F89F718B";

      QuerySet lQs = getTaskScheduingByTaskId( lTaskId );
      lQs.next();
      Assert.assertEquals( LANDING, lQs.getString( "data_type_cd" ) );
      Assert.assertEquals( false, lQs.getBoolean( "driving_task_bool" ) );
      Assert.assertEquals( USAGE_UNTIL_DEADLINE_CASE2, lQs.getFloat( "usage_until_deadline" ), 0 );
      Assert.assertEquals( NON_DRIVEN_DEADLINE_TYPE, lQs.getString( "deadline_type" ) );
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Assert.assertEquals( lDateFormat.parse( DEADLINE_DATE ), lQs.getDate( "deadline_date" ) );
      Assert.assertEquals( lDateFormat.parse( EXTENDED_DEADLINE_DATE_CASE2 ),
            lQs.getDate( "extended_deadline_date" ) );
      Assert.assertEquals( EXTENSION_AMOUNT_CASE2, lQs.getFloat( "extension_amount" ), 0 );
   }


   /**
    *
    * Get assembly capabilities.
    *
    * @param aAssemblyId
    *           The assembly id
    * @return DataSet with query results
    */
   private QuerySet getTaskScheduingByTaskId( String aTaskId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhere( "sched_stask.alt_id = hextoraw(:aBindId)" );
      lArgs.add( "aBindId", aTaskId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }

}
