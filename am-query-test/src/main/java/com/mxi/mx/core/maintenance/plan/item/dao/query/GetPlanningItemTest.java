package com.mxi.mx.core.maintenance.plan.item.dao.query;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Tests the GetPlanningItem.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetPlanningItemTest {

   @ClassRule
   public static DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   /*
    * Returns a planning item when current actual task's nh_event_id in table evt_event is null
    */
   @Test
   public void testPlanningItemRetrieved() {

      // ARRANGE
      String lAltId = "FD9676F282D0447390936DBE485D305D";

      // ACT
      QuerySet lQs = executeQuery( lAltId );

      // ASSERT
      Assert.assertEquals( 1, lQs.getRowCount() );
   }


   /*
    * Returns 0 planning item when current actual task's id doesn't exist
    */
   @Test
   public void testNoPlanningItemRetrieved() {

      // ARRANGE
      String lAltId = "FD9676F282D0447390936DBE485D30DD";

      // ACT
      QuerySet lQs = executeQuery( lAltId );

      // ASSERT
      Assert.assertTrue( lQs.isEmpty() );
   }


   /*
    * Execute the query under test
    * 
    * @param aAltId The alternate identity
    */
   private QuerySet executeQuery( String aAltId ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhere( "sched_stask.alt_id = :aBindId" );
      lArgs.add( "aBindId", aAltId );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.maintenance.plan.item.dao.query.GetPlanningItem", lArgs );
   }


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( iDatabaseConnectionRule.getConnection(), GetPlanningItemTest.class,
            "GetPlanningItemTest.xml" );
   }

}
