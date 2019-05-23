
package com.mxi.mx.common.query.work;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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


/**
 * Tests the GetUnscheduledWorkItems query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetUnscheduledWorkItemsTest {

   @ClassRule
   public static final DatabaseConnectionRule sDatabaseConnectionRule =
         new DatabaseConnectionRule();


   @BeforeClass
   public static void setUp() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetUnscheduledWorkItemsTest.class );
   }


   private static final String DISABLED_TYPE = "DISABLED";
   private static final String ENABLED_TYPE = "ENABLED";


   /**
    * Ensure that work items are not returned when the work item type is disabled
    */
   @Test
   public void testWorkItemDisabled() {
      Set<Integer> lWorkItemIds = execute( DISABLED_TYPE );

      Assert.assertTrue( "no work items", lWorkItemIds.isEmpty() );
   }


   /**
    * Ensure that work items are returned when the work item type is enabled
    */
   @Test
   public void testWorkItemEnabled() {
      Set<Integer> lWorkItemIds = execute( ENABLED_TYPE );

      Assert.assertFalse( "work items", lWorkItemIds.isEmpty() );
   }


   /**
    * Executes the query
    *
    * @param aWorkType
    *           the work item type
    *
    * @return the work item ids
    */
   private Set<Integer> execute( String aWorkType ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aWorkType", aWorkType );
      lArgs.add( "aCurrentDate", new Date() );
      lArgs.add( "aMaxItems", 200 );
      lArgs.add( "aServerId", "TEST" );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lArgs );
      Set<Integer> lWorkItems = new HashSet<Integer>();
      while ( lQs.next() ) {
         lWorkItems.add( lQs.getInteger( "id" ) );
      }

      return lWorkItems;
   }
}
