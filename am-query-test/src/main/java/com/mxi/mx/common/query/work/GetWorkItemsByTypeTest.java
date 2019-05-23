package com.mxi.mx.common.query.work;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.WorkItemBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;


/**
 * Tests the <code>GetWorkItemsByType</code> query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetWorkItemsByTypeTest {

   public static final String WORK_ITEM_TYPE_NAME = "TEST_WORK_ITEM_TYPE";

   @Rule
   public DatabaseConnectionRule connection = new DatabaseConnectionRule();

   @Rule
   public final FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();


   /**
    * Given an invalid work item's type, when GetWorkItemsByType.qrx is executed, it should return
    * empty set.
    */
   @Test
   public void testReturnEmptyWhenSelectWorkItemOfNonExistentType() {
      // ACT
      Set<Integer> results = this.execute( "DUMMY-GHOST-TYPE" );

      // ASSERT
      assertEquals( 0, results.size() );
   }


   /**
    * Given a valid work item type, when GetWorkItemsByType.qrx is executed, work items of the given
    * type should be returned.
    */
   @Test
   public void testReturnWorkItemsOfGivenType() {
      // ARRANGE
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "name", WORK_ITEM_TYPE_NAME );
      dataSetArgument.add( "enabled", 1 );
      MxDataAccess.getInstance().executeInsert( "utl_work_item_type", dataSetArgument );

      Integer workItem1 = new WorkItemBuilder().withType( WORK_ITEM_TYPE_NAME ).build();
      Integer workItem2 = new WorkItemBuilder().withType( WORK_ITEM_TYPE_NAME ).build();

      // ACT
      Set<Integer> results = this.execute( WORK_ITEM_TYPE_NAME );

      // ASSERT
      assertEquals( 2, results.size() );
      assertTrue( results.contains( workItem1 ) );
      assertTrue( results.contains( workItem2 ) );
   }


   /**
    * Given a valid work item type which is disabled, when GetWorkItemsByType.qrx is executed, it
    * should return empty set.
    */
   @Test
   public void testReturnEmptyForDisabledWorkItemType() {
      // ARRANGE
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "name", WORK_ITEM_TYPE_NAME );
      dataSetArgument.add( "enabled", 0 );
      MxDataAccess.getInstance().executeInsert( "utl_work_item_type", dataSetArgument );

      Integer workItem1 = new WorkItemBuilder().withType( WORK_ITEM_TYPE_NAME ).build();

      // ACT
      Set<Integer> results = this.execute( WORK_ITEM_TYPE_NAME );

      // ASSERT
      assertEquals( 0, results.size() );
   }


   private Set<Integer> execute( String workItemType ) {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aWorkItemType", workItemType );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), dataSetArgument );
      Set<Integer> workItems = new HashSet<Integer>();
      while ( lQs.next() ) {
         workItems.add( lQs.getInteger( "id" ) );
      }

      return workItems;
   }
}
