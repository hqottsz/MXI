
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
 * This class tests the query TaskCard.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskCardTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), TaskCardTest.class );
   }


   /**
    * Tests the retrieval of the Taskcard.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testZones() throws Exception {

      int lTaskDbId = 4650;
      int lTaskId = 60550;

      QuerySet lQuerySet = this.execute( new TaskKey( lTaskDbId, lTaskId ) );

      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );
      lQuerySet.next();

      testRow( lQuerySet, "4650", "60550", "0", "COMPLETE", "LOW",
            "A2-CHECK (A-Check Step 2 [A,2A])",

            "A and 2A Check", "24-Nov-2009 03:31:12", "21-Nov-2009 03:31:12",
            "20-Nov-2009 03:31:12", null, "T0000MQU", "WO - 60550" );
   }


   /**
    * This method executes the query in TaskCard.qrx
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
    * @param aEventDbId
    *           the Event dbId
    * @param aEventId
    *           the EventId
    * @param aEventTypeDbId
    *           the EventType db Id
    * @param aEventStatusCd
    *           the event status cd
    * @param aSchedPriorityCd
    *           the sched priority cd
    * @param aEventSdesc
    *           the event sdesc
    * @param aEventLdesc
    *           the event ldesc
    * @param aEventGdt
    *           the event date
    * @param aSchedStartGdt
    *           the sched start date
    * @param aSchedEndGdt
    *           the sched end date
    * @param aActualStartGdt
    *           the actual start date
    * @param aBarcodeSdesc
    *           the barcode
    * @param aWoRefSdesc
    *           the wo ref sdesc
    */
   private void testRow( QuerySet aQuerySet, String aEventDbId, String aEventId,
         String aEventTypeDbId, String aEventStatusCd, String aSchedPriorityCd, String aEventSdesc,
         String aEventLdesc, String aEventGdt, String aSchedStartGdt, String aSchedEndGdt,
         String aActualStartGdt, String aBarcodeSdesc, String aWoRefSdesc ) {

      Assert.assertEquals( "event_db_id", aEventDbId, aQuerySet.getString( "event_db_id" ) );
      Assert.assertEquals( "event_id", aEventId, aQuerySet.getString( "event_id" ) );
      Assert.assertEquals( "event_type_db_id", aEventTypeDbId,
            aQuerySet.getString( "event_type_db_id" ) );
      Assert.assertEquals( "event_status_cd", aEventStatusCd,
            aQuerySet.getString( "event_status_cd" ) );

      Assert.assertEquals( "sched_priority_cd", aSchedPriorityCd,
            aQuerySet.getString( "sched_priority_cd" ) );
      Assert.assertEquals( "event_sdesc", aEventSdesc, aQuerySet.getString( "event_sdesc" ) );
      Assert.assertEquals( "event_ldesc", aEventLdesc, aQuerySet.getString( "event_ldesc" ) );
      Assert.assertEquals( "event_gdt", aEventGdt, aQuerySet.getString( "event_gdt" ) );
      Assert.assertEquals( "sched_start_gdt", aSchedStartGdt,
            aQuerySet.getString( "sched_start_gdt" ) );
      Assert.assertEquals( "sched_end_gdt", aSchedEndGdt, aQuerySet.getString( "sched_end_gdt" ) );
      Assert.assertEquals( "actual_start_gdt", aActualStartGdt,
            aQuerySet.getString( "actual_start_gdt" ) );

      Assert.assertEquals( "barcode_sdesc", aBarcodeSdesc, aQuerySet.getString( "barcode_sdesc" ) );

      Assert.assertEquals( "wo_ref_sdesc", aWoRefSdesc, aQuerySet.getString( "wo_ref_sdesc" ) );
   }
}
