
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
 * This class tests the query PartOnOff.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PartOnOffTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PartOnOffTest.class );
   }


   /**
    * Tests the retrieval of the Part On Off.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testPartOnOff() throws Exception {
      QuerySet lQuerySet = this.execute( new TaskKey( 4650, 87424 ) );

      Assert.assertEquals( "Number of retrieved rows", 2, lQuerySet.getRowCount() );
      Assert.assertTrue( lQuerySet.next() );

      testRow( lQuerySet, "321002203 (ANTI SKD NORM) ", 1, "1000 - 321002203-UNK / 3282-10-627",
            "REPREQ / Unscheduled Failure", 0, "4650:87424:2" );
      Assert.assertTrue( lQuerySet.next() );

      testRow( lQuerySet, "321002203-01 (ADAPTING TACHOMETER) ", 1,
            "1000 - 321002203-01-A / AH-001", "REPREQ / Unscheduled Failure", 0, "4650:87424:1" );
      Assert.assertFalse( lQuerySet.next() );
   }


   /**
    * This method executes the query in PartOnOff.qrx
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
    * @param aPartDescription
    *           the part description
    * @param aRemoveQty
    *           the remove qty
    * @param aPartSerialOff
    *           the part serial off
    * @param aCondReasonOff
    *           the cond reason off
    * @param aWarrentyCoverage
    *           the warrenty coverage
    * @param aShedPartKey
    *           the shed part key
    */
   private void testRow( QuerySet aQuerySet, String aPartDescription, int aRemoveQty,
         String aPartSerialOff, String aCondReasonOff, int aWarrentyCoverage,
         String aShedPartKey ) {

      Assert.assertEquals( "part_description", aPartDescription,
            aQuerySet.getString( "part_description" ) );
      Assert.assertEquals( "removed_quantity", aRemoveQty, aQuerySet.getInt( "removed_quantity" ) );

      Assert.assertEquals( "part_serial_off", aPartSerialOff,
            aQuerySet.getString( "part_serial_off" ) );
      Assert.assertEquals( "cond_reason_off", aCondReasonOff,
            aQuerySet.getString( "cond_reason_off" ) );
      Assert.assertEquals( "warranty_coverage", aWarrentyCoverage,
            aQuerySet.getInt( "warranty_coverage" ) );
      Assert.assertEquals( "sched_part_key", aShedPartKey,
            aQuerySet.getString( "sched_part_key" ) );
   }
}
