
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
 * This class tests the query AssemblyInformation.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AssemblyInformationTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AssemblyInformationTest.class );
   }


   /**
    * Tests the retrieval of the AssemblyInformation.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testAssemblyInformation() throws Exception {
      QuerySet lQuerySet = this.execute( new TaskKey( 4650, 110482 ) );

      Assert.assertEquals( "Number of retrieved rows", 1, lQuerySet.getRowCount() );
      Assert.assertTrue( lQuerySet.next() );

      testRow( lQuerySet, "4650", "110482", "JIC", "STJ - C.2 (Sync Test JIC C.2)", "ACFT",
            "Boeing 767-232 - 1111", "b767-200" );
      Assert.assertFalse( lQuerySet.next() );
   }


   /**
    * This method executes the query in AssemblyInformation.qrx
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
    *           the event db id
    * @param aEventId
    *           the event id
    * @param aTaskClassCd
    *           the task class cd
    * @param aEventSdesc
    *           the event sdesc
    * @param aInvClassCd
    *           the inv class cd
    * @param aInvNoSdesc
    *           the inv no sdesc
    * @param aAssmblBomCd
    *           the assembly bom cd
    */
   private void testRow( QuerySet aQuerySet, String aEventDbId, String aEventId,
         String aTaskClassCd, String aEventSdesc, String aInvClassCd, String aInvNoSdesc,
         String aAssmblBomCd ) {

      Assert.assertEquals( "event_db_id", aEventDbId, aQuerySet.getString( "event_db_id" ) );
      Assert.assertEquals( "event_id", aEventId, aQuerySet.getString( "event_id" ) );
      Assert.assertEquals( "task_class_cd", aTaskClassCd, aQuerySet.getString( "task_class_cd" ) );
      Assert.assertEquals( "event_sdesc", aEventSdesc, aQuerySet.getString( "event_sdesc" ) );
      Assert.assertEquals( "inv_class_cd", aInvClassCd, aQuerySet.getString( "inv_class_cd" ) );
      Assert.assertEquals( "inv_no_sdesc", aInvNoSdesc, aQuerySet.getString( "inv_no_sdesc" ) );
      Assert.assertEquals( "assmbl_bom_cd", aAssmblBomCd, aQuerySet.getString( "assmbl_bom_cd" ) );
   }
}
