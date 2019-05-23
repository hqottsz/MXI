package com.mxi.mx.core.query.adapter.ema.taskbaselinesegment;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ibm.icu.util.Calendar;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Test getTaskTaskKey.qrx query
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetTaskTaskKeyTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static Date iRevisionDtInSameMinute;
   private static Date iRevisionDtInPreviouMinute;


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetTaskTaskKeyTest.class );

      Calendar lCalendar = Calendar.getInstance();
      lCalendar.set( 2017, 03, 05, 15, 47, 0 );
      iRevisionDtInSameMinute = lCalendar.getTime();

      lCalendar.set( 2017, 03, 05, 15, 46, 0 );
      iRevisionDtInPreviouMinute = lCalendar.getTime();
   }


   /**
    * Execute the query.
    *
    * @param aTaskCd
    *           the requirement task code
    * @param aAssmblCd
    *           the assembly code of the requirement
    * @param aAssmblBomCd
    *           the assembly bom code
    * @param aRevisionDate
    *           the revision date of the requirement
    */
   private DataSet getTaskTaskKeyDs( String aTaskCd, String aAssmblCd, String aAssmblBomCd,
         Date aRevisionDate ) {

      // Arrange
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aTaskCd", aTaskCd );
      lArgs.add( "aAssmblCd", aAssmblCd );
      lArgs.add( "aAssmblBomCd", aAssmblBomCd );
      lArgs.add( "aRevisionDate", aRevisionDate );

      // Act
      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            "com/mxi/mx/core/query/adapter/ema/taskbaselinesegment/getTaskTaskKey", lArgs );
      return lDataSet;
   }


   /**
    * Test Case : If a task code is changed on a new revision of a requirement and the revision date
    * in EMA message is same minute as this new revision date, its actual task is created
    * successfully against the new revision
    */
   @Test
   public void testInitializeTaskRevisionWithUpdatedTaskCodeSuccessfully() throws Exception {
      // Arrange and Act
      DataSet lTaskTaskKeyDs =
            getTaskTaskKeyDs( "REQ-111-REV", "A320", "A320", iRevisionDtInSameMinute );
      // Assert
      assertEquals( 1, lTaskTaskKeyDs.getRowCount() );

      lTaskTaskKeyDs.next();

      TaskTaskKey lTaskTaskKey = new TaskTaskKey( 4650, 238687 );
      assertEquals( lTaskTaskKey, lTaskTaskKeyDs.getKey( TaskTaskKey.class, "task_task_key" ) );
   }


   /**
    * Test Case : If a task code is changed on a new revision of a requirement and the revision date
    * in EMA message is before the new revision date, its actual task is created unsuccessfully
    * against the new revision;
    */
   @Test
   public void testInitializeTaskRevisionWithUpdatedTaskCodeUnsuccessfully() throws Exception {
      // Arrange and Act
      DataSet lTaskTaskKeyDs =
            getTaskTaskKeyDs( "REQ-111-REV", "A320", "A320", iRevisionDtInPreviouMinute );
      // Assert
      assertEquals( 0, lTaskTaskKeyDs.getRowCount() );
   }


   /**
    * Test Case : If a task code is not changed on a new revision of a requirement and the revision
    * date in EMA message is same minute as this new revision date, its actual task is created
    * successfully against the new revision;
    */
   @Test
   public void testInitializeTaskRevisionWithSameTaskCodeSuccessfully() throws Exception {
      // Arrange and Act
      DataSet lTaskTaskKeyDs =
            getTaskTaskKeyDs( "REQ-222", "A320", "A320", iRevisionDtInSameMinute );
      // Assert
      assertEquals( 1, lTaskTaskKeyDs.getRowCount() );

      lTaskTaskKeyDs.next();

      TaskTaskKey lTaskTaskKey = new TaskTaskKey( 4650, 238786 );
      assertEquals( lTaskTaskKey, lTaskTaskKeyDs.getKey( TaskTaskKey.class, "task_task_key" ) );
   }


   /**
    * Test Case : If a task code is not changed on a new revision of a requirement and the revision
    * date in EMA message is before the new revision date, its actual task is created unsuccessfully
    * against the new revision;
    */
   @Test
   public void testInitializeTaskRevisionWithSameTaskCodeUnSuccessfully() throws Exception {
      // Arrange and Act
      DataSet lTaskTaskKeyDs =
            getTaskTaskKeyDs( "REQ-222", "A320", "A320", iRevisionDtInPreviouMinute );
      // Act
      assertEquals( 0, lTaskTaskKeyDs.getRowCount() );
   }
}
