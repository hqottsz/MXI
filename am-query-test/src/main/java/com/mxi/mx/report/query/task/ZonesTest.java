
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
 * This class tests the query Zones.qrx
 *
 * @author srengasamy
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ZonesTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ZonesTest.class );
   }


   /**
    * Tests the retrieval of the Tools.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testZones() throws Exception {
      QuerySet lQuerySet = this.execute( new TaskKey( 4650, 110482 ) );

      Assert.assertEquals( "Number of retrieved rows", 2, lQuerySet.getRowCount() );

      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "101 - Zone - Flight Compartment - Left" );

      Assert.assertTrue( lQuerySet.next() );
      testRow( lQuerySet, "102 - Zone - Flight Compartment Cabin - Right" );

      Assert.assertFalse( lQuerySet.next() );
   }


   /**
    * This method executes the query in Zones.qrx
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
    * @param aZone
    *           the Zone
    */
   private void testRow( QuerySet aQuerySet, String aZone ) {
      Assert.assertEquals( "zone", aZone, aQuerySet.getString( "zone" ) );
   }
}
