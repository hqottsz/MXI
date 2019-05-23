package com.mxi.mx.core.query.task.complete;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.TaskKey;


/**
 * Tests the validation for completion of component tasks at various dates and times
 *
 */
public final class IsComponentTaskValidForCompletionOnDateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() throws Exception {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsComponentTaskValidForCompletionOnDateTest.class );
   }


   /**
    * Tests validating the completion of a component task prior to the first removal event but after
    * the manufacture date
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHistoricalTaskCompletionBeforeManufacture() throws Exception {

      Date lCompletionDate =
            new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( "2015-01-01 00:00:00" );

      DataSet lDataSet = execute( new TaskKey( 4650, 1 ), lCompletionDate );

      assertEquals( "count", 0, lDataSet.getRowCount() );

   }


   /**
    * Tests validating the completion of a component task prior to the first removal event but after
    * the manufacture date
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHistoricalTaskCompletionBeforeFirstRemoval() throws Exception {

      Date lCompletionDate =
            new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( "2017-11-01 00:00:00" );

      DataSet lDataSet = execute( new TaskKey( 4650, 1 ), lCompletionDate );

      assertEquals( "count", 1, lDataSet.getRowCount() );

   }


   /**
    * Tests validating the completion of a component task after first removal event
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHistoricalTaskCompletionAfterFirstRemoval() throws Exception {

      Date lCompletionDate =
            new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( "2018-01-02 00:00:00" );

      DataSet lDataSet = execute( new TaskKey( 4650, 1 ), lCompletionDate );

      assertEquals( "count", 0, lDataSet.getRowCount() );

   }


   /**
    * Tests validating the completion of a component task after it has been reinstalled
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHistoricalTaskCompletionAfterReinstall() throws Exception {

      Date lCompletionDate =
            new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( "2018-01-06 00:00:00" );

      DataSet lDataSet = execute( new TaskKey( 4650, 1 ), lCompletionDate );

      assertEquals( "count", 1, lDataSet.getRowCount() );

   }


   /**
    * Tests validating the completion of a component task after it has been reinstalled on another
    * aircraft
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testHistoricalTaskCompletionAfterInstallOnDifferentAircraft() throws Exception {

      Date lCompletionDate =
            new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).parse( "2018-01-10 00:00:00" );

      DataSet lDataSet = execute( new TaskKey( 4650, 1 ), lCompletionDate );

      assertEquals( "count", 0, lDataSet.getRowCount() );

   }


   /**
    * This method executes the query in IsComponentTaskValidForCompletionOnDate.qrx
    *
    * @param aActualTask
    *           The Task to run the validation against
    * @param aCompletionDate
    *           The Date to test the task against
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aActualTask, Date aCompletionDate ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aActualTask, "aTaskDbId", "aTaskId" );
      lDataSetArgument.add( "aCompletionDt", aCompletionDate );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
