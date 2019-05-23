
package com.mxi.mx.integration.query.process;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * This class tests the LookupQueueId query.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class LookupQueueIdTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), LookupQueueIdTest.class );
   }


   /**
    * Tests the query returns one row and it is the expected row.
    */
   @Test
   public void testQuery() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aMsgId", "100" );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.integration.query.process.LookupQueueId", lArgs );

      Assert.assertEquals( "Row Count", 1, lQs.getRowCount() );

      lQs.next();

      Assert.assertEquals( "Queue Id", 1000, lQs.getInt( "queue_id" ) );
   }
}
