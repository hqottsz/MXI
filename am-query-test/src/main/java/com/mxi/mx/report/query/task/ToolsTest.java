
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
 * This class tests the query Tools.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ToolsTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ToolsTest.class );
   }


   /**
    * Tests the retrieval of the Tools.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testTools() throws Exception {
      QuerySet lQuerySet = this.execute( new TaskKey( 5000000, 74542 ) );

      Assert.assertEquals( "Number of retrieved rows", 2, lQuerySet.getRowCount() );

      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "5000000:74542:1", "4650:33171", "MG-A21 (Machina Gizmatico)", "0.30",
            "4650:284005", "4650:21250", "MG-A21", "PageEditToolRequirementsTest", "11",
            "mxsystem mxsystem", 0, 1, 0 );

      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "5000000:74542:2", "4650:33172", "MG-A21X (Machina Gizmatico Xtreme)",
            "0.25", null, "4650:21251", "MG-A21X", null, null, null, 0, 0, 0 );

      Assert.assertFalse( lQuerySet.next() );
   }


   /**
    * This method executes the query in Tools.qrx
    *
    * @param aTaskKey
    *           The task key.
    *
    * @return The QuerySet after execution.
    */
   private QuerySet execute( TaskKey aTaskKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aTaskDbId", aTaskKey.getDbId() );
      lDataSetArgument.add( "aTaskId", aTaskKey.getId() );

      return QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }


   /**
    * Assert the Row Coloumns.
    *
    * @param aQuerySet
    *           the QuerySet
    * @param aToolKey
    *           the tool key
    * @param aBomPartKey
    *           the bompart key
    * @param aToolDescription
    *           the tool description
    * @param aSchedHr
    *           the schedhr
    * @param aInvNoKey
    *           the inv no key
    * @param aPartNoKey
    *           the part no key
    * @param aPartNoOem
    *           the partnooem
    * @param aSerialNoOem
    *           the serial no oem
    * @param aUserKey
    *           the user key
    * @param aUserName
    *           the user name
    * @param aHistBool
    *           the hist bool
    * @param aEventBool
    *           the event bool
    * @param aBaselineBool
    *           the baseline bool.
    */
   private void testRow( QuerySet aQuerySet, String aToolKey, String aBomPartKey,
         String aToolDescription, String aSchedHr, String aInvNoKey, String aPartNoKey,
         String aPartNoOem, String aSerialNoOem, String aUserKey, String aUserName, int aHistBool,
         int aEventBool, int aBaselineBool ) {

      Assert.assertEquals( "tool_key", aToolKey, aQuerySet.getString( "tool_key" ) );
      Assert.assertEquals( "bom_part_key", aBomPartKey, aQuerySet.getString( "bom_part_key" ) );

      Assert.assertEquals( "tool_description", aToolDescription,
            aQuerySet.getString( "tool_description" ) );
      Assert.assertEquals( "sched_hr", aSchedHr, aQuerySet.getString( "sched_hr" ).trim() );
      Assert.assertEquals( "inv_no_key", aInvNoKey, aQuerySet.getString( "inv_no_key" ) );
      Assert.assertEquals( "part_no_key", aPartNoKey, aQuerySet.getString( "part_no_key" ) );
      Assert.assertEquals( "part_no_oem", aPartNoOem, aQuerySet.getString( "part_no_oem" ) );
      Assert.assertEquals( "serial_no_oem", aSerialNoOem, aQuerySet.getString( "serial_no_oem" ) );
      Assert.assertEquals( "user_key", aUserKey, aQuerySet.getString( "user_key" ) );
      Assert.assertEquals( "user_name", aUserName, aQuerySet.getString( "user_name" ) );

      Assert.assertEquals( "hist_bool", aHistBool, aQuerySet.getInt( "hist_bool" ) );
      Assert.assertEquals( "has_checkout_event_bool", aEventBool,
            aQuerySet.getInt( "has_checkout_event_bool" ) );
      Assert.assertEquals( "baseline_bool", aBaselineBool, aQuerySet.getInt( "baseline_bool" ) );
   }
}
